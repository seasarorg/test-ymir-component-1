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
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.YmirTestCase;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.creator.PageCreator;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.freemarker.FreemarkerSourceGenerator;
import org.seasar.ymir.extension.zpt.ZptAnalyzer;
import org.seasar.ymir.impl.AbstractApplication;
import org.seasar.ymir.impl.ApplicationManagerImpl;
import org.seasar.ymir.impl.DefaultRequestProcessor;
import org.seasar.ymir.impl.PathMappingImpl;
import org.seasar.ymir.impl.PathMappingProviderImpl;
import org.seasar.ymir.impl.SingleApplication;
import org.seasar.ymir.impl.YmirImpl;
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

abstract public class SourceCreatorImplTestBase extends YmirTestCase {

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
        container_.register(DefaultRequestProcessor.class);
        container_.register(LocalHotdeployS2Container.class);
        container_.register(YmirNamingConvention.class);
        container_.register(ZptAnalyzer.class);
        container_.register(ConfigurationImpl.class);
        container_.register(ApplicationManagerImpl.class);

        LocalHotdeployS2Container ondemandContainer = (LocalHotdeployS2Container) container_
                .getComponent(LocalHotdeployS2Container.class);
        ondemandContainer.addReferenceClassName(getClass().getName());

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
        ondemandContainer.setCreators(new ComponentCreator[] { new PageCreator(
                namingConvention) });

        Configuration configuration = (Configuration) container_
                .getComponent(Configuration.class);
        configuration.setProperty(AbstractApplication.KEY_ROOTPACKAGENAME,
                "com.example");
        configuration.setProperty(Globals.APPKEY_SOURCECREATOR_SUPERCLASS,
                "com.example.page.TestPageBaseBase");
        configuration.setProperty(Globals.APPKEYPREFIX_SOURCECREATOR_SUPERCLASS
                + "IndexPage$", "com.example.web.IndexPageBaseBase");
        ApplicationManager applicationManager = (ApplicationManager) container_
                .getComponent(ApplicationManager.class);
        PathMappingProviderImpl pathMappingProvider = new PathMappingProviderImpl();
        pathMappingProvider
                .setPathMappings(new PathMapping[] { new PathMappingImpl(
                        "^/([^/]+)\\.(.+)$", "${1}Page", "_${method}", "",
                        null, null) });
        applicationManager.setBaseApplication(new SingleApplication(context,
                configuration, null, container_, ondemandContainer,
                pathMappingProvider));
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

        YmirContext.setYmir((Ymir) container_.getComponent(Ymir.class));
        SingletonPluggableContainerFactory.init();
    }

    protected Configuration getConfiguration() {
        return (Configuration) container_.getComponent(Configuration.class);
    }

    protected File getSourceDir() {
        return ResourceUtil.getBuildDir(getClass());
    }

    protected void tearDown() throws Exception {

        SingletonPluggableContainerFactory.destroy();
    }
}
