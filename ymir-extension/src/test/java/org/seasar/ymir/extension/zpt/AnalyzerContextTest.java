package org.seasar.ymir.extension.zpt;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;
import org.seasar.ymir.extension.creator.mock.MockSourceCreator;

public class AnalyzerContextTest extends TestCase {
    private AnalyzerContext target_ = new AnalyzerContext();

    public void testGetDtoClassName() throws Exception {
        target_.setSourceCreator(new MockSourceCreator() {
            @Override
            public String getRootPackageName() {
                return "com.example";
            }

            @Override
            public String getDtoPackageName() {
                return getRootPackageName() + ".dto";
            }
        });

        assertEquals("com.example.dto.sub.NameDto", target_.getDtoClassName(
                new SimpleClassDesc("com.example.web.sub.SubPage"), "name"));

        assertEquals("net.kankeinai.package.dto.NameDto", target_
                .getDtoClassName(new SimpleClassDesc(
                        "net.kankeinai.package.SubPage"), "name"));
    }

    public void testFindRenderClassName() throws Exception {
        assertEquals("org.seasar.ymir.Note", target_
                .findRenderClassName("note"));
        assertEquals("org.seasar.ymir.Notes", target_
                .findRenderClassName("notes"));
        assertEquals("net.skirnir.freyja.render.html.InputTag", target_
                .findRenderClassName("input"));
        assertEquals("net.skirnir.freyja.render.html.InputTag", target_
                .findRenderClassName("inputTag"));
        assertNull(target_.findRenderClassName("theInput"));
        assertEquals("net.skirnir.freyja.render.html.InputTag", target_
                .findRenderClassName("theInputTag"));
        assertNull(target_.findRenderClassName("radioInput"));
        assertEquals("net.skirnir.freyja.render.html.RadioInputTags", target_
                .findRenderClassName("radioInputTags"));
        assertNull(target_.findRenderClassName("theRadioInput"));
        assertEquals("net.skirnir.freyja.render.html.RadioInputTags", target_
                .findRenderClassName("theRadioInputTags"));
    }
}
