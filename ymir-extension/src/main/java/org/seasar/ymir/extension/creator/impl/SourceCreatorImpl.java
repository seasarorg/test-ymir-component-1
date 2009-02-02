package org.seasar.ymir.extension.creator.impl;

import static org.seasar.ymir.extension.creator.util.SourceCreatorUtils.newPageComponent;
import static org.seasar.ymir.extension.creator.util.SourceCreatorUtils.newRequest;
import static org.seasar.ymir.impl.YmirImpl.PARAM_METHOD;

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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

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
import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.util.ClassTraversal;
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
import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.impl.ConstraintInterceptor;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.ClassDescModifier;
import org.seasar.ymir.extension.creator.ClassDescSet;
import org.seasar.ymir.extension.creator.ClassHint;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.DescValidator;
import org.seasar.ymir.extension.creator.EntityMetaData;
import org.seasar.ymir.extension.creator.InvalidClassDescException;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.SourceGenerator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.TemplateAnalyzer;
import org.seasar.ymir.extension.creator.TemplateProvider;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.DescValidator.Result;
import org.seasar.ymir.extension.creator.action.ActionSelector;
import org.seasar.ymir.extension.creator.action.Condition;
import org.seasar.ymir.extension.creator.action.State;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.extension.creator.action.UpdateByExceptionAction;
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
import org.seasar.ymir.extension.creator.util.MetaUtils;
import org.seasar.ymir.extension.creator.util.PersistentProperties;
import org.seasar.ymir.extension.creator.util.SourceCreatorUtils;
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.message.MessageNotFoundRuntimeException;
import org.seasar.ymir.message.MessagesNotFoundRuntimeException;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.scope.annotation.Inject;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.HTMLUtils;
import org.seasar.ymir.util.ServletUtils;

import net.skirnir.freyja.EvaluationRuntimeException;

public class SourceCreatorImpl implements SourceCreator {
    private static final String SOURCECREATOR_PREFS = "org.seasar.ymir.extension.sourceCreator.prefs";

    private static final String MAPPING_PREFS = "org.seasar.ymir.extension.mapping.prefs";

    public static final ServletContext MOCK_SERVLETCONTEXT = new MockServletContextImpl(
            "/") {
        private static final long serialVersionUID = 0;

        public String getServletContextName() {
            return "";
        }
    };

    public static final HttpServletRequest MOCK_REQUEST = new MockHttpServletRequestImpl(
            MOCK_SERVLETCONTEXT, "/");

    public static final HttpServletResponse MOCK_RESPONSE = new MockHttpServletResponseImpl(
            MOCK_REQUEST);

    private static final String PROPERTY_ID = "id";

    private static final String ID_ANNOTATIONNAME = "org.seasar.dao.annotation.tiger.Id";

    private static final String ID_BODY = "(org.seasar.dao.annotation.tiger.IdType.IDENTITY)";

    private static final String ID_TYPE = "int";

    private static final String PACKAGEPREFIX_JAVA_LANG = "java.lang.";

    private static final String PACKAGEPREFIX_JAVA_UTIL = "java.util.";

    private static final String PACKAGEPREFIX_FREYJA_RENDER_HTML = "net.skirnir.freyja.render.html.";

    private static final String RESOURCE_PREAMBLE_JAVA = "org/seasar/ymir/extension/Preamble.java.txt";

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
                    "updateClasses", new UpdateClassesAction(this)).register(
                    "createConfiguration", new CreateConfigurationAction(this))
            .register("systemConsole", new SystemConsoleAction(this)).register(
                    "resource", new ResourceAction(this)).register(
                    "editTemplate.do", new DoEditTemplateAction(this))
            .register("updateTemplate.do", new DoUpdateTemplateAction(this));

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

    public Response update(Request request, Response response) {
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
        String path = ServletUtils.normalizePath(pathMetaData.getPath());
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

            if (response.getType() != ResponseType.PASSTHROUGH
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
                                    .valueOf(template.exists()), method);
                }
            }
        }

        UpdateAction action = actionSelector_.getAction(condition);
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
                .getRootPackageName() != null);
    }

    public ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas) {
        return gatherClassDescs(pathMetaDatas, null, null);
    }

    public ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas,
            ClassCreationHintBag hintBag, String[] ignoreVariables) {
        Map<String, ClassDesc> classDescMap = new LinkedHashMap<String, ClassDesc>();
        for (int i = 0; i < pathMetaDatas.length; i++) {
            gatherClassDescs(classDescMap, pathMetaDatas[i], hintBag,
                    ignoreVariables);
        }
        ClassDesc[] classDescs = addRelativeClassDescs(classDescMap.values()
                .toArray(new ClassDesc[0]), hintBag);

        return classifyClassDescs(classDescs);
    }

    public void updateClasses(ClassDescBag classDescBag,
            ClassCreationHintBag hintBag) {
        if (setting_.isConverterCreationFeatureEnabled()) {
            // Converter用のClassDescを生成する。
            for (ClassDesc dtoCd : classDescBag.getClassDescs(ClassType.DTO)) {
                String[] pairTypeNames = getPairTypeNames(dtoCd);
                if (pairTypeNames != null) {
                    ClassDesc converterCd = createConverterClassDesc(dtoCd,
                            pairTypeNames, hintBag);
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
            PropertyDesc[] pds = pageClassDescs[i].getPropertyDescs();
            for (int j = 0; j < pds.length; j++) {
                TypeDesc td = pds[j].getTypeDesc();
                if (!DescValidator.validate(td, classDescSet).isValid()
                        || td.getComponentClassDesc().getType() != ClassType.DTO) {
                    continue;
                }

                EntityMetaData metaData = new EntityMetaData(this, null, td
                        .getComponentClassDesc().getName());
                addPropertyToPageIfValid(pageClassDescs[i], new TypeDescImpl(
                        metaData.getConverterClassDesc()), PropertyDesc.WRITE,
                        classDescSet);
            }

            // Pageの親クラスが存在しない場合は生成しておく。
            String superclassName = pageClassDescs[i].getSuperclassName();
            if (superclassName != null) {
                Class<?> superclass = getClass(superclassName);
                if (superclass == null) {
                    ClassDesc classDesc = newClassDesc(superclassName, null);
                    if (!isOuter(classDesc)) {
                        writeSourceFile("PageSuperclass.java", classDesc, false);
                        classDescBag.addAsCreated(classDesc, true);
                    }
                }
            }
        }

        writeSourceFiles(classDescBag);
    }

    boolean isOuter(ClassDesc classDesc) {
        return !classDesc.getPackageName().startsWith(
                getRootPackageName() + ".");
    }

    protected ClassDesc createConverterClassDesc(ClassDesc dtoCd,
            String[] pairTypeNames, ClassCreationHintBag hintBag) {
        ClassDesc clonedDtoCd = (ClassDesc) dtoCd.clone();
        mergeWithExistentClass(clonedDtoCd);
        EntityMetaData metaData = new EntityMetaData(this, hintBag, clonedDtoCd
                .getName());

        ClassDesc converterCd = metaData.getConverterClassDesc();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("targetClassDesc", clonedDtoCd);
        List<TypeDesc> pairTdList = new ArrayList<TypeDesc>();
        for (int i = 0; i < pairTypeNames.length; i++) {
            if ("Object".equals(DescUtils.getShortName(DescUtils
                    .getNonGenericClassName(pairTypeNames[i])))) {
                continue;
            }
            TypeDescImpl td = new TypeDescImpl(pairTypeNames[i]);
            String className = td.getComponentClassDesc().getName();
            Map<String, ClassDesc> map = new HashMap<String, ClassDesc>();
            map.put(className, getClassDesc(getClass(className), false));
            td.setName(pairTypeNames[i], map);

            pairTdList.add(td);
        }
        param.put("pairTypeDescs", pairTdList.toArray(new TypeDesc[0]));
        converterCd.setOptionalSourceGeneratorParameter(param);
        return converterCd;
    }

    public void gatherClassDescs(Map<String, ClassDesc> classDescMap,
            PathMetaData pathMetaData, ClassCreationHintBag hintBag,
            String[] ignoreVariables) {
        String path = pathMetaData.getPath();
        HttpMethod method = pathMetaData.getMethod();
        String pageClassName = pathMetaData.getClassName();
        analyzer_.analyze(getServletContext(), getHttpServletRequest(),
                getHttpServletResponse(), path, method, classDescMap,
                pathMetaData.getTemplate(), pageClassName, hintBag,
                ignoreVariables);

        for (int i = 0; i < classDescModifiers_.length; i++) {
            classDescModifiers_[i].modify(classDescMap, pathMetaData);
        }

        ClassDesc pageClassDesc = classDescMap.get(pageClassName);
        if (pageClassDesc == null && method == HttpMethod.POST) {
            // テンプレートを解析した結果対応するPageクラスを作る必要があると
            // 見なされなかった場合でも、methodがPOSTならPageクラスを作る。
            pageClassDesc = newClassDesc(pageClassName, hintBag);
            classDescMap.put(pageClassName, pageClassDesc);
        }
        if (pageClassDesc != null) {
            // アクションメソッドを追加する。
            MethodDesc actionMethodDesc = newActionMethodDescUnlessExists(
                    pageClassName, path, method);
            if (actionMethodDesc != null) {
                pageClassDesc.setMethodDesc(actionMethodDesc);
            }
            // _prerender()を追加する。
            MethodDesc prerenderMethodDesc = getExtraPathMapping(path, method)
                    .newPrerenderActionMethodDesc(new ActionSelectorSeedImpl());
            pageClassDesc.setMethodDesc(prerenderMethodDesc);
            // _validationFailed(Notes)を追加する。
            MethodDescImpl methodDesc = new MethodDescImpl(
                    ConstraintInterceptor.ACTION_VALIDATIONFAILED);
            methodDesc
                    .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                            Notes.class, "notes") });
            pageClassDesc.setMethodDesc(methodDesc);
            // _permissionDenied(PemissionDeniedException)を追加する。
            methodDesc = new MethodDescImpl(
                    ConstraintInterceptor.ACTION_PERMISSIONDENIED);
            methodDesc
                    .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                            PermissionDeniedException.class, "ex") });
            methodDesc.setThrowsDesc(new ThrowsDescImpl(
                    PermissionDeniedException.class));
            methodDesc.setBodyDesc(new BodyDescImpl(
                    BodyDesc.KEY_PERMISSIONDENIED,
                    new HashMap<String, Object>()));
            methodDesc.setAnnotationDesc(new AnnotationDescImpl(
                    getBeginAnnotation()));
            pageClassDesc.setMethodDesc(methodDesc);
        }
    }

    MethodDesc newActionMethodDescUnlessExists(String pageClassName,
            String path, HttpMethod method) {
        Class<?> pageClass = getClass(pageClassName);
        if (pageClass != null) {
            if (ymir_.findMatchedPathMapping(path, method)
                    .getAction(newPageComponent(pageClass),
                            newRequest(path, method, null)) != null) {
                return null;
            }
        }
        return newActionMethodDesc(path, method, new ActionSelectorSeedImpl());
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

    ClassDesc getClassDesc(String className, boolean onlyDeclared) {
        if (className == null) {
            return null;
        }
        Class<?> clazz = getClass(className);
        if (clazz != null || onlyDeclared) {
            return getClassDesc(clazz, onlyDeclared);
        }

        // クラスが存在しないので、スーパークラスを辿る。
        ClassDesc classDesc = newClassDesc(className, null);
        String superclassName = classDesc.getSuperclassName();
        classDesc = getClassDesc(superclassName, false);
        if (classDesc == null) {
            // スーパークラスが指定されていないのでclassNameなクラスに関する推論が
            // 何も行なえなかったことになる。これではClassDescを作る意味がないのでnullを返す。
            return null;
        }
        classDesc.setName(className);
        classDesc.setSuperclassName(superclassName);

        return classDesc;
    }

    public ClassDesc getClassDesc(Class<?> clazz) {
        return getClassDesc(clazz, true);
    }

    public ClassDesc getClassDesc(Class<?> clazz, boolean onlyDeclared) {
        if (clazz == null) {
            return null;
        }

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }

        ClassDesc classDesc = newClassDesc(clazz.getName(), null);

        String superclassName = null;
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            superclassName = superclass.getName();
        }
        classDesc.setSuperclassName(superclassName);

        AnnotationDesc[] ads = DescUtils.newAnnotationDescs(clazz);
        for (int i = 0; i < ads.length; i++) {
            classDesc.setAnnotationDesc(ads[i]);
        }

        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < pds.length; i++) {
            Method readMethod = pds[i].getReadMethod();
            Method writeMethod = pds[i].getWriteMethod();
            String name = adjustPropertyName(pds[i].getName(), clazz,
                    readMethod, writeMethod);
            if (onlyDeclared) {
                // このクラスで定義されているプロパティだけを対象とする。
                if (readMethod != null
                        && readMethod.getDeclaringClass() != clazz) {
                    readMethod = null;
                }
                if (writeMethod != null
                        && writeMethod.getDeclaringClass() != clazz) {
                    writeMethod = null;
                }
            } else {
                // 祖先クラスにあるプロパティも対象とする。ただしObjectクラスのプロパティは対象外とする。
                if (readMethod != null
                        && readMethod.getDeclaringClass() == Object.class) {
                    readMethod = null;
                }
                if (writeMethod != null
                        && writeMethod.getDeclaringClass() == Object.class) {
                    writeMethod = null;
                }
            }

            PropertyDesc propertyDesc = new PropertyDescImpl(name);
            int mode = PropertyDesc.NONE;
            if (readMethod != null) {
                mode |= PropertyDesc.READ;
                propertyDesc.setGetterName(readMethod.getName());
            }
            if (writeMethod != null) {
                mode |= PropertyDesc.WRITE;
            }
            if (mode == PropertyDesc.NONE) {
                continue;
            }
            propertyDesc.setMode(mode);

            propertyDesc.setTypeDesc(DescUtils
                    .getGenericPropertyTypeName(pds[i]));

            ads = DescUtils.newAnnotationDescs(readMethod);
            for (int j = 0; j < ads.length; j++) {
                propertyDesc.setAnnotationDescForGetter(ads[j]);
            }
            ads = DescUtils.newAnnotationDescs(writeMethod);
            for (int j = 0; j < ads.length; j++) {
                propertyDesc.setAnnotationDescForSetter(ads[j]);
            }

            classDesc.setPropertyDesc(propertyDesc);
        }

        Method[] methods;
        if (onlyDeclared) {
            // このクラスで定義されているメソッドだけを対象とする。
            methods = clazz.getDeclaredMethods();
        } else {
            // 祖先クラスにあるメソッドも対象とする。ただしObjectクラスのメソッドは対象外とする。
            List<Method> methodList = new ArrayList<Method>();
            for (Class<?> c = clazz; c != null && c != Object.class; c = c
                    .getSuperclass()) {
                methodList.addAll(Arrays.asList(c.getDeclaredMethods()));
            }
            for (Class<?> c : clazz.getInterfaces()) {
                methodList.addAll(Arrays.asList(c.getDeclaredMethods()));
            }
            methods = methodList.toArray(new Method[0]);
        }
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            if (name.startsWith("get") || name.startsWith("is")
                    || name.startsWith("set")) {
                continue;
            }
            MethodDesc md = new MethodDescImpl(methods[i]);
            classDesc.setMethodDesc(md);
            String[] source = md.getMetaValue(Globals.META_NAME_SOURCE);
            if (source != null) {
                int idx = 0;
                md.setBodyDesc(new BodyDescImpl(source[idx++]));
                ParameterDesc[] parameterDescs = md.getParameterDescs();
                for (int j = 0; idx < source.length
                        && j < parameterDescs.length; idx++, j++) {
                    parameterDescs[j].setName(source[idx]);
                }
            }
        }

        // 特別な処理を行なう。

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Meta meta = fields[i].getAnnotation(Meta.class);
            if (meta != null && Globals.META_NAME_PROPERTY.equals(meta.name())) {
                String name = meta.value()[0];
                PropertyDesc pd = classDesc.getPropertyDesc(name);
                if (pd == null) {
                    classDesc.addProperty(name, PropertyDesc.NONE);
                    pd = classDesc.getPropertyDesc(name);
                }
                pd.setAnnotationDesc(DescUtils.newAnnotationDesc(meta));
                pd.setTypeDesc(DescUtils.toString(fields[i].getGenericType()));
            }
        }

        return classDesc;
    }

    String adjustPropertyName(String name, Class<?> clazz, Method readMethod,
            Method writeMethod) {
        if (name == null || name.length() < 2
                || Character.isLowerCase(name.charAt(1))) {
            return name;
        }

        String formName = MetaUtils.getFirstValue(readMethod, "formProperty");
        if (formName == null) {
            formName = MetaUtils.getFirstValue(writeMethod, "formProperty");
        }
        if (formName != null) {
            String formFieldName = toFieldName(formName);
            Field formField = findField(formFieldName, clazz);
            if (formField != null) {
                return adjustPropertyName(name, formField.getType(), null, null);
            }
        }

        String fieldName = toFieldName(name);
        String name2 = Character.toLowerCase(name.charAt(0))
                + name.substring(1);
        String fieldName2 = toFieldName(name2);
        if (findField(fieldName, clazz) != null) {
            return name;
        } else if (findField(fieldName2, clazz) != null) {
            return name2;
        }

        return name;
    }

    String toFieldName(String propertyName) {
        if (propertyName == null) {
            return null;
        }
        return setting_.getFieldPrefix() + propertyName
                + setting_.getFieldSuffix();
    }

    Field findField(String fieldName, Class<?> clazz) {
        if (fieldName == null) {
            return null;
        }
        do {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignore) {
            }
        } while ((clazz = clazz.getSuperclass()) != null
                && clazz != Object.class);
        return null;
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
        for (int i = 0; i < classDescs.length; i++) {
            if (!classDescs[i].isTypeOf(ClassType.PAGE)) {
                continue;
            }

            PropertyDesc[] pds = classDescs[i].getPropertyDescs();
            for (int j = 0; j < pds.length; j++) {
                ClassDesc cd = pds[j].getTypeDesc().getComponentClassDesc();
                if (!cd.isTypeOf(ClassType.DTO)) {
                    continue;
                }
                List<ClassDesc> list = pageByDtoMap.get(cd.getName());
                if (list == null) {
                    list = new ArrayList<ClassDesc>();
                    pageByDtoMap.put(cd.getName(), list);
                }
                list.add(classDescs[i]);
            }
        }

        List<ClassDesc> classDescList = new ArrayList<ClassDesc>(Arrays
                .asList(classDescs));
        for (int i = 0; i < classDescs.length; i++) {
            if (classDescs[i].isTypeOf(ClassType.DTO)) {
                EntityMetaData metaData = new EntityMetaData(this, hintBag,
                        classDescs[i].getName());

                if (setting_.isDaoCreationFeatureEnabled()) {
                    // Dao用のClassDescを生成しておく。
                    classDescList.add(metaData.getDaoClassDesc());

                    // Bean用のClassDescを生成しておく。
                    ClassDesc classDesc = metaData.getBeanClassDesc();
                    PropertyDesc[] pds = classDescs[i].getPropertyDescs();
                    for (int j = 0; j < pds.length; j++) {
                        classDesc
                                .setPropertyDesc((PropertyDesc) pds[j].clone());
                    }
                    // プライマリキーがないとS2Daoがエラーになるので生成しておく。
                    PropertyDesc idPd = classDesc.getPropertyDesc(PROPERTY_ID);
                    if (idPd == null) {
                        idPd = classDesc.addProperty(PROPERTY_ID,
                                PropertyDesc.READ | PropertyDesc.WRITE);
                        idPd.setTypeDesc(ID_TYPE);
                    }
                    idPd.setAnnotationDescForGetter(new AnnotationDescImpl(
                            ID_ANNOTATIONNAME, ID_BODY));
                    classDescList.add(classDesc);
                }

                if (setting_.isDxoCreationFeatureEnabled()) {
                    // Dxo用のClassDescを生成しておく。
                    ClassDesc classDesc = metaData.getDxoClassDesc();
                    List<ClassDesc> list = pageByDtoMap.get(classDescs[i]
                            .getName());
                    if (list != null) {
                        for (Iterator<ClassDesc> itr = list.iterator(); itr
                                .hasNext();) {
                            MethodDescImpl md = new MethodDescImpl("convert");
                            ParameterDesc[] pmds = new ParameterDesc[] { new ParameterDescImpl(
                                    new TypeDescImpl(itr.next())) };
                            md.setParameterDescs(pmds);
                            md.setReturnTypeDesc(metaData.getBeanClassDesc()
                                    .getName());
                            classDesc.setMethodDesc(md);
                        }
                    }
                    classDescList.add(classDesc);
                }
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

    boolean addPropertyToPageIfValid(ClassDesc pageClassDesc,
            TypeDesc typeDesc, int mode, ClassDescSet classDescSet) {
        if (DescValidator.validate(typeDesc, classDescSet).isValid()) {
            PropertyDesc propertyDesc = pageClassDesc.addProperty(typeDesc
                    .getInstanceName(), mode);
            propertyDesc.setTypeDesc(typeDesc);
            propertyDesc.notifyUpdatingType();
            AnnotationDesc ad = propertyDesc.getAnnotationDesc(Inject.class
                    .getName());
            if (ad == null) {
                propertyDesc.setAnnotationDescForSetter(new AnnotationDescImpl(
                        Inject.class.getName()));
            }
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
        writeSourceFile(classDesc, classDescSet);
    }

    public void adjustByExistentClass(ClassDesc desc) {
        String className = desc.getName();
        ClassType type = desc.getType();
        Class<?> clazz = getClass(className);
        ClassDesc gapDesc = getClassDesc(clazz);
        if (gapDesc == null) {
            gapDesc = newClassDesc(className, null);
        }

        String baseClassName = className + "Base";
        Class<?> baseClass = (clazz != null
                && clazz.getSuperclass().getName().equals(baseClassName) ? clazz
                .getSuperclass()
                : null);
        // 従属タイプのクラスの場合は既存のBaseクラスの情報とマージしなくて良い。
        ClassDesc baseDesc = desc.getType().isSubordinate() ? null
                : getClassDesc(baseClass);
        if (baseDesc == null) {
            baseDesc = newClassDesc(baseClassName, null);
            baseDesc.setSuperclassName(desc.getSuperclassName());
        }
        if (baseClass != null) {
            if (desc.getSuperclassName() != null) {
                // 親クラス情報は適切にdescに設定されているのでbaseクラスの情報は見ない。
                // そのため、先でのマージのことを考えて、baseクラスの方にdescの情報をセットしておく。
                baseDesc.setSuperclassName(desc.getSuperclassName());
            }

            // abstractかどうかを保持するようにする。
            desc.setBaseClassAbstract(Modifier.isAbstract(baseClass
                    .getModifiers()));
        }

        // baseにあるものは必ず残す。baseになくてsuperやgapにあるものは除去する。
        ClassDesc generated = (ClassDesc) desc.clone();
        desc.clear();
        desc.setOptionalSourceGeneratorParameter(generated
                .getOptionalSourceGeneratorParameter());
        desc.merge(baseDesc, true);

        ClassDesc superDesc = getClassDesc(desc.getSuperclassName(), false);
        if (superDesc == null) {
            superDesc = newClassDesc(Object.class.getName(), null);
            superDesc.setType(type);
        }

        PropertyDesc[] pds = generated.getPropertyDescs();
        for (int i = 0; i < pds.length; i++) {
            PropertyDesc basePd = baseDesc.getPropertyDesc(pds[i].getName());
            if (basePd == null || !basePd.isReadable()) {
                removeModeFrom(pds[i], PropertyDesc.READ, gapDesc);
                removeModeFrom(pds[i], PropertyDesc.READ, superDesc);
            }
            if (basePd == null || !basePd.isWritable()) {
                removeModeFrom(pds[i], PropertyDesc.WRITE, gapDesc);
                removeModeFrom(pds[i], PropertyDesc.WRITE, superDesc);
            }
            if (!pds[i].isReadable() && !pds[i].isWritable()) {
                // GetterもSetterもないものは削除する。
                // ただし@Meta(name="property")なプロパティはformのDTOのフィールドを生成するために残す。
                // superclassがformのDTOのフィールドを持っている時は削除する。
                if (!pds[i].hasMeta(Globals.META_NAME_PROPERTY)
                        || isFormDtoFieldPresent(superDesc, pds[i].getName())) {
                    generated.removePropertyDesc(pds[i].getName());
                }
            }
            // 元々ついているMetaでないアノテーションはBaseを優先させる必要があるため、
            // GeneratedにあるアノテーションのうちBaseにもあるものを除去しておく。
            PropertyDesc generatedPd = generated.getPropertyDesc(pds[i]
                    .getName());
            if (basePd != null && generatedPd != null) {
                List<AnnotationDesc> list = new ArrayList<AnnotationDesc>();
                for (AnnotationDesc ad : generatedPd.getAnnotationDescs()) {
                    if (DescUtils.isMetaAnnotation(ad)
                            || basePd.getAnnotationDesc(ad.getName()) == null) {
                        list.add(ad);
                    }
                }
                generatedPd.setAnnotationDescs(list
                        .toArray(new AnnotationDesc[0]));

                list = new ArrayList<AnnotationDesc>();
                for (AnnotationDesc ad : generatedPd
                        .getAnnotationDescsForGetter()) {
                    if (DescUtils.isMetaAnnotation(ad)
                            || basePd.getAnnotationDescForGetter(ad.getName()) == null) {
                        list.add(ad);
                    }
                }
                generatedPd.setAnnotationDescsForGetter(list
                        .toArray(new AnnotationDesc[0]));

                list = new ArrayList<AnnotationDesc>();
                for (AnnotationDesc ad : generatedPd
                        .getAnnotationDescsForSetter()) {
                    if (DescUtils.isMetaAnnotation(ad)
                            || basePd.getAnnotationDescForSetter(ad.getName()) == null) {
                        list.add(ad);
                    }
                }
                generatedPd.setAnnotationDescsForSetter(list
                        .toArray(new AnnotationDesc[0]));
            }
        }
        MethodDesc[] mds = generated.getMethodDescs();
        for (int i = 0; i < mds.length; i++) {
            MethodDesc generatedMd = mds[i];
            MethodDesc gapMd = gapDesc.getMethodDesc(generatedMd);
            MethodDesc baseMd = baseDesc.getMethodDesc(generatedMd);
            MethodDesc superMd = superDesc.getMethodDesc(generatedMd);
            if (baseMd == null && (gapMd != null || superMd != null)) {
                generated.removeMethodDesc(generatedMd);
            } else if (gapMd != null
                    && !generatedMd.getReturnTypeDesc().equals(
                            gapMd.getReturnTypeDesc())) {
                generatedMd.setReturnTypeDesc((TypeDesc) gapMd
                        .getReturnTypeDesc().clone());
            } else if (superMd != null
                    && !generatedMd.getReturnTypeDesc().equals(
                            superMd.getReturnTypeDesc())) {
                generatedMd.setReturnTypeDesc((TypeDesc) superMd
                        .getReturnTypeDesc().clone());
            }
            // 元々ついているMetaでないアノテーションはBaseを優先させる必要があるため、
            // GeneratedにあるアノテーションのうちBaseにもあるものを除去しておく。
            if (baseMd != null && generated.getMethodDesc(generatedMd) != null) {
                List<AnnotationDesc> list = new ArrayList<AnnotationDesc>();
                for (AnnotationDesc ad : generatedMd.getAnnotationDescs()) {
                    if (DescUtils.isMetaAnnotation(ad)
                            || baseMd.getAnnotationDesc(ad.getName()) == null) {
                        list.add(ad);
                    }
                }
                generatedMd.setAnnotationDescs(list
                        .toArray(new AnnotationDesc[0]));
            }
        }

        desc.merge(generated, true);
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
        classDesc.merge(getClassDesc(getClass(className), false), false);
    }

    public void writeSourceFile(ClassDesc classDesc, ClassDescSet classDescSet)
            throws InvalidClassDescException {
        Result result = DescValidator.validate(classDesc, classDescSet);
        if (!result.isValid()) {
            throw new InvalidClassDescException(result.getClassNames());
        }

        writeString(sourceGenerator_.generateBaseSource(classDesc),
                getSourceFile(classDesc.getName() + "Base"));

        // gap側のクラスは存在しない場合のみ生成する。
        File sourceFile = getSourceFile(classDesc.getName());
        if (!sourceFile.exists()) {
            writeString(sourceGenerator_.generateGapSource(classDesc),
                    sourceFile);
        }
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
        ClassLoader cl = getClassLoader();
        try {
            return Class.forName(className, true, cl);
        } catch (ClassNotFoundException ex) {
            if (className.indexOf('.') < 0) {
                try {
                    return Class.forName(PACKAGEPREFIX_JAVA_LANG + className,
                            true, cl);
                } catch (ClassNotFoundException ex2) {
                    try {
                        return Class.forName(PACKAGEPREFIX_JAVA_UTIL
                                + className, true, cl);
                    } catch (ClassNotFoundException ex3) {
                        try {
                            return Class.forName(
                                    PACKAGEPREFIX_FREYJA_RENDER_HTML
                                            + className, true, cl);
                        } catch (ClassNotFoundException ex4) {
                            ClassTraverser traverser = new ClassTraverser();
                            try {
                                traverser.addReferenceClass(Class.forName(
                                        "org.seasar.ymir.landmark.Landmark",
                                        true, getClassLoader()));
                            } catch (ClassNotFoundException ex5) {
                                return null;
                            }
                            traverser.addClassPattern(getRootPackageName(),
                                    className);

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
                                    return Class.forName(found[0], true, cl);
                                } catch (ClassNotFoundException ignore) {
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
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
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < pds.length; i++) {
            if (adjustPropertyName(pds[i].getName(), clazz,
                    pds[i].getReadMethod(), pds[i].getWriteMethod()).equals(
                    propertyName)) {
                return pds[i];
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

    public String getRootPackageName() {
        return getApplication().getRootPackageName();
    }

    public String getPagePackageName() {
        return getRootPackageName() + "."
                + namingConvention_.getSubApplicationRootPackageName();
    }

    public String getDtoPackageName() {
        return getRootPackageName() + "."
                + namingConvention_.getDtoPackageName();
    }

    public String getDaoPackageName() {
        return getRootPackageName() + "."
                + namingConvention_.getDaoPackageName();
    }

    public String getDxoPackageName() {
        return getRootPackageName() + "."
                + namingConvention_.getDxoPackageName();
    }

    public String getConverterPackageName() {
        return getRootPackageName() + "."
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

    public ClassDesc newClassDesc(String className, ClassCreationHintBag hintBag) {
        // booleanとかのクラスについてはSimpleClassDescを返した方が都合が良いのでこうしている。
        if (className.indexOf('.') < 0) {
            return new SimpleClassDesc(className);
        }

        ClassDescImpl classDescImpl = new ClassDescImpl(className);
        ClassType type = ClassType.typeOfClass(className);
        classDescImpl.setType(type);

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
                if (superclass != null) {
                    if (superclass.getName().endsWith("Base")) {
                        superclass = superclass.getSuperclass();
                    }
                    superclassName = superclass.getName();
                }
            } else {
                superclassName = setting_.getSuperclassName(className);
                if (superclassName == null && type == ClassType.PAGE) {
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
            classDescImpl.setSuperclassName(superclassName);
        }

        return classDescImpl;
    }

    public TemplateProvider getTemplateProvider() {
        return templateProvider_;
    }

    public String filterResponse(String response) {
        if (!shouldUpdate()) {
            return response;
        }

        if (setting_.isInPlaceEditorEnabled()) {
            String jsPrefix = "<script type=\"text/javascript\" src=\""
                    + getHttpServletRequest().getContextPath() + PATH_PREFIX
                    + "resource/js/";
            String jsSuffix = "\"></script>";
            response = response.replace("</head>", jsPrefix
                    + "prototype/prototype.js" + jsSuffix + jsPrefix
                    + "scriptaculous/scriptaculous.js" + jsSuffix + jsPrefix
                    + "sourceCreator.js" + jsSuffix + "</head>");
        }
        if (setting_.isControlPanelEnabled()) {
            response = response.replace("</body>",
                    "<div id=\"__ymir__controlPanel\">"
                            + createControlPanelFormHTML(getRequest())
                            + "</div></body>");
        }

        return response;
    }

    String createControlPanelFormHTML(Request request) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("<form action=\"")
                .append(request.getAbsolutePath())
                .append("\" method=\"post\">")
                .append("<input type=\"hidden\" name=\"")
                .append(PARAM_TASK)
                .append(
                        "\" value=\"systemConsole\" /><input type=\"hidden\" name=\"")
                .append(PARAM_METHOD).append("\" value=\"").append(
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
        sb
                .append("<input type=\"submit\" value=\"[TO SYSTEM CONSOLE]\" /></form>");
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
                return Class
                        .forName(baseClassName.substring(0, dot + 1) + name);
            } catch (ClassNotFoundException ignore) {
            }
            pre = dot - 1;
        }
        return null;
    }

    public MethodDesc newActionMethodDesc(String path, HttpMethod method,
            ActionSelectorSeed seed) {
        MethodDesc methodDesc = getExtraPathMapping(path, method)
                .newActionMethodDesc(seed);
        String returnType = setting_.getActionReturnType(method);
        methodDesc.setReturnTypeDesc(returnType);
        if (returnType.equals(String.class.getName())) {
            methodDesc
                    .setBodyDesc(new BodyDescImpl("return \"passthrough:\";"));
        } else if (returnType.equals(Response.class.getName())) {
            methodDesc
                    .setBodyDesc(new BodyDescImpl(
                            "return new org.seasar.ymir.response.PassthroughResponse();"));
        }
        return methodDesc;
    }
}
