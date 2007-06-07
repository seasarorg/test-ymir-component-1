package org.seasar.ymir.zpt;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletContext;

import org.seasar.framework.mock.servlet.MockServletContextImpl;

import junit.framework.TestCase;

public class I18NPageTypePrefixHandlerTest extends TestCase {
    private I18NPageTypePrefixHandler target_ = new I18NPageTypePrefixHandler();

    public void testDetermineResourcePath() throws Exception {
        ServletContext servletContext = new MockServletContextImpl("/context") {
            private static final long serialVersionUID = 1L;

            @Override
            public URL getResource(String path) throws MalformedURLException {
                Set<String> existsSet = new HashSet<String>();
                existsSet.addAll(Arrays.asList(new String[] {
                    "/path/to/image_ja.jpg", "/path/to/image2.jpg",
                    "/path/t.o/image3", "/path/t_ja.o/image3", }));
                if (existsSet.contains(path)) {
                    return new URL("file:/path");
                } else {
                    return null;
                }
            }
        };
        Locale locale = new Locale("ja", "JP");

        assertEquals("http://hoehoe.com/", target_.determineResourcePath(
                servletContext, "http://hoehoe.com/", locale));

        assertEquals("", target_.determineResourcePath(servletContext, "",
                locale));

        assertEquals("/path/to/image_ja.jpg", target_.determineResourcePath(
                servletContext, "/path/to/image.jpg", locale));

        assertEquals("/path/to/image2.jpg", target_.determineResourcePath(
                servletContext, "/path/to/image2.jpg", locale));

        assertEquals("/path/t.o/image3", target_.determineResourcePath(
                servletContext, "/path/t.o/image3", locale));
    }
}
