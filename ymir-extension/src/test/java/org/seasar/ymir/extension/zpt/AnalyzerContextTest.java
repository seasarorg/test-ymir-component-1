package org.seasar.ymir.extension.zpt;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;
import org.seasar.ymir.extension.creator.impl.TypeDescImpl;

public class AnalyzerContextTest extends TestCase {
    private AnalyzerContext target_;

    @Override
    protected void setUp() throws Exception {
        target_ = new AnalyzerContext();
        target_.setSourceCreator(new SourceCreatorImpl() {
            @Override
            public String getFirstRootPackageName() {
                return "com.example";
            }

            @Override
            public String[] getRootPackageNames() {
                return new String[] { getFirstRootPackageName() };
            }

            @Override
            public String getDtoPackageName() {
                return getFirstRootPackageName() + ".dto";
            }

            @Override
            protected ClassLoader getClassLoader() {
                return getClass().getClassLoader();
            }

            @Override
            public SourceCreatorSetting getSourceCreatorSetting() {
                return new SourceCreatorSetting(this) {
                    @Override
                    public String findDtoClassName(String propertyName) {
                        return null;
                    }
                };
            }
        });
    }

    public void test_inferPropertyClassName() throws Exception {
        target_.setPageClassName("com.example.web.IndexPage");

        assertEquals("com.example.dto.sub.NameDto", target_
                .inferPropertyClassName("name", "com.example.web.sub.SubPage"));

        assertEquals("com.example.dto.NameDto", target_.inferPropertyClassName(
                "name", "net.kankeinai.package.SubPage"));

        assertEquals("[#YMIR-202]既存クラスが上位にあればそれを返すこと",
                "com.example.dto.sub.Name2Dto", target_.inferPropertyClassName(
                        "name2", "com.example.web.sub.sub.SubPage"));
    }

    public void testReplaceSimpleDtoTypeToDefaultType1() throws Exception {
        TypeDesc typeDesc = new TypeDescImpl("com.example.dto.HoeDto");

        target_.replaceSimpleDtoTypeToDefaultType(typeDesc);

        assertEquals("String", typeDesc.getName());
    }

    public void testReplaceSimpleDtoTypeToDefaultType2() throws Exception {
        TypeDesc typeDesc = new TypeDescImpl(
                "java.util.List<com.example.dto.HoeDto>");

        target_.replaceSimpleDtoTypeToDefaultType(typeDesc);

        assertEquals("java.util.List<String>", typeDesc.getName());
    }

    public void testReplaceSimpleDtoTypeToDefaultType3() throws Exception {
        TypeDesc typeDesc = new TypeDescImpl("com.example.dto.HoeDto[]");

        target_.replaceSimpleDtoTypeToDefaultType(typeDesc);

        assertEquals("String[]", typeDesc.getName());
    }

    public void testReplaceSimpleDtoTypeToDefaultType4() throws Exception {
        TypeDesc typeDesc = new TypeDescImpl(
                "java.util.List<com.example.dto.HoeDto[]>[]");

        target_.replaceSimpleDtoTypeToDefaultType(typeDesc);

        assertEquals("java.util.List<String[]>[]", typeDesc.getName());
    }

    public void test_findPropertyClassName() throws Exception {
        target_.setPageClassName("com.example.web.sub.IndexPage");

        assertEquals("com.example.dto.EntryDto", target_.findPropertyClassName(
                "entry", "com.example.web.IndexPage"));

        assertEquals("com.example.dto.sub.EntryDto",
                target_.findPropertyClassName("entry",
                        "com.example.web.sub.IndexPage"));

        assertEquals("com.example.dto.sub.EntryDto", target_
                .findPropertyClassName("entry", null));

        assertEquals("com.example.dto.sub.EntryDto", target_
                .findPropertyClassName("entry",
                        "org.seasar.ymir.render.Selector"));
    }
}
