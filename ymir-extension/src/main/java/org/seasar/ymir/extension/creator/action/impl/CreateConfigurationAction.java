package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.beantable.Globals.APPKEY_BEANTABLE_ENABLE;
import static org.seasar.ymir.extension.Globals.APPKEY_SOURCECREATOR_ECLIPSE_ENABLE;
import static org.seasar.ymir.extension.Globals.APPKEY_SOURCECREATOR_ECLIPSE_PROJECTNAME;
import static org.seasar.ymir.extension.Globals.APPKEY_SOURCECREATOR_ECLIPSE_RESOURCESYNCHRONIZERURL;
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

import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.Application;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.impl.SingleApplication;

public class CreateConfigurationAction extends AbstractAction implements
        UpdateAction {

    private static final String PARAMPREFIX_KEY = SourceCreator.PARAM_PREFIX
            + "key_";

    private static final String POM_XML = "pom.xml";

    private static final String RESOURCESYNCHRONIZERURL_DEFAULT = "http://localhost:8386/";

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
        variableMap.put("beantableEnable", Boolean.valueOf(PropertyUtils
                .valueOf(application.getProperty(APPKEY_BEANTABLE_ENABLE),
                        false)));
        variableMap.put("eclipseEnable", Boolean.valueOf(PropertyUtils.valueOf(
                application.getProperty(APPKEY_SOURCECREATOR_ECLIPSE_ENABLE),
                false)));
        variableMap.put("projectName", application
                .getProperty(APPKEY_SOURCECREATOR_ECLIPSE_PROJECTNAME));
        variableMap.put("resourceSynchronizerURL", application.getProperty(
                APPKEY_SOURCECREATOR_ECLIPSE_RESOURCESYNCHRONIZERURL,
                RESOURCESYNCHRONIZERURL_DEFAULT));
        return getSourceCreator().getResponseCreator().createResponse(
                "createConfiguration", variableMap);
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
            String key = name.substring(PARAMPREFIX_KEY.length());
            String value = request.getParameter(name);
            if (SingleApplication.KEY_PROJECTROOT.equals(key)) {
                application.setProjectRoot(value);
            } else if (SingleApplication.KEY_ROOTPACKAGENAME.equals(key)) {
                application.setRootPackageName(value);
            } else {
                application.setProperty(key, value);
            }
            orderedProp.setProperty(key, value);
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
        MapProperties prop = new MapProperties(new LinkedHashMap());
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
