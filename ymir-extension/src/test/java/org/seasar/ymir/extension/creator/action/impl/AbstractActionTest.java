package org.seasar.ymir.extension.creator.action.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.action.impl.AbstractAction.Parameter;
import org.seasar.ymir.impl.RequestImpl;
import org.seasar.ymir.mock.MockDispatch;

public class AbstractActionTest extends TestCase {

    public void testGetParameters() throws Exception {
        RequestImpl request = new RequestImpl();
        Map<String, String[]> queryParameterMap = new LinkedHashMap<String, String[]>();
        queryParameterMap.put("test", new String[] { "value1", "value2" });
        queryParameterMap.put("__ymir__task", new String[] { "value" });
        queryParameterMap.put("__ymir__template", new String[] { "value" });
        queryParameterMap.put("test2", new String[] { "value3" });
        request.setQueryParameterMap(queryParameterMap);

        MockDispatch dispatch = new MockDispatch();
        dispatch.setQueryParameterMap(queryParameterMap);
        HashMap<String, String[]> pathParameterMap = new HashMap<String, String[]>();
        pathParameterMap.put("path", new String[] { "pathValue" });
        dispatch.setPathParameterMap(pathParameterMap);
        request.enterDispatch(dispatch);

        Parameter[] parameters = new AbstractAction(null) {
        }.getParameters(request);

        assertEquals("パスパラメータが含まれないこと", 3, parameters.length);
        assertEquals("test", parameters[0].getName());
        assertEquals("value1", parameters[0].getValue());
        assertEquals("test", parameters[1].getName());
        assertEquals("value2", parameters[1].getValue());
        assertEquals("test2", parameters[2].getName());
        assertEquals("value3", parameters[2].getValue());
    }
}
