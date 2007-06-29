package org.seasar.ymir.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Notes;
import org.seasar.ymir.PathMappingProvider;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.constraint.NamedConstraint;
import org.seasar.ymir.constraint.impl.FufuConstraint;
import org.seasar.ymir.constraint.impl.FugaConstraint;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.response.ForwardResponse;

public class DefaultRequestProcessorTest extends S2TestCase {

    private DefaultRequestProcessor target_;

    private PathMappingProvider pathMappingProvider_;

    protected void setUpContainer() throws Throwable {
        setServletContext(new MockServletContextImpl("test") {
            private static final long serialVersionUID = 1L;

            public Set getResourcePaths(String path) {
                if ("/path/to/".equals(path)) {
                    return new HashSet<String>(Arrays.asList(new String[] {
                        "/path/to/file", "/path/to/dir/" }));
                } else if ("/".equals(path)) {
                    return new HashSet<String>(Arrays.asList(new String[] {
                        "/file", "/dir/" }));
                } else {
                    return null;
                }
            }
        });
        super.setUpContainer();
    }

    protected void setUp() throws Exception {
        super.setUp();

        include(getClass().getName().replace('.', '/') + ".dicon");

        target_ = (DefaultRequestProcessor) getComponent(DefaultRequestProcessor.class);
        pathMappingProvider_ = (PathMappingProvider) getComponent(PathMappingProvider.class);

        ApplicationManager applicationManager = (ApplicationManager) getComponent(ApplicationManager.class);
        Application application = new MockApplication() {
            public PathMappingProvider getPathMappingProvider() {
                return pathMappingProvider_;
            }

            @Override
            public S2Container getS2Container() {
                return getContainer();
            }
        };
        applicationManager.setBaseApplication(application);

        getContainer().init();
    }

    public void testNormlizeResponse() throws Exception {

        Request request = new MockRequest() {
            public String getPath() {
                return "/article/update.zpt";
            }

            public String getComponentName() {
                return "articlePage";
            }
        };
        Response response = new ForwardResponse("/article/update.zpt");

        Response actual = target_
                .normalizeResponse(response, request.getPath());

        assertEquals("リクエストパスと同じパスへのフォワードはPASSTHROUGHに正規化されること",
                ResponseType.PASSTHROUGH, actual.getType());
    }

    public void testFileResourceExists() throws Exception {

        assertTrue(target_.fileResourceExists("/file"));
        assertFalse(target_.fileResourceExists("/nonexistentfile"));
        assertFalse(target_.fileResourceExists("/dir"));

        assertTrue(target_.fileResourceExists("/path/to/file"));
        assertFalse(target_.fileResourceExists("/path/to/nonexistentfile"));
        assertFalse(target_.fileResourceExists("/path/to/dir"));

        assertFalse("親ディレクトリが存在しない場合もfalseを返すこと", target_
                .fileResourceExists("/nonexistentdir/file"));
    }

    private Comparator<ConstraintBag> COMPARATOR = new Comparator<ConstraintBag>() {
        public int compare(ConstraintBag o1, ConstraintBag o2) {
            return ((NamedConstraint<?>) o1.getConstraint())
                    .getName()
                    .compareTo(
                            ((NamedConstraint<?>) o2.getConstraint()).getName());
        }
    };

    public void testGetConstraint() throws Exception {

        List<ConstraintBag> actual = new ArrayList<ConstraintBag>();
        target_.getConstraint(Hoe.class, actual,
                DefaultRequestProcessor.EMPTY_SUPPRESSTYPESET);
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
