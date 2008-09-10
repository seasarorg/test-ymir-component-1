package org.seasar.ymir.amf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.ymir.util.StringUtils;

import flex.messaging.io.MessageDeserializer;
import flex.messaging.io.MessageIOConstants;
import flex.messaging.io.MessageSerializer;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ActionContext;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.AmfMessageDeserializer;
import flex.messaging.io.amf.AmfTrace;
import flex.messaging.io.amf.Java15AmfMessageSerializer;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.messages.AcknowledgeMessage;
import flex.messaging.messages.CommandMessage;
import flex.messaging.messages.ErrorMessage;
import flex.messaging.messages.Message;
import flex.messaging.messages.RemotingMessage;

/**
 * @author YOKOTA Takehiko
 * 
 */
public class AmfUtils {
    private static final MessageSerializer SERIALIZER = new Java15AmfMessageSerializer();

    private static final MessageDeserializer DESERIALIZER = new AmfMessageDeserializer();

    public static boolean isAmfType(final HttpServletRequest req) {
        String contentType = req.getHeader("content-type");
        if ((!StringUtils.isEmpty(contentType)) && contentType.equals("application/x-amf")) {
            return true;
        } else {
            return false;
        }
    }

    public static ActionContext createActionContext(HttpServletRequest request) throws IOException {
        ActionContext actionContext = new ActionContext();
        SerializationContext serializationContext = SerializationContext.getSerializationContext();

        InputStream inputStream = request.getInputStream();
        synchronized (DESERIALIZER) {
            DESERIALIZER.initialize(serializationContext, inputStream, new AmfTrace());
            int contentLength = request.getContentLength();
            actionContext.setDeserializedBytes(contentLength);
            actionContext.setRequestMessage(new ActionMessage());
            try {
                DESERIALIZER.readMessage(actionContext.getRequestMessage(), actionContext);
            } catch (ClassNotFoundException ex) {
                IOException ioex = new IOException("Can't read AMF message");
                ioex.initCause(ex);
                throw ioex;
            }
        }

        return actionContext;
    }

    public static void createActionContextForErrorResponse(Throwable t, OutputStream os) {
        ActionContext actionContext = new ActionContext();
        actionContext.setStatus(MessageIOConstants.STATUS_ERR);
        final MessageBody responseBody = new MessageBody();
        responseBody.setTargetURI(actionContext.getRequestMessageBody().getResponseURI());
        actionContext.setVersion(ActionMessage.CURRENT_VERSION);
        actionContext.getResponseMessage().addBody(responseBody);
        responseBody.setData(createErrorMessage(t));
        responseBody.setReplyMethod(MessageIOConstants.STATUS_METHOD);

        sendResponseMessage(actionContext, os);
    }

    public static ErrorMessage createErrorMessage(Throwable t) {
        ErrorMessage message = new ErrorMessage();
        message.rootCause = t;
        String faultString = t.getMessage();
        if (StringUtils.isEmpty(faultString)) {
            faultString = t.toString();
        }
        message.faultString = faultString;
        String faultDetail = t.getLocalizedMessage();
        if (StringUtils.isEmpty(faultDetail)) {
            faultDetail = t.toString();
        }
        message.faultDetail = faultDetail;
        message.faultCode = t.toString();
        message.extendedData = CollectionsUtil.newHashMap();
        return message;
    }

    public static void sendResponseMessage(ActionContext actionContext, OutputStream os) {
        SerializationContext serContext = SerializationContext.getSerializationContext();
        try {
            ActionMessage responseMessage = actionContext.getResponseMessage();
            synchronized (SERIALIZER) {
                SERIALIZER.initialize(serContext, os, new AmfTrace());
                SERIALIZER.writeMessage(responseMessage);
            }
        } catch (Throwable t) {
            if (actionContext.getStatus() == MessageIOConstants.STATUS_ERR) {
                throw new IllegalStateException(t);
            } else {
                handleError(actionContext, t, os);
            }
        }
    }

    public static void handleError(ActionContext actionContext, Throwable t, OutputStream os) {
        actionContext.setStatus(MessageIOConstants.STATUS_ERR);
        ActionMessage actionMessage = new ActionMessage();
        actionContext.setResponseMessage(actionMessage);
        int bodyCount = actionContext.getRequestMessage().getBodyCount();
        for (actionContext.setMessageNumber(0); actionContext.getMessageNumber() < bodyCount; actionContext
                .incrementMessageNumber()) {
            final MessageBody responseBody = new MessageBody();
            final MessageBody requestMessageBody = actionContext.getRequestMessageBody();
            responseBody.setTargetURI(requestMessageBody.getResponseURI());
            actionContext.getResponseMessage().addBody(responseBody);
            ErrorMessage errorMessage = createErrorMessage(t);
            Object data = getSingleRequestMessageBodyData(actionContext);
            addMessageInfo(data, errorMessage);
            responseBody.setData(errorMessage);
            responseBody.setReplyMethod(MessageIOConstants.STATUS_METHOD);
            actionMessage.addBody(responseBody);
        }
        sendResponseMessage(actionContext, os);
    }

    static Object getSingleRequestMessageBodyData(ActionContext actionContext) {
        final MessageBody reqBody = actionContext.getRequestMessageBody();
        if (reqBody == null) {
            return null;
        }
        Object data = reqBody.getData();
        if (data == null) {
            return null;
        }
        if (data.getClass().isArray()) {
            return ((Object[]) data)[0];
        } else {
            return data;
        }
    }

    @SuppressWarnings("unchecked")
    static void addMessageInfo(Object data, ErrorMessage errorMessage) {
        if (data instanceof Message) {
            final Message message = (Message) data;
            final Object clientId = message.getClientId();
            if (clientId != null) {
                errorMessage.setClientId(clientId.toString());
            }
            final String messageId = message.getMessageId();
            final String destination = message.getDestination();
            if (messageId != null) {
                errorMessage.setCorrelationId(messageId);
                errorMessage.setDestination(destination);
            }
            if (message instanceof RemotingMessage) {
                RemotingMessage rm = (RemotingMessage) message;
                String operation = rm.getOperation();
                errorMessage.extendedData.put("operation", operation);
                errorMessage.extendedData.put("target", destination + "." + operation);
            }
        }
    }

    public static boolean processIfCommandMessage(ActionContext actionContext) {
        MessageBody requestBody = actionContext.getRequestMessageBody();
        MessageBody responseBody = new MessageBody();
        responseBody.setTargetURI(requestBody.getResponseURI());

        boolean commandMessage = false;
        Object[] bodyObjects = (Object[]) requestBody.getData();
        for (int i = 0; i < bodyObjects.length; i++) {
            Object body = bodyObjects[i];
            if (body instanceof CommandMessage) {
                commandMessage = true;
                CommandMessage command = (CommandMessage) body;
                if (command.getOperation() == CommandMessage.CLIENT_PING_OPERATION) {
                    responseBody.setReplyMethod(MessageIOConstants.RESULT_METHOD);
                }
                break;
            }
        }

        if (commandMessage) {
            ActionMessage responseMessage = new ActionMessage();
            responseMessage.setVersion(actionContext.getVersion());
            responseMessage.addBody(responseBody);
            actionContext.setResponseMessage(responseMessage);
        }

        return commandMessage;
    }

    public static RemotingMessage getRemotingMessage(ActionContext actionContext) {
        MessageBody requestBody = actionContext.getRequestMessageBody();
        Object[] bodyObjects = (Object[]) requestBody.getData();
        for (int i = 0; i < bodyObjects.length; i++) {
            Object body = bodyObjects[i];
            if (body instanceof RemotingMessage) {
                return (RemotingMessage) body;
            }
        }
        return null;
    }

    public static void sendResopnse(ActionContext actionContext, RemotingMessage message, Object response,
            OutputStream os) {
        MessageBody responseBody = new MessageBody();
        responseBody.setTargetURI(actionContext.getRequestMessageBody().getResponseURI());

        responseBody.setReplyMethod(MessageIOConstants.RESULT_METHOD);
        AcknowledgeMessage acknowledge = new AcknowledgeMessage();
        acknowledge.setClientId(message.getClientId());
        acknowledge.setCorrelationId(message.getMessageId());
        acknowledge.setDestination(message.getDestination());
        acknowledge.setBody(response);
        acknowledge.setHeader(CommandMessage.MESSAGING_VERSION, Double.valueOf(3));
        responseBody.setData(acknowledge);

        ActionMessage responseMessage = new ActionMessage();
        responseMessage.setVersion(actionContext.getVersion());
        responseMessage.addBody(responseBody);
        actionContext.setResponseMessage(responseMessage);

        sendResponseMessage(actionContext, os);
    }
}
