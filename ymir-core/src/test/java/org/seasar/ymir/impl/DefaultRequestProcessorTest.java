package org.seasar.ymir.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.PathMappingProvider;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.response.ForwardResponse;

public class DefaultRequestProcessorTest extends S2TestCase {

    private DefaultRequestProcessor target_;

    private PathMappingProvider pathMappingProvider_;

    protected void setUpContainer() throws Throwable {
        setServletContext(new MockServletContextImpl("test") {
            private static final long serialVersionUID = 1L;

            public Set getResourcePaths(String path) {
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

        target_ = (DefaultRequestProcessor) getComponent(DefaultRequestProcessor.class);
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

    public void testNormlizeResponse() throws Exception {

        Request request = new MockRequest() {
            public String getPath() {
                return "/article/update.zpt";
            }

            public String getComponentName() {
                return "articlePage";
            }
        };
        Response response = new ForwardResponse("/article/update.zpt");

        Response actual = target_
                .normalizeResponse(response, request.getPath());

        assertEquals("リクエストパスと同じパスへのフォワードはPASSTHROUGHに正規化されること",
                ResponseType.PASSTHROUGH, actual.getType());
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
}
