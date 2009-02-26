package org.seasar.ymir.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.handler.impl.AnnotationHandlerImpl;
import org.seasar.ymir.cache.impl.CacheManagerImpl;
import org.seasar.ymir.converter.impl.TypeConversionManagerImpl;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.mock.MockYmir;
import org.seasar.ymir.response.ForwardResponse;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.response.RedirectResponse;
import org.seasar.ymir.response.constructor.impl.ResponseConstructorSelectorImpl;
import org.seasar.ymir.response.constructor.impl.ResponseResponseConstructor;
import org.seasar.ymir.response.constructor.impl.StringResponseConstructor;
import org.seasar.ymir.response.scheme.impl.PassthroughStrategy;
import org.seasar.ymir.response.scheme.impl.RedirectStrategy;
import org.seasar.ymir.response.scheme.impl.StrategySelectorImpl;
import org.seasar.ymir.scope.impl.ScopeManagerImpl;

public class ExceptionProcessorImplTest extends TestCase {
    private ExceptionProcessorImpl target_ = new ExceptionProcessorImpl();

    private ExceptionProcessorImpl newExceptionProcessorImpl(
            final S2Container container) {
        ActionManagerImpl actionManager = new ActionManagerImpl();
        AnnotationHandlerImpl annotationHandler = new AnnotationHandlerImpl();
        HotdeployManagerImpl hotdeployManager = new HotdeployManagerImpl();
        ApplicationManagerImpl applicationManager = new ApplicationManagerImpl();
        applicationManager.setHotdeployManager(hotdeployManager);
        applicationManager.setBaseApplication(new MockApplication() {
            @Override
            public S2Container getS2Container() {
                return container;
            }
        });
        hotdeployManager.setApplicationManager(applicationManager);
        ResponseConstructorSelectorImpl responseConstructorSelector = new ResponseConstructorSelectorImpl();
        StringResponseConstructor stringResponseConstructor = new StringResponseConstructor();
        responseConstructorSelector.add(stringResponseConstructor);
        responseConstructorSelector.add(new ResponseResponseConstructor());
        StrategySelectorImpl strategySelector = new StrategySelectorImpl();
        stringResponseConstructor.setStrategySelector(strategySelector);
        strategySelector.add(new RedirectStrategy() {
            @Override
            protected boolean isRichPathExpressionAvailable() {
                return false;
            }
        });
        strategySelector.add(new PassthroughStrategy());
        actionManager
                .setResponseConstructorSelector(responseConstructorSelector);
        ScopeManagerImpl scopeManager = new ScopeManagerImpl();
        actionManager.setScopeManager(scopeManager);
        CacheManagerImpl cacheManager = new CacheManagerImpl();
        cacheManager.setHotdeployManager(hotdeployManager);
        scopeManager.setAnnotationHandler(annotationHandler);
        scopeManager.setApplicationManager(applicationManager);
        scopeManager.setCacheManager(cacheManager);
        scopeManager.setHotdeployManager(hotdeployManager);
        TypeConversionManagerImpl typeConversionManager = new TypeConversionManagerImpl();
        scopeManager.setTypeConversionManager(typeConversionManager);
        typeConversionManager.setHotdeployManager(hotdeployManager);
        annotationHandler.setCacheManager(cacheManager);

        ExceptionProcessorImpl exceptionProcessor = new ExceptionProcessorImpl() {
            @Override
            S2Container getS2Container() {
                return container;
            }
        };
        exceptionProcessor.setActionManager(actionManager);
        exceptionProcessor.setAnnotationHandler(annotationHandler);
        exceptionProcessor.setApplicationManager(applicationManager);
        exceptionProcessor.setCacheManager(cacheManager);
        exceptionProcessor.setYmir(new MockYmir());
        return exceptionProcessor;
    }

    public void testGetComponentName() throws Exception {
        assertEquals("hoeExceptionHandler", target_
                .getComponentName(HoeException.class));
    }

    public void testGetComponentName2() throws Exception {
        assertEquals("URLExceptionHandler", target_
                .getComponentName(URLException.class));
    }

    public void testProcess_グローバルハンドラ_ExceptionHandlerインタフェース()
            throws Exception {
        final S2Container container = new S2ContainerImpl();
        container.register(NullPointerExceptionHandler1.class,
                "nullPointerExceptionHandler");
        ExceptionProcessorImpl target = newExceptionProcessorImpl(container);

        Response actual = target.process(new MockRequest(),
                new NullPointerException());

        assertNotNull(actual);
        assertTrue(actual instanceof RedirectResponse);
        assertEquals("path", ((RedirectResponse) actual).getPath());
    }

    public void testProcess_グローバルハンドラ_ExceptionHandlerアノテーション()
            throws Exception {
        final S2Container container = new S2ContainerImpl();
        container.register(NullPointerExceptionHandler2.class,
                "nullPointerExceptionHandler");
        ExceptionProcessorImpl target = newExceptionProcessorImpl(container);

        Response actual = target.process(new MockRequest(),
                new NullPointerException());

        assertNotNull(actual);
        assertTrue(actual instanceof RedirectResponse);
        assertEquals("path", ((RedirectResponse) actual).getPath());
    }

    public void testProcess_グローバルハンドラ_passthroughはForwardResponseに変換されること()
            throws Exception {
        final S2Container container = new S2ContainerImpl();
        container.register(NullPointerExceptionHandler3.class,
                "nullPointerExceptionHandler");
        ExceptionProcessorImpl target = newExceptionProcessorImpl(container);

        Response actual = target.process(new MockRequest(),
                new NullPointerException());

        assertNotNull(actual);
        assertTrue(actual instanceof ForwardResponse);
    }

    public void testProcess_Page内ハンドラ_ExceptionHandlerインタフェースは機能しないこと()
            throws Exception {
        final S2Container container = new S2ContainerImpl();
        container.register(NullPointerExceptionHandler1.class,
                "nullPointerExceptionHandler");
        ExceptionProcessorImpl target = newExceptionProcessorImpl(container);

        MockRequest request = new MockRequest();
        MockDispatch dispatch = new MockDispatch();
        dispatch.setPageComponent(new PageComponentImpl(new Page1(),
                Page1.class));
        request.enterDispatch(dispatch);

        Response actual = target.process(request, new NullPointerException());

        assertNotNull(actual);
        assertTrue(actual instanceof RedirectResponse);
        assertEquals("Page内で処理されずにグローハンドルが呼ばれること", "path",
                ((RedirectResponse) actual).getPath());
    }

    public void testProcess_Page内ハンドラ_ExceptionHandlerアノテーション()
            throws Exception {
        final S2Container container = new S2ContainerImpl();
        container.register(NullPointerExceptionHandler2.class,
                "nullPointerExceptionHandler");
        ExceptionProcessorImpl target = newExceptionProcessorImpl(container);

        MockRequest request = new MockRequest();
        MockDispatch dispatch = new MockDispatch();
        dispatch.setPageComponent(new PageComponentImpl(new Page2(),
                Page2.class));
        request.enterDispatch(dispatch);

        Response actual = target.process(request, new NullPointerException());

        assertNotNull(actual);
        assertTrue(actual instanceof RedirectResponse);
        assertEquals("より合致するハンドラが呼び出されること", "page2NPE",
                ((RedirectResponse) actual).getPath());

        actual = target.process(request, new IllegalArgumentException());

        assertNotNull(actual);
        assertTrue("スーパークラスのハンドラが呼び出されること",
                actual instanceof PassthroughResponse);
    }

    public void testProcess_Page内ハンドラ_passthroughは変換されないこと() throws Exception {
        final S2Container container = new S2ContainerImpl();
        ExceptionProcessorImpl target = newExceptionProcessorImpl(container);

        MockRequest request = new MockRequest();
        MockDispatch dispatch = new MockDispatch();
        dispatch.setPageComponent(new PageComponentImpl(new Page2(),
                Page2.class));
        request.enterDispatch(dispatch);

        Response actual = target.process(request,
                new IllegalArgumentException());

        assertNotNull(actual);
        assertTrue(actual instanceof PassthroughResponse);
    }
}
