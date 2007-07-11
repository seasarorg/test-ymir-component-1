package org.seasar.ymir.scope.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.external.servlet.HttpServletExternalContextComponentDefRegister;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.Application;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.impl.DefaultRequestProcessor;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.mock.MockYmir;

public class ApplicationScopeTest extends TestCase {
    private S2Container container_;

    private MockServletContextImpl servletContext_;

    private DefaultRequestProcessor requestProcessor_;

    @Override
    protected void setUp() throws Exception {
        container_ = new S2ContainerImpl();
        container_
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        HttpServletExternalContext externalContext = new HttpServletExternalContext();
        servletContext_ = new MockServletContextImpl("/context");
        externalContext.setApplication(servletContext_);
        container_.setExternalContext(externalContext);
        container_.register(ApplicationScope.class);
        container_.init();
        SingletonS2ContainerFactory.setContainer(container_);

        Ymir ymir = new MockYmir() {
            @Override
            public Application getApplication() {
                return new MockApplication() {
                    @Override
                    public boolean isUnderDevelopment() {
                        return false;
                    }
                };
            }
        };
        YmirContext.setYmir(ymir);

        requestProcessor_ = new DefaultRequestProcessor() {
            @Override
            protected Object getComponent(Request request) {
                return new ApplicationScopeTestPage();
            }

            @Override
            protected Class<?> getComponentClass(String componentName) {
                return ApplicationScopeTestPage.class;
            }

            @Override
            protected S2Container getS2Container() {
                return container_;
            }
        };
        requestProcessor_.setYmir(ymir);
    }

    public void test() throws Exception {
        MockRequest request = new MockRequest();
        request.setPath("/test.html").setActionName("_get");

        servletContext_.setAttribute("injectedValue", "INJECTED_VALUE");
        requestProcessor_.process(request);

        ApplicationScopeTestPage page = (ApplicationScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
        assertEquals("OUTJECTED_VALUE", servletContext_
                .getAttribute("outjectedValue"));
    }
}
