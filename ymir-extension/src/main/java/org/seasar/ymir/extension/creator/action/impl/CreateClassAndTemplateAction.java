package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.InvalidClassDescException;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.BodyDescImpl;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;

public class CreateClassAndTemplateAction extends AbstractAction implements
        UpdateAction {

    private static final String PARAM_REDIRECTPATH = SourceCreator.PARAM_PREFIX
            + "redirectPath";

    public CreateClassAndTemplateAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData) {

        if (isSkipButtonPushed(request)) {
            return null;
        }

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("template".equals(subTask)) {
            return actTemplate(request, pathMetaData);
        } else if ("redirect".equals(subTask)) {
            return actRedirect(request, pathMetaData);
        } else {
            return actDefault(request, pathMetaData);
        }
    }

    Response actDefault(Request request, PathMetaData pathMetaData) {

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("pathMetaData", pathMetaData);
        return getSourceCreator().getResponseCreator().createResponse(
                "createClassAndTemplate", variableMap);
    }

    Response actTemplate(Request request, PathMetaData pathMetaData) {

        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        updateMapping(pathMetaData);

        String template = getSourceCreator().getSourceGenerator()
                .generateTemplateSource(
                        getSuffix(pathMetaData.getTemplate().getName()),
                        new HashMap<String, Object>());
        if (template == null) {
            template = "";
        }

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("template", template);
        return getSourceCreator().getResponseCreator().createResponse(
                "createClassAndTemplate_template", variableMap);
    }

    Response actRedirect(Request request, PathMetaData pathMetaData) {
        HttpMethod method = getHttpMethod(request);
        if (method == null) {
            return null;
        }

        String redirectPath = request.getParameter(PARAM_REDIRECTPATH);
        if (redirectPath == null) {
            return null;
        }

        DescPool pool = newDescPool();
        String path = pathMetaData.getPath();
        pool.setBornOf(path);
        ClassDesc classDesc = getSourceCreator().newClassDesc(pool,
                pathMetaData.getClassName(), null);
        MethodDesc methodDesc = getSourceCreator().newActionMethodDesc(pool,
                path, method, new ActionSelectorSeedImpl());
        methodDesc.setReturnTypeDesc(String.class);
        methodDesc.getReturnTypeDesc().setExplicit(true);
        methodDesc.setBodyDesc(new BodyDescImpl("return "
                + quote("redirect:" + redirectPath) + ";", new String[0]));
        classDesc.setMethodDesc(methodDesc);

        String[] lackingClassNames = null;
        try {
            getSourceCreator().updateClass(classDesc);
        } catch (InvalidClassDescException ex) {
            lackingClassNames = ex.getLackingClassNames();
        }

        boolean successfullySynchronized = synchronizeResources(new String[] { getRootPackagePath() });
        pause(1000L);
        openJavaCodeInEclipseEditor(pathMetaData.getClassName());

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("lackingClassNames", lackingClassNames);
        variableMap.put("successfullySynchronized", successfullySynchronized);
        return getSourceCreator().getResponseCreator().createResponse(
                "createClassAndTemplate_redirect", variableMap);
    }
}
