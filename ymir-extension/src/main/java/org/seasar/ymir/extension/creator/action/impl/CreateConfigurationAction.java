package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.Application;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.util.SourceCreatorUtils;
import org.seasar.ymir.impl.SingleApplication;
import org.seasar.ymir.util.StringUtils;

public class CreateConfigurationAction extends AbstractAction implements
        UpdateAction {
    private static final String PARAMPREFIX_KEY = SourceCreator.PARAM_PREFIX
            + "key_";

    private static final Log log = LogFactory
            .getLog(CreateConfigurationAction.class);

    public CreateConfigurationAction(SourceCreator sourceCreator) {
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
        Application application = getSourceCreator().getApplication();
        String projectRootFromProperties = SourceCreatorUtils
                .getProjectRootFromLocalProperties(application);
        if (projectRootFromProperties == null) {
            projectRootFromProperties = SourceCreatorUtils
                    .getProjectRootFromProperties(application);
        }
        boolean shouldSpecifyProjectRoot = (projectRootFromProperties != null || application
                .getProjectRoot() == null);
        boolean existsProjectRoot = (projectRootFromProperties != null && new File(
                projectRootFromProperties).exists());
        boolean canBeEmptyProjectRoot = (SourceCreatorUtils
                .findProjectRootDirectory(application) != null);

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("application", application);
        variableMap.put("setting", getSourceCreatorSetting());
        variableMap.put("reconfigured", isReconfigured());
        variableMap.put("shouldSpecifyProjectRoot", shouldSpecifyProjectRoot);
        variableMap.put("existsProjectRoot", existsProjectRoot);
        variableMap.put("projectRootFromProperties", projectRootFromProperties);
        variableMap.put("canBeEmptyProjectRoot", canBeEmptyProjectRoot);
        return getSourceCreator().getResponseCreator().createResponse(
                "createConfiguration", variableMap);
    }

    boolean isReconfigured() {
        return getSourceCreator().getApplication().getFirstRootPackageName() != null;
    }

    Response actCreate(Request request, PathMetaData pathMetaDataf) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        Application application = getSourceCreator().getApplication();
        MapProperties orderedLocalProp = SourceCreatorUtils
                .readLocalPropertiesInOrder(application);
        MapProperties orderedProp = SourceCreatorUtils
                .readPropertiesInOrder(application);
        orderedLocalProp.removeProperty(SingleApplication.KEY_PROJECTROOT);

        SortedSet<String> nameSet = new TreeSet<String>();
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            nameSet.add(itr.next());
        }
        for (Iterator<String> itr = nameSet.iterator(); itr.hasNext();) {
            String name = itr.next();
            if (!name.startsWith(PARAMPREFIX_KEY)) {
                continue;
            }

            Destination destination = Destination.GLOBAL;
            boolean skip = false;
            String key = name.substring(PARAMPREFIX_KEY.length());
            String value = request.getParameter(name).trim();
            if (SingleApplication.KEY_PROJECTROOT.equals(key)) {
                destination = Destination.LOCAL;
                if (value.trim().length() == 0) {
                    skip = true;
                    value = SourceCreatorUtils
                            .findProjectRootDirectory(application);
                }
                application.setProjectRoot(value);
            } else if (SourceCreatorSetting.APPKEY_SOURCECREATOR_SUPERCLASS
                    .equals(key)) {
                if (!StringUtils.isEmpty(value)) {
                    application.setProperty(key, value);
                    ClassDesc classDesc = getSourceCreator().newClassDesc(
                            newDescPool(), value, null);
                    getSourceCreator().prepareForUpdating(classDesc);
                    getSourceCreator().writeEmptyBaseSourceFileIfNotExists(
                            classDesc);
                } else {
                    application.removeProperty(key);
                    skip = true;
                }
            } else {
                application.setProperty(key, value);
            }
            if (!skip) {
                if (destination == Destination.LOCAL) {
                    orderedLocalProp.setProperty(key, value);
                } else if (destination == Destination.GLOBAL) {
                    orderedProp.setProperty(key, value);
                }
            }
        }

        saveProperties(application.getDefaultLocalPropertiesFilePath(),
                orderedLocalProp);
        saveProperties(application.getDefaultPropertiesFilePath(), orderedProp);

        boolean successfullySynchronized = synchronizeResources(null);

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("successfullySynchronized", successfullySynchronized);
        return getSourceCreator().getResponseCreator().createResponse(
                "createConfiguration_create", variableMap);
    }

    private void saveProperties(String filePath, MapProperties prop) {
        if (filePath == null) {
            log.error("Can't save properties: filePath is null");
            return;
        }

        File file = new File(filePath);
        if (!file.exists() && prop.size() == 0) {
            return;
        }

        file.getParentFile().mkdirs();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            SourceCreatorUtils.writeHeader(fos, null);
            prop.store(fos);
        } catch (IOException ex) {
            throw new RuntimeException("Can't write property file: "
                    + file.getAbsolutePath());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    private enum Destination {
        GLOBAL, LOCAL;
    }
}
