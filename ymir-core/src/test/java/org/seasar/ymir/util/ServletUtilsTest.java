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

    public void test_omitSessionId() throws Exception {
        assertEquals("/a?b=1", ServletUtils
                .omitSessionId("/a;jsessionid=aaaa?b=1"));
    }

    public void test_stripParameters() throws Exception {
        assertEquals("/a.html", ServletUtils.stripParameters("/a.html?b=1"));

        assertEquals("/a.html", ServletUtils
                .stripParameters("/a.html;jsessionid=1?b=1"));

        assertEquals("/a.html", ServletUtils.stripParameters("/a.html#top?b=1"));
    }

    public void test_embedSessionId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequestImpl(
                new MockServletContextImpl("/context"), "/path/to/file.html");

        assertNull(ServletUtils.embedSessionId(null, request));

        assertEquals("既に埋め込まれている場合は何もしないこと", "/a.html;jsessionid=aaa",
                ServletUtils.embedSessionId("/a.html;jsessionid=aaa", request));

        assertEquals("セッションが存在しなければ埋め込まれないこと", "/a.html", ServletUtils
                .embedSessionId("/a.html", request));

        String id = request.getSession(true).getId();

        assertEquals("セッションが存在する場合は埋め込まれること", "/a.html;jsessionid=" + id,
                ServletUtils.embedSessionId("/a.html", request));

        assertEquals("セッションが存在する場合は埋め込まれること", "/a.html;jsessionid=" + id
                + "?a=b", ServletUtils.embedSessionId("/a.html?a=b", request));

        assertEquals("セッションが存在する場合は埋め込まれること", "/a.html;jsessionid=" + id
                + "#top", ServletUtils.embedSessionId("/a.html#top", request));
    }
}
