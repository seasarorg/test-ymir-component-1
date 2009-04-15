package org.seasar.ymir.zpt.util;

import org.seasar.ymir.zpt.util.Parameters.Parameter;

import junit.framework.TestCase;

public class ParametersTest extends TestCase {
    public void test1() throws Exception {
        Parameter[] actual = new Parameters("$a!$b").getParameters();

        int idx = 0;
        assertEquals("$a", actual[idx++].getExpression());
        assertEquals("$b", actual[idx++].getExpression());
    }

    public void test2() throws Exception {
        Parameter[] actual = new Parameters("$a!!$b").getParameters();

        int idx = 0;
        assertEquals("$a!$b", actual[idx++].getExpression());
    }
}
