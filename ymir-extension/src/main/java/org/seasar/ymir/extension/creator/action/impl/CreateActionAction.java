package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.util.Map;

import org.seasar.ymir.ActionNotFoundRuntimeException;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.InvalidClassDescException;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.action.UpdateByExceptionAction;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;

public class CreateActionAction extends AbstractAction implements
        UpdateByExceptionAction {
    public CreateActionAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData, Throwable t) {
        if (isSkipButtonPushed(request)) {
            return null;
        }

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("create".equals(subTask)) {
            return actCreate(request, pathMetaData, t);
        } else {
            return actDefault(request, pathMetaData, t);
        }
    }

    Response actDefault(Request request, PathMetaData pathMetaData, Throwable t) {
        ActionNotFoundRuntimeException anfe = (ActionNotFoundRuntimeException) t;

        ClassDesc classDesc = getSourceCreator().newClassDesc(newDescPool(),
                pathMetaData.getClassName(), null);
        String actionName = getSourceCreator().newActionMethodDesc(classDesc,
                anfe.getPath(), anfe.getMethod(), new ActionSelectorSeedImpl())
                .getName();

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("actionName", actionName);
        return getSourceCreator().getResponseCreator().createResponse(
                "createAction", variableMap);
    }

    Response actCreate(Request request, PathMetaData pathMetaData, Throwable t) {
        ActionNotFoundRuntimeException anfe = (ActionNotFoundRuntimeException) t;

        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        ClassDesc classDesc = getSourceCreator().newClassDesc(newDescPool(),
                pathMetaData.getClassName(), null);
        classDesc.setBornOf(pathMetaData.getPath());
        MethodDesc actionMethodDesc = getSourceCreator().newActionMethodDesc(
                classDesc, anfe.getPath(), anfe.getMethod(),
                new ActionSelectorSeedImpl());
        classDesc.setMethodDesc(actionMethodDesc);

        String[] lackingClassNames = null;
        try {
            getSourceCreator().updateClass(classDesc);
        } catch (InvalidClassDescException ex) {
            lackingClassNames = ex.getLackingClassNames();
        }

        boolean successfullySynchronized = synchronizeResources(new String[] { getRootPackagePath() });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("actionName", actionMethodDesc.getName());
        variableMap.put("lackingClassNames", lackingClassNames);
        variableMap.put("successfullySynchronized", successfullySynchronized);
        return getSourceCreator().getResponseCreator().createResponse(
                "createAction_create", variableMap);
    }
}
