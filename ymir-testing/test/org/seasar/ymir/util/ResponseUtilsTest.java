package org.seasar.ymir.util;

import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseHeader;
import org.seasar.ymir.mock.MockResponse;

import junit.framework.TestCase;

public class ResponseUtilsTest extends TestCase {
    public void testSetNoCache() throws Exception {
        Response response = new MockResponse();

        ResponseUtils.setNoCache(response);

        ResponseHeader[] headers = response.getResponseHeaders();
        assertEquals(3, headers.length);
        int idx = 0;
        assertEquals("Pragma", headers[idx].getName());
        assertEquals("No-cache", headers[idx].getValue());
        assertFalse(headers[idx].isAdd());
        idx++;
        assertEquals("Cache-Control", headers[idx].getName());
        assertEquals("no-cache,no-store,must-revalidate", headers[idx]
                .getValue());
        assertFalse(headers[idx].isAdd());
        idx++;
        assertEquals("Expires", headers[idx].getName());
        assertEquals(Long.valueOf(1), headers[idx].getValue());
        assertFalse(headers[idx].isAdd());
    }
}
