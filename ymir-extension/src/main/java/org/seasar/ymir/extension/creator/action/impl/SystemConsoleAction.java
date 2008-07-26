package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.WebappSourceResourceCollector;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.PathMetaDataCollectorRule;

public class SystemConsoleAction extends AbstractAction implements UpdateAction {
    private static final String PARAM_APPLY = SourceCreator.PARAM_PREFIX
            + "apply";

    public SystemConsoleAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData) {
        if (isSkipButtonPushed(request)) {
            return null;
        }

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("confirmUpdateAllClasses".equals(subTask)) {
            return actConfirmUpdateAllClasses(request, pathMetaData);
        } else if ("updateAllClasses".equals(subTask)) {
            return actUpdateAllClasses(request, pathMetaData);
        } else if ("initializeTemplateCheckedTime".equals(subTask)) {
            return actInitializeTemplateCheckedTime(request, pathMetaData);
        } else {
            return actDefault(request, pathMetaData);
        }
    }

    Response actDefault(Request request, PathMetaData pathMetaData) {
        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", request.getMethod());
        variableMap.put("parameters", getParameters(request));
        return getSourceCreator().getResponseCreator().createResponse(
                "systemConsole", variableMap);
    }

    Response actConfirmUpdateAllClasses(Request request,
            PathMetaData pathMetaData) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        ClassDescBag classDescBag = getSourceCreator().gatherClassDescs(
                gatherPathMetaDatas());

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("classDescBag", classDescBag);
        return getSourceCreator().getResponseCreator().createResponse(
                "systemConsole_confirmUpdateAllClasses", variableMap);
    }

    Response actUpdateAllClasses(Request request, PathMetaData pathMetaData) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        ClassDescBag classDescBag = getSourceCreator().gatherClassDescs(
                gatherPathMetaDatas());

        String[] appliedClassNames = request.getParameterValues(PARAM_APPLY);
        Set<String> appliedClassNameSet = new HashSet<String>();
        if (appliedClassNames != null) {
            appliedClassNameSet.addAll(Arrays.asList(appliedClassNames));
        }
        ClassDesc[] classDescs = classDescBag.getClassDescs();
        for (int i = 0; i < classDescs.length; i++) {
            String name = classDescs[i].getName();
            if (!appliedClassNameSet.contains(name)) {
                classDescBag.remove(name);
            }
        }

        getSourceCreator().updateClasses(classDescBag);

        synchronizeResources(new String[] { getRootPackagePath() });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("classDescBag", classDescBag);
        return getSourceCreator().getResponseCreator().createResponse(
                "systemConsole_updateAllClasses", variableMap);
    }

    PathMetaData[] gatherPathMetaDatas() {
        return new WebappSourceResourceCollector<PathMetaData>(
                getSourceCreator().getWebappSourceRoot(),
                new PathMetaDataCollectorRule(getSourceCreator())).collect()
                .toArray(new PathMetaData[0]);
    }

    Response actInitializeTemplateCheckedTime(Request request,
            PathMetaData pathMetaData) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        Properties prop = getSourceCreator().getSourceCreatorProperties();
        for (Enumeration<?> enm = prop.propertyNames(); enm.hasMoreElements();) {
            String name = (String) enm.nextElement();
            if (!name.startsWith(SourceCreator.PREFIX_CHECKEDTIME)) {
                continue;
            }
            prop.setProperty(name, String.valueOf(System.currentTimeMillis()));
        }
        getSourceCreator().saveSourceCreatorProperties();

        synchronizeResources(new String[] { adjustPath(getSourceCreator()
                .getSourceCreatorPropertiesFile().getAbsolutePath()) });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        return getSourceCreator().getResponseCreator().createResponse(
                "systemConsole_initializeTemplateCheckedTime", variableMap);
    }
}
