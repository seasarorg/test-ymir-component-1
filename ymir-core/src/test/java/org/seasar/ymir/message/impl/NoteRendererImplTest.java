package org.seasar.ymir.message.impl;

import java.util.Locale;

import junit.framework.TestCase;

import org.seasar.ymir.locale.mock.MockLocaleManager;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.Note;

public class NoteRendererImplTest extends TestCase {
    private NoteRendererImpl target_ = new NoteRendererImpl();

    private Messages japaneseMessages_;

    private Messages englishMessages_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        japaneseMessages_ = newMessages(Locale.JAPANESE);
        englishMessages_ = newMessages(Locale.ENGLISH);
    }

    private Messages newMessages(Locale locale) {
        return newMessages(locale, "");
    }

    private Messages newMessages(Locale locale, String suffix) {
        MessagesImpl messages = new MessagesImpl() {
            @Override
            String[] getPageNames() {
                return new String[] { "indexPage" };
            }

            @Override
            void updateMessages() {
            }
        };
        messages.addPath(NoteRendererImplTest.class.getName().replace('.', '/')
                + suffix + ".xproperties");
        MockLocaleManager localeManager = new MockLocaleManager();
        localeManager.setLocale(locale);
        messages.setLocaleManager(localeManager);
        messages.init();

        return messages;
    }

    public void test_単一プロパティ_en() throws Exception {
        assertEquals("Error: A is illegal.", target_.render(new Note(
                "template", new Object[] { "a" }), englishMessages_));
    }

    public void test_単一プロパティ_ja() throws Exception {
        assertEquals("エラー：エーが不正です。", target_.render(new Note("template",
                new Object[] { "a" }), japaneseMessages_));
    }

    public void test_複合プロパティ_en() throws Exception {
        assertEquals("Error: B of A is illegal.", target_.render(new Note(
                "template", new Object[] { "a.b" }), englishMessages_));
    }

    public void test_複合プロパティ_ja() throws Exception {
        assertEquals("エラー：エーのビーが不正です。", target_.render(new Note("template",
                new Object[] { "a.b" }), japaneseMessages_));
    }

    public void test_複合プロパティでセグメントが空文字列_en() throws Exception {
        assertEquals("Error: A is illegal.", target_.render(new Note(
                "template", new Object[] { "a.value" }), englishMessages_));
    }

    public void test_複合プロパティでセグメントが空文字列_ja() throws Exception {
        assertEquals("エラー：エーが不正です。", target_.render(new Note("template",
                new Object[] { "a.value" }), japaneseMessages_));
    }

    public void test_インデックスつき単一プロパティ_en() throws Exception {
        assertEquals("Error: 1th A is illegal.", target_.render(new Note(
                "template", new Object[] { "a[0]" }), englishMessages_));
    }

    public void test_インデックスつき単一プロパティ_ja() throws Exception {
        assertEquals("エラー：1番目のエーが不正です。", target_.render(new Note("template",
                new Object[] { "a[0]" }), japaneseMessages_));
    }

    public void test_インデックスつき複合プロパティ_en() throws Exception {
        assertEquals("Error: C of 2th B of 1th A is illegal.", target_.render(
                new Note("template", new Object[] { "a[0].b[1].c" }),
                englishMessages_));
    }

    public void test_インデックスつき複合プロパティ_ja() throws Exception {
        assertEquals("エラー：1番目のエーの2番目のビーのシーが不正です。", target_.render(new Note(
                "template", new Object[] { "a[0].b[1].c" }), japaneseMessages_));
    }

    public void test_パラメータ固有テンプレート_ja() throws Exception {
        assertEquals("エラー：1番目のエーの2番目のシーが間違っています。", target_.render(new Note(
                "template", new Object[] { "a[0].c[1]" }), japaneseMessages_));
    }

    public void testStripIndex() throws Exception {
        assertNull(target_.stripIndex(null));

        assertEquals("a.b.c", target_.stripIndex("a[10].b[2].c[3]"));
    }

    public void testGetLastSegment() throws Exception {
        assertNull(target_.getLastSegment(null));

        assertEquals("a", target_.getLastSegment("a"));

        assertEquals("c", target_.getLastSegment("a.b.c"));
    }

    public void test詳細表示用のテンプレートがない場合は簡易表示されること1() throws Exception {
        Messages messages = newMessages(Locale.JAPANESE, "Simple");
        assertEquals("label.a.b.cに対応するエントリが存在する場合", "エラー：エーのビーのシーが不正です。",
                target_.render(new Note("template",
                        new Object[] { "a[0].b[1].c" }), messages));
    }

    public void test詳細表示用のテンプレートがない場合は簡易表示されること2() throws Exception {
        Messages messages = newMessages(Locale.JAPANESE, "Simple");
        assertEquals("label.d.e.fに対応するエントリが存在しない場合", "エラー：エフが不正です。", target_
                .render(new Note("template", new Object[] { "d[0].e[1].f" }),
                        messages));
    }
}
