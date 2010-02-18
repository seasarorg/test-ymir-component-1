package org.seasar.ymir.extension.creator;

import static org.seasar.ymir.extension.creator.SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH;

import org.seasar.ymir.extension.creator.impl.SourceCreatorImplTestBase;
import org.seasar.ymir.render.Selector;

public class SourceCreatorSettingTest extends SourceCreatorImplTestBase {
    private SourceCreatorSetting target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target_ = new SourceCreatorSetting(getSourceCreator());
        target_.setProperty(APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "net.skirnir.freyja.render.*");
    }

    public void test_findDtoClassName() throws Exception {
        assertEquals("org.seasar.ymir.message.Note", target_
                .findDtoClassName("note"));
        assertEquals("org.seasar.ymir.message.Notes", target_
                .findDtoClassName("notes"));
        assertEquals("net.skirnir.freyja.render.html.Input", target_
                .findDtoClassName("input"));
        assertEquals("net.skirnir.freyja.render.html.Input", target_
                .findDtoClassName("theInput"));
        assertEquals("net.skirnir.freyja.render.html.Input", target_
                .findDtoClassName("theInputs"));
        assertEquals("net.skirnir.freyja.render.html.Radio", target_
                .findDtoClassName("radio"));
        assertEquals("net.skirnir.freyja.render.html.Radio", target_
                .findDtoClassName("theRadio"));
        assertEquals("net.skirnir.freyja.render.html.Radio", target_
                .findDtoClassName("theRadios"));
    }

    public void test_isOnDtoSearchPath() throws Exception {
        target_.setProperty(APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");
        assertTrue(target_.isOnDtoSearchPath(Selector.class.getName()));
    }

    // XXX 本当はサブパッケージは対象外の方が分かりやすいが、S2のClassPatternを使っているためそれができない…。
    public void test_isOnDtoSearchPath_サブパッケージも対象() throws Exception {
        target_.setProperty(APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.*");
        assertTrue(target_.isOnDtoSearchPath(Selector.class.getName()));
    }

    public void test_getSuperclassName() throws Exception {
        target_.setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_CREATEBASECLASSES,
                String.valueOf(true));
        assertEquals("com.example.web.sub.PageBase", target_
                .getSuperclassName("com.example.web.sub.HoePage"));
        assertEquals("com.example.web.sub.PageBase", target_
                .getSuperclassName("com.example.web.sub.HoePageBase"));
        assertEquals("com.example.web.PageBase", target_
                .getSuperclassName("com.example.web.sub.PageBase"));
        assertNull(target_.getSuperclassName("com.example.web.PageBase"));
        assertNull(target_.getSuperclassName("com.example.web.sub.Hoehoe"));
        assertNull(target_.getSuperclassName("com.example.web.sub.HoehoeBase"));

        target_.setProperty(
                SourceCreatorSetting.APPKEYPREFIX_SOURCECREATOR_SUPERCLASS
                        + "com.example.web.sub.HoePage",
                "com.example.web.sub.PageParent");
        assertEquals("superclass指定がcreateBaseClassses指定よりも優先されること",
                "com.example.web.sub.PageParent", target_
                        .getSuperclassName("com.example.web.sub.HoePage"));
    }
}
