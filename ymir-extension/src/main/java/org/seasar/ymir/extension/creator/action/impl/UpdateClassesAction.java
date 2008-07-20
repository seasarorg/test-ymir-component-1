package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;
import static org.seasar.ymir.extension.Globals.APPKEY_SOURCECREATOR_FEATURE_CREATECONVERTER_ENABLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.AnnotatedDesc;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.PropertyTypeHintBag;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.MetaAnnotationDescImpl;

public class UpdateClassesAction extends AbstractAction implements UpdateAction {
    protected static final String PARAM_APPLY = SourceCreator.PARAM_PREFIX
            + "apply";

    protected static final String PARAMPREFIX_PROPERTYTYPE = SourceCreator.PARAM_PREFIX
            + "propertyType_";

    protected static final String PARAMPREFIX_CONVERTER_PAIRCLASS = SourceCreator.PARAM_PREFIX
            + "converter_pairClass_";

    protected static final String PREFIX_CHECKEDTIME = "updateClassesAction.checkedTime.";

    protected static final String PREFIX_CLASSCHECKED = "updateClassesAction.class.checked.";

    private static final String SUFFIX_ARRAY = "[]";

    private static final String PACKAGEPREFIX_JAVA_LANG = "java.lang.";

    private static final Set<String> primitiveSet_;

    static {
        Set<String> primitiveSet = new HashSet<String>();
        primitiveSet.addAll(Arrays.asList(new String[] { "boolean", "byte",
            "char", "short", "int", "long", "float", "double" }));
        primitiveSet_ = Collections.unmodifiableSet(primitiveSet);
    }

    public UpdateClassesAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData) {
        if (isSkipButtonPushed(request)) {
            return null;
        }

        String subTask = request.getParameter(PARAM_SUBTASK);
        if ("update".equals(subTask)) {
            return actUpdate(request, pathMetaData);
        } else {
            return actDefault(request, pathMetaData);
        }
    }

    Response actDefault(Request request, PathMetaData pathMetaData) {
        if (!shouldUpdate(pathMetaData)) {
            return null;
        }

        ClassDescBag classDescBag = getSourceCreator().gatherClassDescs(
                new PathMetaData[] { pathMetaData });
        if (classDescBag.isEmpty()) {
            return null;
        }

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("template", pathMetaData.getTemplate());
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("createdClassDescs", createClassDescDtos(classDescBag
                .getCreatedClassDescs()));
        variableMap.put("updatedClassDescs", createClassDescDtos(classDescBag
                .getUpdatedClassDescs()));
        variableMap.put("converterCreated", Boolean.valueOf(PropertyUtils
                .valueOf(getSourceCreator().getApplication().getProperty(
                        APPKEY_SOURCECREATOR_FEATURE_CREATECONVERTER_ENABLE),
                        false)));
        return getSourceCreator().getResponseCreator().createResponse(
                "updateClasses", variableMap);
    }

    protected ClassDescDto[] createClassDescDtos(ClassDesc[] classDescs) {
        Properties prop = getSourceCreator().getSourceCreatorProperties();

        ClassDescDto[] dtos = new ClassDescDto[classDescs.length];
        for (int i = 0; i < classDescs.length; i++) {
            String name = classDescs[i].getName();
            ClassType type = classDescs[i].getType();
            if (type == ClassType.DAO || type == ClassType.BEAN
                    || type == ClassType.DXO) {
                dtos[i] = new ClassDescDto(classDescs[i], false);
            } else {
                if (type == ClassType.DTO) {
                    AnnotationDesc[] ads = getSourceCreator()
                            .createAnnotationDescs(
                                    getSourceCreator().getClass(
                                            classDescs[i].getName() + "Base"));
                    for (AnnotationDesc ad : ads) {
                        if (AnnotatedDesc.ANNOTATION_NAME_META.equals(ad
                                .getName())
                                || AnnotatedDesc.ANNOTATION_NAME_METAS
                                        .equals(ad.getName())) {
                            classDescs[i].setAnnotationDesc(ad);
                        }
                    }
                }

                dtos[i] = new ClassDescDto(classDescs[i], PropertyUtils
                        .valueOf(prop.getProperty(PREFIX_CLASSCHECKED + name),
                                true));
            }
        }

        return dtos;
    }

    Response actUpdate(Request request, PathMetaData pathMetaData) {
        String method = request.getParameter(PARAM_METHOD);
        if (method == null) {
            return null;
        }

        List<PropertyTypeHint> hintList = new ArrayList<PropertyTypeHint>();
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            String name = itr.next();
            if (!name.startsWith(PARAMPREFIX_PROPERTYTYPE)) {
                continue;
            }
            String classAndPropertyName = name
                    .substring(PARAMPREFIX_PROPERTYTYPE.length());
            int slash = classAndPropertyName.indexOf('/');
            if (slash < 0) {
                continue;
            }
            String className = classAndPropertyName.substring(0, slash);
            String propertyName = classAndPropertyName.substring(slash + 1);
            String typeName = request.getParameter(name);
            boolean array;
            if (typeName.endsWith(SUFFIX_ARRAY)) {
                array = true;
                typeName = typeName.substring(0, typeName.length()
                        - SUFFIX_ARRAY.length());
            } else {
                array = false;
            }
            typeName = normalizeTypeName(typeName);
            hintList.add(new PropertyTypeHint(className, propertyName,
                    typeName, array));
        }

        ClassDescBag classDescBag = getSourceCreator().gatherClassDescs(
                new PathMetaData[] { pathMetaData },
                new PropertyTypeHintBag(hintList
                        .toArray(new PropertyTypeHint[0])), null);

        String[] appliedClassNames = request.getParameterValues(PARAM_APPLY);
        Set<String> appliedClassNameSet = new HashSet<String>();
        if (appliedClassNames != null) {
            appliedClassNameSet.addAll(Arrays.asList(appliedClassNames));
        }
        Properties prop = getSourceCreator().getSourceCreatorProperties();
        ClassDesc[] classDescs = classDescBag.getClassDescs();
        for (int i = 0; i < classDescs.length; i++) {
            String name = classDescs[i].getName();
            if (classDescs[i].isTypeOf(ClassType.DTO)) {
                Class<?>[] pairClasses = toClasses(request
                        .getParameter(PARAMPREFIX_CONVERTER_PAIRCLASS + name));
                if (pairClasses.length > 0) {
                    classDescs[i].setAnnotationDesc(new MetaAnnotationDescImpl(
                            "conversion", new String[0], pairClasses));
                }
            }

            String checked;
            if (appliedClassNameSet.contains(name)) {
                checked = String.valueOf(true);
            } else {
                checked = String.valueOf(false);
                classDescBag.remove(name);
            }
            prop.setProperty(PREFIX_CLASSCHECKED + name, checked);
        }
        getSourceCreator().saveSourceCreatorProperties();

        getSourceCreator().updateClasses(classDescBag);

        synchronizeResources(new String[] { getRootPackagePath(),
            getPath(pathMetaData.getTemplate()) });

        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("classDescBag", classDescBag);
        variableMap.put("actionName", getSourceCreator().getActionName(
                request.getCurrentDispatch().getPath(), method));
        variableMap.put("suggestionExists", Boolean
                .valueOf(classDescBag.getClassDescMap(ClassType.PAGE).size()
                        + classDescBag.getCreatedClassDescMap(ClassType.BEAN)
                                .size() > 0));
        variableMap.put("pageClassDescs", classDescBag
                .getClassDescs(ClassType.PAGE));
        variableMap.put("renderActionName", RequestProcessor.METHOD_RENDER);
        variableMap.put("createdBeanClassDescs", classDescBag
                .getCreatedClassDescs(ClassType.BEAN));
        return getSourceCreator().getResponseCreator().createResponse(
                "updateClasses_update", variableMap);
    }

    Class<?>[] toClasses(String classString) {
        String[] classNames = PropertyUtils.toLines(classString, ",");
        List<Class<?>> list = new ArrayList<Class<?>>(classNames.length);
        for (String className : classNames) {
            Class<?> clazz = getSourceCreator().getClass(className);
            if (clazz != null) {
                list.add(clazz);
            }
        }
        return list.toArray(new Class[0]);
    }

    String normalizeTypeName(String typeName) {
        if (typeName == null || typeName.indexOf('.') >= 0
                || primitiveSet_.contains(typeName)) {
            return typeName;
        } else {
            return PACKAGEPREFIX_JAVA_LANG + typeName;
        }
    }

    boolean shouldUpdate(PathMetaData pathMetaData) {
        Template template = pathMetaData.getTemplate();
        if (template == null || !template.exists()) {
            return false;
        }
        boolean shouldUpdate = (template.lastModified() > getCheckedTime(template));
        if (shouldUpdate) {
            updateCheckedTime(template);
        }
        return shouldUpdate;
    }

    long getCheckedTime(Template template) {
        Properties prop = getSourceCreator().getSourceCreatorProperties();
        String key = PREFIX_CHECKEDTIME + template.getPath();
        String timeString = prop.getProperty(key);
        long time;
        if (timeString == null) {
            time = 0L;
        } else {
            time = Long.parseLong(timeString);
        }

        return time;
    }

    void updateCheckedTime(Template template) {
        Properties prop = getSourceCreator().getSourceCreatorProperties();
        String key = PREFIX_CHECKEDTIME + template.getPath();
        prop.setProperty(key, String.valueOf(System.currentTimeMillis()));
        getSourceCreator().saveSourceCreatorProperties();
    }
}
