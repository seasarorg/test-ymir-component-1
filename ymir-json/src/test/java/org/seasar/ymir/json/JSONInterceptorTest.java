package org.seasar.ymir.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageMetaData;
import org.seasar.ymir.impl.PageComponentImpl;
import org.seasar.ymir.impl.PageMetaDataImpl;
import org.seasar.ymir.impl.TypeConversionManagerImpl;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.test.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.test.mock.servlet.MockServletContextImpl;

import net.sf.json.JSONObject;

public class JSONInterceptorTest extends TestCase {
    public void testActionInvoking() throws Exception {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("hoe", "HOE");
        jsonObject.put("fuga", "FUGA");

        JSONInterceptor target = new JSONInterceptor() {
            @Override
            HttpServletRequest getHttpServletRequest() {
                return new MockHttpServletRequestImpl(
                        new MockServletContextImpl("/json"), "/index.html") {
                    @Override
                    public BufferedReader getReader() throws IOException {
                        return new BufferedReader(new StringReader(jsonObject
                                .toString()));
                    }

                    @Override
                    public String getContentType() {
                        return "text/javascript; charset=UTF-8";
                    }
                };
            }
        };
        target.setTypeConversionManager(new TypeConversionManagerImpl());

        MockRequest request = new MockRequest();
        MockDispatch dispatch = new MockDispatch();
        TestPage testPage = new TestPage();
        PageComponent pageComponent = new PageComponentImpl(testPage,
                TestPage.class);
        PageMetaDataImpl pageMetaData = new PageMetaDataImpl(TestPage.class,
                null) {
            @Override
            protected boolean isStrictInjection(S2Container container) {
                return true;
            }
        };
        pageComponent.setRelatedObject(PageMetaData.class, pageMetaData);
        dispatch.setPageComponent(pageComponent);
        request.enterDispatch(dispatch);

        target.actionInvoking(request, null, null);

        assertEquals("HOE", testPage.getHoe());
        assertNull(testPage.getFuga());
    }
}
