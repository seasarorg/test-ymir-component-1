package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.extension.creator.SourceCreator.PARAM_TASK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.AnnotatedDesc;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.ClassHint;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.impl.MetaAnnotationDescImpl;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.GenericsUtils;
import org.seasar.ymir.extension.creator.util.type.Token;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
import org.seasar.ymir.extension.zpt.ParameterRole;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;

public class UpdateClassesAction extends AbstractAction implements UpdateAction {
    protected static final String PARAM_BUTTON_ALWAYS_SKIP = SourceCreator.PARAM_PREFIX
            + "button_always_skip";

    protected static final String PARAM_APPLY = SourceCreator.PARAM_PREFIX
            + "apply";

    protected static final String PARAMPREFIX_PROPERTYTYPE = SourceCreator.PARAM_PREFIX
            + "propertyType_";

    protected static final String PARAMPREFIX_CONVERTER_PAIRTYPENAME = SourceCreator.PARAM_PREFIX
            + "converter_pairTypeName_";

    protected static final String PARAMPREFIX_CLASSNAME = SourceCreator.PARAM_PREFIX
            + "className_";

    protected static final String PARAMPREFIX_SUPERCLASSNAME = SourceCreator.PARAM_PREFIX
            + "superclassName_";

    protected static final String PARAMPREFIX_PARAMETERROLE = SourceCreator.PARAM_PREFIX
            + "parameterRole_";

    protected static final String PREFIX_CLASSCHECKED = "updateClassesAction.class.checked.";

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
        if (!shouldUpdate(request, pathMetaData)) {
            return null;
        }

        Notes warnings = new Notes();
        ClassDescBag classDescBag = getSourceCreator().gatherClassDescs(
                newDescPool(), warnings, true, pathMetaData);
        if (classDescBag.isEmpty()) {
            return null;
        }

        Set<ClassDto> ambiguousClassSet = new TreeSet<ClassDto>();
        for (ClassDesc cd : classDescBag.getClassDescs()) {
            ClassDto classDto = null;
            for (PropertyDesc pd : cd.getPropertyDescs()) {
                if (BeanUtils.isAmbiguousPropertyName(pd.getName())) {
                    if (classDto == null) {
                        classDto = new ClassDto(cd.getName());
                        ambiguousClassSet.add(classDto);
                    }
                    classDto.addProperty(new PropertyDto(pd.getName()));
                }
            }
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
        variableMap.put("converterCreated", getSourceCreatorSetting()
                .isConverterCreationFeatureEnabled());
        variableMap.put("ambiguousClasses", ambiguousClassSet
                .toArray(new ClassDto[0]));
        variableMap.put("warnings", warnings);
        return getSourceCreator().getResponseCreator().createResponse(
                "updateClasses", variableMap);
    }

    protected ClassDescDto[] createClassDescDtos(ClassDesc[] classDescs) {
        ClassDescDto[] dtos = new ClassDescDto[classDescs.length];
        for (int i = 0; i < classDescs.length; i++) {
            dtos[i] = createClassDescDto(classDescs[i]);
        }

        return dtos;
    }

    protected ClassDescDto createClassDescDto(ClassDesc classDesc) {
        Properties prop = getSourceCreator().getSourceCreatorProperties();

        ClassDescDto dto;
        String name = classDesc.getName();
        ClassType type = classDesc.getType();
        if (type == ClassType.DAO || type == ClassType.BEAN
                || type == ClassType.DXO) {
            dto = new ClassDescDto(classDesc, false);
        } else {
            if (type == ClassType.DTO) {
                AnnotationDesc[] ads = DescUtils
                        .newAnnotationDescs(getSourceCreator().getClass(
                                classDesc.getName() + "Base"));
                for (AnnotationDesc ad : ads) {
                    if (AnnotatedDesc.ANNOTATION_NAME_META.equals(ad.getName())
                            || AnnotatedDesc.ANNOTATION_NAME_METAS.equals(ad
                                    .getName())) {
                        classDesc.setAnnotationDesc(ad);
                    }
                }
            }

            dto = new ClassDescDto(classDesc, PropertyUtils.valueOf(prop
                    .getProperty(PREFIX_CLASSCHECKED + name), true));
        }

        return dto;
    }

    Response actUpdate(Request request, PathMetaData pathMetaData) {
        if (request.getParameter(PARAM_BUTTON_ALWAYS_SKIP) != null) {
            getSourceCreator().getSourceCreatorSetting()
                    .setSourceCreatorEnabledWith(request.getPath(), false);
            return null;
        }

        HttpMethod method = getHttpMethod(request);
        if (method == null) {
            return null;
        }

        updateMapping(pathMetaData);

        ClassNameMapping classNameMapping = new ClassNameMapping(request
                .getParameterMap());

        Notes warnings = new Notes();

        List<PropertyTypeHint> propertyTypeHintList = new ArrayList<PropertyTypeHint>();
        Map<String, ClassHint> classHintMap = new HashMap<String, ClassHint>();
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            String name = itr.next();
            if (name.startsWith(PARAMPREFIX_PROPERTYTYPE)) {
                String classAndPropertyName = name
                        .substring(PARAMPREFIX_PROPERTYTYPE.length());
                int slash = classAndPropertyName.indexOf('/');
                if (slash < 0) {
                    continue;
                }
                String actualClassName = classNameMapping
                        .toActual(classAndPropertyName.substring(0, slash));
                String propertyName = classAndPropertyName.substring(slash + 1);
                String typeName = request.getParameter(name);

                typeName = resolveTypeName(typeName, actualClassName, warnings);
                propertyTypeHintList.add(new PropertyTypeHint(actualClassName,
                        propertyName, typeName));
            } else if (name.startsWith(PARAMPREFIX_SUPERCLASSNAME)) {
                String className = name.substring(PARAMPREFIX_SUPERCLASSNAME
                        .length());
                String actualClassName = classNameMapping.toActual(className);
                String superclassName = resolveTypeName(request
                        .getParameter(name), actualClassName, warnings);

                ClassHint classHint = classHintMap.get(actualClassName);
                if (classHint == null) {
                    classHint = new ClassHint(actualClassName);
                    classHintMap.put(actualClassName, classHint);
                }
                classHint.setSuperclassName(superclassName);
            } else if (name.startsWith(PARAMPREFIX_PARAMETERROLE)) {
                String classAndParameterName = name
                        .substring(PARAMPREFIX_PARAMETERROLE.length());
                int slash = classAndParameterName.indexOf('/');
                if (slash < 0) {
                    continue;
                }
                String actualClassName = classNameMapping
                        .toActual(classAndParameterName.substring(0, slash));
                String parameterName = classAndParameterName
                        .substring(slash + 1);
                ParameterRole role = ParameterRole.valueOf(request
                        .getParameter(name));

                ClassHint classHint = classHintMap.get(actualClassName);
                if (classHint == null) {
                    classHint = new ClassHint(actualClassName);
                    classHintMap.put(actualClassName, classHint);
                }
                classHint.setParameterRole(parameterName, role);
            }
        }

        ClassCreationHintBag hintBag = new ClassCreationHintBag(
                propertyTypeHintList.toArray(new PropertyTypeHint[0]),
                classHintMap.values().toArray(new ClassHint[0]));
        ClassDescBag classDescBag = getSourceCreator().gatherClassDescs(
                newDescPool(hintBag), warnings, true, pathMetaData);

        String[] appliedOriginalClassNames = request
                .getParameterValues(PARAM_APPLY);
        Set<String> appliedActualClassNameSet = new HashSet<String>();
        if (appliedOriginalClassNames != null) {
            for (String appliedOriginalClassName : appliedOriginalClassNames) {
                appliedActualClassNameSet.add(classNameMapping
                        .toActual(appliedOriginalClassName));
            }
        }

        Properties prop = getSourceCreator().getSourceCreatorProperties();
        ClassDesc[] classDescs = classDescBag.getClassDescs();
        for (int i = 0; i < classDescs.length; i++) {
            String actualName = classDescs[i].getName();
            if (classDescs[i].isTypeOf(ClassType.DTO)) {
                String[] pairTypeNames = resolveTypeNames(request
                        .getParameter(PARAMPREFIX_CONVERTER_PAIRTYPENAME
                                + classNameMapping.toOriginal(actualName)),
                        actualName, warnings);
                if (pairTypeNames.length > 0) {
                    classDescs[i].setAnnotationDesc(new MetaAnnotationDescImpl(
                            "conversion", pairTypeNames, new Class[0]));
                }
            }

            String checked;
            if (appliedActualClassNameSet.contains(actualName)) {
                checked = String.valueOf(true);
            } else {
                checked = String.valueOf(false);
                classDescBag.remove(actualName);
            }
            prop.setProperty(PREFIX_CLASSCHECKED + actualName, checked);
        }
        getSourceCreator().saveSourceCreatorProperties();

        getSourceCreator().updateClasses(classDescBag);

        boolean successfullySynchronized = synchronizeResources(new String[] {
            getRootPackagePath(), getPath(pathMetaData.getTemplate()) });
        pause(1000L);
        openJavaCodeInEclipseEditor(pathMetaData.getClassName());

        DescPool pool = newDescPool();
        String path = pathMetaData.getPath();
        Map<String, Object> variableMap = newVariableMap();
        variableMap.put("request", request);
        variableMap.put("method", method);
        variableMap.put("parameters", getParameters(request));
        variableMap.put("pathMetaData", pathMetaData);
        variableMap.put("classDescBag", classDescBag);
        variableMap.put("actionName", getSourceCreator().newActionMethodDesc(
                pool, path, method, new ActionSelectorSeedImpl()).getName());
        variableMap.put("suggestionExists", Boolean
                .valueOf(classDescBag.getClassDescMap(ClassType.PAGE).size()
                        + classDescBag.getCreatedClassDescMap(ClassType.BEAN)
                                .size() > 0));
        variableMap.put("pageClassDescs", classDescBag
                .getClassDescs(ClassType.PAGE));
        variableMap.put("renderActionName", getSourceCreator()
                .getExtraPathMapping(path, method)
                .newPrerenderActionMethodDesc(pool,
                        new ActionSelectorSeedImpl()).getName());
        variableMap.put("createdBeanClassDescs", classDescBag
                .getCreatedClassDescs(ClassType.BEAN));
        variableMap.put("successfullySynchronized", successfullySynchronized);
        variableMap.put("warnings", warnings);
        return getSourceCreator().getResponseCreator().createResponse(
                "updateClasses_update", variableMap);
    }

    String[] resolveTypeNames(String typeNameString, String baseClassName,
            Notes warnings) {
        String[] typeNames = PropertyUtils.toLines(typeNameString, ",");
        List<String> list = new ArrayList<String>(typeNames.length);
        for (String typeName : typeNames) {
            list.add(resolveTypeName(typeName, baseClassName, warnings));
        }
        return list.toArray(new String[0]);
    }

    String resolveTypeName(String typeName, final String baseClassName,
            final Notes warnings) {
        if (typeName == null) {
            return null;
        } else if (typeName.length() == 0) {
            return Object.class.getName();
        }

        TypeToken type = new TypeToken(typeName);
        type.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor) {
                String name = GenericsUtils.getComponentName(acceptor
                        .getBaseName());
                if (name.indexOf('.') < 0 && !ClassUtils.isPrimitive(name)) {
                    String className;
                    Class<?> clazz = getSourceCreator().findClass(name,
                            baseClassName);
                    if (clazz == null) {
                        clazz = getSourceCreator().getClass(name);
                    }
                    if (clazz != null) {
                        className = clazz.getName();
                    } else {
                        className = ClassUtils.getPackageName(baseClassName)
                                + "." + name;
                        warnings.add(new Note("warning.cannotResolveTypeName",
                                name));
                    }
                    acceptor.setBaseName(ClassUtils.getClassName(className,
                            ClassUtils.isArray(acceptor.getBaseName())));
                }
                return null;
            }
        });
        return type.getAsString();
    }

    boolean shouldUpdate(Request request, PathMetaData pathMetaData) {
        Template template = pathMetaData.getTemplate();
        if (template == null || !template.exists()) {
            return false;
        }

        // タスクが明示的に指定されている場合は「UPDATE CLASSES」ボタンからの遷移なので
        // 常にUpdate処理を行なうことにする。
        if (request.getParameter(PARAM_TASK) != null) {
            return true;
        }

        if (getSourceCreatorSetting()
                .isTryingToUpdateClassesWhenTemplateModified()) {
            boolean shouldUpdate = (template.lastModified() > getSourceCreator()
                    .getCheckedTime(template));
            if (shouldUpdate) {
                getSourceCreator().updateCheckedTime(template);
            }
            return shouldUpdate;
        } else {
            return false;
        }
    }

    protected static class ClassNameMapping {
        private Map<String, String> toActualClassNameMap_ = new HashMap<String, String>();

        private Map<String, String> toOriginalClassNameMap_ = new HashMap<String, String>();

        protected ClassNameMapping(Map<String, String[]> paramMap) {
            initialize(paramMap);
        }

        public String toOriginal(String actualClassName) {
            String originalClassName = toOriginalClassNameMap_
                    .get(actualClassName);
            if (originalClassName == null) {
                return actualClassName;
            } else {
                return originalClassName;
            }
        }

        public String toActual(String originalClassName) {
            String actualClassName = toActualClassNameMap_
                    .get(originalClassName);
            if (actualClassName == null) {
                return originalClassName;
            } else {
                return actualClassName;
            }
        }

        void initialize(Map<String, String[]> paramMap) {
            // initializeのテスト用。
            if (paramMap == null) {
                return;
            }

            for (Iterator<String> itr = paramMap.keySet().iterator(); itr
                    .hasNext();) {
                String name = itr.next();
                if (!name.startsWith(PARAMPREFIX_CLASSNAME)) {
                    continue;
                }
                String originalClassName = name.substring(PARAMPREFIX_CLASSNAME
                        .length());
                String actualClassName = paramMap.get(name)[0];
                if (actualClassName.trim().length() == 0) {
                    actualClassName = originalClassName;
                }
                toActualClassNameMap_.put(originalClassName, actualClassName);
                toOriginalClassNameMap_.put(actualClassName, originalClassName);
            }
        }
    }
}
