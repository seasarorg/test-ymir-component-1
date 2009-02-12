package org.seasar.ymir.extension.creator.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.cms.pluggable.SingletonPluggableContainerFactory;
import org.seasar.cms.pluggable.hotdeploy.DistributedHotdeployBehavior;
import org.seasar.cms.pluggable.hotdeploy.LocalHotdeployS2Container;
import org.seasar.cms.pluggable.impl.ConfigurationImpl;
import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.external.servlet.HttpServletExternalContextComponentDefRegister;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.creator.PageCreator;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.mapping.PathMappingExtraData;
import org.seasar.ymir.extension.creator.mapping.impl.YmirPathMappingExtraData;
import org.seasar.ymir.extension.freemarker.FreemarkerSourceGenerator;
import org.seasar.ymir.extension.zpt.ZptAnalyzer;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.impl.AbstractApplication;
import org.seasar.ymir.impl.ApplicationManagerImpl;
import org.seasar.ymir.impl.PathMappingProviderImpl;
import org.seasar.ymir.impl.RequestProcessorImpl;
import org.seasar.ymir.impl.SingleApplication;
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.impl.YmirPathMapping;
import org.seasar.ymir.locale.impl.LocaleManagerImpl;
import org.seasar.ymir.message.impl.MessagesImpl;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.session.impl.SessionManagerImpl;
import org.seasar.ymir.testing.TestCaseBase;
import org.seasar.ymir.token.impl.TokenManagerImpl;

abstract public class SourceCreatorImplTestBase extends TestCaseBase {

    protected S2Container container_;

    protected DistributedHotdeployBehavior ondemandBehavior_;

    protected SourceCreatorImpl target_;

    protected ClassDesc constructClassDesc() {
        ClassDesc classDesc = new ClassDescImpl("com.example.web.TestPage");
        PropertyDesc pd = new PropertyDescImpl("param1");
        pd.setMode(PropertyDesc.READ | PropertyDesc.WRITE);
        classDesc.setPropertyDesc(pd);
        return classDesc;
    }

    protected SourceCreatorImpl getSourceCreator() {

        return target_;
    }

    protected void setUp() throws Exception {

        ServletContext context = new MockServletContextImpl("/context") {
            private static final long serialVersionUID = 1L;

            @Override
            public URL getResource(String path) throws MalformedURLException {
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                return new File(new File(ResourceUtil.getBuildDir(getClass()),
                        "webapp"), path).toURI().toURL();
            }
        };
        SingletonPluggableContainerFactory.setApplication(context);
        SingletonPluggableContainerFactory.prepareForContainer();

        container_ = new S2ContainerImpl();
        container_.setClassLoader(getClass().getClassLoader());
        SingletonPluggableContainerFactory.getRootContainer().include(
                container_);
        container_.setExternalContext(new HttpServletExternalContext());
        container_
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        MockHttpServletRequestImpl request = new MockHttpServletRequestImpl(
                context, "/servlet");
        container_.register(YmirImpl.class);
        container_.register(SourceCreatorImpl.class);
        container_.register(RequestProcessorImpl.class);
        container_.register(LocalHotdeployS2Container.class);
        container_.register(YmirNamingConvention.class);
        container_.register(ZptAnalyzer.class);
        container_.register(ConfigurationImpl.class);
        container_.register(ApplicationManagerImpl.class);
        container_.register(HotdeployManagerImpl.class);
        container_.register(MessagesImpl.class, Globals.NAME_MESSAGES);
        container_.register(LocaleManagerImpl.class);
        container_.register(SessionManagerImpl.class);
        container_.register(TokenManagerImpl.class);

        LocalHotdeployS2Container localHotdeployS2Container = (LocalHotdeployS2Container) container_
                .getComponent(LocalHotdeployS2Container.class);
        localHotdeployS2Container.addReferenceClassName(getClass().getName());

        MockRequest ymirRequest = new MockRequest();
        ymirRequest.setParameterValues("aaa", new String[] { "a&?", "b" });
        ymirRequest.setParameter("bbb", "c");
        ymirRequest.setMethod(HttpMethod.GET);
        MockDispatch dispatch = new MockDispatch();
        dispatch.setAbsolutePath("/context/path");
        ymirRequest.enterDispatch(dispatch);
        container_.register(ymirRequest);

        container_.init();

        container_.getRoot().getExternalContext().setRequest(request);
        container_.getRoot().getExternalContext().setResponse(
                new MockHttpServletResponseImpl(request));
        container_.getRoot().getExternalContext().setApplication(context);

        ondemandBehavior_ = (DistributedHotdeployBehavior) S2ContainerBehavior
                .getProvider();

        YmirNamingConvention namingConvention = (YmirNamingConvention) container_
                .getComponent(YmirNamingConvention.class);
        namingConvention.addRootPackageName("com.example");
        localHotdeployS2Container
                .setCreators(new ComponentCreator[] { new PageCreator(
                        namingConvention) });

        Configuration configuration = (Configuration) container_
                .getComponent(Configuration.class);
        configuration.setProperty(AbstractApplication.KEY_ROOTPACKAGENAME,
                "com.example");
        configuration.setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_SUPERCLASS,
                "com.example.page.TestPageBaseBase");
        configuration
                .setProperty(
                        SourceCreatorSetting.APPKEYPREFIX_SOURCECREATOR_SUPERCLASS
                                + "IndexPage$",
                        "com.example.web.IndexPageBaseBaseBase");
        configuration.setProperty(
                SourceCreatorSetting.APPKEYPREFIX_SOURCECREATOR_SUPERCLASS
                        + "HndexPage$", "com.example.web.HndexPageBaseBase");
        ApplicationManager applicationManager = (ApplicationManager) container_
                .getComponent(ApplicationManager.class);
        PathMappingProviderImpl pathMappingProvider = new PathMappingProviderImpl();
        pathMappingProvider
                .setPathMappings(new PathMapping[] { new YmirPathMapping(
                        "/([^/]+)\\.(.+)", "${1}Page") });
        applicationManager.setBaseApplication(new SingleApplication(context,
                configuration, null, container_, localHotdeployS2Container,
                pathMappingProvider));
        configuration.setProperty(AbstractApplication.KEY_PROJECTROOT,
                getProjectRootDir().getCanonicalPath());
        configuration.setProperty(AbstractApplication.KEY_WEBAPPSOURCEROOT,
                new File(ResourceUtil.getBuildDir(getClass()), "webapp")
                        .getAbsolutePath());
        configuration.setProperty(AbstractApplication.KEY_SOURCEDIRECTORY,
                getSourceDir().getCanonicalPath());
        configuration.setProperty(AbstractApplication.KEY_RESOURCESDIRECTORY,
                ResourceUtil.getBuildDir(getClass()).getCanonicalPath());

        target_ = (SourceCreatorImpl) container_
                .getComponent(SourceCreator.class);
        target_.setNamingConvention(namingConvention);
        FreemarkerSourceGenerator sourceGenerator = new FreemarkerSourceGenerator();
        sourceGenerator.setSourceCreator(target_);
        target_.setSourceGenerator(sourceGenerator);
        target_
                .setPathMappingExtraDatas(new PathMappingExtraData<?>[] { new YmirPathMappingExtraData() });

        YmirContext.setYmir((Ymir) container_.getComponent(Ymir.class));
        SingletonPluggableContainerFactory.init();
    }

    protected Configuration getConfiguration() {
        return (Configuration) container_.getComponent(Configuration.class);
    }

    protected File getProjectRootDir() {
        return ResourceUtil.getBuildDir(getClass());
    }

    protected File getSourceDir() {
        return ResourceUtil.getBuildDir(getClass());
    }

    protected void tearDown() throws Exception {

        SingletonPluggableContainerFactory.destroy();
    }
}
