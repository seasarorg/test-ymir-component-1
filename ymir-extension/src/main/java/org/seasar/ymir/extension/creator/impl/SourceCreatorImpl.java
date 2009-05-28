package org.seasar.ymir.extension.creator.impl;

import static java.beans.Introspector.decapitalize;
import static org.seasar.ymir.constraint.Globals.APPKEY_CORE_CONSTRAINT_PERMISSIONDENIEDMETHOD_ENABLE;
import static org.seasar.ymir.constraint.Globals.APPKEY_CORE_CONSTRAINT_VALIDATIONFAILEDMETHOD_ENABLE;
import static org.seasar.ymir.extension.creator.PropertyDesc.PROBABILITY_DEFAULT;
import static org.seasar.ymir.extension.creator.PropertyDesc.PROBABILITY_MAXIMUM;
import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;
import static org.seasar.ymir.util.ClassUtils.getShortName;
import static org.seasar.ymir.util.ClassUtils.getShorterName;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.pluggable.ClassTraverser;
import org.seasar.cms.pluggable.hotdeploy.LocalHotdeployS2Container;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.StringUtils;
import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.ActionNotFoundRuntimeException;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseCreator;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.constraint.ConstraintInterceptor;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.Born;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.ClassDescModifier;
import org.seasar.ymir.extension.creator.ClassDescSet;
import org.seasar.ymir.extension.creator.ClassHint;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.Desc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.DescValidator;
import org.seasar.ymir.extension.creator.EntityMetaData;
import org.seasar.ymir.extension.creator.ImportDesc;
import org.seasar.ymir.extension.creator.InvalidClassDescException;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.MethodDescKey;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.SourceGenerator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.TemplateAnalyzer;
import org.seasar.ymir.extension.creator.TemplateProvider;
import org.seasar.ymir.extension.creator.ThrowsDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.DescValidator.Result;
import org.seasar.ymir.extension.creator.action.ActionSelector;
import org.seasar.ymir.extension.creator.action.Condition;
import org.seasar.ymir.extension.creator.action.State;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.action.UpdateByExceptionAction;
import org.seasar.ymir.extension.creator.action.impl.ClassifyParametersAction;
import org.seasar.ymir.extension.creator.action.impl.CreateActionAction;
import org.seasar.ymir.extension.creator.action.impl.CreateClassAction;
import org.seasar.ymir.extension.creator.action.impl.CreateClassAndTemplateAction;
import org.seasar.ymir.extension.creator.action.impl.CreateConfigurationAction;
import org.seasar.ymir.extension.creator.action.impl.CreateMessageAction;
import org.seasar.ymir.extension.creator.action.impl.CreateMessagesAction;
import org.seasar.ymir.extension.creator.action.impl.CreateTemplateAction;
import org.seasar.ymir.extension.creator.action.impl.DoEditTemplateAction;
import org.seasar.ymir.extension.creator.action.impl.DoUpdateTemplateAction;
import org.seasar.ymir.extension.creator.action.impl.ResourceAction;
import org.seasar.ymir.extension.creator.action.impl.SystemConsoleAction;
import org.seasar.ymir.extension.creator.action.impl.UpdateClassesAction;
import org.seasar.ymir.extension.creator.mapping.ActionSelectorSeed;
import org.seasar.ymir.extension.creator.mapping.ExtraPathMapping;
import org.seasar.ymir.extension.creator.mapping.PathMappingExtraData;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;
import org.seasar.ymir.extension.creator.mapping.impl.ExtraPathMappingImpl;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.GenericsUtils;
import org.seasar.ymir.extension.creator.util.PersistentProperties;
import org.seasar.ymir.extension.creator.util.SourceCreatorUtils;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
import org.seasar.ymir.extension.zpt.AnalyzerUtils;
import org.seasar.ymir.extension.zpt.ParameterRole;
import org.seasar.ymir.id.action.Action;
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.message.MessageNotFoundRuntimeException;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.MessagesNotFoundRuntimeException;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.scope.annotation.Inject;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.HTMLUtils;
import org.seasar.ymir.util.MetaUtils;
import org.seasar.ymir.util.ServletUtils;

import net.skirnir.freyja.EvaluationRuntimeException;

public class SourceCreatorImpl implements SourceCreator {
    private static final String PREFIX_ABSTRACT = "Abstract";

    private static final String MULTIPLE_SUFFIX = "ies";

    private static final String SINGULAR_SUFFIX = "y";

    private static final String MULTIPLE_SUFFIX2 = "s";

    private static final String SINGULAR_SUFFIX2 = "";

    private static final String PROPERTY_ID = "id";

    private static final String ID_ANNOTATIONNAME = "org.seasar.dao.annotation.tiger.Id";

    private static final String ID_BODY = "(org.seasar.dao.annotation.tiger.IdType.IDENTITY)";

    private static final Class<?> ID_TYPE = Integer.TYPE;

    private static final String PACKAGEPREFIX_JAVA_LANG = "java.lang.";

    private static final String PACKAGEPREFIX_JAVA_UTIL = "java.util.";

    private static final String RESOURCE_PREAMBLE_JAVA = "org/seasar/ymir/extension/Preamble.java.txt";

    private static final String PROP_LENGTH = "length";

    private static final String PROP_SIZE = "size";

    private static final Comparator<PropertyDesc> COMPARATOR_PROPERTYDESC_BY_NAME = new Comparator<PropertyDesc>() {
        public int compare(PropertyDesc o1, PropertyDesc o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private static final Comparator<MethodDesc> COMPARATOR_METHODDESC_BY_NAME = new Comparator<MethodDesc>() {
        public int compare(MethodDesc o1, MethodDesc o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private static final String ACTIONMETHOD_FIELD_KEY = "KEY";

    private static final Map<HttpMethod, Class<? extends Action>> ACTIONINTERFACE_BY_HTTPMETHOD_MAP;

    private static final Comparator<? super Field> COMPARATOR_FIELD_BY_NAME = new Comparator<Field>() {
        public int compare(Field o1, Field o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private YmirImpl ymir_;

    private NamingConvention namingConvention_;

    private TemplateAnalyzer analyzer_;

    private ClassDescModifier[] classDescModifiers_ = new ClassDescModifier[0];

    private String sourceEncoding_ = "UTF-8";

    private SourceGenerator sourceGenerator_;

    private ResponseCreator responseCreator_;

    public Properties sourceCreatorProperties_;

    private ApplicationManager applicationManager_;

    private TemplateProvider templateProvider_ = new DefaultTemplateProvider(
            this);

    private SourceCreatorSetting setting_ = new SourceCreatorSetting(this);

    private Map<Class<? extends PathMapping>, PathMappingExtraData<?>> pathMappingExtraDataMap_ = new HashMap<Class<? extends PathMapping>, PathMappingExtraData<?>>();

    private ActionSelector<UpdateAction> byRequestingActionSelector_ = new ActionSelector<UpdateAction>()
            .register(
                    new Condition(State.TRUE, State.ANY, State.ANY,
                            HttpMethod.GET), new ClassifyParametersAction(this))
            .register(
                    new Condition(State.TRUE, State.ANY, State.ANY,
                            HttpMethod.POST),
                    new ClassifyParametersAction(this)).register(
                    "classifyParameters", new ClassifyParametersAction(this))
            .register("createConfiguration",
                    new CreateConfigurationAction(this)).register(
                    "systemConsole", new SystemConsoleAction(this)).register(
                    "resource", new ResourceAction(this)).register(
                    "editTemplate.do", new DoEditTemplateAction(this))
            .register("updateTemplate.do", new DoUpdateTemplateAction(this));

    private ActionSelector<UpdateAction> actionSelector_ = new ActionSelector<UpdateAction>()
            .register(
                    new Condition(State.ANY, State.ANY, State.FALSE,
                            HttpMethod.GET), new CreateTemplateAction(this))
            .register(
                    new Condition(State.ANY, State.ANY, State.TRUE,
                            HttpMethod.GET), new UpdateClassesAction(this))
            .register(
                    new Condition(State.ANY, State.FALSE, State.FALSE,
                            HttpMethod.POST),
                    new CreateClassAndTemplateAction(this)).register(
                    new Condition(State.ANY, State.ANY, State.TRUE,
                            HttpMethod.POST), new UpdateClassesAction(this))
            .register(
                    new Condition(State.TRUE, State.TRUE, State.FALSE,
                            HttpMethod.POST), new CreateTemplateAction(this))
            .register("createClass", new CreateClassAction(this)).register(
                    "createTemplate", new CreateTemplateAction(this)).register(
                    "createClassAndTemplate",
                    new CreateClassAndTemplateAction(this)).register(
                    "updateClasses", new UpdateClassesAction(this));

    private ActionSelector<UpdateByExceptionAction> byExceptionActionSelector_ = new ActionSelector<UpdateByExceptionAction>()
            .register(MessagesNotFoundRuntimeException.class,
                    new CreateMessagesAction(this)).register("createMessages",
                    new CreateMessagesAction(this)).register(
                    MessageNotFoundRuntimeException.class,
                    new CreateMessageAction(this)).register("createMessage",
                    new CreateMessageAction(this)).register(
                    ActionNotFoundRuntimeException.class,
                    new CreateActionAction(this)).register("createAction",
                    new CreateActionAction(this));

    private boolean initialized_;

    private Log log_ = LogFactory.getLog(SourceCreatorImpl.class);

    static {
        Map<HttpMethod, Class<? extends Action>> map = new HashMap<HttpMethod, Class<? extends Action>>();
        for (HttpMethod method : HttpMethod.values()) {
            String name = method.name();
            try {
                @SuppressWarnings("unchecked")
                Class<? extends Action> actionInterface = (Class<? extends Action>) ClassUtils
                        .forName("org.seasar.ymir.id.action."
                                + name.substring(0, 1)
                                + name.substring(1, name.length())
                                        .toLowerCase() + "Action");
                map.put(method, actionInterface);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(
                        "Cannot find Action interface. Please add Action interface for HTTP method '"
                                + name + "'");
            }
        }
        ACTIONINTERFACE_BY_HTTPMETHOD_MAP = Collections.unmodifiableMap(map);
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTemplateAnalyzer(TemplateAnalyzer analyzer) {
        analyzer_ = analyzer;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setNamingConvention(NamingConvention namingConvention) {
        namingConvention_ = namingConvention;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setYmir(Ymir ymir) {
        if (ymir instanceof YmirImpl) {
            ymir_ = (YmirImpl) ymir;
        } else {
            throw new ComponentNotFoundRuntimeException("YmirImpl");
        }
    }

    @Binding(value = "@org.seasar.ymir.util.ContainerUtils@findAllComponents(container, @org.seasar.ymir.extension.creator.ClassDescModifier@class)", bindingType = BindingType.MUST)
    public void setClassDescModifiers(ClassDescModifier[] classDescModifiers) {
        classDescModifiers_ = classDescModifiers;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setSourceEncoding(String sourceEncoding) {
        sourceEncoding_ = sourceEncoding;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setSourceGenerator(SourceGenerator sourceGenerator) {
        sourceGenerator_ = sourceGenerator;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setResponseCreator(ResponseCreator responseCreator) {
        responseCreator_ = responseCreator;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(value = "@org.seasar.ymir.util.ContainerUtils@findAllComponents(container, @org.seasar.ymir.extension.creator.mapping.PathMappingExtraData@class)", bindingType = BindingType.MUST)
    public void setPathMappingExtraDatas(
            PathMappingExtraData<?>[] pathMappingExtraDatas) {
        for (int i = 0; i < pathMappingExtraDatas.length; i++) {
            pathMappingExtraDataMap_.put(pathMappingExtraDatas[i]
                    .getPathMappingClass(), pathMappingExtraDatas[i]);
        }
    }

    public Response updateByRequesting(Request request) {
        return update(request, null, byRequestingActionSelector_);
    }

    public Response update(Request request, Response response) {
        return update(request, response, actionSelector_);
    }

    Response update(Request request, Response response,
            ActionSelector<UpdateAction> actionSelector) {
        synchronized (this) {
            if (!initialized_) {
                setProjectRootIfNotDetecetd(getApplication());
                initialized_ = true;
            }
        }

        if (!shouldUpdate()) {
            return response;
        }

        LazyPathMetaData pathMetaData = createLazyPathMetaData(request,
                response);
        String path = pathMetaData.getPath();
        String forwardPath = pathMetaData.getForwardPath();
        HttpMethod method = pathMetaData.getMethod();

        if (request.getParameter(PARAM_TASK) == null
                && !shouldUpdate(forwardPath)) {
            return response;
        }

        Object condition = null;
        if (request.getParameter(PARAM_TASK) != null) {
            condition = request.getParameter(PARAM_TASK);
        } else if (path.startsWith(PATH_PREFIX)) {
            int slash = path.indexOf('/', PATH_PREFIX.length());
            if (slash >= 0) {
                condition = path.substring(PATH_PREFIX.length(), slash);
            } else {
                condition = path.substring(PATH_PREFIX.length());
            }
        } else {
            if (!request.getCurrentDispatch().isMatched()) {
                return response;
            }

            if (response != null
                    && response.getType() != ResponseType.PASSTHROUGH
                    && response.getType() != ResponseType.FORWARD) {
                return response;
            }
        }

        if (condition == null) {
            if (!isAlreadyConfigured(getApplication())) {
                condition = "createConfiguration";
            } else {
                String className = pathMetaData.getClassName();
                File sourceFile = pathMetaData.getSourceFile();
                Template template = pathMetaData.getTemplate();

                if ("".equals(forwardPath)) {
                    if (className == null || sourceFile.exists()) {
                        return response;
                    }
                    condition = "createClass";
                } else {
                    condition = new Condition(State.valueOf(className != null),
                            State.valueOf(sourceFile.exists()), State
                                    .valueOf(template != null
                                            && template.exists()), method);
                }
            }
        }

        UpdateAction action = actionSelector.getAction(condition);
        if (action != null) {
            Response newResponse = action.act(request, pathMetaData);
            if (newResponse != null) {
                response = newResponse;
            }
        }
        return response;
    }

    void setProjectRootIfNotDetecetd(Application application) {
        if (application.getProjectRoot() != null
                && new File(application.getProjectRoot()).exists()) {
            return;
        }

        String projectRoot = SourceCreatorUtils
                .findProjectRootDirectory(application);
        application.setProjectRoot(projectRoot);
        log_.info("Project root has been detected and set automatically: "
                + projectRoot);
    }

    LazyPathMetaData createLazyPathMetaData(Request request, Response response) {
        String path = request.getCurrentDispatch().getPath();
        String forwardPath = null;
        if (response != null) {
            if (response.getType() == ResponseType.FORWARD) {
                forwardPath = response.getPath();
            } else if (response.getType() == ResponseType.PASSTHROUGH) {
                forwardPath = path;
            }
        }

        return new LazyPathMetaData(this, path, getOriginalMethod(request),
                forwardPath);
    }

    public Response updateByException(Request request, Throwable t) {
        if (!shouldUpdate()) {
            return null;
        }

        if (t instanceof EvaluationRuntimeException && t.getCause() != null) {
            t = t.getCause();
        }
        String path = ServletUtils.normalizePath(request.getCurrentDispatch()
                .getPath());
        Object condition = null;
        if (request.getParameter(PARAM_TASK) != null) {
            condition = request.getParameter(PARAM_TASK);
        } else if (path.startsWith(PATH_PREFIX)) {
            int slash = path.indexOf('/', PATH_PREFIX.length());
            if (slash >= 0) {
                condition = path.substring(PATH_PREFIX.length(), slash);
            } else {
                condition = path.substring(PATH_PREFIX.length());
            }
        } else {
            condition = t.getClass();
        }

        UpdateByExceptionAction action = byExceptionActionSelector_
                .getAction(condition);
        if (action == null) {
            return null;
        } else {
            return action
                    .act(request, createLazyPathMetaData(request, null), t);
        }
    }

    HttpMethod getOriginalMethod(Request request) {
        String originalMethod = request.getParameter(PARAM_METHOD);
        if (originalMethod != null) {
            return HttpMethod.enumOf(originalMethod);
        } else {
            return request.getMethod();
        }
    }

    public boolean shouldUpdate() {
        return setting_.isSourceCreatorEnabled();
    }

    public boolean shouldUpdate(String path) {
        return path == null || setting_.isSourceCreatorEnabledWith(path);
    }

    boolean isAlreadyConfigured(Application application) {
        String originalProjectRoot = SourceCreatorUtils
                .getOriginalProjectRoot(application);
        String projectRoot = application.getProjectRoot();
        return (projectRoot != null
                && (originalProjectRoot == null || originalProjectRoot
                        .equals(projectRoot)) && application
                .getFirstRootPackageName() != null);
    }

    public ClassDescBag gatherClassDescs(DescPool pool, Notes warnings,
            boolean analyzeTemplate, PathMetaData... pathMetaDatas) {
        return gatherClassDescs(pool, warnings, analyzeTemplate, null,
                pathMetaDatas);
    }

    public ClassDescBag gatherClassDescs(DescPool pool, Notes warnings,
            boolean analyzeTemplate, String[] ignoreVariables,
            PathMetaData... pathMetaDatas) {
        for (int i = 0; i < pathMetaDatas.length; i++) {
            gatherClassDescs(pool, warnings, analyzeTemplate, ignoreVariables,
                    pathMetaDatas[i]);
        }
        ClassDesc[] classDescs = addRelativeClassDescs(pool
                .getGeneratedClassDescs().toArray(new ClassDesc[0]), pool
                .getHintBag());

        return classifyClassDescs(classDescs);
    }

    public void updateClasses(ClassDescBag classDescBag) {
        if (setting_.isConverterCreationFeatureEnabled()) {
            // Converter用のClassDescを生成する。
            for (ClassDesc dtoCd : classDescBag.getClassDescs(ClassType.DTO)) {
                String[] pairTypeNames = getPairTypeNames(dtoCd);
                if (pairTypeNames != null) {
                    ClassDesc converterCd = createConverterClassDesc(dtoCd,
                            pairTypeNames);
                    if (getClass(converterCd.getName()) == null) {
                        classDescBag.addAsCreated(converterCd);
                    } else {
                        classDescBag.addAsUpdated(converterCd);
                    }
                }
            }
        }

        ClassDescSet classDescSet = classDescBag.getClassDescSet();
        ClassDesc[] pageClassDescs = classDescBag.getClassDescs(ClassType.PAGE);
        for (int i = 0; i < pageClassDescs.length; i++) {
            // Dtoに触るようなプロパティを持っているなら
            // Converterのsetterを自動生成する。
            addConverterSetterToPageClassDesc(pageClassDescs[i], classDescSet);

            // Pageの親クラスが存在しない場合は生成しておく。
            String superclassName = pageClassDescs[i].getSuperclassName();
            if (superclassName != null) {
                Class<?> superclass = getClass(superclassName);
                if (superclass == null) {
                    ClassDesc classDesc = newClassDesc(pageClassDescs[i]
                            .getDescPool(), superclassName, null);
                    if (!isOuter(classDesc)) {
                        writeSourceFile("PageSuperclass.java", classDesc, false);
                        classDescBag.addAsCreated(classDesc, true);
                    }
                }
            }
        }

        writeSourceFiles(classDescBag);
    }

    void addConverterSetterToPageClassDesc(ClassDesc pageClassDesc,
            ClassDescSet classDescSet) {
        addConverterSetterToPageClassDesc(pageClassDesc, pageClassDesc,
                classDescSet, new HashSet<String>());
    }

    void addConverterSetterToPageClassDesc(ClassDesc pageClassDesc,
            ClassDesc dtoClassDesc, ClassDescSet classDescSet,
            Set<String> processedClassNameSet) {
        for (PropertyDesc pd : dtoClassDesc.getPropertyDescs()) {
            TypeDesc td = pd.getTypeDesc();
            ClassDesc dtoCd = td.getComponentClassDesc();
            if (!processedClassNameSet.add(dtoCd.getName())) {
                continue;
            }

            if (!DescValidator.validate(td, classDescSet).isValid()
                    || dtoCd.getType() != ClassType.DTO) {
                continue;
            }

            EntityMetaData metaData = new EntityMetaData(pageClassDesc
                    .getDescPool(), dtoCd.getName());
            TypeDesc typeDesc = pageClassDesc.getDescPool().newTypeDesc(
                    metaData.getConverterClassDesc());

            PropertyDesc propertyDesc = pageClassDesc.getPropertyDesc(typeDesc
                    .getInstanceName());
            if (propertyDesc != null && propertyDesc.isWritable()) {
                // 既にSetterを追加済みなのでスキップする。
                // また、こうしないと循環参照している場合に無限ループに陥ってしまう。
                continue;
            }

            addComponentSetterToPageIfValid(pageClassDesc, typeDesc,
                    classDescSet);

            addConverterSetterToPageClassDesc(pageClassDesc, dtoCd,
                    classDescSet, processedClassNameSet);
        }
    }

    protected ClassDesc createConverterClassDesc(ClassDesc dtoCd,
            String[] pairTypeNames) {
        DescPool pool = dtoCd.getDescPool();
        String oldBornOf = pool.getBornOf();
        try {
            pool.setBornOf(dtoCd.getBornOf());

            ClassDesc converterCd = new EntityMetaData(pool, dtoCd.getName())
                    .getConverterClassDesc();

            Map<String, Object> parameter = converterCd
                    .getSourceGeneratorParameter();
            ClassDesc clonedDtoCd = dtoCd.transcriptTo(DescPool.newInstance(
                    this, null).getClassDesc(dtoCd.getName()));
            mergeWithExistentClass(clonedDtoCd);
            parameter.put(Globals.PARAMETER_TARGETCLASSDESC, clonedDtoCd);
            addToAuxDescList(converterCd, clonedDtoCd);
            List<TypeDesc> pairTdList = new ArrayList<TypeDesc>();
            for (int i = 0; i < pairTypeNames.length; i++) {
                Class<?> pairClass = getClass(GenericsUtils
                        .getNonGenericClassName(pairTypeNames[i]));
                if (pairClass == null || pairClass == Object.class) {
                    continue;
                }
                pool.registerClassDesc(newClassDesc(pool, pairClass, false));
                pairTdList.add(pool.newTypeDesc(pairTypeNames[i]));
            }
            TypeDesc[] pairTds = pairTdList.toArray(new TypeDesc[0]);
            parameter.put(Globals.PARAMETER_PAIRTYPEDESCS, pairTds);
            addToAuxDescList(converterCd, pairTds);

            return converterCd;
        } finally {
            pool.setBornOf(oldBornOf);
        }
    }

    private void addToAuxDescList(ClassDesc classDesc, Desc<?>... descs) {
        Map<String, Object> parameter = classDesc.getSourceGeneratorParameter();
        @SuppressWarnings("unchecked")
        List<Desc<?>> list = (List<Desc<?>>) parameter
                .get(Globals.PARAMETER_AUXDESCLIST);
        if (list == null) {
            list = new ArrayList<Desc<?>>();
            parameter.put(Globals.PARAMETER_AUXDESCLIST, list);
        }
        list.addAll(Arrays.asList(descs));
    }

    public void gatherClassDescs(DescPool pool, Notes warnings,
            boolean analyzeTemplate, String[] ignoreVariables,
            PathMetaData pathMetaData) {
        String path = pathMetaData.getPath();
        String oldBornOf = pool.getBornOf();
        try {
            if (analyzeTemplate) {
                // analyzeTemplateがfalseの場合は予めpoolにbornOfが設定されている、かつそれを使うべき
                // なので、ここではpathに置き換えない。
                pool.setBornOf(path);
            }

            HttpMethod method = pathMetaData.getMethod();
            String pageClassName = pathMetaData.getClassName();
            if (analyzeTemplate) {
                analyzer_.analyze(getServletContext(), getHttpServletRequest(),
                        getHttpServletResponse(), getRequest(), path, method,
                        pathMetaData.getTemplate(), pageClassName,
                        ignoreVariables, pool, warnings);
            } else {
                // テンプレートを解析する場合は必要に応じてTemplateAnalyzer#analyze()の中で
                // finishAnalyzing()は呼び出されるので明示的に呼び出す必要はない。
                // テンプレートを解析しない場合は明示的に呼び出すようにする。
                finishAnalyzing(pool);
            }

            for (int i = 0; i < classDescModifiers_.length; i++) {
                classDescModifiers_[i].modify(pool, pathMetaData);
            }

            ClassDesc pageClassDesc = pool.getClassDesc(pageClassName);

            if (analyzeTemplate) {
                // アクションメソッドがなければ追加する。
                // postするようなフォームを持つ画面で、postの後バリデーションエラーで自画面に戻ってきた
                // ところで自動生成を行なうと、methodがPOSTになっているため、(1)_get()が消えて
                // しまう、(2)ボタンに名前をつけていても、デフォルトの_post()が生成されてしまう、という
                // 問題が発生する。これを避けるため、methodに依らずGETでアクションメソッドを生成するように
                // している。
                MethodDesc actionMethodDesc = newActionMethodDesc(pool, path,
                        HttpMethod.GET, new ActionSelectorSeedImpl());
                if (pageClassDesc.getMethodDesc(actionMethodDesc) == null) {
                    pageClassDesc.setMethodDesc(actionMethodDesc);
                }

                // _prerender()を追加する。
                MethodDesc prerenderMethodDesc = newPrerenderActionMethodDesc(
                        pool, path, method, new ActionSelectorSeedImpl());
                pageClassDesc.setMethodDesc(prerenderMethodDesc);
            }

            if (isValidationFailedMethodEnabled()) {
                // _validationFailed(Notes)を追加する。
                MethodDesc methodDesc = new MethodDescImpl(pageClassDesc
                        .getDescPool(),
                        ConstraintInterceptor.ACTION_VALIDATIONFAILED);
                methodDesc
                        .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                                pageClassDesc.getDescPool(), Notes.class,
                                "notes") });
                pageClassDesc.setMethodDesc(methodDesc);
            }

            if (isPermissionDeniedMethodEnabled()) {
                // _permissionDenied(PemissionDeniedException)を追加する。
                MethodDesc methodDesc = new MethodDescImpl(pageClassDesc
                        .getDescPool(),
                        ConstraintInterceptor.ACTION_PERMISSIONDENIED);
                methodDesc
                        .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                                pageClassDesc.getDescPool(),
                                PermissionDeniedException.class, "ex") });
                methodDesc.setThrowsDesc(new ThrowsDescImpl(
                        PermissionDeniedException.class));
                methodDesc.setBodyDesc(new BodyDescImpl(
                        BodyDesc.KEY_PERMISSIONDENIED,
                        new HashMap<String, Object>(), new String[0]));
                pageClassDesc.setMethodDesc(methodDesc);
            }
        } finally {
            pool.setBornOf(oldBornOf);
        }
    }

    public void finishAnalyzing(DescPool pool) {
        for (Iterator<ClassDesc> itr = pool.iterator(); itr.hasNext();) {
            ClassDesc classDesc = itr.next();

            // 中身のないDTOは除外しておく。
            // ただしFormDtoは残す。
            if (isEmptyDto(classDesc) && !isFormDto(classDesc)) {
                itr.remove();
                continue;
            }

            // スーパークラスを設定する。
            ClassCreationHintBag hintBag = pool.getHintBag();
            if (hintBag != null) {
                ClassHint hint = hintBag.getClassHint(classDesc.getName());
                if (hint != null) {
                    classDesc.setSuperclassName(hint.getSuperclassName());
                }
            }
        }

        for (ClassDesc classDesc : pool.getGeneratedClassDescs()) {
            // プロパティの型に対応するDTOがDescPoolに存在しない場合は、
            // そのDTOは上のフェーズで除外された、すなわちDTOかもしれないと考えて
            // 解析を進めたが結局DTOであることが確定しなかったので、
            // 型をデフォルトクラスに差し替える。
            // [#YMIR-198] ただし明示的に型を指定されている場合は差し替えない。
            for (PropertyDesc pd : classDesc.getPropertyDescs()) {
                if (!pd.isTypeAlreadySet(PropertyDesc.PROBABILITY_MAXIMUM)) {
                    replaceSimpleDtoTypeToDefaultType(pd);
                }
            }
        }
    }

    private boolean isEmptyDto(ClassDesc classDesc) {
        return isTypeOf(classDesc, ClassType.DTO) && classDesc.isEmpty();
    }

    private boolean isTypeOf(ClassDesc classDesc, ClassType type) {
        return classDesc.isTypeOf(type) && !isOuter(classDesc);
    }

    private boolean isFormDto(ClassDesc classDesc) {
        Boolean formDto = (Boolean) classDesc
                .getAttribute(Globals.ATTR_FORMDTO);
        return formDto != null && formDto.booleanValue();
    }

    private boolean isDto(ClassDesc classDesc) {
        return isTypeOf(classDesc, ClassType.DTO);
    }

    void replaceSimpleDtoTypeToDefaultType(final PropertyDesc propertyDesc) {
        final TypeDesc typeDesc = propertyDesc.getTypeDesc();
        if (isDto(typeDesc.getComponentClassDesc())
                && !propertyDesc.getDescPool().contains(
                        typeDesc.getComponentClassDesc())) {
            typeDesc.setComponentClassDesc(propertyDesc.isMayBoolean()
                    && propertyDesc.getReferCount() == 0 ? Boolean.TYPE
                    : String.class);
        }
    }

    @Begin
    Begin getBeginAnnotation() {
        try {
            return SourceCreatorImpl.class.getDeclaredMethod(
                    "getBeginAnnotation", new Class<?>[0]).getAnnotation(
                    Begin.class);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("Logic error!", ex);
        }
    }

    ClassDescBag classifyClassDescs(ClassDesc[] classDescs) {
        ClassDescBag classDescBag = new ClassDescBag();
        for (int i = 0; i < classDescs.length; i++) {
            if (getClass(classDescs[i].getName()) == null) {
                classDescBag.addAsCreated(classDescs[i]);
            } else {
                classDescBag.addAsUpdated(classDescs[i]);
            }
        }

        return classDescBag;
    }

    public ClassDesc newClassDesc(DescPool pool, Class<?> clazz,
            boolean onlyDeclared) {
        return newClassDesc(pool, clazz, null, onlyDeclared);
    }

    public ClassDesc newClassDesc(DescPool pool, Class<?> clazz,
            String qualifier, boolean onlyDeclared) {
        if (clazz == null) {
            return null;
        }

        ClassDesc classDesc = newClassDesc(pool, clazz.getName(), qualifier,
                null);

        AnnotationDesc[] ads = DescUtils.newAnnotationDescs(clazz);
        for (int i = 0; i < ads.length; i++) {
            classDesc.setAnnotationDesc(ads[i]);
        }

        for (PropertyDescriptor descriptor : getPropertyDescriptors(clazz,
                onlyDeclared)) {
            classDesc.setPropertyDesc(new PropertyDescImpl(pool, descriptor));
        }

        for (Method method : getMethods(clazz, onlyDeclared)) {
            MethodDesc md = new MethodDescImpl(pool, method);
            classDesc.setMethodDesc(md);
            String[] source = md.getMetaValue(Globals.META_NAME_SOURCE);
            if (source != null) {
                List<String> classNameList = new ArrayList<String>();
                for (Class<?> cl : md
                        .getMetaClassValue(Globals.META_NAME_SOURCE)) {
                    classNameList.add(cl.getName());
                }
                int idx = 0;
                md.setBodyDesc(new BodyDescImpl(source[idx++], classNameList
                        .toArray(new String[0])));
                ParameterDesc[] parameterDescs = md.getParameterDescs();
                for (int i = 0; idx < source.length
                        && i < parameterDescs.length; idx++, i++) {
                    parameterDescs[i].setName(source[idx]);
                }
            }

            // メソッドがアクションである場合はアクションに関する情報を埋め込んでおく。
            Class<?> actionMethodClass = getClass(clazz.getName() + "$"
                    + method.getName());
            if (actionMethodClass != null) {
                boolean action = true;
                String actionKey = null;
                try {
                    actionKey = (String) actionMethodClass.getField(
                            ACTIONMETHOD_FIELD_KEY).get(null);
                } catch (Throwable t) {
                    if (log_.isDebugEnabled()) {
                        log_.debug("Cannot get field '"
                                + ACTIONMETHOD_FIELD_KEY + "' value of class '"
                                + actionMethodClass + "'", t);
                    }
                    action = false;
                }
                if (action) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Action> actionInterface = actionMethodClass
                            .getInterfaces()[0];
                    setActionInfo(md, actionInterface, actionKey);
                }
            }
        }

        // 特別な処理を行なう。

        Field[] fields = clazz.getDeclaredFields();
        Arrays.sort(fields, COMPARATOR_FIELD_BY_NAME);
        for (Field field : fields) {
            // フォームDTOのためのフィールドである場合の処理を行なう。
            if (MetaUtils.hasMeta(field, Globals.META_NAME_PROPERTY)) {
                String propertyName = MetaUtils.getFirstValue(field,
                        Globals.META_NAME_PROPERTY);
                PropertyDesc pd = classDesc.getPropertyDesc(propertyName);
                if (pd == null) {
                    pd = classDesc.addPropertyDesc(propertyName,
                            PropertyDesc.NONE);
                }
                pd.setTypeDesc(field.getGenericType());
                for (Annotation annotation : field.getAnnotations()) {
                    pd.setAnnotationDesc(DescUtils
                            .newAnnotationDesc(annotation));
                }
            }

            // パラメータ情報をPropertyDescに設定する。
            String name = field.getName();
            if (name.startsWith(Globals.CONSTANT_PREFIX_PARAMETER)) {
                String propertyName = name
                        .substring(Globals.CONSTANT_PREFIX_PARAMETER.length());
                int delim = propertyName.indexOf("$");
                if (delim >= 0) {
                    propertyName = propertyName.substring(0, delim);
                }
                PropertyDesc propertyDesc = classDesc
                        .getPropertyDesc(propertyName);
                if (propertyDesc != null
                        && (propertyDesc
                                .getAnnotationDescOnGetter(RequestParameter.class
                                        .getName()) != null || propertyDesc
                                .getAnnotationDescOnSetter(RequestParameter.class
                                        .getName()) != null)) {
                    try {
                        DescUtils.addParameter(propertyDesc, (String) field
                                .get(null), MetaUtils.getValue(field,
                                Globals.META_NAME_BORNOF));
                    } catch (IllegalArgumentException ex) {
                        throw new RuntimeException("May logic error", ex);
                    } catch (IllegalAccessException ignore) {
                    } catch (ClassCastException ignore) {
                    }
                }
            }
        }

        return classDesc;
    }

    public void setActionInfo(MethodDesc methodDesc,
            Class<? extends Action> actionInterface, String actionKey) {
        methodDesc.setAttribute(Globals.ATTR_ACTION, Boolean.TRUE);
        methodDesc.setAttribute(Globals.ATTR_ACTION_INTERFACE, actionInterface);
        methodDesc.setAttribute(Globals.ATTR_ACTION_KEY, actionKey);
    }

    Method[] getMethods(Class<?> clazz, boolean onlyDeclared) {
        Set<Method> excludeMethodSet = new HashSet<Method>();
        for (PropertyDescriptor descriptor : getPropertyDescriptors(clazz,
                onlyDeclared)) {
            Method method = descriptor.getReadMethod();
            if (method != null) {
                excludeMethodSet.add(method);
            }
            method = descriptor.getWriteMethod();
            if (method != null) {
                excludeMethodSet.add(method);
            }
        }

        List<Method> list = new ArrayList<Method>();
        for (Method method : ClassUtils.getMethods(clazz)) {
            if (onlyDeclared && method.getDeclaringClass() != clazz) {
                continue;
            } else if (getOriginalDeclaringClass(method) == Object.class) {
                continue;
            } else if (excludeMethodSet.contains(method)) {
                continue;
            }
            list.add(method);
        }

        Collections.sort(list, new Comparator<Method>() {
            public int compare(Method o1, Method o2) {
                int cmp = o1.getName().compareTo(o2.getName());
                if (cmp != 0) {
                    return cmp;
                }
                Type[] types1 = o1.getGenericParameterTypes();
                Type[] types2 = o2.getGenericParameterTypes();
                cmp = types1.length - types2.length;
                if (cmp != 0) {
                    return cmp;
                }
                for (int i = 0; i < types1.length; i++) {
                    cmp = types1[i].toString().compareTo(types2[i].toString());
                    if (cmp != 0) {
                        return cmp;
                    }
                }
                return 0;
            }
        });

        return list.toArray(new Method[0]);
    }

    private Class<?> getOriginalDeclaringClass(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        while (true) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass == null) {
                return clazz;
            } else {
                try {
                    superclass.getDeclaredMethod(method.getName(), method
                            .getParameterTypes());
                } catch (SecurityException ignore) {
                } catch (NoSuchMethodException ex) {
                    return clazz;
                }
            }
            clazz = superclass;
        }
    }

    private PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz,
            boolean onlyDeclared) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }

        List<PropertyDescriptor> list = new ArrayList<PropertyDescriptor>();
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            Method readMethod = descriptor.getReadMethod();
            Method writeMethod = descriptor.getWriteMethod();
            if (readMethod != null
                    && readMethod.getDeclaringClass() == Object.class
                    || writeMethod != null
                    && writeMethod.getDeclaringClass() == Object.class) {
                // Objectクラスのプロパティは対象外とする。
                continue;
            }

            if (onlyDeclared) {
                if (readMethod != null
                        && readMethod.getDeclaringClass() != clazz) {
                    readMethod = null;
                }
                if (writeMethod != null
                        && writeMethod.getDeclaringClass() != clazz) {
                    writeMethod = null;
                }
            }
            if (readMethod == null && writeMethod == null) {
                continue;
            }

            try {
                list.add(new PropertyDescriptor(getPropertyName(descriptor),
                        readMethod, writeMethod));
            } catch (IntrospectionException ex) {
                throw new RuntimeException("Can't happen!", ex);
            }
        }

        Collections.sort(list, new Comparator<PropertyDescriptor>() {
            public int compare(PropertyDescriptor o1, PropertyDescriptor o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return list.toArray(new PropertyDescriptor[0]);
    }

    private String getPropertyName(PropertyDescriptor descriptor) {
        String name = descriptor.getName();
        if (findField(descriptor.getReadMethod(), name) == null
                && findField(descriptor.getWriteMethod(), name) == null
                && name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
            // aNameのようなパラメータに対応するプロパティとして生成されたプロパティ
            // であるかもしれないため、そのようなフィールドも検索する。
            // （このような名称はJavaBeans名規約に反するため推奨しないが、
            // もしも作られてしまったときのために検索するようにしている。）
            String n = Character.toLowerCase(name.charAt(0))
                    + name.substring(1);
            if (findField(descriptor.getReadMethod(), n) != null
                    || findField(descriptor.getWriteMethod(), n) != null) {
                return n;
            }
        }

        return name;
    }

    public Field findField(Method accessorMethod, String propertyName) {
        Field field = null;
        if (accessorMethod != null) {
            String formName = MetaUtils.getFirstValue(accessorMethod,
                    Globals.META_NAME_FORMPROPERTY);
            if (formName != null) {
                Field formField = findField(formName, accessorMethod
                        .getDeclaringClass());
                if (formField != null) {
                    field = findField(propertyName, formField.getType());
                }
            } else {
                field = findField(propertyName, accessorMethod
                        .getDeclaringClass());
            }
        }
        return field;
    }

    private Field findField(String propertyName, Class<?> clazz) {
        Field field = null;
        if (clazz != null) {
            field = DescUtils.findField(setting_.getFieldName(propertyName),
                    clazz);
            if (field == null) {
                field = DescUtils.findField(propertyName, clazz);
            }
        }
        return field;
    }

    String quote(String value) {
        if (value == null) {
            return "null";
        } else {
            return StringUtils.quoteString(value, '"');
        }
    }

    ClassDesc[] addRelativeClassDescs(ClassDesc[] classDescs,
            ClassCreationHintBag hintBag) {
        Map<String, List<ClassDesc>> pageByDtoMap = new HashMap<String, List<ClassDesc>>();
        for (ClassDesc classDesc : classDescs) {
            if (!classDesc.isTypeOf(ClassType.PAGE)) {
                continue;
            }

            for (PropertyDesc propertyDesc : classDesc.getPropertyDescs()) {
                ClassDesc cd = propertyDesc.getTypeDesc()
                        .getComponentClassDesc();
                if (!cd.isTypeOf(ClassType.DTO)) {
                    continue;
                }
                List<ClassDesc> list = pageByDtoMap.get(cd.getName());
                if (list == null) {
                    list = new ArrayList<ClassDesc>();
                    pageByDtoMap.put(cd.getName(), list);
                }
                list.add(classDesc);
            }
        }

        List<ClassDesc> classDescList = new ArrayList<ClassDesc>(Arrays
                .asList(classDescs));
        for (ClassDesc classDesc : classDescs) {
            DescPool pool = classDesc.getDescPool();
            String oldBornOf = pool.getBornOf();
            try {
                pool.setBornOf(classDesc.getBornOf());

                if (classDesc.isTypeOf(ClassType.DTO)) {
                    EntityMetaData metaData = new EntityMetaData(pool,
                            classDesc.getName());

                    if (setting_.isDaoCreationFeatureEnabled()) {
                        // Dao用のClassDescを生成しておく。
                        ClassDesc daoClassDesc = metaData.getDaoClassDesc();
                        classDescList.add(daoClassDesc);

                        // Bean用のClassDescを生成しておく。
                        ClassDesc beanClassDesc = metaData.getBeanClassDesc();
                        PropertyDesc[] pds = classDesc.getPropertyDescs();
                        for (int j = 0; j < pds.length; j++) {
                            beanClassDesc.setPropertyDesc(pds[j]
                                    .transcriptTo(pool.newPropertyDesc(pds[j]
                                            .getName())));
                        }
                        // プライマリキーがないとS2Daoがエラーになるので生成しておく。
                        PropertyDesc idPd = beanClassDesc
                                .getPropertyDesc(PROPERTY_ID);
                        if (idPd == null) {
                            idPd = beanClassDesc.addPropertyDesc(PROPERTY_ID,
                                    PropertyDesc.READ | PropertyDesc.WRITE);
                            idPd.setTypeDesc(ID_TYPE);
                        }
                        idPd.setAnnotationDescOnGetter(new AnnotationDescImpl(
                                ID_ANNOTATIONNAME, ID_BODY));
                        classDescList.add(beanClassDesc);
                    }

                    if (setting_.isDxoCreationFeatureEnabled()) {
                        // Dxo用のClassDescを生成しておく。
                        ClassDesc dxoClassDesc = metaData.getDxoClassDesc();
                        List<ClassDesc> list = pageByDtoMap.get(classDesc
                                .getName());
                        if (list != null) {
                            for (Iterator<ClassDesc> itr = list.iterator(); itr
                                    .hasNext();) {
                                MethodDescImpl md = new MethodDescImpl(pool,
                                        "convert");
                                ParameterDesc[] pmds = new ParameterDesc[] { new ParameterDescImpl(
                                        pool,
                                        new TypeDescImpl(pool, itr.next())) };
                                md.setParameterDescs(pmds);
                                md.setReturnTypeDesc(pool.newTypeDesc(metaData
                                        .getBeanClassDesc().getName()));
                                dxoClassDesc.setMethodDesc(md);
                            }
                        }
                        classDescList.add(dxoClassDesc);
                    }
                }
            } finally {
                pool.setBornOf(oldBornOf);
            }
        }

        return classDescList.toArray(new ClassDesc[0]);
    }

    String[] getPairTypeNames(ClassDesc classDesc) {
        String[] pairTypeNames = classDesc.getMetaValue("conversion");
        if (pairTypeNames == null) {
            pairTypeNames = MetaUtils.getValue(getClass(classDesc.getName()
                    + "Base"), "conversion");
        }
        return pairTypeNames;
    }

    boolean addComponentSetterToPageIfValid(ClassDesc pageClassDesc,
            TypeDesc typeDesc, ClassDescSet classDescSet) {
        if (DescValidator.validate(typeDesc, classDescSet).isValid()) {
            PropertyDesc propertyDesc = pageClassDesc.addPropertyDesc(typeDesc
                    .getInstanceName(), PropertyDesc.WRITE);
            propertyDesc.setTypeDesc(typeDesc);
            propertyDesc.setAnnotationDescOnSetter(new AnnotationDescImpl(
                    Inject.class.getName()));
            return true;
        } else {
            return false;
        }
    }

    void writeSourceFiles(ClassDescBag classDescBag) {
        for (ClassType type : ClassType.values()) {
            ClassDesc[] classDescs = classDescBag.getClassDescs(type);
            ClassDescSet classDescSet = classDescBag.getClassDescSet();
            for (int i = 0; i < classDescs.length; i++) {
                try {
                    updateClass(classDescs[i], classDescSet);
                } catch (InvalidClassDescException ex) {
                    // ソースファイルの生成に失敗した。
                    classDescBag.remove(classDescs[i].getName());
                    classDescBag.addAsFailed(classDescs[i], ex
                            .getLackingClassNames());
                }
            }
        }
    }

    public void updateClass(ClassDesc classDesc)
            throws InvalidClassDescException {
        updateClass(classDesc, null);
    }

    void updateClass(ClassDesc classDesc, ClassDescSet classDescSet)
            throws InvalidClassDescException {
        // 既存のクラスの情報を使って調整する。
        adjustByExistentClass(classDesc);

        // 必要に応じて要素を並べ替えておく。
        if (setting_.shouldSortElementsByName()) {
            sortElementsByName(classDesc);
        }

        // 自動生成後に自動生成処理の呼び出し元に返すべき付加情報を設定する。
        prepareForAttribute(classDesc);

        // メソッドのボディの準備をする。
        prepareForMethodBody(classDesc);

        // import文生成のための準備をする。
        prepareForImportDesc(classDesc);

        writeSourceFile(classDesc, classDescSet);
    }

    private void sortElementsByName(ClassDesc classDesc) {
        PropertyDesc[] propertyDescs = classDesc.getPropertyDescs();
        Arrays.sort(propertyDescs, COMPARATOR_PROPERTYDESC_BY_NAME);
        for (PropertyDesc propertyDesc : propertyDescs) {
            @SuppressWarnings("unchecked")
            Born<String>[] parameters = (Born<String>[]) propertyDesc
                    .getAttribute(Globals.ATTR_PARAMETERS);
            if (parameters != null) {
                Arrays.sort(parameters);
            }
        }
        classDesc.setPropertyDescs(propertyDescs);

        MethodDesc[] methodDescs = classDesc.getMethodDescs();
        Arrays.sort(methodDescs, COMPARATOR_METHODDESC_BY_NAME);
        classDesc.setMethodDescs(methodDescs);
    }

    public void prepareForMethodBody(ClassDesc classDesc) {
        MethodDesc[] mds = classDesc.getMethodDescs();
        for (int i = 0; i < mds.length; i++) {
            BodyDesc bodyDesc = mds[i].getBodyDesc();
            String evaluatedBody = sourceGenerator_
                    .generateBodySource(bodyDesc);
            mds[i].setAttribute(Globals.ATTR_EVALUATEDBODY, evaluatedBody);

            boolean shouldRemainSourceMeta = false;
            if (evaluatedBody != null && evaluatedBody.length() > 0) {
                shouldRemainSourceMeta = true;
            } else {
                for (ParameterDesc pd : mds[i].getParameterDescs()) {
                    if (pd.getNameAsIs() != null) {
                        shouldRemainSourceMeta = true;
                        break;
                    }
                }
            }
            if (shouldRemainSourceMeta) {
                List<String> list = new ArrayList<String>();
                list.add(evaluatedBody != null ? evaluatedBody : "");
                for (ParameterDesc pd : mds[i].getParameterDescs()) {
                    list.add(pd.getName());
                }
                Class<?>[] dependingClasses;
                if (bodyDesc != null) {
                    dependingClasses = toClasses(bodyDesc
                            .getDependingClassNames());
                } else {
                    dependingClasses = new Class<?>[0];
                }
                mds[i].setAnnotationDesc(new MetaAnnotationDescImpl(
                        Globals.META_NAME_SOURCE, list.toArray(new String[0]),
                        dependingClasses));
            }
        }
    }

    private Class<?>[] toClasses(String[] classNames) {
        List<Class<?>> list = new ArrayList<Class<?>>();
        for (String className : classNames) {
            Class<?> clazz = getClass(className);
            if (clazz != null) {
                list.add(clazz);
            }
        }
        return list.toArray(new Class<?>[0]);
    }

    public void prepareForImportDesc(ClassDesc classDesc) {
        Map<String, Object> parameter = classDesc.getSourceGeneratorParameter();

        EntityMetaData entityMetaData = new EntityMetaData(classDesc
                .getDescPool(), classDesc.getName());

        Set<String> importClassNameSet = new HashSet<String>();
        Set<String> baseImportClassNameSet = new HashSet<String>();

        switch (classDesc.getType()) {
        case BEAN:
            importClassNameSet
                    .add("org.seasar.ymir.beantable.annotation.Managed");
            break;

        case PAGE:
            for (MethodDesc md : classDesc.getMethodDescs()) {
                if (isAction(md)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Action> actionInterface = (Class<? extends Action>) md
                            .getAttribute(Globals.ATTR_ACTION_INTERFACE);
                    baseImportClassNameSet.add(actionInterface.getName());
                    md.setAttribute(Globals.ATTR_ACTION_INTERFACE_SHORTNAME,
                            ClassUtils.getShortName(actionInterface));
                }

                BodyDesc bodyDesc = md.getBodyDesc();
                if (bodyDesc != null) {
                    baseImportClassNameSet.addAll(Arrays.asList(bodyDesc
                            .getDependingClassNames()));
                }
            }
            break;

        case DTO:
            baseImportClassNameSet.add(Serializable.class.getName());
            baseImportClassNameSet.add(Array.class.getName());
            break;

        case DAO:
            importClassNameSet.add("org.seasar.dao.annotation.tiger.S2Dao");
            break;

        case DXO:
            importClassNameSet.add(List.class.getName());
            importClassNameSet.add(entityMetaData.getBeanClassDesc().getName());
            importClassNameSet.add(entityMetaData.getDtoClassDesc().getName());
            break;

        case CONVERTER:
            importClassNameSet.clear();
            baseImportClassNameSet.add(Binding.class.getName());
            baseImportClassNameSet.add(BindingType.class.getName());
            baseImportClassNameSet.add(Messages.class.getName());
            baseImportClassNameSet.add(TypeConversionManager.class.getName());
            if (((TypeDesc[]) parameter.get(Globals.PARAMETER_PAIRTYPEDESCS)).length > 0) {
                baseImportClassNameSet.add(ArrayList.class.getName());
                baseImportClassNameSet.add(List.class.getName());
            }
            break;

        default:
            throw new IllegalArgumentException("Logic error");
        }

        parameter.put(Globals.PARAMETER_PREAMBLE, getJavaPreamble());
        parameter.put(Globals.PARAMETER_CLASSDESC, classDesc);
        parameter.put(Globals.PARAMETER_IMPORTCLASSSET, importClassNameSet);
        parameter.put(Globals.PARAMETER_BASEIMPORTCLASSSET,
                baseImportClassNameSet);
    }

    public void prepareForAttribute(ClassDesc classDesc) {
        if (classDesc.isTypeOf(ClassType.PAGE)) {
            List<MethodDesc> list = new ArrayList<MethodDesc>();
            for (MethodDesc methodDesc : classDesc.getMethodDescs()) {
                if (isAction(methodDesc)) {
                    list.add(methodDesc);
                }
            }
            classDesc.setAttribute(Globals.ATTR_ACTION, list
                    .toArray(new MethodDesc[0]));
        }
    }

    public void adjustByExistentClass(ClassDesc desc) {
        DescPool pool = desc.getDescPool();
        String oldBornOf = pool.getBornOf();
        try {
            pool.setBornOf(null);

            String className = desc.getName();
            Class<?> clazz = getClass(className);
            ClassDesc gapDesc = newClassDesc(pool, clazz, true);
            if (gapDesc == null) {
                gapDesc = newClassDesc(pool, className, null);
            }

            String baseClassName = className + "Base";
            Class<?> baseClass = getClass(baseClassName);
            // 従属タイプのクラスの場合は既存のBaseクラスの情報とマージしなくて良い。
            ClassDesc baseDesc = desc.getType().isSubordinate() ? null
                    : newClassDesc(pool, baseClass, true);
            if (baseDesc == null) {
                baseDesc = newClassDesc(pool, baseClassName, null);
                if (!baseClassName.equals(desc.getSuperclassName())) {
                    baseDesc.setSuperclassName(desc.getSuperclassName());
                }
            }

            if (baseClass != null) {
                // abstractかどうかを保持するようにする。
                desc.setAbstract(Modifier.isAbstract(baseClass.getModifiers()));
            }

            ClassDesc generated = desc.transcriptTo(newClassDesc(pool, desc
                    .getName(), null));
            desc.clear();
            desc.setSourceGeneratorParameter(generated
                    .getSourceGeneratorParameter());
            desc.setAttributeMap(generated.getAttributeMap());

            // baseのうち同じパス由来でないメンバを残す。
            ClassDesc baseDescOtherBoneOf = baseDesc.transcriptTo(pool
                    .getClassDesc(baseDesc.getName()));
            baseDescOtherBoneOf.removeBornOf(desc.getBornOf());
            desc.merge(baseDescOtherBoneOf, true);

            ClassDesc superDesc = newClassDesc(pool, getClass(desc
                    .getSuperclassName()), false);
            if (superDesc == null) {
                superDesc = newClassDesc(pool, Object.class, false);
            }

            PropertyDesc[] pds = generated.getPropertyDescs();
            for (int i = 0; i < pds.length; i++) {
                PropertyDesc generatedPd = pds[i];
                PropertyDesc basePd = baseDesc.getPropertyDesc(generatedPd
                        .getName());

                // baseにあるものは必ず残す。baseになくてsuperやgapにあるものは除去する。
                if (basePd == null || !basePd.isReadable()) {
                    removeModeFrom(generatedPd, PropertyDesc.READ, gapDesc);
                    removeModeFrom(generatedPd, PropertyDesc.READ, superDesc);
                }
                if (basePd == null || !basePd.isWritable()) {
                    removeModeFrom(generatedPd, PropertyDesc.WRITE, gapDesc);
                    removeModeFrom(generatedPd, PropertyDesc.WRITE, superDesc);
                }

                if (!generatedPd.isReadable() && !generatedPd.isWritable()) {
                    // GetterもSetterもないものは削除する。
                    // ただし@Meta(name="property")なプロパティはformのDTOのフィールドを生成するために残す。
                    // superclassがformのDTOのフィールドを持っている時は削除する。
                    if (!pds[i].hasMeta(Globals.META_NAME_PROPERTY)
                            || isFormDtoFieldPresent(superDesc, generatedPd
                                    .getName())) {
                        generated.removePropertyDesc(generatedPd.getName());
                    }
                }

                if (basePd != null && generatedPd != null) {
                    TypeDesc baseTd = basePd.getTypeDesc();
                    TypeDesc generatedTd = generatedPd.getTypeDesc();
                    if (generatedTd.getCollectionImplementationClassName() == null) {
                        generatedTd.setCollectionImplementationClassName(baseTd
                                .getCollectionImplementationClassName());
                    }

                    // 元々ついているMetaでないアノテーションはBaseを優先させる必要があるため、
                    // GeneratedにあるアノテーションのうちBaseにもあるものについてはBaseのものをGeneratedに上書きする。
                    generatedPd.setAnnotationDescs(mergeAnnotationDescs(basePd
                            .getAnnotationDescs(), generatedPd
                            .getAnnotationDescs()));
                    generatedPd
                            .setAnnotationDescsOnGetter(mergeAnnotationDescs(
                                    basePd.getAnnotationDescsOnGetter(),
                                    generatedPd.getAnnotationDescsOnGetter()));
                    generatedPd
                            .setAnnotationDescsOnSetter(mergeAnnotationDescs(
                                    basePd.getAnnotationDescsOnSetter(),
                                    generatedPd.getAnnotationDescsOnSetter()));
                }
            }

            MethodDesc[] mds = generated.getMethodDescs();
            for (int i = 0; i < mds.length; i++) {
                MethodDesc generatedMd = mds[i];
                MethodDesc gapMd = gapDesc.getMethodDesc(generatedMd);
                MethodDesc baseMd = baseDesc.getMethodDesc(generatedMd);
                MethodDesc otherBornOfMd = desc.getMethodDesc(generatedMd);
                MethodDesc superMd = superDesc.getMethodDesc(generatedMd);
                if (baseMd == null && (gapMd != null || superMd != null)) {
                    generated.removeMethodDesc(generatedMd);
                    continue;
                } else if (isAction(generatedMd)) {
                    MethodDescKey key = new MethodDescKey(generatedMd);
                    for (MethodDesc md : gapDesc.getMethodDescs(generatedMd
                            .getName())) {
                        if (!key.equals(new MethodDescKey(md))) {
                            // アクションについては、メソッドシグネチャが一致しなくとも名前が一致するメソッドがあった場合は
                            // （Ymirはアクション名が同一なメソッドが複数存在することを許さないので）生成しない。
                            // ただし、メソッドシグネチャまで一致するメソッドは残す。そうでないとオーバライドされ
                            // ている時に困るので。
                            generated.removeMethodDesc(generatedMd);
                            continue;
                        }
                    }
                }

                TypeDesc returnTd = generatedMd.getReturnTypeDesc();
                if (gapMd != null
                        && !returnTd.equals(gapMd.getReturnTypeDesc())) {
                    TypeDesc td = gapMd.getReturnTypeDesc();
                    generatedMd.setReturnTypeDesc(td.transcriptTo(desc
                            .getDescPool().newTypeDesc(td)));
                } else if (superMd != null
                        && !returnTd.equals(superMd.getReturnTypeDesc())) {
                    TypeDesc td = superMd.getReturnTypeDesc();
                    generatedMd.setReturnTypeDesc(td.transcriptTo(desc
                            .getDescPool().newTypeDesc(td)));
                } else if (otherBornOfMd != null
                        && Void.TYPE.getName().equals(returnTd.getName())) {
                    TypeDesc td = otherBornOfMd.getReturnTypeDesc();
                    generatedMd.setReturnTypeDesc(td.transcriptTo(desc
                            .getDescPool().newTypeDesc(td)));
                }

                // メソッドシグネチャが一致するメソッドがgapまたはbaseにある場合は、throwsDescを既存クラスから構築する。
                // そうしないとオーバライドしている場合に困るので。
                MethodDesc existentMd = gapMd != null ? gapMd : baseMd;
                if (existentMd != null) {
                    ThrowsDesc throwsDesc = new ThrowsDescImpl();
                    for (String throwableClassName : existentMd.getThrowsDesc()
                            .getThrowableClassNames()) {
                        throwsDesc.addThrowable(throwableClassName);
                    }
                    generatedMd.setThrowsDesc(throwsDesc);
                }

                // 元々ついているMetaでないアノテーションはBaseを優先させる必要があるため、
                // GeneratedにあるアノテーションのうちBaseにもあるものについてはBaseのものをGeneratedに上書きする。
                if (baseMd != null) {
                    generatedMd.setAnnotationDescs(mergeAnnotationDescs(baseMd
                            .getAnnotationDescs(), generatedMd
                            .getAnnotationDescs()));
                }
            }

            // マージ後にもgenerated側のプロパティやメソッドの順番を保持したいため、こうしている。

            Map<String, PropertyDesc> newDescPdMap = new LinkedHashMap<String, PropertyDesc>();
            Map<String, PropertyDesc> descPdMap = new LinkedHashMap<String, PropertyDesc>();
            for (PropertyDesc descPd : desc.getPropertyDescs()) {
                descPdMap.put(descPd.getName(), descPd);
            }
            for (PropertyDesc generatedPd : generated.getPropertyDescs()) {
                String key = generatedPd.getName();
                PropertyDesc pd = descPdMap.get(key);
                if (pd != null) {
                    newDescPdMap.put(key, pd);
                    descPdMap.remove(key);
                }
            }
            for (PropertyDesc descPd : descPdMap.values()) {
                newDescPdMap.put(descPd.getName(), descPd);
            }
            desc.setPropertyDescs(newDescPdMap.values().toArray(
                    new PropertyDesc[0]));

            Map<MethodDescKey, MethodDesc> newDescMdMap = new LinkedHashMap<MethodDescKey, MethodDesc>();
            Map<MethodDescKey, MethodDesc> descMdMap = new LinkedHashMap<MethodDescKey, MethodDesc>();
            for (MethodDesc descMd : desc.getMethodDescs()) {
                descMdMap.put(new MethodDescKey(descMd), descMd);
            }
            for (MethodDesc generatedMd : generated.getMethodDescs()) {
                MethodDescKey key = new MethodDescKey(generatedMd);
                MethodDesc md = descMdMap.get(key);
                if (md != null) {
                    newDescMdMap.put(key, md);
                    descMdMap.remove(key);
                }
            }
            for (MethodDesc descMd : descMdMap.values()) {
                newDescMdMap.put(new MethodDescKey(descMd), descMd);
            }
            desc.setMethodDescs(newDescMdMap.values()
                    .toArray(new MethodDesc[0]));

            desc.merge(generated, true);

            // 最終調整。

            if (desc.isTypeOf(ClassType.DTO)) {
                MethodDesc methodDesc = new MethodDescImpl(pool, "toString");
                desc.removeMethodDesc(methodDesc);
            }
        } finally {
            pool.setBornOf(oldBornOf);
        }
    }

    // 元々ついているMetaでないアノテーションはBaseを優先させる必要があるため、
    // GeneratedにあるアノテーションのうちBaseにもあるものについてはBaseのものをGeneratedに上書きする。
    private AnnotationDesc[] mergeAnnotationDescs(
            AnnotationDesc[] baseAnnotationDescs,
            AnnotationDesc[] generatedAnnotationDescs) {
        Map<String, AnnotationDesc> baseNonMetaAdMap = new HashMap<String, AnnotationDesc>();
        Map<String, AnnotationDesc> baseMetaAdMap = new HashMap<String, AnnotationDesc>();
        Map<String, AnnotationDesc> generatedNonMetaAdMap = new HashMap<String, AnnotationDesc>();
        Map<String, AnnotationDesc> generatedMetaAdMap = new HashMap<String, AnnotationDesc>();

        for (AnnotationDesc ad : baseAnnotationDescs) {
            if (DescUtils.isMetaAnnotation(ad)) {
                baseMetaAdMap.put(ad.getName(), ad);
            } else {
                baseNonMetaAdMap.put(ad.getName(), ad);
            }
        }
        for (AnnotationDesc ad : generatedAnnotationDescs) {
            if (DescUtils.isMetaAnnotation(ad)) {
                generatedMetaAdMap.put(ad.getName(), ad);
            } else {
                generatedNonMetaAdMap.put(ad.getName(), ad);
            }
        }

        // 非MetaはBaseが優先。
        ClassDesc dummyCd = new ClassDescImpl(null, "");
        for (AnnotationDesc ad : DescUtils.merge(baseNonMetaAdMap.values()
                .toArray(new AnnotationDesc[0]), generatedNonMetaAdMap.values()
                .toArray(new AnnotationDesc[0]), false)) {
            dummyCd.setAnnotationDesc(ad);
        }

        // MetaはGeneratedが優先。
        for (AnnotationDesc ad : DescUtils.merge(baseMetaAdMap.values()
                .toArray(new AnnotationDesc[0]), generatedMetaAdMap.values()
                .toArray(new AnnotationDesc[0]), true)) {
            dummyCd.setAnnotationDesc(ad);
        }

        return dummyCd.getAnnotationDescs();
    }

    private boolean isAction(MethodDesc methodDesc) {
        if (methodDesc == null) {
            return false;
        } else {
            Boolean action = (Boolean) methodDesc
                    .getAttribute(Globals.ATTR_ACTION);
            return action != null && action.booleanValue();
        }
    }

    boolean isFormDtoFieldPresent(ClassDesc cd, String name) {
        Class<?> clazz = getClass(cd.getName());
        if (clazz == null || clazz == Object.class) {
            return false;
        }
        do {
            for (Field field : clazz.getDeclaredFields()) {
                if (name.equals(MetaUtils.getFirstValue(field, "property"))) {
                    return true;
                }
            }
        } while ((clazz = clazz.getSuperclass()) != Object.class);
        return false;
    }

    void removeModeFrom(PropertyDesc pd, int mode, ClassDesc cd) {
        PropertyDesc p = cd.getPropertyDesc(pd.getName());
        if (p != null) {
            if ((p.getMode() & mode) != 0) {
                pd.setMode(pd.getMode() & ~mode);
            }
        }
    }

    public void mergeWithExistentClass(ClassDesc classDesc) {
        String className = classDesc.getName();
        classDesc.merge(newClassDesc(classDesc.getDescPool(),
                getClass(className), false), false);
    }

    public void writeSourceFile(ClassDesc classDesc, ClassDescSet classDescSet)
            throws InvalidClassDescException {
        // ClassDescからソースファイルを出力可能かのチェックを行なう。
        Result result = DescValidator.validate(classDesc, classDescSet);
        if (!result.isValid()) {
            throw new InvalidClassDescException(result.getClassNames());
        }

        writeString(generateBaseSource(classDesc), getSourceFile(classDesc
                .getName()
                + "Base"));

        // gap側のクラスは存在しない場合のみ生成する。
        File sourceFile = getSourceFile(classDesc.getName());
        if (!sourceFile.exists()) {

            writeString(generateGapSource(classDesc), sourceFile);
        }
    }

    @SuppressWarnings("unchecked")
    public String generateGapSource(ClassDesc classDesc) {
        if (classDesc == null) {
            return null;
        }

        ImportDesc importDesc = new ImportDescImpl(this, classDesc);
        classDesc.getSourceGeneratorParameter().put(
                Globals.PARAMETER_IMPORTDESC, importDesc);

        HashSet<String> set = new HashSet<String>(((Set<String>) classDesc
                .getSourceGeneratorParameter().get(
                        Globals.PARAMETER_IMPORTCLASSSET)));
        classDesc.setTouchedClassNameSet(set);

        // importを正しく生成するために2パスにしている。
        sourceGenerator_.generateGapSource(classDesc);
        importDesc.add(set);

        return sourceGenerator_.generateGapSource(classDesc);
    }

    @SuppressWarnings("unchecked")
    public String generateBaseSource(ClassDesc classDesc) {
        if (classDesc == null) {
            return null;
        }

        Map<String, Object> parameter = classDesc.getSourceGeneratorParameter();
        ImportDesc importDesc = new ImportDescImpl(this, classDesc);
        parameter.put(Globals.PARAMETER_IMPORTDESC, importDesc);

        HashSet<String> set = new HashSet<String>(((Set<String>) parameter
                .get(Globals.PARAMETER_BASEIMPORTCLASSSET)));
        classDesc.setTouchedClassNameSet(set);
        List<Desc<?>> list = (List<Desc<?>>) parameter
                .get(Globals.PARAMETER_AUXDESCLIST);
        if (list != null) {
            for (Desc<?> desc : list) {
                desc.setTouchedClassNameSet(set);
            }
        }

        // importを正しく生成するために2パスにしている。
        sourceGenerator_.generateBaseSource(classDesc);
        importDesc.add(set);

        return sourceGenerator_.generateBaseSource(classDesc);
    }

    public void writeSourceFile(String templateName, ClassDesc classDesc,
            boolean force) {
        File sourceFile = getSourceFile(classDesc.getName());
        if (force || !sourceFile.exists()) {
            writeString(sourceGenerator_.generateClassSource(templateName,
                    classDesc), sourceFile);
        }
    }

    void writeString(String string, File file) {
        if (string == null) {
            return;
        }
        file.getParentFile().mkdirs();
        try {
            writeString(string, new FileOutputStream(file));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    void writeString(String string, OutputStream os) {
        try {
            if (string == null) {
                return;
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    os, sourceEncoding_));
            writer.write(string);
            writer.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    public MatchedPathMapping findMatchedPathMapping(String path,
            HttpMethod method) {
        if (path == null) {
            return null;
        }
        return ymir_.findMatchedPathMapping(path, method);
    }

    public boolean isDenied(String path, HttpMethod method) {
        if (path == null) {
            return true;
        }
        MatchedPathMapping matched = ymir_.findMatchedPathMapping(path, method);
        if (matched == null) {
            return true;
        } else {
            return matched.getPathMapping().isDenied();
        }
    }

    public String getComponentName(String path, HttpMethod method) {
        if (path == null) {
            return null;
        }
        MatchedPathMapping matched = ymir_.findMatchedPathMapping(path, method);
        if (matched == null) {
            return null;
        } else {
            return matched.getPathMapping().getPageComponentName(
                    matched.getVariableResolver());
        }
    }

    public String getClassName(String componentName) {
        if (componentName != null) {
            LocalHotdeployS2Container hotdeployContainer = getApplication()
                    .getHotdeployS2Container();
            String className = hotdeployContainer.getNamingConvention()
                    .fromComponentNameToClassName(componentName);
            if (className != null) {
                return className;
            }
        }
        return null;
    }

    public Class<?> getClass(String className) {
        if (className == null) {
            return null;
        }

        Class<?> clazz = ClassUtils.getPrimitive(className);
        if (clazz != null) {
            return clazz;
        }

        ClassLoader cl = getClassLoader();
        try {
            return cl.loadClass(className);
        } catch (ClassNotFoundException ex) {
            if (className.indexOf('.') < 0) {
                try {
                    return cl.loadClass(PACKAGEPREFIX_JAVA_LANG + className);
                } catch (ClassNotFoundException ex2) {
                    try {
                        return cl
                                .loadClass(PACKAGEPREFIX_JAVA_UTIL + className);
                    } catch (ClassNotFoundException ex3) {
                        String dtoClassName = setting_
                                .findDtoClassName(className);
                        if (dtoClassName != null) {
                            try {
                                return cl.loadClass(dtoClassName);
                            } catch (ClassNotFoundException ignore) {
                            }
                        }
                        ClassTraverser traverser = new ClassTraverser();
                        for (Class<?> landmark : getLandmarks()) {
                            traverser.addReferenceClass(landmark);
                        }
                        for (String rootPackageName : getRootPackageNames()) {
                            traverser.addClassPattern(rootPackageName,
                                    className);
                        }

                        final String[] found = new String[1];
                        traverser
                                .setClassHandler(new ClassTraversal.ClassHandler() {
                                    public void processClass(
                                            String packageName,
                                            String shortClassName) {
                                        found[0] = packageName + "."
                                                + shortClassName;
                                    }
                                });
                        traverser.traverse();
                        if (found[0] != null) {
                            try {
                                return cl.loadClass(found[0]);
                            } catch (ClassNotFoundException ignore) {
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private Class<?>[] getLandmarks() {
        ClassLoader classLoader = getClassLoader();
        List<Class<?>> landmarkList = new ArrayList<Class<?>>();
        for (String landmarkClassName : PropertyUtils.toLines(getApplication()
                .getProperty(Globals.APPKEY_LANDMARK,
                        Globals.LANDMARK_CLASSNAME))) {
            try {
                landmarkList.add(classLoader.loadClass(landmarkClassName));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return landmarkList.toArray(new Class<?>[0]);
    }

    protected ClassLoader getClassLoader() {
        return getApplication().getHotdeployS2Container().getContainer()
                .getClassLoader();
    }

    public PropertyDescriptor getPropertyDescriptor(String className,
            String propertyName) {
        Class<?> clazz = getClass(className);
        if (clazz == null || propertyName == null) {
            return null;
        }

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            if (descriptor.getName().equals(propertyName)) {
                return descriptor;
            }
        }
        return null;
    }

    public Template getTemplate(String path) {
        return templateProvider_.getTemplate(path);
    }

    public File getSourceFile(String className) {
        return new File(getSourceDirectory(), className.replace('.', '/')
                + ".java");
    }

    public Properties getSourceCreatorProperties() {
        if (sourceCreatorProperties_ == null) {
            sourceCreatorProperties_ = new Properties();
            File file = getSourceCreatorPropertiesFile();
            if (file.exists()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    sourceCreatorProperties_.load(new BufferedInputStream(fis));
                } catch (IOException ex) {
                    log_.error("Can't read properties: " + file);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException ignore) {
                        }
                    }
                }
            }
        }
        return sourceCreatorProperties_;
    }

    public File getSourceCreatorPropertiesFile() {
        return new File(getPreferencesDirectory(), SOURCECREATOR_PREFS);
    }

    File getMappingPropertiesFile() {
        return new File(getPreferencesDirectory(), MAPPING_PREFS);
    }

    public PersistentProperties getMappingProperties() {
        return new PersistentProperties(getMappingPropertiesFile());
    }

    @SuppressWarnings("unchecked")
    public void saveSourceCreatorProperties() {
        if (sourceCreatorProperties_ == null) {
            return;
        }

        MapProperties prop = new MapProperties(new TreeMap(
                sourceCreatorProperties_));

        File file = getSourceCreatorPropertiesFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            prop.store(new BufferedOutputStream(fos), "ISO-8859-1");
            fos = null;
        } catch (IOException ex) {
            log_.error("Can't write properties: " + file);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    public File getSourceDirectory() {
        String sourceDirectory = getApplication().getSourceDirectory();
        if (sourceDirectory != null) {
            return new File(sourceDirectory);
        } else {
            return null;
        }
    }

    public File getResourcesDirectory() {
        String resourcesDirectory = getApplication().getResourcesDirectory();
        if (resourcesDirectory != null) {
            return new File(resourcesDirectory);
        } else {
            return null;
        }
    }

    File getPreferencesDirectory() {
        return new File(getApplication().getProjectRoot(),
                Globals.PATH_PREFERENCES_DIRECTORY);
    }

    public File getWebappSourceRoot() {
        return new File(getApplication().getWebappSourceRoot());
    }

    public TemplateAnalyzer getTemplateAnalyzer() {
        return analyzer_;
    }

    public String getSourceEncoding() {
        return sourceEncoding_;
    }

    public String getFirstRootPackageName() {
        return getApplication().getFirstRootPackageName();
    }

    public String[] getRootPackageNames() {
        return getApplication().getRootPackageNames();
    }

    public String getPagePackageName() {
        return getFirstRootPackageName() + "."
                + namingConvention_.getSubApplicationRootPackageName();
    }

    public String getDtoPackageName() {
        return getFirstRootPackageName() + "."
                + namingConvention_.getDtoPackageName();
    }

    public String getDaoPackageName() {
        return getFirstRootPackageName() + "."
                + namingConvention_.getDaoPackageName();
    }

    public String getDxoPackageName() {
        return getFirstRootPackageName() + "."
                + namingConvention_.getDxoPackageName();
    }

    public String getConverterPackageName() {
        return getFirstRootPackageName() + "."
                + namingConvention_.getConverterPackageName();
    }

    public SourceGenerator getSourceGenerator() {
        return sourceGenerator_;
    }

    public ResponseCreator getResponseCreator() {
        return responseCreator_;
    }

    public Application getApplication() {
        return applicationManager_.findContextApplication();
    }

    public ServletContext getServletContext() {
        return (ServletContext) getRootS2Container().getComponent(
                ServletContext.class);
    }

    public HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) getRootS2Container().getComponent(
                HttpServletRequest.class);
    }

    public HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) getRootS2Container().getComponent(
                HttpServletResponse.class);
    }

    Request getRequest() {
        return (Request) getRootS2Container().getComponent(Request.class);
    }

    S2Container getS2Container() {
        return getApplication().getS2Container();
    }

    S2Container getRootS2Container() {
        return SingletonS2ContainerFactory.getContainer();
    }

    public void registerUpdateAction(Object condition, UpdateAction updateAction) {
        actionSelector_.register(condition, updateAction);
    }

    public ClassDesc newClassDesc(DescPool pool, String className,
            ClassCreationHintBag hintBag) {
        return newClassDesc(pool, className, null, hintBag);
    }

    public ClassDesc newClassDesc(DescPool pool, String className,
            String qualifier, ClassCreationHintBag hintBag) {
        ClassDesc classDesc = new ClassDescImpl(pool, className, qualifier);

        // スーパークラスをセットする。
        String superclassName = null;
        if (hintBag != null) {
            ClassHint hint = hintBag.getClassHint(className);
            if (hint != null) {
                superclassName = hint.getSuperclassName();
            }
        }
        if (superclassName == null) {
            Class<?> clazz = getClass(className);
            if (clazz != null) {
                Class<?> superclass = clazz.getSuperclass();
                if (superclass != null && superclass != Object.class) {
                    if (superclass.getName().equals(className + "Base")) {
                        superclass = superclass.getSuperclass();
                    }
                    superclassName = superclass.getName();
                }
            }
            if (superclassName == null) {
                superclassName = setting_.getSuperclassName(className);
                if (superclassName == null
                        && classDesc.isTypeOf(ClassType.PAGE)) {
                    superclassName = setting_.getPageSuperclassName();
                    if (className.equals(superclassName)) {
                        // Page共通の親クラス名をPageBase等にしていると、共通の親クラスが存在しない場合に
                        // そのさらに親クラスとして自分自身が見つかってしまい無限ループに陥ってしまう。
                        // それを避けるためにこうしている。
                        superclassName = null;
                    }
                }
                if (superclassName != null) {
                    Class<?> superclass = findClass(ClassUtils
                            .getShortName(superclassName), className);
                    if (superclass != null) {
                        superclassName = superclass.getName();
                    }
                }
            }
        }

        if (superclassName != null) {
            classDesc.setSuperclassName(superclassName);
        }

        return classDesc;
    }

    public TemplateProvider getTemplateProvider() {
        return templateProvider_;
    }

    public String filterResponse(String response) {
        if (!shouldUpdate()) {
            return response;
        }

        if (setting_.isInPlaceEditorEnabled()
                || setting_.isControlPanelEnabled()) {
            String jsPrefix = "<script type=\"text/javascript\" src=\""
                    + getHttpServletRequest().getContextPath() + PATH_PREFIX
                    + "resource/js/";
            String jsSuffix = "\"></script>";
            response = response.replace("</head>", jsPrefix
                    + "prototype/prototype.js" + jsSuffix + jsPrefix
                    + "scriptaculous/scriptaculous.js" + jsSuffix + jsPrefix
                    + "sourceCreator.js" + jsSuffix + "</head>");

            if (setting_.isInPlaceEditorEnabled()) {
                response = Pattern
                        .compile("(<body(\\s+[^>]*)?>)")
                        .matcher(response)
                        .replaceFirst(
                                "$1"
                                        + Matcher
                                                .quoteReplacement("<div id=\"__ymir__inPlaceEditor\">"));
                response = response.replace("</body>", "</div></body>");
            }
            if (setting_.isControlPanelEnabled()) {
                response = response.replace("</body>",
                        "<div id=\"__ymir__controlPanel\">"
                                + createControlPanelFormHTML(getRequest())
                                + createUpdateClassesButtonHTML(getRequest())
                                + "</div></body>");
            }
        }

        return response;
    }

    String createControlPanelFormHTML(Request request) {
        return createButtonHTML(request, "systemConsole", "SYSTEM CONSOLE");
    }

    String createUpdateClassesButtonHTML(Request request) {
        return createButtonHTML(request, "updateClasses", "UPDATE CLASSES");
    }

    String createButtonHTML(Request request, String taskName, String buttonLabel) {
        StringBuilder sb = new StringBuilder();
        sb.append("<form action=\"").append(request.getAbsolutePath()).append(
                "\" method=\"post\">").append("<input type=\"hidden\" name=\"")
                .append(PARAM_TASK).append("\" value=\"").append(taskName)
                .append("\" /><input type=\"hidden\" name=\"").append(
                        PARAM_METHOD).append("\" value=\"").append(
                        request.getMethod()).append("\" />");
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            String name = itr.next();
            if (name.startsWith(PARAM_PREFIX)) {
                continue;
            }
            String encodedName;
            try {
                encodedName = HTMLUtils
                        .filter(URLEncoder.encode(name, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException("Can't happen!", ex);
            }
            for (String value : request.getParameterValues(name)) {
                String encodedValue;
                try {
                    encodedValue = HTMLUtils.filter(URLEncoder.encode(value,
                            "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException("Can't happen!", ex);
                }
                sb.append("<input type=\"hidden\" name=\"").append(encodedName)
                        .append("\" value=\"").append(encodedValue).append(
                                "\" />");
            }
        }
        sb.append("<input type=\"submit\" value=\"").append(buttonLabel)
                .append("\" /></form>");
        return sb.toString();
    }

    public String getTemplateEncoding() {
        return getApplication().getTemplateEncoding();
    }

    public long getCheckedTime(Template template) {
        Properties prop = getSourceCreatorProperties();
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

    public void updateCheckedTime(Template template) {
        Properties prop = getSourceCreatorProperties();
        String key = PREFIX_CHECKEDTIME + template.getPath();
        prop.setProperty(key, String.valueOf(System.currentTimeMillis()));
        saveSourceCreatorProperties();
    }

    public String getJavaPreamble() {
        return IOUtils.readString(getClassLoader().getResourceAsStream(
                RESOURCE_PREAMBLE_JAVA), SourceGenerator.TEMPLATE_ENCODING,
                true);
    }

    public SourceCreatorSetting getSourceCreatorSetting() {
        return setting_;
    }

    public ExtraPathMapping getExtraPathMapping(String path, HttpMethod method) {
        MatchedPathMapping mapping = findMatchedPathMapping(path, method);
        if (mapping == null) {
            return null;
        }

        Class<?> clazz = mapping.getPathMapping().getClass();
        PathMappingExtraData<?> pathMappingExtraData;
        do {
            pathMappingExtraData = pathMappingExtraDataMap_.get(clazz);
            clazz = clazz.getSuperclass();
        } while (pathMappingExtraData == null && clazz != Object.class);
        if (pathMappingExtraData == null) {
            throw new RuntimeException("PathMappingExtraData not found."
                    + " Please register PathMappingExtraData Component: "
                    + clazz.getName());
        }
        return new ExtraPathMappingImpl(pathMappingExtraData, mapping, path,
                method);
    }

    public Class<?> findClass(String name, String baseClassName) {
        int pre = baseClassName.length();
        int dot;
        while ((dot = baseClassName.lastIndexOf('.', pre)) >= 0) {
            try {
                return ClassUtils.forName(baseClassName.substring(0, dot + 1)
                        + name);
            } catch (ClassNotFoundException ignore) {
            }
            pre = dot - 1;
        }
        return null;
    }

    public MethodDesc newActionMethodDesc(DescPool pool, String path,
            HttpMethod method, ActionSelectorSeed seed) {
        MethodDesc methodDesc = getExtraPathMapping(path, method)
                .newActionMethodDesc(pool, seed);
        String returnType = setting_.getActionReturnType(method);
        methodDesc.setReturnTypeDesc(returnType);
        setActionMethodDescBodyTo(methodDesc);
        setActionInfo(methodDesc, getActionInterface(method), seed
                .getActionKey());

        return methodDesc;
    }

    private Class<? extends Action> getActionInterface(HttpMethod method) {
        return ACTIONINTERFACE_BY_HTTPMETHOD_MAP.get(method);
    }

    public MethodDesc newPrerenderActionMethodDesc(DescPool pool, String path,
            HttpMethod method, ActionSelectorSeed seed) {
        return getExtraPathMapping(path, method).newPrerenderActionMethodDesc(
                pool, seed);
    }

    void setActionMethodDescBodyTo(MethodDesc methodDesc) {
        String returnType = methodDesc.getReturnTypeDesc().getCompleteName();
        if (returnType.equals(String.class.getName())) {
            methodDesc.setBodyDesc(new BodyDescImpl("return \"passthrough:\";",
                    new String[0]));
        } else if (returnType.equals(Response.class.getName())) {
            methodDesc.setBodyDesc(new BodyDescImpl(
                    "return new PassthroughResponse();",
                    new String[] { PassthroughResponse.class.getName() }));
        }
    }

    boolean isValidationFailedMethodEnabled() {
        return PropertyUtils.valueOf(getApplication().getProperty(
                APPKEY_CORE_CONSTRAINT_VALIDATIONFAILEDMETHOD_ENABLE), true);
    }

    boolean isPermissionDeniedMethodEnabled() {
        return PropertyUtils.valueOf(getApplication().getProperty(
                APPKEY_CORE_CONSTRAINT_PERMISSIONDENIEDMETHOD_ENABLE), true);
    }

    public boolean isGeneratedClass(String className) {
        if (className == null) {
            return false;
        }

        return className.startsWith(getFirstRootPackageName() + ".");
    }

    public boolean isDtoClass(String className) {
        if (className == null) {
            return false;
        }
        return setting_.isOnDtoSearchPath(className)
                || new ClassDescImpl(null, className).isTypeOf(ClassType.DTO);
    }

    public String getActionKeyFromParameterName(String path, HttpMethod method,
            String parameterName) {
        if (parameterName == null) {
            return null;
        }

        MatchedPathMapping matched = findMatchedPathMapping(path, method);
        if (matched == null) {
            return null;
        }

        return matched.getPathMapping().getActionKeyFromParameterName(
                parameterName);
    }

    public ParameterRole inferParameterRole(String path, HttpMethod method,
            String className, String parameterName, ClassHint classHint) {
        if (classHint != null) {
            ParameterRole role = classHint.getParameterRole(parameterName);
            if (role != ParameterRole.UNDECIDED) {
                return role;
            }
        }

        String actionKey = getActionKeyFromParameterName(path, method,
                parameterName);
        if (actionKey == null) {
            return ParameterRole.PARAMETER;
        }

        if (getPropertyDescriptor(className, actionKey) != null) {
            return ParameterRole.PARAMETER;
        }

        String methodName = newActionMethodDesc(
                DescPool.newInstance(this, null), path, method,
                new ActionSelectorSeedImpl(actionKey)).getName();
        Class<?> clazz = getClass(className);
        if (clazz != null) {
            for (Method m : ClassUtils.getMethods(clazz)) {
                if (m.getName().equals(methodName)) {
                    return ParameterRole.BUTTON;
                }
            }
        }

        return ParameterRole.UNDECIDED;
    }

    public ClassDesc buildTransitionClassDesc(DescPool pool, String path,
            HttpMethod method, Map<String, String[]> parameterMap) {
        MatchedPathMapping matched = findMatchedPathMapping(path, method);
        if (matched == null || matched.isDenied()) {
            return null;
        }
        String pageClassName = getClassName(matched.getPageComponentName());
        if (pageClassName == null) {
            return null;
        }
        ClassDesc classDesc = pool.getClassDesc(pageClassName);
        for (Iterator<String> itr = parameterMap.keySet().iterator(); itr
                .hasNext();) {
            String name = itr.next();
            if (!AnalyzerUtils.shouldGeneratePropertyForParameter(name)) {
                continue;
            }
            if (BeanUtils.isSingleSegment(name)) {
                ParameterRole role;
                String[] values = parameterMap.get(name);
                if (!(values == null || values.length == 0 || values.length == 1
                        && "".equals(values[0]))) {
                    role = ParameterRole.PARAMETER;
                } else {
                    role = inferParameterRole(path, method, pageClassName,
                            name, getClassHint(pool, pageClassName));
                }

                switch (role) {
                case PARAMETER:
                    PropertyDesc propertyDesc = addPropertyDesc(classDesc,
                            name, PropertyDesc.WRITE | PropertyDesc.READ,
                            pageClassName);
                    propertyDesc
                            .setAnnotationDescOnSetter(new AnnotationDescImpl(
                                    RequestParameter.class.getName()));
                    DescUtils.addParameter(propertyDesc, name);
                    break;

                case BUTTON:
                    MethodDesc methodDesc = newActionMethodDesc(classDesc
                            .getDescPool(), path, method,
                            new ActionSelectorSeedImpl(name));
                    if (classDesc.getMethodDesc(methodDesc) == null) {
                        classDesc.setMethodDesc(methodDesc);
                    }
                    break;

                case UNDECIDED:
                    String[] parameters = (String[]) classDesc
                            .getAttribute(Globals.ATTR_UNDECIDEDPARAMETERNAMES);
                    if (parameters == null) {
                        parameters = new String[] { name };
                    } else {
                        parameters = (String[]) ArrayUtil.add(parameters, name);
                    }
                    classDesc.setAttribute(
                            Globals.ATTR_UNDECIDEDPARAMETERNAMES, parameters);
                    break;

                default:
                    throw new RuntimeException("Logic error");
                }
            } else {
                PropertyDesc propertyDesc = addPropertyDesc(classDesc,
                        BeanUtils.getFirstSimpleSegment(name),
                        PropertyDesc.READ, pageClassName);
                propertyDesc.setAnnotationDescOnGetter(new AnnotationDescImpl(
                        RequestParameter.class.getName()));
                DescUtils.addParameter(propertyDesc, name);
            }
        }

        return classDesc;
    }

    public PropertyDesc addPropertyDesc(ClassDesc classDesc,
            String propertyName, int mode, String propertyTypeAlias,
            boolean asCollection, String collectionClassName, int probability,
            String pageClassName) {
        PropertyDesc propertyDesc = classDesc.addPropertyDesc(propertyName,
                mode);
        if (log_.isDebugEnabled()) {
            log_.debug("Adding property '" + propertyName
                    + "' (object path is '" + getPathExpression(propertyDesc)
                    + "' ...");
        }
        if (propertyDesc.isTypeAlreadySet(probability)) {
            // 差し替えない。
            if (log_.isDebugEnabled()) {
                log_
                        .debug("Nothing has been done because type of this property had been set.");
            }
            return propertyDesc;
        }

        TypeDesc typeDesc;

        // プロパティ型のヒント情報を見る。
        // ヒントがなければ、実際のクラスからプロパティ型を取得する。
        String className = classDesc.getName();
        String baseClassName = getGeneratedClassName(className, pageClassName);
        PropertyTypeHint hint = getPropertyTypeHint(classDesc.getDescPool(),
                className, propertyName);
        if (hint != null) {
            typeDesc = classDesc.getDescPool().newTypeDesc(
                    hint.getTypeName(),
                    getQualifier(propertyTypeAlias != null ? propertyTypeAlias
                            : asCollection ? toSingular(propertyName)
                                    : propertyName, GenericsUtils
                            .getComponentClassName(hint.getTypeName())));
            probability = PROBABILITY_MAXIMUM;
        } else {
            PropertyDescriptor descriptor = findPropertyDescriptor(classDesc,
                    propertyName);
            if (descriptor != null) {
                Method readMethod = descriptor.getReadMethod();
                if (readMethod != null) {
                    propertyDesc.setGetterName(readMethod.getName());
                }

                String componentClassName;
                Class<?> componentClass = getClass(GenericsUtils
                        .getComponentPropertyTypeName(descriptor));
                String qualifier = null;
                String interfaceClassName = null;

                if (componentClass.isInterface()) {
                    // インタフェースの場合は実装クラス型を推論する。
                    String qfr = findQualifier(propertyDesc);
                    componentClassName = findPropertyClassName(
                            propertyTypeAlias != null ? propertyTypeAlias
                                    : qfr != null ? qfr
                                            + getShorterName(componentClass)
                                            : decapitalize(ClassUtils
                                                    .getShorterName(componentClass)),
                            baseClassName);

                    if (qfr != null) {
                        // qualifierはrepeat変数以上に強い。
                        probability = PROBABILITY_TYPE;
                    }

                    interfaceClassName = componentClass.getName();
                } else {
                    // そうでない場合は実際の型をそのまま使う。
                    componentClassName = componentClass.getName();
                    qualifier = getQualifier(
                            propertyTypeAlias != null ? propertyTypeAlias
                                    : asCollection ? toSingular(propertyName)
                                            : propertyName, componentClassName);
                    if (isOuter(descriptor)) {
                        probability = PROBABILITY_MAXIMUM;
                    } else {
                        probability = PROBABILITY_TYPE;
                    }
                }

                String typeName = GenericsUtils
                        .getGenericPropertyTypeName(descriptor);
                if (Collection.class.isAssignableFrom(descriptor
                        .getPropertyType())) {
                    TypeToken typeToken = new TypeToken(typeName);
                    typeToken.getTypes()[0].setBaseName(componentClassName);
                    typeDesc = classDesc.getDescPool().newTypeDesc(
                            typeToken.getAsString(), qualifier);
                } else if (descriptor.getPropertyType().isArray()) {
                    typeDesc = classDesc.getDescPool().newTypeDesc(
                            componentClassName + "[]", qualifier);
                } else {
                    typeDesc = classDesc.getDescPool().newTypeDesc(
                            componentClassName, qualifier);
                }

                if (interfaceClassName != null) {
                    ClassDesc componentClassDesc = typeDesc
                            .getComponentClassDesc();
                    componentClassDesc
                            .setInterfaceTypeDescs(new TypeDesc[] { classDesc
                                    .getDescPool().newTypeDesc(
                                            interfaceClassName) });

                    // スーパークラスが未設定の場合は、インタフェースの抽象実装クラスがあれば
                    // それをスーパークラスとする。
                    if (componentClassDesc.getSuperclassName() == null) {
                        String abstractClassName = getSourceCreatorSetting()
                                .findDtoClassName(
                                        PREFIX_ABSTRACT
                                                + getShortName(interfaceClassName));
                        if (abstractClassName != null) {
                            componentClassDesc
                                    .setSuperclassName(abstractClassName);
                        }
                    }
                }
            } else {
                // 推論できなかった場合は名前から推論を行なう。
                String seed = propertyTypeAlias != null ? propertyTypeAlias
                        : asCollection ? toSingular(propertyName)
                                : propertyName;
                String propertyClassName = inferPropertyClassName(seed,
                        baseClassName);
                typeDesc = classDesc.getDescPool().newTypeDesc(
                        propertyClassName,
                        getQualifier(seed, propertyClassName));
                if (asCollection) {
                    setToCollection(typeDesc, collectionClassName);
                }
            }
        }

        if (typeDesc.isCollection()
                && List.class.getName().equals(
                        typeDesc.getCollectionClassName())) {
            typeDesc.setCollectionImplementationClassName(propertyDesc
                    .getTypeDesc().getCollectionImplementationClassName());
        }

        propertyDesc.setTypeDesc(typeDesc);
        propertyDesc.notifyTypeUpdated(probability);

        if (log_.isDebugEnabled()) {
            log_.debug("This property's type has been inferred as '" + typeDesc
                    + "'");
        }

        return propertyDesc;
    }

    public PropertyDesc addPropertyDesc(ClassDesc classDesc,
            String propertyName, int mode, String pageClassName) {
        return addPropertyDesc(classDesc, propertyName, mode, null, false,
                null, PROBABILITY_DEFAULT, pageClassName);
    }

    private String getPathExpression(PropertyDesc propertyDesc) {
        LinkedList<String> list = new LinkedList<String>();
        int count = 10;
        Desc<?> desc = propertyDesc;
        do {
            if (desc instanceof PropertyDesc) {
                list.addFirst(((PropertyDesc) desc).getName());
            } else if (desc instanceof ClassDesc) {
                list.addFirst("[" + ((ClassDesc) desc).getShortName() + "]");
            }
            count--;
        } while ((desc = desc.getParent()) != null && count >= 0);

        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (String segment : list) {
            sb.append(delim).append(segment);
            delim = "/";
        }
        return sb.toString();
    }

    private PropertyTypeHint getPropertyTypeHint(DescPool pool,
            String className, String propertyName) {
        if (pool.getHintBag() != null) {
            return pool.getHintBag().getPropertyTypeHint(className,
                    propertyName);
        } else {
            return null;
        }
    }

    private ClassHint getClassHint(DescPool pool, String className) {
        if (pool.getHintBag() != null) {
            return pool.getHintBag().getClassHint(className);
        } else {
            return null;
        }
    }

    private String toSingular(String name) {
        if (name == null) {
            return null;
        }
        if (name.endsWith(MULTIPLE_SUFFIX)) {
            return name.substring(0, name.length() - MULTIPLE_SUFFIX.length())
                    + SINGULAR_SUFFIX;
        } else if (name.endsWith(MULTIPLE_SUFFIX2)) {
            return name.substring(0, name.length() - MULTIPLE_SUFFIX2.length())
                    + SINGULAR_SUFFIX2;
        }
        return name;
    }

    private String getQualifier(String seed, String className) {
        if (!getSourceCreatorSetting().isOnDtoSearchPath(className)) {
            return null;
        }

        String suffix = ClassUtils.getShorterName(className);
        if (seed.endsWith(suffix)) {
            return seed.substring(0, seed.length() - suffix.length());
        } else {
            return null;
        }
    }

    private boolean isOuter(PropertyDescriptor descriptor) {
        Method method = descriptor.getReadMethod();
        if (method == null) {
            method = descriptor.getWriteMethod();
        }
        return isOuter(method.getDeclaringClass().getName());
    }

    private PropertyDescriptor findPropertyDescriptor(ClassDesc classDesc,
            String propertyName) {
        String className = classDesc.getName();
        PropertyDescriptor descriptor = getPropertyDescriptor(className,
                propertyName);
        if (descriptor == null) {
            descriptor = getPropertyDescriptor(classDesc.getSuperclassName(),
                    propertyName);
        }
        return descriptor;
    }

    private String findQualifier(PropertyDesc propertyDesc) {
        // TODO ClassDesc が親を複数持つことがありうるが、再帰的にqualifierを辿るとその場合に正しくないqualifierを返す場合がある。
        // この問題に対処できるまでは、直上の親しか見ないようにしている。
        Desc<?> parent = propertyDesc.getParent();
        if (parent instanceof ClassDesc) {
            return ((ClassDesc) parent).getQualifier();
        } else {
            return null;
        }
        //        for (Desc<?> desc = propertyDesc; desc != null; desc = desc.getParent()) {
        //            if (!(desc instanceof ClassDesc)) {
        //                continue;
        //            }
        //            String qualifier = ((ClassDesc) desc).getQualifier();
        //            if (qualifier != null) {
        //                return qualifier;
        //            }
        //        }
        //        return null;
    }

    public String findPropertyClassName(String propertyName,
            String baseClassName) {
        String rootPackageName = getFirstRootPackageName();
        if (!baseClassName.startsWith(rootPackageName + ".")) {
            // 実際にこういうケースがあったら、このメソッドを以前の形（AnalyzerContextにあった時の形）に戻すこと。
            throw new IllegalArgumentException();
        }
        String subPackageName = baseClassName.substring(rootPackageName
                .length(), baseClassName.lastIndexOf('.'));
        int dot = subPackageName.indexOf('.', 1);
        if (dot >= 0) {
            subPackageName = subPackageName.substring(dot);
        } else {
            subPackageName = "";
        }
        return findClassName(getDtoPackageName(), subPackageName,
                getDtoShortClassName(propertyName));
    }

    public String inferPropertyClassName(String propertyName,
            String baseClassName) {
        String className = findPropertyClassName(propertyName, baseClassName);
        if (getClass(className) != null) {
            return className;
        }

        // DTO型名に対応するクラスが存在しない場合はDTOサーチパス上のクラスから推論する。

        String classNameFromSearchPath = getSourceCreatorSetting()
                .findDtoClassName(propertyName);
        if (classNameFromSearchPath != null) {
            className = classNameFromSearchPath;
        }

        return className;
    }

    private String findClassName(String packageName, String subPackageName,
            String shortClassName) {
        String fullClassName = packageName + subPackageName + "."
                + shortClassName;
        if (getClass(fullClassName) != null) {
            return fullClassName;
        }

        String className;
        int pre = subPackageName.length();
        int idx;
        while ((idx = subPackageName.lastIndexOf('.', pre)) >= 0) {
            className = packageName + subPackageName.substring(0, idx) + "."
                    + shortClassName;
            if (getClass(className) != null) {
                return className;
            }
            pre = idx - 1;
        }

        return fullClassName;
    }

    public void setToCollection(TypeDesc typeDesc, String collectionClassName) {
        if (collectionClassName == null) {
            removePropertyIfNotExistActually(typeDesc.getComponentClassDesc(),
                    PROP_LENGTH);
        } else {
            removePropertyIfNotExistActually(typeDesc.getComponentClassDesc(),
                    PROP_SIZE);
        }
        typeDesc.setCollectionClassName(collectionClassName);
        typeDesc.setCollection(true);
    }

    public boolean isOuter(ClassDesc classDesc) {
        return isOuter(classDesc.getName());
    }

    public boolean isOuter(String typeName) {
        return !isGeneratedClass(GenericsUtils.getNonGenericClassName(typeName));
    }

    String getDtoShortClassName(String propertyName) {
        if (propertyName == null) {
            return null;
        }
        return DescUtils.capFirst(propertyName) + ClassType.DTO.getSuffix();
    }

    private void removePropertyIfNotExistActually(ClassDesc classDesc,
            String propertyName) {
        if (getPropertyDescriptor(classDesc.getName(), propertyName) == null) {
            classDesc.removePropertyDesc(propertyName);
        }
    }

    public String getGeneratedClassName(String className,
            String generatedClassName) {
        if (isGeneratedClass(className)) {
            return className;
        } else {
            return generatedClassName;
        }
    }
}
