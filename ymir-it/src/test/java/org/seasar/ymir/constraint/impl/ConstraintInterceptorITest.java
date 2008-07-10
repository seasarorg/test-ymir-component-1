package org.seasar.ymir.constraint.impl;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.test.PageTestCase;

import com.example.web.ConstraintInterceptorTestPage;

public class ConstraintInterceptorITest extends
        PageTestCase<ConstraintInterceptorTestPage> {
    @Override
    protected Class<ConstraintInterceptorTestPage> getPageClass() {
        return ConstraintInterceptorTestPage.class;
    }

    public void test_app_diconに登録したConstraintBundleコンポーネントが正しく登録されること()
            throws Exception {
        ConstraintInterceptor actual = null;
        try {
            actual = getComponent(ConstraintInterceptor.class);
        } catch (ComponentNotFoundRuntimeException ex) {
            fail();
        }

        assertEquals(1, actual.getConstraintBagsFromConstraintBundles().length);
    }

    public void test_アクション制約とクラス制約と共通制約を返すこと・ただしgetterの制約は含まないこと()
            throws Exception {
        Request request = prepareForProcessing(
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

    public void test_SuppressConstraintアノテーションが付与されている場合はクラス制約を含まないこと_old()
            throws Exception {
        Request request = prepareForProcessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button2=");
        processRequest(request);
        Notes actual = getNotes(request);

        assertNotNull(actual);
        assertEquals(2, actual.getNotes().length);
        assertNotNull("メソッド制約は含むこと", actual.get("button2"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_SuppressConstraintアノテーションが付与されている場合はクラス制約を含まないこと()
            throws Exception {
        Request request = prepareForProcessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button22=");
        processRequest(request);
        Notes actual = getNotes(request);

        assertNotNull(actual);
        assertEquals(2, actual.getNotes().length);
        assertNotNull("メソッド制約は含むこと", actual.get("button22"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_SuppressConstraintアノテーションがConstraintTypeつきで付与されている場合は指定されたクラス制約を含まないこと_old()
            throws Exception {
        Request request = prepareForProcessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button3=");
        processRequest(request);
        Notes actual = getNotes(request);

        assertNotNull(actual);
        assertEquals(3, actual.getNotes().length);
        assertNotNull("指定されなかったクラス制約は含むこと", actual.get("tora"));
        assertNotNull("メソッド制約は含むこと", actual.get("button3"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_SuppressConstraintアノテーションがConstraintTypeつきで付与されている場合は指定されたクラス制約を含まないこと()
            throws Exception {
        Request request = prepareForProcessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button32=");
        processRequest(request);
        Notes actual = getNotes(request);

        assertNotNull(actual);
        assertEquals(3, actual.getNotes().length);
        assertNotNull("指定されなかったクラス制約は含むこと", actual.get("tora"));
        assertNotNull("メソッド制約は含むこと", actual.get("button32"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_複数Constraintの一括指定系Constraintアノテーションが正しく解釈されること()
            throws Exception {
        Request request = prepareForProcessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button4=");
        processRequest(request);
        Notes actual = getNotes(request);

        assertNotNull(actual);
        assertEquals(3, actual.getNotes().length);
        assertNotNull(actual.get("button4_1"));
        assertNotNull(actual.get("button4_2"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_Validatorアノテーションがついているメソッドが呼び出されること() throws Exception {
        Request request = prepareForProcessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button5=");
        processRequest(request);

        assertEquals("validator3", request.getAttribute("validator3"));
        assertEquals("validator4", request.getAttribute("validator4"));
        assertEquals("validator32", request.getAttribute("validator32"));
        assertEquals("validator42", request.getAttribute("validator42"));
    }

    public void test_パラメータつきでアクションが呼ばれる時はValidatorアノテーションがついているアクションが引数を取るものであればパラメータが渡されること()
            throws Exception {
        Request request = prepareForProcessing(
                "/constraintInterceptorTest.html", Request.METHOD_GET,
                "button6[1][hoe]=");
        processRequest(request);

        assertEquals(Integer.valueOf(1), request.getAttribute("param1"));
        assertEquals("hoe", request.getAttribute("param2"));
        assertEquals(Integer.valueOf(1), request.getAttribute("param12"));
        assertEquals("hoe", request.getAttribute("param22"));
    }
}
