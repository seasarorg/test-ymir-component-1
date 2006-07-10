package org.seasar.cms.ymir.creator.impl;

import java.io.File;

import javax.servlet.ServletContext;

import org.seasar.cms.ymir.Configuration;
import org.seasar.cms.ymir.YmirTestCase;
import org.seasar.cms.ymir.RequestProcessor;
import org.seasar.cms.ymir.container.hotdeploy.DistributedOndemandBehavoir;
import org.seasar.cms.ymir.container.hotdeploy.LocalOndemandCreatorContainer;
import org.seasar.cms.ymir.container.hotdeploy.OndemandUtils;
import org.seasar.cms.ymir.creator.ClassDesc;
import org.seasar.cms.ymir.creator.PropertyDesc;
import org.seasar.cms.ymir.creator.SourceCreator;
import org.seasar.cms.ymir.freemarker.FreemarkerSourceGenerator;
import org.seasar.cms.ymir.impl.ConfigurationImpl;
import org.seasar.cms.ymir.impl.DefaultRequestProcessor;
import org.seasar.cms.ymir.zpt.ZptAnalyzer;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.ExternalComponentDeployerProvider;
import org.seasar.framework.container.hotdeploy.OndemandCreatorContainer;
import org.seasar.framework.container.hotdeploy.creator.PageCreator;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.impl.servlet.HttpServletExternalContext;
import org.seasar.framework.container.impl.servlet.HttpServletExternalContextComponentDefRegister;
import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.util.ResourceUtil;

abstract public class SourceCreatorImplTestBase extends YmirTestCase {

    protected S2Container container_;

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

        ComponentDeployerFactory
            .setProvider(new ExternalComponentDeployerProvider());
        S2ContainerBehavior.setProvider(new DistributedOndemandBehavoir());
        container_ = new S2ContainerImpl();
        ((S2ContainerImpl) container_).setClassLoader(getClass()
            .getClassLoader());
        container_.setExternalContext(new HttpServletExternalContext());
        container_
            .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        ServletContext context = new MockServletContextImpl("/context");
        MockHttpServletRequestImpl request = new MockHttpServletRequestImpl(
            context, "/servlet");
        container_.getExternalContext().setRequest(request);
        container_.getExternalContext().setResponse(
            new MockHttpServletResponseImpl(request));
        container_.register(SourceCreatorImpl.class);
        container_.register(DefaultRequestProcessor.class);
        container_.register(LocalOndemandCreatorContainer.class);
        container_.register(ZptAnalyzer.class);
        container_.register(ConfigurationImpl.class);

        Configuration configuration = (Configuration) container_
            .getComponent(Configuration.class);
        configuration.setProperty(Configuration.KEY_WEBAPPROOT, new File(
            ResourceUtil.getBuildDir(getClass()), "webapp").getCanonicalPath());

        DefaultRequestProcessor processor = (DefaultRequestProcessor) container_
            .getComponent(RequestProcessor.class);
        processor.addPathMapping("^/([^/]+)\\.(.+)$", "${1}Page", "_${method}",
            "", null);

        LocalOndemandCreatorContainer creatorContainer = (LocalOndemandCreatorContainer) container_
            .getComponent(OndemandCreatorContainer.class);
        creatorContainer.setRootPackageName("com.example");
        creatorContainer.addCreator(new PageCreator());
        OndemandUtils.start(container_);

        target_ = (SourceCreatorImpl) container_
            .getComponent(SourceCreator.class);
        target_.setPagePackageName("com.example.page");
        target_.setDtoPackageName("com.example.dto");
        target_.setSourceDirectoryPath(ResourceUtil.getBuildDir(getClass())
            .getCanonicalPath());
        target_.setClassesDirectoryPath(ResourceUtil.getBuildDir(getClass())
            .getCanonicalPath());
        FreemarkerSourceGenerator sourceGenerator = new FreemarkerSourceGenerator();
        sourceGenerator.setSourceCreator(target_);
        target_.setSourceGenerator(sourceGenerator);
    }

    protected void tearDown() throws Exception {

        OndemandUtils.stop(container_);
    }
}
