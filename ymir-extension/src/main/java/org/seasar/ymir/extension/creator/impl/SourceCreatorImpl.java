package org.seasar.ymir.extension.creator.impl;

import static org.seasar.ymir.extension.Globals.APPKEYPREFIX_SOURCECREATOR_ENABLE;
import static org.seasar.ymir.extension.Globals.APPKEYPREFIX_SOURCECREATOR_SUPERCLASS;
import static org.seasar.ymir.extension.Globals.APPKEY_SOURCECREATOR_ENABLE;
import static org.seasar.ymir.extension.Globals.APPKEY_SOURCECREATOR_FEATURE_CREATEDAO_ENABLE;
import static org.seasar.ymir.extension.Globals.APPKEY_SOURCECREATOR_FEATURE_CREATEDXO_ENABLE;
import static org.seasar.ymir.extension.Globals.APPKEY_SOURCECREATOR_SUPERCLASS;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.cms.pluggable.ClassTraverser;
import org.seasar.cms.pluggable.hotdeploy.LocalHotdeployS2Container;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.log.Logger;
import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.StringUtils;
import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.ActionNotFoundException;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.MessageNotFoundRuntimeException;
import org.seasar.ymir.MessagesNotFoundRuntimeException;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseCreator;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.impl.ConstraintInterceptor;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
import org.seasar.ymir.extension.creator.ClassDescModifier;
import org.seasar.ymir.extension.creator.ClassDescSet;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.DescValidator;
import org.seasar.ymir.extension.creator.EntityMetaData;
import org.seasar.ymir.extension.creator.InvalidClassDescException;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHintBag;
import org.seasar.ymir.extension.creator.SourceCreator;
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
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.util.HTMLUtils;
import org.seasar.ymir.util.ServletUtils;

import net.skirnir.freyja.EvaluationRuntimeException;

public class SourceCreatorImpl implements SourceCreator {
    private static final String SOURCECREATOR_PROPERTIES = "sourceCreator.properties";

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

    private ActionSelector<UpdateAction> actionSelector_ = new ActionSelector<UpdateAction>()
            .register(
                    new Condition(State.ANY, State.ANY, State.FALSE,
                            Request.METHOD_GET), new CreateTemplateAction(this))
            .register(
                    new Condition(State.ANY, State.ANY, State.TRUE,
                            Request.METHOD_GET), new UpdateClassesAction(this))
            .register(
                    new Condition(State.ANY, State.FALSE, State.FALSE,
                            Request.METHOD_POST),
                    new CreateClassAndTemplateAction(this))
            .register(
                    new Condition(State.ANY, State.ANY, State.TRUE,
                            Request.METHOD_POST), new UpdateClassesAction(this))
            .register(
                    new Condition(State.TRUE, State.TRUE, State.FALSE,
                            Request.METHOD_POST),
                    new CreateTemplateAction(this)).register("createClass",
                    new CreateClassAction(this)).register("createTemplate",
                    new CreateTemplateAction(this)).register(
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
                    new CreateMessageAction(this))
            .register(ActionNotFoundException.class,
                    new CreateActionAction(this)).register("createAction",
                    new CreateActionAction(this));

    public Logger logger_ = Logger.getLogger(getClass());

    public Response update(Request request, Response response) {
        Application application = getApplication();
        if (!shouldUpdate(application)) {
            return response;
        }

        LazyPathMetaData pathMetaData = createLazyPathMetaData(request,
                response);
        String path = ServletUtils.normalizePath(pathMetaData.getPath());
        String forwardPath = pathMetaData.getForwardPath();
        String method = pathMetaData.getMethod();

        if (!shouldUpdate(forwardPath)) {
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
            if (!isAlreadyConfigured(application)) {
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
        Application application = getApplication();
        if (!shouldUpdate(application)) {
            return null;
        }

        if ((t instanceof WrappingRuntimeException || t instanceof EvaluationRuntimeException)
                && t.getCause() != null) {
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

    String getOriginalMethod(Request request) {
        String originalMethod = request.getParameter(PARAM_METHOD);
        if (originalMethod != null) {
            return originalMethod;
        } else {
            return request.getMethod();
        }
    }

    public boolean shouldUpdate(Application application) {
        return !"false".equals(application
                .getProperty(APPKEY_SOURCECREATOR_ENABLE));
    }

    public boolean shouldUpdate(String path) {
        return path == null
                || !"false".equals(getApplication().getProperty(
                        APPKEYPREFIX_SOURCECREATOR_ENABLE
                                + ServletUtils.normalizePath(path)));
    }

    boolean isAlreadyConfigured(Application application) {
        if (application.getProjectRoot() == null) {
            return false;
        } else if (!new File(application.getProjectRoot()).exists()) {
            return false;
        } else if (application.getRootPackageName() == null) {
            return false;
        }
        return true;
    }

    public ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas) {
        return gatherClassDescs(pathMetaDatas, null, null);
    }

    public ClassDescBag gatherClassDescs(PathMetaData[] pathMetaDatas,
            PropertyTypeHintBag hintBag, String[] ignoreVariables) {
        Map<String, ClassDesc> classDescMap = new LinkedHashMap<String, ClassDesc>();
        for (int i = 0; i < pathMetaDatas.length; i++) {
            gatherClassDescs(classDescMap, pathMetaDatas[i], hintBag,
                    ignoreVariables);
        }
        ClassDesc[] classDescs = addRelativeClassDescs(classDescMap.values()
                .toArray(new ClassDesc[0]));

        return classifyClassDescs(classDescs);
    }

    public void updateClasses(ClassDescBag classDescBag) {
        if (isConverterCreated()) {
            // Converter用のClassDescを生成する。
            for (ClassDesc dtoCd : classDescBag.getClassDescs(ClassType.DTO)) {
                Class<?>[] pairClasses = getPairClasses(dtoCd);
                if (pairClasses != null) {
                    ClassDesc converterCd = createConverterClassDesc(dtoCd,
                            pairClasses);
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
            // Dtoに対応するBeanに対応するDaoのsetterを自動生成する。
            // Dxoのsetterも自動生成する。
            // Converterのsetterも自動生成する。
            // _render()のボディも自動生成する。
            PropertyDesc[] pds = pageClassDescs[i].getPropertyDescs();
            for (int j = 0; j < pds.length; j++) {
                TypeDesc td = pds[j].getTypeDesc();
                if (!DescValidator.validate(td, classDescSet).isValid()
                        || td.getClassDesc().getType() != ClassType.DTO) {
                    continue;
                }

                EntityMetaData metaData = new EntityMetaData(this, td
                        .getClassDesc().getName());
                boolean daoExists = addPropertyIfValid(pageClassDescs[i],
                        new TypeDescImpl(metaData.getDaoClassDesc()),
                        PropertyDesc.WRITE, classDescSet);
                boolean dxoExists = addPropertyIfValid(pageClassDescs[i],
                        new TypeDescImpl(metaData.getDxoClassDesc()),
                        PropertyDesc.WRITE, classDescSet);
                addPropertyIfValid(pageClassDescs[i], new TypeDescImpl(metaData
                        .getConverterClassDesc()), PropertyDesc.WRITE,
                        classDescSet);

                MethodDesc md = pageClassDescs[i]
                        .getMethodDesc(new MethodDescImpl(
                                RequestProcessor.METHOD_RENDER));
                if (md != null && td.isArray() && pds[j].isReadable()
                        && daoExists && dxoExists) {
                    addSelectStatement(md, pds[j], metaData);
                }
            }
        }

        writeSourceFiles(classDescBag);
    }

    protected ClassDesc createConverterClassDesc(ClassDesc dtoCd,
            Class<?>[] pairClasses) {
        ClassDesc clonedDtoCd = (ClassDesc) dtoCd.clone();
        mergeWithExistentClass(clonedDtoCd);
        EntityMetaData metaData = new EntityMetaData(this, clonedDtoCd
                .getName());

        ClassDesc converterCd = metaData.getConverterClassDesc();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("targetClassDesc", clonedDtoCd);
        ClassDesc[] pairCds = new ClassDesc[pairClasses.length];
        for (int i = 0; i < pairClasses.length; i++) {
            pairCds[i] = getClassDesc(pairClasses[i], pairClasses[i].getName(),
                    false);
        }
        param.put("pairClassDescs", pairCds);
        converterCd.setOptionalSourceGeneratorParameter(param);
        return converterCd;
    }

    public void gatherClassDescs(Map<String, ClassDesc> classDescMap,
            PathMetaData pathMetaData, PropertyTypeHintBag hintBag,
            String[] ignoreVariables) {
        String path = pathMetaData.getPath();
        String method = pathMetaData.getMethod();
        String pageClassName = pathMetaData.getClassName();
        analyzer_.analyze(getServletContext(), getHttpServletRequest(),
                getHttpServletResponse(), path, method, classDescMap,
                pathMetaData.getTemplate(), pageClassName, hintBag,
                ignoreVariables);

        for (int i = 0; i < classDescModifiers_.length; i++) {
            classDescModifiers_[i].modify(classDescMap, pathMetaData);
        }

        ClassDesc pageClassDesc = classDescMap.get(pageClassName);
        if (pageClassDesc == null
                && method.equalsIgnoreCase(Request.METHOD_POST)) {
            // テンプレートを解析した結果対応するPageクラスを作る必要があると
            // 見なされなかった場合でも、methodがPOSTならPageクラスを作る。
            pageClassDesc = newClassDesc(pageClassName);
            classDescMap.put(pageClassName, pageClassDesc);
        }
        if (pageClassDesc != null) {
            // アクションメソッドを追加する。
            pageClassDesc.setMethodDesc(new MethodDescImpl(getActionName(path,
                    method)));
            // _render()を追加する。
            pageClassDesc.setMethodDesc(new MethodDescImpl(
                    RequestProcessor.METHOD_RENDER));
            // _validationFailed(Notes)を追加する。
            MethodDescImpl methodDesc = new MethodDescImpl(
                    ConstraintInterceptor.ACTION_VALIDATIONFAILED);
            methodDesc
                    .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                            Notes.class) });
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
                    ConstraintInterceptor.ACTION_PERMISSIONDENIED,
                    new HashMap<String, Object>()));
            methodDesc.setAnnotationDesc(new AnnotationDescImpl(
                    getBeginAnnotation()));
            pageClassDesc.setMethodDesc(methodDesc);
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

    public ClassDesc getClassDesc(Class<?> clazz, String className) {
        return getClassDesc(clazz, className, true);
    }

    public ClassDesc getClassDesc(Class<?> clazz, String className,
            boolean onlyDeclared) {
        if (clazz == null) {
            return null;
        }

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }

        ClassDesc classDesc = newClassDesc(className);

        AnnotationDesc[] ads = createAnnotationDescs(clazz);
        for (int i = 0; i < ads.length; i++) {
            classDesc.setAnnotationDesc(ads[i]);
        }

        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < pds.length; i++) {
            String name = pds[i].getName();
            Method readMethod = pds[i].getReadMethod();
            Method writeMethod = pds[i].getWriteMethod();
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
            Class<?> propertyType = pds[i].getPropertyType();
            if (propertyType == null) {
                logger_.info("**** PropertyType is NULL: name=" + name);
                continue;
            }

            TypeDesc propertyTypeDesc = propertyDesc.getTypeDesc();
            String componentType;
            if (propertyType.isArray()) {
                componentType = propertyType.getComponentType().getName();
                propertyTypeDesc.setArray(true);
            } else {
                componentType = propertyType.getName();
            }
            propertyTypeDesc.setClassDesc(new SimpleClassDesc(componentType));
            propertyDesc.setMode(mode);

            ads = createAnnotationDescs(readMethod);
            for (int j = 0; j < ads.length; j++) {
                propertyDesc.setAnnotationDescForGetter(ads[j]);
            }
            ads = createAnnotationDescs(writeMethod);
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
            for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
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
            ads = createAnnotationDescs(methods[i]);
            for (int j = 0; j < ads.length; j++) {
                md.setAnnotationDesc(ads[j]);
            }
            Class<?> returnType = methods[i].getReturnType();
            md.setReturnTypeDesc(returnType.getName());
            if (clazz.getName().endsWith("Base")) {
                if (returnType == String.class
                        && methods[i].getParameterTypes().length == 0) {
                    // Baseクラスについては、引数なしで返り値がStringのメソッドについてはボディを用意する。
                    // （他のもそうしたいが今はこれだけ）
                    try {
                        String value = (String) methods[i].invoke(clazz
                                .newInstance(), new Object[0]);
                        BodyDesc bodyDesc = new BodyDescImpl("return "
                                + quote(value) + ";");
                        md.setBodyDesc(bodyDesc);
                    } catch (Throwable ignore) {
                    }
                } else if (methods[i].getName().equals(
                        ConstraintInterceptor.ACTION_PERMISSIONDENIED)) {
                    // Baseクラスについては、_permissionDeniedのボディが消えると困るので用意する。
                    ParameterDesc[] ps = md.getParameterDescs();
                    for (int j = 0; j < ps.length; j++) {
                        if (PermissionDeniedException.class.getName().equals(
                                ps[j].getTypeDesc().getName())
                                && ps[j].getNameAsIs() == null) {
                            ps[j].setName("ex");
                            break;
                        }
                    }
                    md.setBodyDesc(new BodyDescImpl(
                            ConstraintInterceptor.ACTION_PERMISSIONDENIED,
                            new HashMap<String, Object>()));
                }
            }
            classDesc.setMethodDesc(md);
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
                pd.setAnnotationDesc(createAnnotationDesc(meta));
                pd.setTypeDesc(fields[i].getType().getName());
            }
        }

        return classDesc;
    }

    String quote(String value) {
        if (value == null) {
            return "null";
        } else {
            return StringUtils.quoteString(value, '"');
        }
    }

    public AnnotationDesc[] createAnnotationDescs(AnnotatedElement element) {
        if (element == null) {
            return new AnnotationDesc[0];
        }
        Annotation[] annotations = element.getAnnotations();
        AnnotationDesc[] ads = new AnnotationDesc[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            ads[i] = createAnnotationDesc(annotations[i]);
        }
        return ads;
    }

    AnnotationDesc createAnnotationDesc(Annotation annotation) {
        if (annotation instanceof Metas) {
            return new MetasAnnotationDescImpl((Metas) annotation);
        } else if (annotation instanceof Meta) {
            return new MetaAnnotationDescImpl((Meta) annotation);
        } else {
            return new AnnotationDescImpl(annotation);
        }
    }

    ClassDesc[] addRelativeClassDescs(ClassDesc[] classDescs) {
        Map<String, List<ClassDesc>> pageByDtoMap = new HashMap<String, List<ClassDesc>>();
        for (int i = 0; i < classDescs.length; i++) {
            if (!classDescs[i].isTypeOf(ClassType.PAGE)) {
                continue;
            }
            PropertyDesc[] pds = classDescs[i].getPropertyDescs();
            for (int j = 0; j < pds.length; j++) {
                ClassDesc cd = pds[j].getTypeDesc().getClassDesc();
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
                EntityMetaData metaData = new EntityMetaData(this,
                        classDescs[i].getName());

                if (PropertyUtils.valueOf(getApplication().getProperty(
                        APPKEY_SOURCECREATOR_FEATURE_CREATEDAO_ENABLE), true)) {
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

                if (PropertyUtils.valueOf(getApplication().getProperty(
                        APPKEY_SOURCECREATOR_FEATURE_CREATEDXO_ENABLE), true)) {
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

    boolean isConverterCreated() {
        return PropertyUtils.valueOf(getApplication().getProperty(
                Globals.APPKEY_SOURCECREATOR_FEATURE_CREATECONVERTER_ENABLE),
                false);
    }

    Class<?>[] getPairClasses(ClassDesc classDesc) {
        Class<?>[] pairClasses = classDesc.getMetaClassValues("conversion");
        if (pairClasses == null) {
            pairClasses = getMetaClassValue(getClass(classDesc.getName()
                    + "Base"), "conversion");
        }
        return pairClasses;
    }

    Class<?>[] getMetaClassValue(Class<?> clazz, String name) {
        if (clazz == null) {
            return null;
        }
        Meta meta = clazz.getAnnotation(Meta.class);
        if (meta != null) {
            if (name.equals(meta.name())) {
                return meta.classValue();
            }
        }
        Metas metas = clazz.getAnnotation(Metas.class);
        if (metas != null) {
            for (Meta m : metas.value()) {
                if (name.equals(m.name())) {
                    return m.classValue();
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    void addSelectStatement(MethodDesc methodDesc, PropertyDesc propertyDesc,
            EntityMetaData metaData) {
        BodyDesc bodyDesc = methodDesc.getBodyDesc();
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityMetaData", metaData);
        if (bodyDesc == null) {
            bodyDesc = new BodyDescImpl(RequestProcessor.METHOD_RENDER, root);
        } else {
            bodyDesc.setRoot(root);
        }
        List<PropertyDesc> propertyDescList = (List<PropertyDesc>) root
                .get("propertyDescs");
        if (propertyDescList == null) {
            propertyDescList = new ArrayList<PropertyDesc>();
            root.put("propertyDescs", propertyDescList);
        }
        propertyDescList.add(propertyDesc);
        methodDesc.setBodyDesc(bodyDesc);
    }

    boolean addPropertyIfValid(ClassDesc classDesc, TypeDesc typeDesc,
            int mode, ClassDescSet classDescSet) {
        if (DescValidator.validate(typeDesc, classDescSet).isValid()) {
            PropertyDesc propertyDesc = classDesc.addProperty(typeDesc
                    .getInstanceName(), mode);
            propertyDesc.setTypeDesc(typeDesc);
            propertyDesc.notifyUpdatingType();
            AnnotationDesc ad = propertyDesc.getAnnotationDesc(Binding.class
                    .getName());
            if (ad == null) {
                propertyDesc
                        .setAnnotationDescForSetter(new AnnotationDescImpl(
                                Binding.class.getName(),
                                "(bindingType = org.seasar.framework.container.annotation.tiger.BindingType.MUST)"));
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
        Class<?> clazz = getClass(className);
        ClassDesc gapDesc = getClassDesc(clazz, className);
        if (gapDesc == null) {
            gapDesc = newClassDesc(className);
        }

        Class<?> baseClass = (clazz != null
                && clazz.getSuperclass().getName().equals(className + "Base") ? clazz
                .getSuperclass()
                : null);
        // 従属タイプのクラスの場合は既存のBaseクラスの情報とマージしなくて良い。
        ClassDesc baseDesc = desc.getType().isSubordinate() ? null
                : getClassDesc(baseClass, className);
        if (baseDesc == null) {
            baseDesc = newClassDesc(className);
            baseDesc.setSuperclass(desc.getSuperclass());
        }
        if (baseClass != null) {
            // baseクラスが既に存在するならば再生成ということなので、親クラス情報を維持するようにする。
            desc.setSuperclass(null);
            if (baseClass.getSuperclass() != Object.class) {
                baseDesc.setSuperclass(baseClass.getSuperclass());
            } else {
                baseDesc.setSuperclass(null);
            }

            // abstractかどうかを保持するようにする。
            desc.setBaseClassAbstract(Modifier.isAbstract(baseClass
                    .getModifiers()));
        }

        // baseにあるものは必ず残す。baseになくてsuperやgapにあるものは除去する。
        ClassDesc generated = (ClassDesc) desc.clone();
        desc.clear();
        desc.merge(baseDesc);

        ClassDesc superDesc = getClassDesc(getClass(desc.getSuperclassName()),
                className, false);
        if (superDesc == null) {
            superDesc = newClassDesc(Object.class.getName());
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
            if (!pds[i].isReadable() && !pds[i].isWritable()
                    && !pds[i].hasMeta(Globals.META_NAME_PROPERTY)) {
                generated.removePropertyDesc(pds[i].getName());
            }
        }
        MethodDesc[] mds = generated.getMethodDescs();
        for (int i = 0; i < mds.length; i++) {
            if (baseDesc.getMethodDesc(mds[i]) == null
                    && (gapDesc.getMethodDesc(mds[i]) != null || superDesc
                            .getMethodDesc(mds[i]) != null)) {
                generated.removeMethodDesc(mds[i]);
            }
        }

        desc.merge(generated);
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
        classDesc.merge(getClassDesc(getClass(className), className, false));
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

    public MatchedPathMapping findMatchedPathMapping(String path, String method) {
        if (path == null) {
            return null;
        }
        return ymir_.findMatchedPathMapping(path, method);
    }

    public boolean isDenied(String path, String method) {
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

    public String getComponentName(String path, String method) {
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

    public String getActionName(String path, String method) {
        if (path == null) {
            return null;
        }
        MatchedPathMapping matched = ymir_.findMatchedPathMapping(path, method);
        if (matched == null) {
            return null;
        } else {
            return matched.getPathMapping().getActionName(
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
                    ClassTraverser traverser = new ClassTraverser();
                    try {
                        traverser.addReferenceClass(Class.forName(
                                "org.seasar.ymir.landmark.Landmark", true,
                                getClassLoader()));
                    } catch (ClassNotFoundException ex3) {
                        return null;
                    }
                    traverser.addClassPattern(getRootPackageName(), className);

                    final String[] found = new String[1];
                    traverser
                            .setClassHandler(new ClassTraversal.ClassHandler() {
                                public void processClass(String packageName,
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
            if (pds[i].getName().equals(propertyName)) {
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
                    logger_.error("Can't read properties: " + file);
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
        return new File(getResourcesDirectory(), SOURCECREATOR_PROPERTIES);
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
            logger_.error("Can't write properties: " + file);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    public void setNamingConvention(NamingConvention namingConvention) {
        namingConvention_ = namingConvention;
    }

    public void setYmir(Ymir ymir) {
        if (ymir instanceof YmirImpl) {
            ymir_ = (YmirImpl) ymir;
        } else {
            throw new ComponentNotFoundRuntimeException("YmirImpl");
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

    public File getWebappSourceRoot() {
        return new File(getApplication().getWebappSourceRoot());
    }

    public TemplateAnalyzer getTemplateAnalyzer() {
        return analyzer_;
    }

    public void setTemplateAnalyzer(TemplateAnalyzer analyzer) {
        analyzer_ = analyzer;
    }

    public void setClassDescModifiers(ClassDescModifier[] classDescModifiers) {
        classDescModifiers_ = classDescModifiers;
    }

    public String getSourceEncoding() {
        return sourceEncoding_;
    }

    public void setSourceEncoding(String sourceEncoding) {
        sourceEncoding_ = sourceEncoding;
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

    public void setSourceGenerator(SourceGenerator sourceGenerator) {
        sourceGenerator_ = sourceGenerator;
    }

    public ResponseCreator getResponseCreator() {
        return responseCreator_;
    }

    public void setResponseCreator(ResponseCreator responseCreator) {
        responseCreator_ = responseCreator;
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

    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    public ClassDesc newClassDesc(String className) {
        // booleanとかのクラスについてはSimpleClassDescを返した方が都合が良いのでこうしている。
        if (className.indexOf('.') < 0) {
            return new SimpleClassDesc(className);
        }

        ClassDescImpl classDescImpl = new ClassDescImpl(className);

        // スーパークラスをセットする。
        Application application = getApplication();
        for (Enumeration<String> enm = application.propertyNames(); enm
                .hasMoreElements();) {
            String key = enm.nextElement();
            if (!key.startsWith(APPKEYPREFIX_SOURCECREATOR_SUPERCLASS)) {
                continue;
            }
            if (!Pattern.compile(
                    key.substring(APPKEYPREFIX_SOURCECREATOR_SUPERCLASS
                            .length())).matcher(className).find()) {
                continue;
            }
            String superclassName = application.getProperty(key);
            Class<?> superclass = getClass(superclassName);
            if (superclass == null) {
                throw new RuntimeException(
                        "Superclass is not found: superclass key=" + key
                                + ", value=" + superclassName);
            }
            classDescImpl.setSuperclass(superclass);
            break;
        }
        if (classDescImpl.getSuperclassName() == null
                && classDescImpl.isTypeOf(ClassType.PAGE)) {
            String superclassName = application
                    .getProperty(APPKEY_SOURCECREATOR_SUPERCLASS);
            Class<?> superclass = getClass(superclassName);
            if (superclassName != null && superclass == null) {
                throw new RuntimeException(
                        "Superclass is not found: superclass key="
                                + APPKEY_SOURCECREATOR_SUPERCLASS + ", value="
                                + superclassName);
            }
            classDescImpl.setSuperclass(superclass);
        }

        return classDescImpl;
    }

    public TemplateProvider getTemplateProvider() {
        return templateProvider_;
    }

    public String filterResponse(String response) {
        Application application = getApplication();
        if (!shouldUpdate(application)) {
            return response;
        }

        if ("true".equals(application.getProperty(
                Globals.APPKEY_SOURCECREATOR_ENABLEINPLACEEDITOR, String
                        .valueOf(true)))) {
            String jsPrefix = "<script type=\"text/javascript\" src=\""
                    + getHttpServletRequest().getContextPath() + PATH_PREFIX
                    + "resource/js/";
            String jsSuffix = "\"></script>";
            response = response.replace("</head>", jsPrefix
                    + "prototype/prototype.js" + jsSuffix + jsPrefix
                    + "scriptaculous/scriptaculous.js" + jsSuffix + jsPrefix
                    + "sourceCreator.js" + jsSuffix + "</head>");
        }
        if ("true".equals(application.getProperty(
                Globals.APPKEY_SOURCECREATOR_ENABLECONTROLPANEL, String
                        .valueOf(true)))) {
            response = response.replace("</body>",
                    "<div class=\"__ymir__controlPanel\">"
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
}
