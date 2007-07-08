package org.seasar.ymir.interceptor.impl;

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
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.NamedConstraint;
import org.seasar.ymir.constraint.impl.FufuConstraint;
import org.seasar.ymir.constraint.impl.FugaConstraint;
import org.seasar.ymir.impl.ConstraintBag;
import org.seasar.ymir.impl.Hoe;
import org.seasar.ymir.impl.Test2Page;
import org.seasar.ymir.impl.TestPage;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockRequest;

public class CheckConstraintInterceptorTest extends S2TestCase {
    private CheckConstraintInterceptor target_;

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

        target_ = (CheckConstraintInterceptor) getComponent(CheckConstraintInterceptor.class);

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
        target_.getConstraint(Hoe.class, actual,
                CheckConstraintInterceptor.EMPTY_SUPPRESSTYPESET);
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

        Hoe hoe = new Hoe();
        ConstraintBag[] actual = target_.getConstraintBags(hoe, null);
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

        Hoe hoe = new Hoe();
        ConstraintBag[] actual = target_.getConstraintBags(hoe, Hoe.class
                .getMethod("_render", new Class[0]));
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

        Hoe hoe = new Hoe();
        ConstraintBag[] actual = target_.getConstraintBags(hoe, Hoe.class
                .getMethod("_get", new Class[0]));
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

        Hoe hoe = new Hoe();
        ConstraintBag[] actual = target_.getConstraintBags(hoe, Hoe.class
                .getMethod("_head", new Class[0]));
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

    public void test_Validatorアノテーションがついているメソッドがバリデーション時に併せて呼ばれること()
            throws Exception {

        Object component = new TestPage();
        Request request = new MockRequest().setPath("/test.html")
                .setComponentName("TestPage");

        Notes actual = target_.confirmConstraint(component, TestPage.class
                .getMethod("_get", new Class[0]), request);

        assertNull("バリデーションに成功した場合はNotesがセットされないこと", actual);
    }

    public void test2_Validatorアノテーションがついているメソッドがバリデーション時に併せて呼ばれること2()
            throws Exception {

        Object component = new Test2Page();
        Request request = new MockRequest().setPath("/test2.html")
                .setComponentName("Test2Page");

        Notes actual = target_.confirmConstraint(component, Test2Page.class
                .getMethod("_get", new Class[0]), request);

        assertNotNull("バリデーションに失敗した場合はNotesに適切な情報がセットされること", actual);
        assertEquals(2, actual.size());
        assertEquals(1, actual.getNotes("validate").length);
        assertEquals(1, actual.getNotes("validate2").length);
    }
}