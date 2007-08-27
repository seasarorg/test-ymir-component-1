package org.seasar.ymir.impl;

import java.util.Locale;

import junit.framework.TestCase;

import org.seasar.ymir.Ymir;
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
            String getPageName() {
                return "no_page";
            }

            @Override
            protected Ymir getYmir() {
                return new MockYmir();
            }
        };
        message.setLocaleManager(localeManager);
        message.addPath(path);
        message.init();

        // ## Act ##
        // ## Assert ##
        assertEquals("1a", message.getMessage("a"));
        assertEquals("1b", message.getMessage("b"));
    }

    public void testNoResource() throws Exception {
        // ## Arrange ##
        final String path = MessagesImplTest.class.getName().replace('.', '/')
                + "-testNoResource.xproperties";

        final MockLocaleManager localeManager = new MockLocaleManager();
        localeManager.setLocale(Locale.JAPAN);

        final MessagesImpl message = new MessagesImpl() {
            @Override
            String getPageName() {
                return "no_page";
            }

            @Override
            protected Ymir getYmir() {
                return new MockYmir();
            }
        };
        message.setLocaleManager(localeManager);
        message.addPath(path);

        // ## Act ##
        // ## Assert ##
        try {
            message.init();
            fail();
        } catch (final IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void testMultiLocale() throws Exception {
        // ## Arrange ##
        final String path = MessagesImplTest.class.getName().replace('.', '/')
                + "-testMultiLocale.xproperties";

        final MockLocaleManager localeManager = new MockLocaleManager();

        final MessagesImpl message = new MessagesImpl() {
            @Override
            String getPageName() {
                return "no_page";
            }

            @Override
            protected Ymir getYmir() {
                return new MockYmir();
            }
        };
        message.setLocaleManager(localeManager);
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
            String getPageName() {
                return "no_page";
            }

            @Override
            protected Ymir getYmir() {
                return new MockYmir();
            }
        };
        message.setLocaleManager(localeManager);
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

}
