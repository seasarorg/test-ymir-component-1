package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.Application;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.ClassDescImpl;
import org.seasar.ymir.impl.SingleApplication;
import org.seasar.ymir.util.StringUtils;

public class CreateConfigurationAction extends AbstractAction implements
        UpdateAction {
    private static final String PARAMPREFIX_KEY = SourceCreator.PARAM_PREFIX
            + "key_";

    private static final String POM_XML = "pom.xml";

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
        boolean reconfigured = isReconfigured();
        Application application = getSourceCreator().getApplication();
        String webappRoot = application.getWebappRoot();
        if (webappRoot != null) {
            String projectRoot = findProjectRootDirectory(webappRoot);
            if (projectRoot != null) {
                application.setProjectRoot(projectRoot);
            }
        }

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("application", application);
        variableMap.put("setting", getSourceCreatorSetting());
        variableMap.put("reconfigured", reconfigured);
        return getSourceCreator().getResponseCreator().createResponse(
                "createConfiguration", variableMap);
    }

    boolean isReconfigured() {
        String projectRoot = getSourceCreator().getApplication()
                .getProjectRoot();
        return projectRoot != null && !new File(projectRoot).exists();
    }

    String findProjectRootDirectory(String webappRoot) {
        File dir = new File(webappRoot);
        while (dir != null && !new File(dir, POM_XML).exists()) {
            dir = dir.getParentFile();
        }
        if (dir != null) {
            return dir.getAbsolutePath();
        } else {
            return null;
        }
    }

    Response actCreate(Request request, PathMetaData pathMetaDataf) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        Application application = getSourceCreator().getApplication();
        MapProperties orderedProp = readAppPropertiesInOrder(application);

        SortedSet<String> nameSet = new TreeSet<String>();
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            nameSet.add(itr.next());
        }
        for (Iterator<String> itr = nameSet.iterator(); itr.hasNext();) {
            String name = itr.next();
            if (!name.startsWith(PARAMPREFIX_KEY)) {
                continue;
            }

            boolean remove = false;
            String key = name.substring(PARAMPREFIX_KEY.length());
            String value = request.getParameter(name).trim();
            if (SingleApplication.KEY_PROJECTROOT.equals(key)) {
                application.setProjectRoot(value);
            } else if (SingleApplication.KEY_ROOTPACKAGENAME.equals(key)) {
                application.setRootPackageName(value);
            } else if (SourceCreatorSetting.APPKEY_SOURCECREATOR_SUPERCLASS
                    .equals(key)) {
                if (!StringUtils.isEmpty(value)) {
                    application.setProperty(key, value);
                    getSourceCreator().writeSourceFile("PageSuperClass.java",
                            new ClassDescImpl(value), false);
                } else {
                    application.removeProperty(key);
                    remove = true;
                }
            } else {
                application.setProperty(key, value);
            }
            if (!remove) {
                orderedProp.setProperty(key, value);
            }
        }

        String propertiesFilePath = application.getDefaultPropertiesFilePath();
        if (propertiesFilePath != null) {
            File file = new File(propertiesFilePath);
            file.getParentFile().mkdirs();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                writeHeader(fos, null);
                orderedProp.store(fos);
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

        if (getSourceCreatorSetting().isConverterCreationFeatureEnabled()) {
            getSourceCreator().writeSourceFile(
                    "ConverterSuperClass.java",
                    new ClassDescImpl(application.getRootPackageName()
                            + ".converter.Converter"), false);
        }

        synchronizeResources(null);

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        return getSourceCreator().getResponseCreator().createResponse(
                "createConfiguration_create", variableMap);
    }

    void writeHeader(OutputStream out, String header) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        if (header != null) {
            sb.append(header);
        } else {
            sb.append(new Date());
        }
        out.write(sb.append(System.getProperty("line.separator")).toString()
                .getBytes("ISO-8859-1"));
    }

    MapProperties readAppPropertiesInOrder(Application application) {
        MapProperties prop = new MapProperties(
                new LinkedHashMap<String, String>());
        String filePath = application.getDefaultPropertiesFilePath();
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    prop.load(new FileInputStream(file));
                } catch (IOException ignore) {
                }
            }
        }
        return prop;
    }
}
