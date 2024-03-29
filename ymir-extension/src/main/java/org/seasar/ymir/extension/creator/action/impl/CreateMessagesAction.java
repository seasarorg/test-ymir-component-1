package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.kvasir.util.io.impl.FileResource;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.action.UpdateByExceptionAction;
import org.seasar.ymir.message.MessagesNotFoundRuntimeException;

public class CreateMessagesAction extends AbstractAction implements
        UpdateByExceptionAction {

    private static final String SUFFIX_XPROPERTIES = ".xproperties";

    protected static final String PARAM_MESSAGESNAME = SourceCreator.PARAM_PREFIX
            + "messagesName";

    public CreateMessagesAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData, Throwable t) {
        if (!getSourceCreatorSetting().isMessagesCreationFeatureEnabled()) {
            return null;
        }

        if (isSkipButtonPushed(request)) {
            return null;
        }

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("create".equals(subTask)) {
            return actCreate(request, pathMetaData);
        } else {
            return actDefault(request, pathMetaData, t);
        }
    }

    Response actDefault(Request request, PathMetaData pathMetaData, Throwable t) {
        MessagesNotFoundRuntimeException mnfre = (MessagesNotFoundRuntimeException) t;

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("messageKey", mnfre.getMessageKey());
        variableMap.put("messagesName", mnfre.getMessagesName());
        return getSourceCreator().getResponseCreator().createResponse(
                "createMessages", variableMap);
    }

    Response actCreate(Request request, PathMetaData pathMetaData) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        String messagesName = request.getParameter(PARAM_MESSAGESNAME);
        if (messagesName == null) {
            return null;
        }

        createMessages(messagesName);

        boolean successfullySynchronized = synchronizeResources(new String[] { getResourcesPath() });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);

        variableMap.put("parameters", getParameters(request));
        variableMap.put("method", method);
        variableMap.put("messagesName", messagesName);
        variableMap.put("successfullySynchronized", successfullySynchronized);
        return getSourceCreator().getResponseCreator().createResponse(
                "createMessages_create", variableMap);
    }

    void createMessages(String messagesName) {
        OutputStream os;
        try {
            os = new FileResource(getSourceCreator().getResourcesDirectory())
                    .getChildResource(messagesName + SUFFIX_XPROPERTIES)
                    .getOutputStream();
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
        IOUtils.writeString(os, "", "UTF-8", false);
    }
}
