package org.seasar.ymir.extension.zpt;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;
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
        });
    }

    public void testGetDtoClassName() throws Exception {
        assertEquals("com.example.dto.sub.NameDto", target_.getDtoClassName(
                new SimpleClassDesc("com.example.web.sub.SubPage"), "name"));

        assertEquals("net.kankeinai.package.dto.NameDto", target_
                .getDtoClassName(new SimpleClassDesc(
                        "net.kankeinai.package.SubPage"), "name"));

        assertEquals("[#YMIR-202]既存クラスが上位にあればそれを返すこと",
                "com.example.dto.sub.Name2Dto", target_.getDtoClassName(
                        new SimpleClassDesc("com.example.web.sub.sub.SubPage"),
                        "name2"));
    }

    public void testFindRenderClassName() throws Exception {
        assertEquals("org.seasar.ymir.message.Note", target_
                .findRenderClassName("note"));
        assertEquals("org.seasar.ymir.message.Notes", target_
                .findRenderClassName("notes"));
        assertEquals("互換性のため", "net.skirnir.freyja.render.html.InputTag",
                target_.findRenderClassName("input"));
        assertEquals("net.skirnir.freyja.render.html.InputTag", target_
                .findRenderClassName("inputTag"));
        assertNull(target_.findRenderClassName("theInput"));
        assertEquals("net.skirnir.freyja.render.html.InputTag", target_
                .findRenderClassName("theInputTag"));
        assertEquals("net.skirnir.freyja.render.html.InputTag", target_
                .findRenderClassName("theInputTags"));
        assertNull(target_.findRenderClassName("radioInput"));
        assertEquals("net.skirnir.freyja.render.html.RadioInputTags", target_
                .findRenderClassName("radioInputTags"));
        assertNull(target_.findRenderClassName("theRadioInput"));
        assertEquals("net.skirnir.freyja.render.html.RadioInputTags", target_
                .findRenderClassName("theRadioInputTags"));
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
}
