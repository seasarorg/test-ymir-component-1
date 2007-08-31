package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.Notes;
import org.seasar.ymir.PageTestCase;
import org.seasar.ymir.Request;

import com.example.web.ConstraintInterceptorTestPage;

public class ConstraintInterceptorTest extends
        PageTestCase<ConstraintInterceptorTestPage> {
    @Override
    protected Class<ConstraintInterceptorTestPage> getPageClass() {
        return ConstraintInterceptorTestPage.class;
    }

    public void test_アクション制約とクラス制約と共通制約を返すこと・ただしgetterの制約は含まないこと()
            throws Exception {
        Request request = prepareForPrecessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button1=");
        processRequest(request);
        Notes actual = getNotes(request);

        assertNotNull(actual);
        assertEquals(7, actual.getNotes().length);
        assertNotNull(actual.get("saru"));
        assertNotNull(actual.get("tora"));
        assertNotNull(actual.get("fuga"));
        assertNotNull(actual.get("button1"));
        assertNotNull(actual.get("bundle"));
        assertNotNull(actual.get("validator1"));
        assertNotNull(actual.get("validator2"));
    }

    public void test_SuppressConstraintアノテーションが付与されている場合はクラス制約を含まないこと()
            throws Exception {
        Request request = prepareForPrecessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button2=");
        processRequest(request);
        Notes actual = getNotes(request);

        assertNotNull(actual);
        assertEquals(2, actual.getNotes().length);
        assertNotNull(actual.get("button2"));
        assertNotNull(actual.get("bundle"));
    }

    public void test_SuppressConstraintアノテーションがConstraintTypeつきで付与されている場合は指定されたクラス制約を含まないこと()
            throws Exception {
        Request request = prepareForPrecessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button3=");
        processRequest(request);
        Notes actual = getNotes(request);

        assertNotNull(actual);
        assertEquals(3, actual.getNotes().length);
        assertNotNull(actual.get("tora"));
        assertNotNull(actual.get("button3"));
        assertNotNull(actual.get("bundle"));
    }

    public void test_複数Constraintの一括指定系Constraintアノテーションが正しく解釈されること()
            throws Exception {
        Request request = prepareForPrecessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button4=");
        processRequest(request);
        Notes actual = getNotes(request);

        assertNotNull(actual);
        assertEquals(3, actual.getNotes().length);
        assertNotNull(actual.get("button4_1"));
        assertNotNull(actual.get("button4_2"));
        assertNotNull(actual.get("bundle"));
    }

    public void test_Validatorアノテーションがついているメソッドが呼び出されること() throws Exception {
        Request request = prepareForPrecessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button5=");
        processRequest(request);

        assertEquals("validator3", request.getAttribute("validator3"));
        assertEquals("validator4", request.getAttribute("validator4"));
    }

    public void test_パラメータつきでアクションが呼ばれる時はValidatorアノテーションがついているアクションが引数を取るものであればパラメータが渡されること()
            throws Exception {
        Request request = prepareForPrecessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button6[1][hoe]=");
        processRequest(request);

        assertEquals(Integer.valueOf(1), request.getAttribute("param1"));
        assertEquals("hoe", request.getAttribute("param2"));
    }
}
