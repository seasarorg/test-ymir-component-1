package org.seasar.cms.ymir.extension.creator.action.impl;

import static org.seasar.cms.ymir.impl.DefaultRequestProcessor.PARAM_METHOD;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.seasar.cms.ymir.Request;
import org.seasar.cms.ymir.Response;
import org.seasar.cms.ymir.extension.creator.PathMetaData;
import org.seasar.cms.ymir.extension.creator.impl.SourceCreatorImpl;

public class CreateTemplateAction extends AbstractUpdateAction {

    private static final String PARAM_TEMPLATE = SourceCreatorImpl.PARAM_PREFIX
            + "template";

    public CreateTemplateAction(SourceCreatorImpl sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData) {

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("create".equals(subTask)) {
            return actCreate(request, pathMetaData);
        } else {
            return actDefault(request, pathMetaData);
        }
    }

    Response actDefault(Request request, PathMetaData pathMetaData) {

        boolean actionMethodNotFound = false;
        Class pageClass = getSourceCreator().getClass(
                pathMetaData.getClassName());
        if (pageClass != null) {
            Method actionMethod = getActionMethod(pageClass, pathMetaData
                    .getActionName());
            if (actionMethod == null) {
                actionMethodNotFound = true;
            }
        }

        String template = getSourceCreator().getSourceGenerator()
                .generateTemplateSource(
                        getSuffix(pathMetaData.getTemplateFile().getName()),
                        new HashMap());
        if (template == null) {
            template = "";
        }

        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("template", template);
        variableMap.put("actionMethodNotFound", actionMethodNotFound);
        return getSourceCreator().getResponseCreator().createResponse(
                "createTemplate", variableMap);
    }

    Method getActionMethod(Class pageClass, String actionName) {
        try {
            return pageClass.getMethod(actionName, new Class[0]);
        } catch (SecurityException ex) {
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    Response actCreate(Request request, PathMetaData pathMetaData) {

        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        String template = request.getParameter(PARAM_TEMPLATE);
        if (template == null) {
            return null;
        }

        File templateFile = pathMetaData.getTemplateFile();
        templateFile.getParentFile().mkdirs();
        getSourceCreator().writeString(template, templateFile);

        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        return getSourceCreator().getResponseCreator().createResponse(
                "createTemplate_create", variableMap);
    }
}
