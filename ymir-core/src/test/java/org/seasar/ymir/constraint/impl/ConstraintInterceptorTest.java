package org.seasar.ymir.constraint.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.S2Container;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Notes;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.NamedConstraint;
import org.seasar.ymir.impl.ActionImpl;
import org.seasar.ymir.impl.ConstraintBag;
import org.seasar.ymir.impl.Hoe;
import org.seasar.ymir.impl.Hoe7;
import org.seasar.ymir.impl.MethodInvokerImpl;
import org.seasar.ymir.impl.PageComponentImpl;
import org.seasar.ymir.impl.Test2Page;
import org.seasar.ymir.impl.Test3Page;
import org.seasar.ymir.impl.Test4Page;
import org.seasar.ymir.impl.TestPage;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockRequest;

public class ConstraintInterceptorTest extends S2TestCase {
    private ConstraintInterceptor target_;

    private Comparator<ConstraintBag> COMPARATOR = new Comparator<ConstraintBag>() {
        public int compare(ConstraintBag o1, ConstraintBag o2) {
            return ((NamedConstraint<?>) o1.getConstraint())
                    .getName()
                    .compareTo(
                            ((NamedConstraint<?>) o2.getConstraint()).getName());
        }
    };

    protected void setUp() throws Exception {
        super.setUp();

        include(getClass().getName().replace('.', '/') + ".dicon");
        getContainer().init();

        target_ = (ConstraintInterceptor) getComponent(ConstraintInterceptor.class);

        ApplicationManager applicationManager = (ApplicationManager) getComponent(ApplicationManager.class);
        Application application = new MockApplication() {
            @Override
            public S2Container getS2Container() {
                return getContainer();
            }
        };
        applicationManager.setBaseApplication(application);
    }

    public void testGetConstraint() throws Exception {

        List<ConstraintBag> actual = new ArrayList<ConstraintBag>();
        target_.getConstraintBag(Hoe.class, actual,
                ConstraintInterceptor.EMPTY_SUPPRESSTYPESET);
        for (Iterator<ConstraintBag> itr = actual.iterator(); itr.hasNext();) {
            ConstraintBag bag = itr.next();
            bag.confirm(null, null);
        }
        Collections.sort(actual, COMPARATOR);

        assertEquals(2, actual.size());
        assertEquals("saru", ((FugaConstraint) actual.get(0).getConstraint())
                .getName());
        assertEquals("tora", ((FufuConstraint) actual.get(1).getConstraint())
                .getName());
    }

    /*
     * アクションを指定しなかった場合は共通制約を返すこと。
     * ただしgetterの制約は含まないこと。
     */
    public void testGetConstraints2() throws Exception {

        ConstraintBag[] actual = target_.getConstraintBags(Hoe.class, null,
                target_.getSuppressTypeSet(null), true);
        for (int i = 0; i < actual.length; i++) {
            actual[i].confirm(null, null);
        }
        Arrays.sort(actual, COMPARATOR);

        assertEquals(3, actual.length);
        assertEquals("fuga", ((FugaConstraint) actual[0].getConstraint())
                .getName());
        assertEquals("saru", ((FugaConstraint) actual[1].getConstraint())
                .getName());
        assertEquals("tora", ((FufuConstraint) actual[2].getConstraint())
                .getName());
    }

    /*
     * アクションを指定した場合はアクションの制約と共通制約を返すこと。
     * ただしgetterの制約は含まないこと。
     */
    public void testGetConstraints4() throws Exception {

        Method action = Hoe.class.getMethod("_render", new Class[0]);
        ConstraintBag[] actual = target_.getConstraintBags(Hoe.class,
                new MethodInvokerImpl(action, new Object[0]), target_
                        .getSuppressTypeSet(action), true);
        for (int i = 0; i < actual.length; i++) {
            actual[i].confirm(null, null);
        }
        Arrays.sort(actual, COMPARATOR);

        assertEquals(4, actual.length);
        assertEquals("fuga", ((FugaConstraint) actual[0].getConstraint())
                .getName());
        assertEquals("render", ((FugaConstraint) actual[1].getConstraint())
                .getName());
        assertEquals("saru", ((FugaConstraint) actual[2].getConstraint())
                .getName());
        assertEquals("tora", ((FufuConstraint) actual[3].getConstraint())
                .getName());
    }

    /*
     * SuppressConstraintアノテーションが付与されている場合は共通制約を含まないこと。
     */
    public void testGetConstraints5() throws Exception {

        Method action = Hoe.class.getMethod("_get", new Class[0]);
        ConstraintBag[] actual = target_.getConstraintBags(Hoe.class,
                new MethodInvokerImpl(action, new Object[0]), target_
                        .getSuppressTypeSet(action), true);
        for (int i = 0; i < actual.length; i++) {
            actual[i].confirm(null, null);
        }
        Arrays.sort(actual, COMPARATOR);

        assertEquals(1, actual.length);
        assertEquals("get", ((FugaConstraint) actual[0].getConstraint())
                .getName());
    }

    /*
     * SuppressConstraintアノテーションがConstraintTypeつきで付与されている場合は、
     * 指定されたタイプの共通制約を含まないこと。
     */
    public void testGetConstraints6() throws Exception {

        Method action = Hoe.class.getMethod("_head", new Class[0]);
        ConstraintBag[] actual = target_.getConstraintBags(Hoe.class,
                new MethodInvokerImpl(action, new Object[0]), target_
                        .getSuppressTypeSet(action), true);
        for (int i = 0; i < actual.length; i++) {
            actual[i].confirm(null, null);
        }
        Arrays.sort(actual, COMPARATOR);

        assertEquals(2, actual.length);
        assertEquals("head", ((FugaConstraint) actual[0].getConstraint())
                .getName());
        assertEquals("tora", ((FufuConstraint) actual[1].getConstraint())
                .getName());
    }

    /*
     * 複数Constraintの一括指定系Constraintアノテーションが正しく解釈されること。
     */
    public void testGetConstraints7() throws Exception {

        Method action = Hoe7.class.getMethod("_get", new Class[0]);
        ConstraintBag[] actual = target_.getConstraintBags(Hoe7.class,
                new MethodInvokerImpl(action, new Object[0]), target_
                        .getSuppressTypeSet(action), true);
        for (int i = 0; i < actual.length; i++) {
            actual[i].confirm(null, null);
        }
        Arrays.sort(actual, COMPARATOR);

        assertEquals(3, actual.length);
        assertEquals("1", ((FugaConstraint) actual[0].getConstraint())
                .getName());
        assertEquals("2", ((FugaConstraint) actual[1].getConstraint())
                .getName());
        assertEquals("3", ((FugaConstraint) actual[2].getConstraint())
                .getName());
    }

    public void test_Validatorアノテーションがついているメソッドがバリデーション時に併せて呼ばれること()
            throws Exception {

        Object component = new TestPage();
        Request request = new MockRequest().setPath("/test.html")
                .setComponentName("TestPage");
        PageComponent pageComponent = new PageComponentImpl(component,
                TestPage.class);
        request.setPageComponent(pageComponent);

        Notes actual = target_.confirmConstraint(pageComponent, new ActionImpl(
                component, new MethodInvokerImpl(TestPage.class.getMethod(
                        "_get", new Class[0]), new Object[0])), request);

        assertNull("バリデーションに成功した場合はNotesがセットされないこと", actual);
    }

    public void test2_Validatorアノテーションがついているメソッドがバリデーション時に併せて呼ばれること2()
            throws Exception {

        Object component = new Test2Page();
        Request request = new MockRequest().setPath("/test2.html")
                .setComponentName("Test2Page");
        PageComponent pageComponent = new PageComponentImpl(component,
                Test2Page.class);
        request.setPageComponent(pageComponent);

        Notes actual = target_.confirmConstraint(pageComponent, new ActionImpl(
                component, new MethodInvokerImpl(Test2Page.class.getMethod(
                        "_get", new Class[0]), new Object[0])), request);

        assertNotNull("バリデーションに失敗した場合はNotesに適切な情報がセットされること", actual);
        assertEquals(2, actual.size());
        assertEquals(1, actual.getNotes("validate").length);
        assertEquals(1, actual.getNotes("validate2").length);
    }

    public void test3_Validatorアノテーションにアクション指定がついている時は指定されたアクションについてだけ呼び出されること()
            throws Exception {

        Object component = new Test3Page();
        Request request = new MockRequest().setPath("/test3.html")
                .setComponentName("Test3Page");
        PageComponent pageComponent = new PageComponentImpl(component,
                Test3Page.class);
        request.setPageComponent(pageComponent);

        Notes actual = target_.confirmConstraint(pageComponent, new ActionImpl(
                component, new MethodInvokerImpl(Test3Page.class.getMethod(
                        "_get", new Class[0]), new Object[0])), request);

        assertNull(actual);
    }

    public void test3_Validatorアノテーションにアクション指定がついている時は指定されたアクションについてだけ呼び出されること2()
            throws Exception {

        Object component = new Test3Page();
        Request request = new MockRequest().setPath("/test3.html")
                .setComponentName("Test3Page");
        PageComponent pageComponent = new PageComponentImpl(component,
                Test3Page.class);
        request.setPageComponent(pageComponent);

        Notes actual = target_.confirmConstraint(pageComponent, new ActionImpl(
                component, new MethodInvokerImpl(Test3Page.class.getMethod(
                        "_post", new Class[0]), new Object[0])), request);

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    public void test4_添え字つきでアクションが呼ばれる時はValidatorアノテーションがついているアクションが引数を取るものであれば添え字が渡されること()
            throws Exception {

        Test4Page component = new Test4Page();
        Request request = new MockRequest().setPath("/test4.html")
                .setComponentName("Test4Page");
        PageComponent pageComponent = new PageComponentImpl(component,
                Test4Page.class);
        request.setPageComponent(pageComponent);

        target_.confirmConstraint(pageComponent, new ActionImpl(component,
                new MethodInvokerImpl(Test4Page.class.getMethod("_post_button",
                        new Class[] { Integer.TYPE }), new Object[] { Integer
                        .valueOf(1) })), request);

        assertEquals(1, component.getIdx());
    }
}
