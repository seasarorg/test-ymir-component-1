package org.seasar.ymir.extension.creator.action.impl;

import java.io.InputStream;

import junit.framework.TestCase;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.extension.creator.impl.PathMetaDataImpl;
import org.seasar.ymir.extension.creator.mock.MockSourceCreator;
import org.seasar.ymir.extension.mock.MockServletContext;
import org.seasar.ymir.mock.MockRequest;

public class ResourceActionTest extends TestCase {

    private ResourceAction target_ = new ResourceAction(new MockSourceCreator()
            .setServletContext(new MockServletContext() {
                @Override
                public String getMimeType(String file) {
                    if (file.endsWith(".js")) {
                        return "text/javascript";
                    } else {
                        return null;
                    }
                }
            }));

    public void testGetResourcePath() throws Exception {
        assertEquals("js/prototype/prototype.js", target_
                .getResourcePath(new PathMetaDataImpl(
                        "/__ymir__/resource/js/prototype/prototype.js",
                        Request.METHOD_GET, false, null, null, null, null,
                        null, null)));
    }

    public void testGetResourcePath_パスが不適切な場合はnullを返すこと() throws Exception {
        assertNull(target_.getResourcePath(new PathMetaDataImpl(
                "/js/prototype/prototype.js", Request.METHOD_GET, false, null,
                null, null, null, null, null)));
    }

    public void testAct() throws Exception {

        Response actual = target_.act(new MockRequest(), new PathMetaDataImpl(
                "/__ymir__/resource/js/prototype/prototype.js",
                Request.METHOD_GET, false, null, null, null, null, null, null));

        assertEquals(ResponseType.SELF_CONTAINED, actual.getType());
        assertEquals("text/javascript", actual.getContentType());
        InputStream actual2 = actual.getInputStream();
        assertNotNull(actual2);
        actual2.close();
    }

    public void testAct_パスが不適切な場合はVoidResponseを返すこと() throws Exception {

        Response actual = target_.act(new MockRequest(), new PathMetaDataImpl(
                "/js/prototype/prototype.js", Request.METHOD_GET, false, null,
                null, null, null, null, null));

        assertEquals(ResponseType.VOID, actual.getType());
    }
}
