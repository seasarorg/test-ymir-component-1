package org.seasar.ymir.extension.zpt;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;

public class AnalyzerContextTest extends TestCase {
    private AnalyzerContext target_ = new AnalyzerContext();

    public void testGetDtoClassName() throws Exception {
        target_.setSourceCreator(new SourceCreatorImpl() {
            @Override
            public String getRootPackageName() {
                return "com.example";
            }

            @Override
            public String getDtoPackageName() {
                return getRootPackageName() + ".dto";
            }

            @Override
            protected ClassLoader getClassLoader() {
                return getClass().getClassLoader();
            }
        });

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
}
