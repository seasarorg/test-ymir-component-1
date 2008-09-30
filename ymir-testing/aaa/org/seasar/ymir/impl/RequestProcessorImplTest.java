package org.seasar.ymir.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PathMappingProvider;
import org.seasar.ymir.mock.MockApplication;

public class RequestProcessorImplTest extends S2TestCase {
    private RequestProcessorImpl target_;

    private PathMappingProvider pathMappingProvider_;

    protected void setUpContainer() throws Throwable {
        setServletContext(new MockServletContextImpl("test") {
            private static final long serialVersionUID = 1L;

            @Override
            public Set<String> getResourcePaths(String path) {
                if ("/path/to/".equals(path)) {
                    return new HashSet<String>(Arrays.asList(new String[] {
                        "/path/to/file", "/path/to/dir/" }));
                } else if ("/".equals(path)) {
                    return new HashSet<String>(Arrays.asList(new String[] {
                        "/file", "/dir/" }));
                } else {
                    return null;
                }
            }
        });
        super.setUpContainer();
    }

    protected void setUp() throws Exception {
        super.setUp();

        include(getClass().getName().replace('.', '/') + ".dicon");

        target_ = (RequestProcessorImpl) getComponent(RequestProcessorImpl.class);
        pathMappingProvider_ = (PathMappingProvider) getComponent(PathMappingProvider.class);

        ApplicationManager applicationManager = (ApplicationManager) getComponent(ApplicationManager.class);
        Application application = new MockApplication() {
            public PathMappingProvider getPathMappingProvider() {
                return pathMappingProvider_;
            }

            @Override
            public S2Container getS2Container() {
                return getContainer();
            }
        };
        applicationManager.setBaseApplication(application);

        getContainer().init();
    }

    public void testFileResourceExists() throws Exception {
        assertTrue(target_.fileResourceExists("/file"));
        assertFalse(target_.fileResourceExists("/nonexistentfile"));
        assertFalse(target_.fileResourceExists("/dir"));

        assertTrue(target_.fileResourceExists("/path/to/file"));
        assertFalse(target_.fileResourceExists("/path/to/nonexistentfile"));
        assertFalse(target_.fileResourceExists("/path/to/dir"));

        assertFalse("親ディレクトリが存在しない場合もfalseを返すこと", target_
                .fileResourceExists("/nonexistentdir/file"));
    }

    public void testCreatePageComponent() throws Exception {
        PageComponent pageComponent = target_.createPageComponent("parentPage");

        assertNotNull(pageComponent);
        assertSame(target_.getS2Container().getComponent("parentPage"),
                pageComponent.getPage());
        assertEquals(ParentPage.class, pageComponent.getPageClass());
        assertEquals(2, pageComponent.getChildren().length);

        PageComponent pageComponent1 = pageComponent.getChildren()[0];
        assertSame(target_.getS2Container().getComponent("child1Page"),
                pageComponent1.getPage());
        assertEquals(Child1Page.class, pageComponent1.getPageClass());
        assertEquals(0, pageComponent1.getChildren().length);

        PageComponent pageComponent2 = pageComponent.getChildren()[1];
        assertSame(target_.getS2Container().getComponent("child2Page"),
                pageComponent2.getPage());
        assertEquals(Child2Page.class, pageComponent2.getPageClass());
        assertEquals(1, pageComponent2.getChildren().length);

        PageComponent pageComponent3 = pageComponent2.getChildren()[0];
        assertSame(target_.getS2Container().getComponent("child3Page"),
                pageComponent3.getPage());
        assertEquals(Child3Page.class, pageComponent3.getPageClass());
        assertEquals(0, pageComponent3.getChildren().length);
    }
}
