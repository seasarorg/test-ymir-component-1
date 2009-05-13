package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.extension.creator.SourceCreator.PARAM_PREFIX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.Action;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.InvalidClassDescException;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.BodyDescImpl;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;
import org.seasar.ymir.extension.creator.util.SourceCreatorUtils;

public class CreateTemplateAction extends AbstractAction implements
        UpdateAction {

    private static final String PARAM_TEMPLATE = PARAM_PREFIX + "template";

    private static final String PARAM_TRANSITION = PARAM_PREFIX + "transition";

    private static final String PARAM_TRANSITIONREDIRECT = PARAM_TRANSITION
            + "_redirect";

    public CreateTemplateAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData) {

        if (isSkipButtonPushed(request)) {
            return null;
        }

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("create".equals(subTask)) {
            return actCreate(request, pathMetaData);
        } else {
            return actDefault(request, pathMetaData);
        }
    }

    Response actDefault(Request request, PathMetaData pathMetaData) {

        boolean actionMethodNotFound = false;
        Class<?> pageClass = getSourceCreator().getClass(
                pathMetaData.getClassName());
        if (pageClass != null) {
            String path = pathMetaData.getPath();
            HttpMethod method = pathMetaData.getMethod();
            Request newRequest = SourceCreatorUtils.newRequest(path, method,
                    null);
            Action action = getSourceCreator().findMatchedPathMapping(path,
                    method).getAction(
                    SourceCreatorUtils.newPageComponent(pageClass), newRequest);
            if (action == null) {
                actionMethodNotFound = true;
            }
        }

        Template template = pathMetaData.getTemplate();
        String templateSource = "";
        if (!template.isDirectory()) {
            templateSource = getSourceCreator().getSourceGenerator()
                    .generateTemplateSource(getSuffix(template.getName()),
                            new HashMap<String, Object>());
        }

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("templateSource", templateSource);
        variableMap.put("actionMethodNotFound", actionMethodNotFound);
        return getSourceCreator().getResponseCreator().createResponse(
                "createTemplate", variableMap);
    }

    Response actCreate(Request request, PathMetaData pathMetaData) {
        HttpMethod method = getHttpMethod(request);
        if (method == null) {
            return null;
        }

        String templateString = request.getParameter(PARAM_TEMPLATE);
        String transition = request.getParameter(PARAM_TRANSITION);
        boolean redirect = "true".equals(request
                .getParameter(PARAM_TRANSITIONREDIRECT));
        boolean successfullySynchronized;
        if (templateString != null) {
            Template template = pathMetaData.getTemplate();
            try {
                IOUtils.writeString(template.getOutputStream(), templateString,
                        template.getEncoding(), false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            successfullySynchronized = synchronizeResources(new String[] { getPath(template) });
            pause(1000L);
            openResourceInEclipseEditor(getPath(template));
        } else if (transition != null) {
            if (PropertyUtils.valueOf(request.getParameter("mkdir"), false)) {
                pathMetaData.getTemplate().mkdirs();
            }

            DescPool pool = newDescPool();
            String path = pathMetaData.getPath();
            pool.setBornOf(path);
            ClassDesc classDesc = getSourceCreator().newClassDesc(pool,
                    pathMetaData.getClassName(), null);
            MethodDesc actionMethodDesc = getSourceCreator()
                    .newActionMethodDesc(pool, path, method,
                            new ActionSelectorSeedImpl());
            actionMethodDesc.setReturnTypeDesc(String.class);
            actionMethodDesc.getReturnTypeDesc().setExplicit(true);
            if (redirect) {
                transition = "redirect:" + transition;
            }
            actionMethodDesc.setBodyDesc(new BodyDescImpl("return "
                    + quote(transition.trim()) + ";", new String[0]));
            classDesc.setMethodDesc(actionMethodDesc);
            getSourceCreator().adjustByExistentClass(classDesc);
            try {
                getSourceCreator().updateClass(classDesc);
            } catch (InvalidClassDescException ex) {
                throw new RuntimeException("Can't happen!", ex);
            }

            successfullySynchronized = synchronizeResources(new String[] { getRootPackagePath() });
            pause(1000L);
            openJavaCodeInEclipseEditor(pathMetaData.getClassName());
        } else {
            return null;
        }

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("templateCreatd", (templateString != null));
        variableMap.put("transitionSet", (transition != null));
        variableMap.put("transition", transition);
        variableMap.put("successfullySynchronized", successfullySynchronized);
        return getSourceCreator().getResponseCreator().createResponse(
                "createTemplate_create", variableMap);
    }
}
