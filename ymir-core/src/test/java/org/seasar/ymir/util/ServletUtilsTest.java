package org.seasar.ymir.util;

import junit.framework.TestCase;

import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

public class ServletUtilsTest extends TestCase {
    public void testConstructRequestURL_httpに変換できること() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequestImpl(
                new MockServletContextImpl("/context"), "/path/to/file.html");
        request.setProtocol("http");
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setQueryString("param=value");

        assertEquals("http://localhost/context/path/to/file.html?param=value",
                ServletUtils.constructRequestURL(request, "http", 80));
    }

    public void testConstructRequestURL_httpsに変換できること() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequestImpl(
                new MockServletContextImpl("/context"), "/path/to/file.html");
        request.setProtocol("http");
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setQueryString("param=value");

        assertEquals("https://localhost/context/path/to/file.html?param=value",
                ServletUtils.constructRequestURL(request, "https", 443));
    }

    public void testConstructRequestURL_ポートがデフォルトポートでない場合はポート番号が付与されること()
            throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequestImpl(
                new MockServletContextImpl("/context"), "/path/to/file.html");
        request.setProtocol("http");
        request.setServerName("localhost");
        request.setServerPort(8080);
        request.setQueryString("param=value");

        assertEquals(
                "http://localhost:8080/context/path/to/file.html?param=value",
                ServletUtils.constructRequestURL(request, "http", 8080));
    }

    public void testGetTrunk() throws Exception {
        assertNull(ServletUtils.getTrunk(null));

        assertEquals("/path/to/file", ServletUtils.getTrunk("/path/to/file"));

        assertEquals("/path/to/file", ServletUtils
                .getTrunk("/path/to/file?a=1"));
    }
}
