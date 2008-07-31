package org.seasar.ymir.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;

public class RequestImplTest extends TestCase {
    private RequestImpl target_;

    private Map<String, String[]> queryParameterMap_;

    @Override
    protected void setUp() throws Exception {
        String path = "/index.html";
        String queryString = "param=value_q1&param=value_q2";
        String method = Request.METHOD_GET;

        queryParameterMap_ = new HashMap<String, String[]>();
        queryParameterMap_
                .put("param", new String[] { "value_q1", "value_q2" });

        target_ = new RequestImpl("/context", method, "UTF-8",
                queryParameterMap_, new HashMap<String, FormFile[]>(), null,
                Locale.JAPAN);

        PathMapping pathMapping = new PathMappingImpl("^/[a-zA-Z_]+.html$",
                "indexPage", "index", null, null, null,
                "param=value_u1;param=value_u2;param2=value2_u");

        target_.enterDispatch(new DispatchImpl(target_.getContextPath(), path,
                queryString, Dispatcher.REQUEST, new MatchedPathMappingImpl(
                        pathMapping, pathMapping.match(path, method))));
    }

    public void testGetParameter() throws Exception {
        assertEquals("クエリパラメータがない場合はURIパラメータが返されること", "value2_u", target_
                .getParameter("param2"));
    }

    public void testGetParameter_String_String() throws Exception {
        assertEquals("クエリパラメータがない場合はURIパラメータが返されること", "value2_u", target_
                .getParameter("param2", null));
    }

    public void testGetParameterValues() throws Exception {
        String[] actual = target_.getParameterValues("param");

        Arrays.sort(actual);
        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", 4, actual.length);
        int idx = 0;
        assertEquals("value_q1", actual[idx++]);
        assertEquals("value_q2", actual[idx++]);
        assertEquals("value_u1", actual[idx++]);
        assertEquals("value_u2", actual[idx++]);
    }

    public void testGetParameterValues_String_StringArray() throws Exception {
        String[] actual = target_.getParameterValues("param", null);

        Arrays.sort(actual);
        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", 4, actual.length);
        int idx = 0;
        assertEquals("value_q1", actual[idx++]);
        assertEquals("value_q2", actual[idx++]);
        assertEquals("value_u1", actual[idx++]);
        assertEquals("value_u2", actual[idx++]);
    }

    public void testGetParameterNames() throws Exception {
        List<String> nameList = new ArrayList<String>();
        for (Iterator<String> itr = target_.getParameterNames(); itr.hasNext();) {
            nameList.add(itr.next());
        }
        String[] actual = nameList.toArray(new String[0]);
        Arrays.sort(actual);

        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", 2, actual.length);
        int idx = 0;
        assertEquals("param", actual[idx++]);
        assertEquals("param2", actual[idx++]);
    }

    @SuppressWarnings("unchecked")
    public void testGetParameterMap() throws Exception {
        Map<String, String[]> actual = target_.getParameterMap();

        List<Map.Entry<String, String[]>> entryList = new ArrayList<Map.Entry<String, String[]>>();
        for (Iterator<Map.Entry<String, String[]>> itr = actual.entrySet()
                .iterator(); itr.hasNext();) {
            entryList.add(itr.next());
        }
        Map.Entry<String, String[]>[] entries = entryList
                .toArray(new Map.Entry[0]);
        Arrays.sort(entries, new Comparator<Map.Entry<String, String[]>>() {
            public int compare(Entry<String, String[]> o1,
                    Entry<String, String[]> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        int idx = 0;
        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", "param", entries[idx]
                .getKey());
        String[] values = entries[idx++].getValue();
        Arrays.sort(values);
        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", 4, values.length);
        int i = 0;
        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", "value_q1", values[i++]);
        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", "value_q2", values[i++]);
        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", "value_u1", values[i++]);
        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", "value_u2", values[i++]);

        assertEquals("クエリパラメータとURIパラメータの両方が取得できること", "param2", entries[idx]
                .getKey());
        values = entries[idx++].getValue();
        Arrays.sort(values);
        i = 0;
        assertEquals(1, values.length);
        assertEquals("value2_u", values[i++]);
    }

    public void testLeaveDispatch_リクエストパラメータからURIパラメータが取り除かれること()
            throws Exception {
        assertNotSame(queryParameterMap_, target_.getParameterMap());
        target_.leaveDispatch();
        assertSame(queryParameterMap_, target_.getParameterMap());
    }
}
