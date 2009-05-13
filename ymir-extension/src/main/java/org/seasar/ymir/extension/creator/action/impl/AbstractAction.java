package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.extension.creator.SourceCreatorSetting.APPKEY_SOURCECREATOR_ECLIPSE_PROJECTNAME;
import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.Application;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;
import org.seasar.ymir.extension.creator.util.PersistentProperties;

abstract public class AbstractAction {
    private static final String PARAM_BUTTON_SKIP = SourceCreator.PARAM_PREFIX
            + "button_skip";

    public static final String PARAM_SUBTASK = SourceCreator.PARAM_PREFIX
            + "subTask";

    private static final int TIMEOUT_MILLISEC = 3 * 1000;

    private static final String SP = System.getProperty("line.separator");

    private static final String RESOURCESYNCHRONIZER_ENCODING = "UTF-8";

    private final Log log_ = LogFactory.getLog(AbstractAction.class);

    private SourceCreator sourceCreator_;

    public AbstractAction(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    public SourceCreator getSourceCreator() {
        return sourceCreator_;
    }

    protected String getSuffix(String name) {
        int dot = name.lastIndexOf('.');
        if (dot < 0) {
            return "";
        } else {
            return name.substring(dot);
        }
    }

    protected String quote(String string) {
        StringBuffer sb = new StringBuffer();
        sb.append('"');
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (ch == '"' || ch == '\\') {
                sb.append('\\');
            }
            sb.append(ch);
        }
        sb.append('"');
        return sb.toString();
    }

    protected Parameter[] getParameters(Request request) {
        List<Parameter> list = new ArrayList<Parameter>();
        for (Iterator<Map.Entry<String, String[]>> itr = request
                .getParameterMap().entrySet().iterator(); itr.hasNext();) {
            Map.Entry<String, String[]> entry = itr.next();
            String name = entry.getKey();
            if (name.startsWith(SourceCreatorImpl.PARAM_PREFIX)) {
                continue;
            }
            String[] values = entry.getValue();
            for (int i = 0; i < values.length; i++) {
                list.add(new Parameter(name, values[i]));
            }
        }
        return list.toArray(new Parameter[0]);
    }

    protected boolean isSkipButtonPushed(Request request) {
        return (request.getParameter(PARAM_BUTTON_SKIP) != null);
    }

    protected HttpMethod getHttpMethod(Request request) {
        return HttpMethod.enumOf(request.getParameter(PARAM_METHOD));
    }

    public static class Parameter {
        private String name_;

        private String value_;

        public Parameter(String name, String value) {
            name_ = name;
            value_ = value;
        }

        public String getName() {
            return name_;
        }

        public String getValue() {
            return value_;
        }
    }

    protected Map<String, Object> newVariableMap() {
        Map<String, Object> variableMap = new HashMap<String, Object>();

        Application application = getSourceCreator().getApplication();
        variableMap.put("sourceCreator", getSourceCreator());
        variableMap.put("resourceAutoSynchronized", getSourceCreatorSetting()
                .isResourceSynchronized());
        variableMap.put("templateEncoding", application.getTemplateEncoding());

        return variableMap;
    }

    protected boolean synchronizeResources(String[] paths) {
        if (!getSourceCreatorSetting().isResourceSynchronized()) {
            return true;
        }
        if (paths != null && paths.length == 0) {
            return true;
        }

        String projectPath = "/"
                + getSourceCreatorSetting().getEclipseProjectName();
        if (paths == null) {
            // プロジェクト全体をリフレッシュする。
            try {
                synchronizeResources0(projectPath);
                return true;
            } catch (IOException ignore) {
                return false;
            }
        }

        Set<String> absolutePathSet = new LinkedHashSet<String>();
        for (String path : paths) {
            if (path == null) {
                continue;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(projectPath);
            if (path.length() > 0 && !path.startsWith("/")) {
                sb.append("/");
            }
            sb.append(path);
            absolutePathSet.add(sb.toString());
        }

        absolutePathSet.add(projectPath + "/"
                + Globals.PATH_PREFERENCES_DIRECTORY);

        try {
            String[] absolutePaths = absolutePathSet.toArray(new String[0]);
            do {
                Set<String> absolutePathForRetrySet = new LinkedHashSet<String>();
                for (String absolutePath : synchronizeResources0(absolutePaths)) {
                    absolutePathForRetrySet.add(getParentPath(absolutePath));
                }
                absolutePaths = absolutePathForRetrySet.toArray(new String[0]);
            } while (absolutePaths.length > 0);

            return true;
        } catch (IOException ignore) {
            return false;
        }
    }

    String getParentPath(String path) {
        if (path == null) {
            return null;
        }
        int slash = path.lastIndexOf('/');
        if (slash < 0) {
            return "";
        }
        return path.substring(0, slash);
    }

    protected String[] synchronizeResources0(String... absolutePaths)
            throws IOException {
        if (absolutePaths != null && absolutePaths.length == 0) {
            return new String[0];
        }

        StringBuilder sb = new StringBuilder().append("refresh?");
        String delim = "";
        for (String absolutePath : absolutePaths) {
            sb.append(delim).append(absolutePath).append("=INFINITE");
            delim = "&";
        }
        URL url = getSourceCreatorSetting().constructResourceSynchronizerURL(
                sb.toString());
        if (url == null) {
            return new String[0];
        }

        InputStream is = null;
        Set<String> okAbsolutePathSet;
        try {
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(TIMEOUT_MILLISEC);
            connection.connect();
            is = connection.getInputStream();
            String response = IOUtils.readString(is,
                    RESOURCESYNCHRONIZER_ENCODING, false);
            okAbsolutePathSet = new HashSet<String>(Arrays.asList(PropertyUtils
                    .toArray(response)));
            if (okAbsolutePathSet.contains("/")) {
                okAbsolutePathSet.add("");
            }
            if (log_.isDebugEnabled()) {
                log_.debug("Response from " + url + " is:" + SP + response);
            }
        } catch (IOException ex) {
            log_.warn("I/O error occured on a resourceSynchronizing server: "
                    + url, ex);
            throw ex;
        } finally {
            IOUtils.closeQuietly(is);
        }

        List<String> ngAbsolutePathList = new ArrayList<String>();
        for (String absolutePath : absolutePaths) {
            if (!okAbsolutePathSet.contains(absolutePath)) {
                ngAbsolutePathList.add(absolutePath);
            }
        }
        return ngAbsolutePathList.toArray(new String[0]);
    }

    protected void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }

    protected void openJavaCodeInEclipseEditor(String className) {
        if (!getSourceCreatorSetting().isResourceSynchronized()) {
            return;
        }
        if (className == null) {
            return;
        }

        URL url;
        try {
            url = getSourceCreatorSetting().constructResourceSynchronizerURL(
                    "select?project="
                            + URLEncoder.encode(getSourceCreatorSetting()
                                    .getEclipseProjectName(),
                                    RESOURCESYNCHRONIZER_ENCODING)
                            + "&classname="
                            + URLEncoder.encode(className,
                                    RESOURCESYNCHRONIZER_ENCODING)
                            + "&openInEditor=true&line=3");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }

        connectToEclipse(url);
    }

    protected void openResourceInEclipseEditor(String path) {
        if (!getSourceCreatorSetting().isResourceSynchronized()) {
            return;
        }
        if (path == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        String projectName = getSourceCreatorSetting().getEclipseProjectName();
        if (projectName == null) {
            log_.warn("Do nothing because property '"
                    + APPKEY_SOURCECREATOR_ECLIPSE_PROJECTNAME
                    + "' is not specified in app.properties");
            return;
        }

        sb.append(projectName);
        if (projectName.endsWith("/")) {
            if (path.startsWith("/")) {
                sb.append(path.substring(1 /*= "/".length() */));
            } else {
                sb.append(path);
            }
        } else {
            if (!path.startsWith("/")) {
                sb.append("/");
            }
            sb.append(path);
        }

        URL url;
        try {
            url = getSourceCreatorSetting().constructResourceSynchronizerURL(
                    "resource?path="
                            + URLEncoder.encode(sb.toString(),
                                    RESOURCESYNCHRONIZER_ENCODING));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }

        connectToEclipse(url);
    }

    protected void connectToEclipse(URL url) {
        if (url == null) {
            return;
        }

        InputStream is = null;
        try {
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(TIMEOUT_MILLISEC);
            connection.connect();
            is = connection.getInputStream();
            String response = IOUtils.readString(is,
                    RESOURCESYNCHRONIZER_ENCODING, false);
            if (log_.isDebugEnabled()) {
                log_.debug("Response from " + url + " is:" + SP + response);
            }
        } catch (IOException ex) {
            log_.warn("I/O error occured on a resourceSynchronizing server: "
                    + url, ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    protected String getRootPackagePath() {
        String sourcePath = adjustPath(getSourceCreator().getApplication()
                .getSourceDirectory());
        if (sourcePath == null) {
            return null;
        }
        String rootPackageName = getSourceCreator().getApplication()
                .getFirstRootPackageName();
        if (rootPackageName == null) {
            return sourcePath;
        } else {
            return sourcePath + "/" + rootPackageName.replace('.', '/');
        }
    }

    protected String getResourcesPath() {
        return adjustPath(getSourceCreator().getApplication()
                .getResourcesDirectory());
    }

    protected String getWebappSourceRootPath() {
        return adjustPath(getSourceCreator().getApplication()
                .getWebappSourceRoot());
    }

    protected String getPath(Template template) {
        if (template == null) {
            return null;
        }
        String webappSourceRootPath = getWebappSourceRootPath();
        if (webappSourceRootPath == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(webappSourceRootPath);
        String templatePath = template.getPath();
        if (!templatePath.startsWith("/")) {
            sb.append("/");
        }
        sb.append(templatePath);
        return sb.toString();
    }

    protected String adjustPath(String filePath) {
        if (filePath == null) {
            return null;
        }
        filePath = filePath.replace('\\', '/');

        String projectRoot = getProjectRoot();
        if (projectRoot == null) {
            return null;
        }

        if (filePath.startsWith(projectRoot)) {
            return filePath.substring(projectRoot.length());
        } else {
            return null;
        }
    }

    protected String getProjectRoot() {
        String projectRoot = getSourceCreator().getApplication()
                .getProjectRoot();
        if (projectRoot == null || projectRoot.length() == 0) {
            return null;
        } else {
            return projectRoot.replace('\\', '/');
        }
    }

    protected SourceCreatorSetting getSourceCreatorSetting() {
        return sourceCreator_.getSourceCreatorSetting();
    }

    protected void updateMapping(PathMetaData... pathMetaDatas) {
        PersistentProperties props = sourceCreator_.getMappingProperties();
        for (PathMetaData pathMetaData : pathMetaDatas) {
            // 「/」はEclipseのPreferencesStoreでキーとしてうまく扱えないようなので、
            // %に置換しておく。
            props.setProperty(pathMetaData.getPath().replace('/', '%'),
                    pathMetaData.getClassName());
            props.setProperty(pathMetaData.getClassName(), pathMetaData
                    .getPath());
        }
        props.save();
    }

    protected DescPool newDescPool() {
        return newDescPool(null);
    }

    protected DescPool newDescPool(ClassCreationHintBag hintBag) {
        return DescPool.newInstance(sourceCreator_, hintBag);
    }
}
