package org.seasar.ymir.constraint.impl;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.testing.PageTestCase;

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

    public void test_アクション制約とクラス制約と共通制約を返すこと・getterの制約も含むこと() throws Exception {
        process(ConstraintInterceptorTestPage.class, "button1");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(8, actual.getNotes().length);
        assertNotNull(actual.get("saru"));
        assertNotNull(actual.get("tora"));
        assertNotNull(actual.get("fugar"));
        assertNotNull(actual.get("fuga"));
        assertNotNull(actual.get("button1"));
        assertNotNull(actual.get("bundle"));
        assertNotNull(actual.get("validator1"));
        assertNotNull(actual.get("validator2"));
    }

    public void test_SuppressConstraintアノテーションが付与されている場合はクラス制約を含まないこと_old()
            throws Exception {
        process(ConstraintInterceptorTestPage.class, "button2");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(2, actual.getNotes().length);
        assertNotNull("メソッド制約は含むこと", actual.get("button2"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_SuppressConstraintアノテーションが付与されている場合はクラス制約を含まないこと()
            throws Exception {
        process(ConstraintInterceptorTestPage.class, "button22");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(2, actual.getNotes().length);
        assertNotNull("メソッド制約は含むこと", actual.get("button22"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_SuppressConstraintアノテーションがConstraintTypeつきで付与されている場合は指定されたクラス制約を含まないこと_old()
            throws Exception {
        process(ConstraintInterceptorTestPage.class, "button3");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(3, actual.getNotes().length);
        assertNotNull("指定されなかったクラス制約は含むこと", actual.get("tora"));
        assertNotNull("メソッド制約は含むこと", actual.get("button3"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_SuppressConstraintアノテーションがConstraintTypeつきで付与されている場合は指定されたクラス制約を含まないこと()
            throws Exception {
        process(ConstraintInterceptorTestPage.class, "button32");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(3, actual.getNotes().length);
        assertNotNull("指定されなかったクラス制約は含むこと", actual.get("tora"));
        assertNotNull("メソッド制約は含むこと", actual.get("button32"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_複数Constraintの一括指定系Constraintアノテーションが正しく解釈されること()
            throws Exception {
        process(ConstraintInterceptorTestPage.class, "button4");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(3, actual.getNotes().length);
        assertNotNull(actual.get("button4_1"));
        assertNotNull(actual.get("button4_2"));
        assertNotNull("ConstraintBundleで指定された制約は含むこと", actual.get("bundle"));
    }

    public void test_Validatorアノテーションがついているメソッドが呼び出されること() throws Exception {
        process(ConstraintInterceptorTestPage.class, "button5");

        MockHttpServletRequest request = getHttpServletRequest();
        assertEquals("validator3", request.getAttribute("validator3"));
        assertEquals("validator4", request.getAttribute("validator4"));
        assertEquals("validator32", request.getAttribute("validator32"));
        assertEquals("validator42", request.getAttribute("validator42"));
    }

    public void test_パラメータつきでアクションが呼ばれる時はValidatorアノテーションがついているアクションが引数を取るものであればパラメータが渡されること()
            throws Exception {
        process(ConstraintInterceptorTestPage.class, "button6[1][hoe]");

        MockHttpServletRequest request = getHttpServletRequest();
        assertEquals(Integer.valueOf(1), request.getAttribute("param1"));
        assertEquals("hoe", request.getAttribute("param2"));
        assertEquals(Integer.valueOf(1), request.getAttribute("param12"));
        assertEquals("hoe", request.getAttribute("param22"));
    }

    public void test_YMIR267_Validatorの引数が解決できること() throws Exception {
        getServletContext().setAttribute("app", "app");
        process(ConstraintInterceptorTestPage.class, "button7[1][hoe]");

        MockHttpServletRequest request = getHttpServletRequest();
        assertEquals("app", request.getAttribute("app"));
        assertSame(getHttpServletRequest(), request.getAttribute("request"));
        assertEquals(Integer.valueOf(1), request.getAttribute("param12"));
        assertEquals("hoe", request.getAttribute("param22"));
    }
}
