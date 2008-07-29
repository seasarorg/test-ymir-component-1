package org.seasar.ymir.extension.creator.action.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
import org.seasar.ymir.Request;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;

abstract public class AbstractAction {
    private static final String PARAM_BUTTON_SKIP = SourceCreator.PARAM_PREFIX
            + "button_skip";

    public static final String PARAM_SUBTASK = SourceCreator.PARAM_PREFIX
            + "subTask";

    private static final int TIMEOUT_MILLISEC = 3 * 1000;

    private final Log log_ = LogFactory.getLog(getClass());

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

        variableMap.put("resourceAutoSynchronized", getSourceCreatorSetting()
                .isResourceSynchronized());

        return variableMap;
    }

    protected void synchronizeResources(String[] paths) {
        if (!getSourceCreatorSetting().isResourceSynchronized()) {
            return;
        }
        if (paths != null && paths.length == 0) {
            return;
        }

        String projectPath = "/"
                + getSourceCreatorSetting().getEclipseProjectName();
        if (paths == null) {
            // プロジェクト全体をリフレッシュする。
            synchronizeResources0(projectPath);
            return;
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

        String[] absolutePaths = absolutePathSet.toArray(new String[0]);
        do {
            Set<String> absolutePathForRetrySet = new LinkedHashSet<String>();
            for (String absolutePath : synchronizeResources0(absolutePaths)) {
                absolutePathForRetrySet.add(getParentPath(absolutePath));
            }
            absolutePaths = absolutePathForRetrySet.toArray(new String[0]);
        } while (absolutePaths.length > 0);
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

    protected String[] synchronizeResources0(String... absolutePaths) {
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
            String response = IOUtils.readString(is, "UTF-8", false);
            okAbsolutePathSet = new HashSet<String>(Arrays.asList(PropertyUtils
                    .toArray(response)));
            if (log_.isDebugEnabled()) {
                log_.debug("Response from " + url + " is:" + response);
            }
        } catch (IOException ex) {
            log_.warn("I/O error occured on a resourceSynchronizing server: "
                    + url, ex);
            return new String[0];
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

    protected String getRootPackagePath() {
        String sourcePath = adjustPath(getSourceCreator().getApplication()
                .getSourceDirectory());
        if (sourcePath == null) {
            return null;
        }
        String rootPackageName = getSourceCreator().getApplication()
                .getRootPackageName();
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
}
