package org.seasar.ymir.extension.creator;

import static org.seasar.ymir.extension.creator.SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH;

import org.seasar.ymir.extension.creator.impl.SourceCreatorImplTestBase;

public class SourceCreatorSettingTest extends SourceCreatorImplTestBase {
    private SourceCreatorSetting setting_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setting_ = new SourceCreatorSetting(getSourceCreator());
        setting_.setProperty(APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "net.skirnir.freyja.render.*");
    }

    public void test_findDtoClassName() throws Exception {
        assertEquals("org.seasar.ymir.message.Note", setting_
                .findDtoClassName("note"));
        assertEquals("org.seasar.ymir.message.Notes", setting_
                .findDtoClassName("notes"));
        assertEquals("net.skirnir.freyja.render.html.Input", setting_
                .findDtoClassName("input"));
        assertEquals("net.skirnir.freyja.render.html.Input", setting_
                .findDtoClassName("theInput"));
        assertEquals("net.skirnir.freyja.render.html.Input", setting_
                .findDtoClassName("theInputs"));
        assertEquals("net.skirnir.freyja.render.html.Radio", setting_
                .findDtoClassName("radio"));
        assertEquals("net.skirnir.freyja.render.html.Radio", setting_
                .findDtoClassName("theRadio"));
        assertEquals("net.skirnir.freyja.render.html.Radio", setting_
                .findDtoClassName("theRadios"));
    }
}
