package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.extension.Globals.APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGE_ENABLE;
import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.io.IOException;
import java.util.Map;

import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.collection.I18NProperties;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.kvasir.util.io.impl.FileResource;
import org.seasar.ymir.MessageNotFoundRuntimeException;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.action.UpdateByExceptionAction;

public class CreateMessageAction extends AbstractAction implements
        UpdateByExceptionAction {

    protected static final String PARAM_VALUE = SourceCreator.PARAM_PREFIX
            + "value";

    private static final String SUFFIX_XPROPERTIES = ".xproperties";

    public CreateMessageAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, Throwable t) {
        if (!PropertyUtils.valueOf(
                getSourceCreator().getApplication().getProperty(
                        APPKEY_SOURCECREATOR_FEATURE_CREATEMESSAGE_ENABLE),
                true)) {
            return null;
        }

        if (isSkipButtonPushed(request)) {
            return null;
        }

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("create".equals(subTask)) {
            return actCreate(request, t);
        } else {
            return actDefault(request, t);
        }
    }

    Response actDefault(Request request, Throwable t) {
        MessageNotFoundRuntimeException mnfre = (MessageNotFoundRuntimeException) t;

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("messageKey", mnfre.getMessageKey());
        variableMap.put("messagesName", mnfre.getMessagesName());
        return getSourceCreator().getResponseCreator().createResponse(
                "createMessage", variableMap);
    }

    Response actCreate(Request request, Throwable t) {
        MessageNotFoundRuntimeException mnfre = (MessageNotFoundRuntimeException) t;

        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        String value = request.getParameter(PARAM_VALUE);
        if (value == null) {
            value = "";
        }

        createMessage(mnfre.getMessagesName(), mnfre.getMessageKey(), value);

        synchronizeResources(new String[] { getResourcesPath() });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("method", method);
        variableMap.put("messageKey", mnfre.getMessageKey());
        variableMap.put("messagesName", mnfre.getMessagesName());
        return getSourceCreator().getResponseCreator().createResponse(
                "createMessage_create", variableMap);
    }

    void createMessage(String messagesName, String messageKey, String value) {
        I18NProperties properties = new I18NProperties(new FileResource(
                getSourceCreator().getResourcesDirectory()), messagesName,
                SUFFIX_XPROPERTIES);
        properties.setProperty(messageKey, value);
        try {
            properties.store();
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }
}
