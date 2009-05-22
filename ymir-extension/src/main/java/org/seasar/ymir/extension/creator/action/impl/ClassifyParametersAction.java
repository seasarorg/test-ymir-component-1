package org.seasar.ymir.extension.creator.action.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.ClassHint;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.zpt.ParameterRole;

public class ClassifyParametersAction extends AbstractAction implements
        UpdateAction {
    protected static final String PARAMPREFIX_PARAMETERROLE = SourceCreator.PARAM_PREFIX
            + "parameterRole_";

    public ClassifyParametersAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData) {
        if (isSkipButtonPushed(request)) {
            return null;
        }

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("classify".equals(subTask)) {
            return actClassify(request, pathMetaData);
        } else {
            return actDefault(request, pathMetaData);
        }
    }

    boolean shouldUpdate(Request request, PathMetaData pathMetaData) {
        // テンプレートの変更を検出して自動的に自動生成処理（updateClasses）を行なうような設定で、かつ
        // テンプレートが変更されている場合は、ここでは処理せずにupdateClassesに処理を任せた方が
        // 精度の高い自動生成が行なえるため、falseを返すようにする。
        if (isTryingToUpdateClassesWhenTemplateModifiedAndModified(request,
                pathMetaData)) {
            return false;
        }

        // ボタンかもしれないパラメータつきリクエストに対応するアクションがなければ作成するようにする。
        return getUndecidedParameterNames(request, pathMetaData, null).length > 0;
    }

    boolean isTryingToUpdateClassesWhenTemplateModifiedAndModified(
            Request request, PathMetaData pathMetaData) {
        Template template = getSourceCreator().getTemplate(
                pathMetaData.getPath());
        if (!template.exists()) {
            return false;
        }

        if (getSourceCreatorSetting()
                .isTryingToUpdateClassesWhenTemplateModified()) {
            return (template.lastModified() > getSourceCreator()
                    .getCheckedTime(template));
        } else {
            return false;
        }
    }

    protected String[] getUndecidedParameterNames(Request request,
            PathMetaData pathMetaData, ClassCreationHintBag hintBag) {
        String path = pathMetaData.getPath();
        HttpMethod method = request.getMethod();
        String className = pathMetaData.getClassName();
        ClassHint classHint = null;
        if (hintBag != null) {
            classHint = hintBag.getClassHint(className);
        }

        List<String> list = new ArrayList<String>();
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            String name = itr.next();
            if ("".equals(request.getParameter(name))
                    && getSourceCreator().inferParameterRole(path, method,
                            className, name, classHint) == ParameterRole.UNDECIDED) {
                list.add(getSourceCreator().getActionKeyFromParameterName(path,
                        method, name));
            }
        }
        return list.toArray(new String[0]);
    }

    Response actDefault(Request request, PathMetaData pathMetaData) {
        if (!shouldUpdate(request, pathMetaData)) {
            return null;
        }

        // この自動生成ロジックはPRGのgetに関して行なうべきものであるため、
        // 空のパラメータを持つformでsubmitを押された場合はこの処理を行ないたくない。
        // そのため、HTTP methodがpostなら何もしないようにする。
        // HTTP methodがgetの場合はリクエストがformのsubmitなのかPRGのgetなのか
        // 区別がつかないため、画面の注釈を見てもらってユーザに判断してもらうようにする。
        if (request.getMethod() != HttpMethod.GET) {
            return null;
        }

        DescPool pool = newDescPool();
        pool.setBornOf(Globals.BORNOFPREFIX_REQUEST + pathMetaData.getPath());
        ClassDesc classDesc = getSourceCreator().newClassDesc(pool,
                pathMetaData.getClassName(), null);
        classDesc.setAttribute(Globals.ATTR_UNDECIDEDPARAMETERNAMES,
                getUndecidedParameterNames(request, pathMetaData, null));

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("template", pathMetaData.getTemplate());
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("classDesc", new ClassDescDto(classDesc, false));
        return getSourceCreator().getResponseCreator().createResponse(
                "classifyParameters", variableMap);
    }

    Response actClassify(Request request, PathMetaData pathMetaData) {
        HttpMethod method = getHttpMethod(request);
        if (method == null) {
            return null;
        }

        updateMapping(pathMetaData);

        Map<String, ClassHint> classHintMap = new HashMap<String, ClassHint>();
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            String name = itr.next();
            if (name.startsWith(PARAMPREFIX_PARAMETERROLE)) {
                String classAndParameterName = name
                        .substring(PARAMPREFIX_PARAMETERROLE.length());
                int slash = classAndParameterName.indexOf('/');
                if (slash < 0) {
                    continue;
                }
                String className = classAndParameterName.substring(0, slash);
                String parameterName = classAndParameterName
                        .substring(slash + 1);
                ParameterRole role = ParameterRole.valueOf(request
                        .getParameter(name));

                ClassHint classHint = classHintMap.get(className);
                if (classHint == null) {
                    classHint = new ClassHint(className);
                    classHintMap.put(className, classHint);
                }
                classHint.setParameterRole(parameterName, role);
            }
        }

        ClassCreationHintBag hintBag = new ClassCreationHintBag(
                new PropertyTypeHint[0], classHintMap.values().toArray(
                        new ClassHint[0]));

        DescPool pool = newDescPool(hintBag);
        pool.setBornOf(Globals.BORNOFPREFIX_REQUEST + pathMetaData.getPath());
        getSourceCreator().buildTransitionClassDesc(pool,
                pathMetaData.getPath(), method,
                filterSystemParamers(request.getParameterMap()));

        ClassDescBag classDescBag = getSourceCreator().gatherClassDescs(pool,
                null, false, pathMetaData);

        getSourceCreator().updateClasses(classDescBag);

        boolean successfullySynchronized = synchronizeResources(new String[] { getRootPackagePath() });
        pause(1000L);
        openJavaCodeInEclipseEditor(pathMetaData.getClassName());

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("classDescBag", classDescBag);
        variableMap.put("successfullySynchronized", successfullySynchronized);
        return getSourceCreator().getResponseCreator().createResponse(
                "classifyParameters_classify", variableMap);
    }

    private Map<String, String[]> filterSystemParamers(
            Map<String, String[]> parameterMap) {
        if (parameterMap == null) {
            return null;
        }
        Map<String, String[]> map = new LinkedHashMap<String, String[]>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(SourceCreator.PARAM_PREFIX)) {
                map.put(key, entry.getValue());
            }
        }
        return map;
    }
}
