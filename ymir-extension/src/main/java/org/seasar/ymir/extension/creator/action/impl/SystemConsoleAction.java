package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.extension.creator.SourceCreator.PREFIX_CHECKEDTIME;
import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.WebappSourceResourceCollector;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.PathMetaDataCollectorRule;
import org.seasar.ymir.extension.creator.impl.TemplateCollectorRule;

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
        } else if ("setSourceCreatorEnabledWithThisTemplate".equals(subTask)) {
            return actSetSourceCreatorEnabledWithThisTemplate(request,
                    pathMetaData);
        } else if ("createPathMappings".equals(subTask)) {
            return actCreatePathMappings(request, pathMetaData);
        } else {
            return actDefault(request, pathMetaData);
        }
    }

    Response actDefault(Request request, PathMetaData pathMetaData) {
        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", request.getMethod());
        variableMap.put("parameters", getParameters(request));
        variableMap
                .put("systemInformation", new SystemInformation(
                        getSourceCreator().getApplication(),
                        getYmirNamingConvention()));
        variableMap.put("sourceCreatorEnabledWithThisTemplate",
                getSourceCreator().getSourceCreatorSetting()
                        .isSourceCreatorEnabledWith(request.getPath()));
        return getSourceCreator().getResponseCreator().createResponse(
                "systemConsole", variableMap);
    }

    YmirNamingConvention getYmirNamingConvention() {
        try {
            return (YmirNamingConvention) getSourceCreator().getApplication()
                    .getS2Container().getComponent(YmirNamingConvention.class);
        } catch (ComponentNotFoundRuntimeException ex) {
            return null;
        }
    }

    Response actConfirmUpdateAllClasses(Request request,
            PathMetaData pathMetaData) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        ClassDescBag classDescBag = getSourceCreator().gatherClassDescs(
                gatherPathMetaDatas(), null);

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
                gatherPathMetaDatas(), null);

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

        boolean successfullySynchronized = synchronizeResources(new String[] { getRootPackagePath() });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("classDescBag", classDescBag);
        variableMap.put("successfullySynchronized", successfullySynchronized);
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
            if (name.startsWith(PREFIX_CHECKEDTIME)) {
                prop.remove(name);
            }
        }

        List<Template> templateList = new WebappSourceResourceCollector<Template>(
                getSourceCreator().getWebappSourceRoot(),
                new TemplateCollectorRule(getSourceCreator())).collect();
        for (Template template : templateList) {
            prop.setProperty(PREFIX_CHECKEDTIME + template.getPath(), String
                    .valueOf(System.currentTimeMillis()));
        }

        getSourceCreator().saveSourceCreatorProperties();

        boolean successfullySynchronized = synchronizeResources(new String[] { adjustPath(getSourceCreator()
                .getSourceCreatorPropertiesFile().getAbsolutePath()) });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("successfullySynchronized", successfullySynchronized);
        return getSourceCreator().getResponseCreator().createResponse(
                "systemConsole_initializeTemplateCheckedTime", variableMap);
    }

    Response actSetSourceCreatorEnabledWithThisTemplate(Request request,
            PathMetaData pathMetaData) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        getSourceCreator().getSourceCreatorSetting()
                .setSourceCreatorEnabledWith(
                        request.getPath(),
                        PropertyUtils.valueOf(request.getParameter("value"),
                                false));

        boolean successfullySynchronized = synchronizeResources(new String[] { adjustPath(getSourceCreator()
                .getSourceCreatorPropertiesFile().getAbsolutePath()) });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("successfullySynchronized", successfullySynchronized);
        return getSourceCreator().getResponseCreator().createResponse(
                "systemConsole_setSourceCreatorEnabledWithThisTemplate",
                variableMap);
    }

    Response actCreatePathMappings(Request request, PathMetaData pathMetaData) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        updateMapping(gatherPathMetaDatas());

        boolean successfullySynchronized = synchronizeResources(new String[] { Globals.PATH_PREFERENCES_DIRECTORY
                + "/" + SourceCreator.MAPPING_PREFS });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("successfullySynchronized", successfullySynchronized);
        return getSourceCreator().getResponseCreator().createResponse(
                "systemConsole_createPathMappings", variableMap);
    }
}
