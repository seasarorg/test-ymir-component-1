package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.LazyPathMetaData;

public class SystemConsoleAction extends AbstractAction implements UpdateAction {

    private static final String PARAM_APPLY = SourceCreator.PARAM_PREFIX
            + "apply";

    public SystemConsoleAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData) {

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("confirmUpdateAllClasses".equals(subTask)) {
            return actConfirmUpdateAllClasses(request, pathMetaData);
        } else if ("updateAllClasses".equals(subTask)) {
            return actUpdateAllClasses(request, pathMetaData);
        } else {
            return actDefault(request, pathMetaData);
        }
    }

    Response actDefault(Request request, PathMetaData pathMetaData) {

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("method", request.getMethod());
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
                gatherPaths());

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("method", method);
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
                gatherPaths());

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

        getSourceCreator().updateClasses(classDescBag, false);

        synchronizeResources(new String[] { getRootPackagePath() });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("method", method);
        variableMap.put("classDescBag", classDescBag);
        return getSourceCreator().getResponseCreator().createResponse(
                "systemConsole_updateAllClasses", variableMap);
    }

    PathMetaData[] gatherPaths() {

        List<PathMetaData> pathList = new ArrayList<PathMetaData>();
        File dir = getSourceCreator().getWebappSourceRoot();
        gatherPaths(dir, "", pathList);
        return pathList.toArray(new PathMetaData[0]);
    }

    void gatherPaths(File root, String path, List<PathMetaData> pathList) {

        File dir;
        if (path.length() == 0) {
            dir = root;
        } else {
            dir = new File(root, path);
        }
        File[] childFiles = dir.listFiles();
        for (int i = 0; i < childFiles.length; i++) {
            String childName = childFiles[i].getName();
            String childPath = path + "/" + childName;
            if (shouldIgnore(childPath, childName)) {
                continue;
            }
            if (childFiles[i].isDirectory()) {
                gatherPaths(root, childPath, pathList);
            } else {
                addPathMetaDataIfNecessary(childPath, Request.METHOD_GET,
                        pathList);
                addPathMetaDataIfNecessary(childPath, Request.METHOD_POST,
                        pathList);
            }
        }
    }

    void addPathMetaDataIfNecessary(String path, String method,
            List<PathMetaData> pathList) {

        PathMetaData pathMetaData = new LazyPathMetaData(getSourceCreator(),
                path, method, path);
        if (pathMetaData.getComponentName() != null && !pathMetaData.isDenied()) {
            pathList.add(pathMetaData);
        }
    }

    boolean shouldIgnore(String path, String name) {

        if (path.equals("/WEB-INF/classes") || path.equals("/WEB-INF/lib")
                || path.equals("/WEB-INF/web.xml") || path.equals("/META-INF")) {
            return true;
        }

        if ("CVS".equals(name) || ".svn".equals(name) || "_svn".equals(name)) {
            return true;
        }

        return false;
    }
}
