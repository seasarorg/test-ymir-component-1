package org.seasar.ymir.convention;

import junit.framework.TestCase;

public class YmirNamingConventionTest extends TestCase {
    private YmirNamingConvention target_ = new YmirNamingConvention();

    public void testAddIgnorePackageName() throws Exception {
        target_.addRootPackageName("com.example");

        target_.addIgnorePackageName(".ignore1, com.example2.ignore");

        String[] actual = target_.getIgnorePackageNames();
        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals("com.example.ignore1", actual[idx++]);
        assertEquals("com.example2.ignore", actual[idx++]);
    }

    public void testFromComponentNameToClassName() throws Exception {
        target_.addRootPackageName("com.example");

        assertEquals("com.example.web.RootPage", target_
                .fromComponentNameToClassName("rootPage"));
        assertEquals("com.example.web._RootPage", target_
                .fromComponentNameToClassName("_RootPage"));
        assertEquals("com.example.web.sub.RootPage", target_
                .fromComponentNameToClassName("sub_rootPage"));
        assertEquals("com.example.web.sub._RootPage", target_
                .fromComponentNameToClassName("sub__RootPage"));
    }

    public void testFromComponentNameToPartOfClassName() throws Exception {
        target_.addRootPackageName("com.example");

        assertEquals("RootPage", target_
                .fromComponentNameToPartOfClassName("rootPage"));
        assertEquals("_RootPage", target_
                .fromComponentNameToPartOfClassName("_RootPage"));
        assertEquals("sub.RootPage", target_
                .fromComponentNameToPartOfClassName("sub_rootPage"));
        assertEquals("sub._RootPage", target_
                .fromComponentNameToPartOfClassName("sub__RootPage"));
    }

    public void testFromClassNameToComponentName() throws Exception {
        target_.addRootPackageName("com.example");

        assertEquals("rootPage", target_
                .fromClassNameToComponentName("com.example.web.RootPage"));
        assertEquals("_RootPage", target_
                .fromClassNameToComponentName("com.example.web._RootPage"));
        assertEquals("sub_rootPage", target_
                .fromClassNameToComponentName("com.example.web.sub.RootPage"));
        assertEquals("sub__RootPage", target_
                .fromClassNameToComponentName("com.example.web.sub._RootPage"));
    }

    public void testFromClassNameToShortComponentName() throws Exception {
        target_.addRootPackageName("com.example");

        assertEquals("rootPage", target_
                .fromClassNameToShortComponentName("com.example.web.RootPage"));
        assertEquals("_RootPage", target_
                .fromClassNameToShortComponentName("com.example.web._RootPage"));
        assertEquals(
                "rootPage",
                target_
                        .fromClassNameToShortComponentName("com.example.web.sub.RootPage"));
        assertEquals(
                "_RootPage",
                target_
                        .fromClassNameToShortComponentName("com.example.web.sub._RootPage"));
    }

    public void testSplit1() throws Exception {
        String[] actual = target_.split("a");

        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("a", actual[idx++]);
    }

    public void testSplit2() throws Exception {
        String[] actual = target_.split("a_b");

        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals("a", actual[idx++]);
        assertEquals("b", actual[idx++]);
    }

    public void testSplit3() throws Exception {
        String[] actual = target_.split("_a");

        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("_a", actual[idx++]);
    }

    public void testSplit4() throws Exception {
        String[] actual = target_.split("a__b");

        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals("a", actual[idx++]);
        assertEquals("_b", actual[idx++]);
    }

    public void testSplit5() throws Exception {
        String[] actual = target_.split("a_");

        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("a", actual[idx++]);
    }
}
