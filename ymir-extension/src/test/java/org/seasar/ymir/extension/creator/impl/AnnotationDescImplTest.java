package org.seasar.ymir.extension.creator.impl;

import java.lang.reflect.Method;
import java.util.TreeSet;

import junit.framework.TestCase;

public class AnnotationDescImplTest extends TestCase {
    private void assertAnalyzedString(String methodName, String expected)
            throws Exception {
        Method method = Hoe.class.getMethod(methodName, new Class[0]);
        assertEquals(expected, new AnnotationDescImpl(
                method.getAnnotations()[0]).getAsString());
    }

    public void testAnalyze1() throws Exception {
        assertAnalyzedString("method1",
                "@org.seasar.ymir.extension.creator.impl.Annotation3");
    }

    public void testAnalyze2() throws Exception {
        assertAnalyzedString("method2",
                "@org.seasar.ymir.extension.creator.impl.Annotation4(\"\")");
    }

    public void testAnalyze3() throws Exception {
        assertAnalyzedString("method3",
                "@org.seasar.ymir.extension.creator.impl.Annotation4(\"\")");
    }

    public void testAnalyze4() throws Exception {
        assertAnalyzedString("method4",
                "@org.seasar.ymir.extension.creator.impl.Annotation4({\"\", \"\"})");
    }

    public void testAnalyze5() throws Exception {
        assertAnalyzedString(
                "method5",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = false, byteValue = 0, charValue = \'\\0\', classValue = java.lang.Object.class, doubleValue = 0.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE1, floatValue = 0.0, intValue = 1, longValue = 0, shortValue = 0)");
    }

    public void testAnalyze6() throws Exception {
        assertAnalyzedString(
                "method6",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = false, byteValue = 1, charValue = \'\\0\', classValue = java.lang.Object.class, doubleValue = 0.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE1, floatValue = 0.0, intValue = 0, longValue = 0, shortValue = 0)");
    }

    public void testAnalyze7() throws Exception {
        assertAnalyzedString(
                "method7",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = false, byteValue = 0, charValue = \'\\u0001\', classValue = java.lang.Object.class, doubleValue = 0.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE1, floatValue = 0.0, intValue = 0, longValue = 0, shortValue = 0)");
    }

    public void testAnalyze8() throws Exception {
        assertAnalyzedString(
                "method8",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = false, byteValue = 0, charValue = \'\\0\', classValue = java.lang.Object.class, doubleValue = 0.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE1, floatValue = 0.0, intValue = 0, longValue = 0, shortValue = 1)");
    }

    public void testAnalyze9() throws Exception {
        assertAnalyzedString(
                "method9",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = false, byteValue = 0, charValue = \'\\0\', classValue = java.lang.Object.class, doubleValue = 0.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE1, floatValue = 0.0, intValue = 0, longValue = 1, shortValue = 0)");
    }

    public void testAnalyze10() throws Exception {
        assertAnalyzedString(
                "method10",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = false, byteValue = 0, charValue = \'\\0\', classValue = java.lang.Object.class, doubleValue = 0.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE1, floatValue = 1.0, intValue = 0, longValue = 0, shortValue = 0)");
    }

    public void testAnalyze11() throws Exception {
        assertAnalyzedString(
                "method11",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = false, byteValue = 0, charValue = \'\\0\', classValue = java.lang.Object.class, doubleValue = 1.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE1, floatValue = 0.0, intValue = 0, longValue = 0, shortValue = 0)");
    }

    public void testAnalyze12() throws Exception {
        assertAnalyzedString(
                "method12",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = true, byteValue = 0, charValue = \'\\0\', classValue = java.lang.Object.class, doubleValue = 0.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE1, floatValue = 0.0, intValue = 0, longValue = 0, shortValue = 0)");
    }

    public void testAnalyze13() throws Exception {
        assertAnalyzedString(
                "method13",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = false, byteValue = 0, charValue = \'\\0\', classValue = java.lang.Object.class, doubleValue = 0.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE2, floatValue = 0.0, intValue = 0, longValue = 0, shortValue = 0)");
    }

    public void testAnalyze14() throws Exception {
        assertAnalyzedString(
                "method14",
                "@org.seasar.ymir.extension.creator.impl.Annotation1("
                        + "booleanValue = false, byteValue = 0, charValue = \'\\0\', classValue = java.lang.String.class, doubleValue = 0.0, enumValue = org.seasar.ymir.extension.creator.impl.Enum1.VALUE1, floatValue = 0.0, intValue = 0, longValue = 0, shortValue = 0)");
    }

    public void testAnalyze15() throws Exception {
        assertAnalyzedString(
                "method15",
                "@org.seasar.ymir.extension.creator.impl.Annotation2("
                        + "annotation = @org.seasar.ymir.extension.creator.impl.Annotation3)");
    }

    public void test_RequestParameter() throws Exception {
        assertAnalyzedString("RequestParameter",
                "@org.seasar.ymir.scope.annotation.RequestParameter");
    }

    public void test_RequestParameter2() throws Exception {
        assertAnalyzedString("RequestParameter2",
                "@org.seasar.ymir.scope.annotation.RequestParameter(populateWhereNull = false)");
    }

    public void test_addDependingClassNamesTo() throws Exception {
        AnnotationDescImpl target = new AnnotationDescImpl("com.example.Hoe",
                "value");
        TreeSet<String> set = new TreeSet<String>();

        target.addDependingClassNamesTo(set);

        String[] actual = set.toArray(new String[0]);
        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("com.example.Hoe", actual[idx++]);
    }
}
