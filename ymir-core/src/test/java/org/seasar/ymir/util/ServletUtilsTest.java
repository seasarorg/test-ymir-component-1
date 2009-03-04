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

    public void testGetQueryString() throws Exception {
        assertEquals("a=b", ServletUtils.getQueryString("hoge.html?a=b"));
        assertEquals("a", ServletUtils.getQueryString("hoge.html?a"));
        assertNull(ServletUtils.getQueryString("hoge.html?"));
        assertNull(ServletUtils.getQueryString("hoge.html"));
    }

    public void testNormalizePath() throws Exception {
        assertNull(ServletUtils.normalizePath(null));

        assertEquals("", ServletUtils.normalizePath(""));

        assertEquals("", ServletUtils.normalizePath("/"));

        assertEquals("/a/b/c", ServletUtils.normalizePath("/a/b/c"));

        assertEquals("/a/b/c", ServletUtils.normalizePath("/a/b/c/"));
    }

    public void testToAbsolutePath() throws Exception {
        assertEquals("/path/to/page.html", ServletUtils.toAbsolutePath(
                "/path/to/page.html", ""));

        assertEquals("/path/to/page2.html", ServletUtils.toAbsolutePath(
                "/path/to/page.html", "page2.html"));

        assertEquals("/path/page2.html", ServletUtils.toAbsolutePath(
                "/path/to/page.html", "../page2.html"));

        assertEquals("/path/to/page2.html", ServletUtils.toAbsolutePath(
                "/path/to/page.html", "./page2.html"));

        assertEquals("/path/to/page.html#", ServletUtils.toAbsolutePath(
                "/path/to/page.html", "#"));

        assertEquals("/path/to/page2.html#", ServletUtils.toAbsolutePath(
                "/path/to/page.html", "page2.html#"));

        assertEquals("/path/to/", ServletUtils.toAbsolutePath(
                "/path/to/page.html", "."));

        assertEquals("/path/to/?edit=", ServletUtils.toAbsolutePath(
                "/path/to/page.html", ".?edit="));

        assertEquals("/path/to/", ServletUtils.toAbsolutePath("/path/to/", "."));

        assertEquals("/path/", ServletUtils.toAbsolutePath("/path/to/", ".."));

        assertEquals("/path", ServletUtils.toAbsolutePath("", "path"));

        assertEquals("空文字列はカレントパスを表すこと", "/path", ServletUtils.toAbsolutePath(
                "/path", ""));
    }
}
