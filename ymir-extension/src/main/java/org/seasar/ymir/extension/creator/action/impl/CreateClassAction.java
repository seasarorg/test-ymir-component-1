package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.util.Map;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.InvalidClassDescException;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.BodyDescImpl;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;

public class CreateClassAction extends AbstractAction implements UpdateAction {
    private static final String PARAM_TRANSITION = SourceCreator.PARAM_PREFIX
            + "transition";

    private static final String PARAM_TRANSITIONREDIRECT = PARAM_TRANSITION
            + "_redirect";

    public CreateClassAction(SourceCreator sourceCreator) {
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
        String path = request.getCurrentDispatch().getPath();
        String method = request.getMethod();
        String actionName = getSourceCreator()
                .getExtraPathMapping(path, method).newActionMethodDesc(
                        new ActionSelectorSeedImpl()).getName();

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("actionName", actionName);
        return getSourceCreator().getResponseCreator().createResponse(
                "createClass", variableMap);
    }

    Response actCreate(Request request, PathMetaData pathMetaData) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        String transition = request.getParameter(PARAM_TRANSITION);
        boolean redirect = "true".equals(request
                .getParameter(PARAM_TRANSITIONREDIRECT));

        ClassDesc classDesc = getSourceCreator().newClassDesc(
                pathMetaData.getClassName(), ClassType.PAGE, null);
        String path = request.getCurrentDispatch().getPath();
        MethodDesc actionMethodDesc = getSourceCreator().getExtraPathMapping(path,
                method).newActionMethodDesc(new ActionSelectorSeedImpl());
        actionMethodDesc.setReturnTypeDesc(String.class.getName(), true);
        if (transition != null && transition.trim().length() > 0) {
            if (redirect) {
                transition = "redirect:" + transition;
            }
            actionMethodDesc.setBodyDesc(new BodyDescImpl("return "
                    + quote(transition.trim()) + ";"));
        }
        classDesc.setMethodDesc(actionMethodDesc);

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
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("lackingClassNames", lackingClassNames);
        return getSourceCreator().getResponseCreator().createResponse(
                "createClass_create", variableMap);
    }
}
