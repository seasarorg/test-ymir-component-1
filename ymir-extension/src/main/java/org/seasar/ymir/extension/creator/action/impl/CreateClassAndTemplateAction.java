package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.InvalidClassDescException;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.BodyDescImpl;
import org.seasar.ymir.extension.creator.impl.MetaAnnotationDescImpl;
import org.seasar.ymir.extension.creator.mapping.ExtraPathMapping;
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

        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        String redirectPath = request.getParameter(PARAM_REDIRECTPATH);
        if (redirectPath == null) {
            return null;
        }

        ClassDesc classDesc = getSourceCreator().newClassDesc(
                pathMetaData.getClassName(), ClassType.PAGE, null);
        String path = request.getCurrentDispatch().getPath();
        ExtraPathMapping mapping = getSourceCreator().getExtraPathMapping(path,
                method);
        MethodDesc methodDesc = mapping
                .newActionMethodDesc(new ActionSelectorSeedImpl());
        methodDesc.setReturnTypeDesc(String.class.getName(), true);
        methodDesc.setBodyDesc(new BodyDescImpl("return "
                + quote("redirect:" + redirectPath) + ";"));
        classDesc.setMethodDesc(methodDesc);
        MethodDesc renderMethodDesc = mapping
                .newRenderActionMethodDesc(new ActionSelectorSeedImpl());
        renderMethodDesc.setAnnotationDesc(new MetaAnnotationDescImpl(
                Globals.META_NAME_ACTIONTYPE,
                new String[] { Globals.META_VALUE_ACTIONTYPE_RENDER },
                new Class[0]));
        classDesc.setMethodDesc(renderMethodDesc);

        String[] lackingClassNames = null;
        try {
            getSourceCreator().writeSourceFile(classDesc, null);
        } catch (InvalidClassDescException ex) {
            lackingClassNames = ex.getLackingClassNames();
        }

        synchronizeResources(new String[] { getRootPackagePath() });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("lackingClassNames", lackingClassNames);
        return getSourceCreator().getResponseCreator().createResponse(
                "createClassAndTemplate_redirect", variableMap);
    }
}
