package org.seasar.ymir.impl;

import java.util.Locale;

import junit.framework.TestCase;

import org.seasar.ymir.Messages;
import org.seasar.ymir.Note;

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
        MessagesImpl messages = new MessagesImpl() {
            @Override
            String getPageName() {
                return "page";
            }

            @Override
            void updateMessages() {
            }
        };
        messages
                .addPath("org/seasar/ymir/impl/NoteRendererImplTest.xproperties");
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
}
