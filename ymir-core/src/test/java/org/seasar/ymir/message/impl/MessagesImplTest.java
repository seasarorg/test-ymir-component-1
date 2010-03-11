package org.seasar.ymir.message.impl;

import java.util.Locale;

import junit.framework.TestCase;

import org.seasar.ymir.Ymir;
import org.seasar.ymir.locale.mock.MockLocaleManager;
import org.seasar.ymir.message.MessageProvider;
import org.seasar.ymir.mock.MockYmir;

public class MessagesImplTest extends TestCase {
    public void testOneResource() throws Exception {
        // ## Arrange ##
        final String path = MessagesImplTest.class.getName().replace('.', '/')
                + "-testOneResource1.xproperties";

        final MockLocaleManager localeManager = new MockLocaleManager();
        localeManager.setLocale(Locale.JAPAN);

        final MessagesImpl message = new MessagesImpl() {
            @Override
            String[] getPageNames() {
                return new String[] { "no_page" };
            }

            @Override
            protected Ymir getYmir() {
                return new MockYmir();
            }
        };
        message.setLocaleManager(localeManager);
        message.setMessageProviders(new MessageProvider[] { message });
        message.addPath(path);
        message.init();

        // ## Act ##
        // ## Assert ##
        assertEquals("1a", message.getMessage("a"));
        assertEquals("1b", message.getMessage("b"));
    }

    public void testMultiLocale() throws Exception {
        // ## Arrange ##
        final String path = MessagesImplTest.class.getName().replace('.', '/')
                + "-testMultiLocale.xproperties";

        final MockLocaleManager localeManager = new MockLocaleManager();

        final MessagesImpl message = new MessagesImpl() {
            @Override
            String[] getPageNames() {
                return new String[] { "no_page" };
            }

            @Override
            protected Ymir getYmir() {
                return new MockYmir();
            }
        };
        message.setLocaleManager(localeManager);
        message.setMessageProviders(new MessageProvider[] { message });
        message.addPath(path);
        message.init();

        // ## Act ##
        // ## Assert ##

        localeManager.setLocale(Locale.JAPAN);
        assertEquals("aaa_ja_JP", message.getMessage("a"));

        localeManager.setLocale(Locale.JAPANESE);
        assertEquals("aaa_ja", message.getMessage("a"));

        localeManager.setLocale(Locale.ENGLISH);
        assertEquals("aaa_en", message.getMessage("a"));

        localeManager.setLocale(Locale.ITALIAN);
        assertEquals("aaa", message.getMessage("a"));
    }

    public void testMultiResource() throws Exception {
        // ## Arrange ##
        final String path1 = MessagesImplTest.class.getName().replace('.', '/')
                + "-testMultiResource1.xproperties";
        final String path2 = MessagesImplTest.class.getName().replace('.', '/')
                + "-testMultiResource2.xproperties";
        final String path3 = MessagesImplTest.class.getName().replace('.', '/')
                + "-testMultiResource3.xproperties";

        final MockLocaleManager localeManager = new MockLocaleManager();
        localeManager.setLocale(Locale.JAPAN);

        /*
         * path1の方が親になる
         */
        final MessagesImpl message = new MessagesImpl() {
            @Override
            String[] getPageNames() {
                return new String[] { "no_page" };
            }

            @Override
            protected Ymir getYmir() {
                return new MockYmir();
            }
        };
        message.setLocaleManager(localeManager);
        message.setMessageProviders(new MessageProvider[] { message });
        message.addPath(path1);
        message.addPath(path2);
        message.addPath(path3);
        message.init();

        // ## Act ##
        // ## Assert ##
        assertEquals("a3", message.getMessage("a"));
        assertEquals("b2", message.getMessage("b"));
        assertEquals("c1", message.getMessage("c"));
        assertEquals("d3", message.getMessage("d"));
    }

    public void test1_埋め込み値の解釈が行なわれること() throws Exception {
        // ## Arrange ##
        final String path = MessagesImplTest.class.getName().replace('.', '/')
                + "-test1.xproperties";

        final MockLocaleManager localeManager = new MockLocaleManager();
        localeManager.setLocale(Locale.JAPAN);

        final MessagesImpl message = new MessagesImpl() {
            @Override
            String[] getPageNames() {
                return new String[] { "no_page" };
            }

            @Override
            protected Ymir getYmir() {
                return new MockYmir();
            }
        };
        message.setLocaleManager(localeManager);
        message.setMessageProviders(new MessageProvider[] { message });
        message.addPath(path);
        message.init();

        // ## Act ##
        // ## Assert ##
        assertEquals("埋め込み値の解釈が行なわれること", "ABC", message.getMessage("a"));

        assertEquals("埋め込み値に対応する値がない場合は空文字列として扱われること", "AC", message
                .getMessage("c"));

        assertEquals("埋め込み値の解釈が再帰的に行なわれること", "DEF", message.getMessage("e"));
    }

    public void test_getPageNameCandidates() throws Exception {
        MessagesImpl target = new MessagesImpl();

        String[] actual = target.getPageNameCandidates(null);
        assertEquals(0, actual.length);

        actual = target.getPageNameCandidates(new String[] { "" });
        assertEquals(0, actual.length);

        actual = target.getPageNameCandidates(new String[] { "_" });
        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("_", actual[idx++]);

        actual = target.getPageNameCandidates(new String[] { "a" });
        assertEquals(1, actual.length);
        idx = 0;
        assertEquals("a", actual[idx++]);

        actual = target.getPageNameCandidates(new String[] { "_RootPage" });
        assertEquals(2, actual.length);
        idx = 0;
        assertEquals("_RootPage", actual[idx++]);
        assertEquals("_Root", actual[idx++]);

        actual = target.getPageNameCandidates(new String[] { "pkg__RootPage" });
        assertEquals(3, actual.length);
        idx = 0;
        assertEquals("pkg__RootPage", actual[idx++]);
        assertEquals("pkg__Root", actual[idx++]);
        assertEquals("pkg", actual[idx++]);

        actual = target.getPageNameCandidates(new String[] { "indexPage" });
        assertEquals(2, actual.length);
        idx = 0;
        assertEquals("indexPage", actual[idx++]);
        assertEquals("index", actual[idx++]);

        actual = target.getPageNameCandidates(new String[] { "pkg_indexPage" });
        assertEquals(3, actual.length);
        idx = 0;
        assertEquals("pkg_indexPage", actual[idx++]);
        assertEquals("pkg_index", actual[idx++]);
        assertEquals("pkg", actual[idx++]);

        actual = target.getPageNameCandidates(new String[] {
            "pkg_sub_forwardPage", "pkg_sub_indexPage" });
        assertEquals(6, actual.length);
        idx = 0;
        assertEquals("pkg_sub_forwardPage", actual[idx++]);
        assertEquals("pkg_sub_forward", actual[idx++]);
        assertEquals("pkg_sub", actual[idx++]);
        assertEquals("pkg", actual[idx++]);
        assertEquals("pkg_sub_indexPage", actual[idx++]);
        assertEquals("pkg_sub_index", actual[idx++]);
    }

    public void testMessageProvider() throws Exception {
        // ## Arrange ##
        final String path = MessagesImplTest.class.getName().replace('.', '/')
                + "-testMessageProvider.xproperties";

        final MockLocaleManager localeManager = new MockLocaleManager();
        localeManager.setLocale(Locale.JAPAN);

        final String secondPath = MessagesImplTest.class.getName().replace('.',
                '/')
                + "-testMessageProvider-second.xproperties";

        final MessageProviderImpl secondMessageProvider = new MessageProviderImpl();
        secondMessageProvider.addPath(secondPath);
        secondMessageProvider.init();

        final MessagesImpl message = new MessagesImpl() {
            @Override
            String[] getPageNames() {
                return new String[] { "no_page" };
            }

            @Override
            protected Ymir getYmir() {
                return new MockYmir();
            }
        };
        message.setLocaleManager(localeManager);
        message.setMessageProviders(new MessageProvider[] { message,
            secondMessageProvider });
        message.addPath(path);
        message.init();

        // ## Act ##
        // ## Assert ##
        assertEquals("1a", message.getMessage("a"));
        assertEquals("1c_ja", message.getMessage("c"));
        assertEquals("1c", message.getProperty("c"));
    }
}
