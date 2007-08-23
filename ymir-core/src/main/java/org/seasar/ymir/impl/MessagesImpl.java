package org.seasar.ymir.impl;

import java.util.Locale;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.kvasir.util.collection.I18NProperties;
import org.seasar.kvasir.util.io.Resource;
import org.seasar.kvasir.util.io.impl.JavaResource;
import org.seasar.ymir.LocaleManager;
import org.seasar.ymir.Messages;
import org.seasar.ymir.Request;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.util.MessagesUtils;

public class MessagesImpl implements Messages {

    private final String[] path_;

    private I18NProperties messages_;

    private LocaleManager localeManager_;

    public void setLocaleManager(final LocaleManager localeManager) {
        localeManager_ = localeManager;
    }

    public MessagesImpl(final String path) {
        this(new String[] { path });
    }

    public MessagesImpl(final String... path) {
        path_ = path;
        init();
    }

    void init() {
        for (final String path : path_) {
            final Resource resource = new JavaResource(path, Thread
                    .currentThread().getContextClassLoader());

            String name;
            final int slash = path.lastIndexOf('/');
            if (slash >= 0) {
                name = path.substring(slash + 1);
            } else {
                name = path;
            }
            String baseName;
            String suffix;
            final int dot = name.lastIndexOf('.');
            if (dot >= 0) {
                baseName = name.substring(0, dot);
                suffix = name.substring(dot);
            } else {
                baseName = name;
                suffix = "";
            }

            final I18NProperties properties = new I18NProperties(resource
                    .getParentResource(), baseName, suffix, messages_);
            messages_ = properties;
        }

    }

    public String getProperty(final String name) {
        updateMessages();
        return messages_.getProperty(name);
    }

    public String getProperty(final String name, final Locale locale) {
        updateMessages();
        return messages_.getProperty(name, locale);
    }

    public String getMessage(final String name) {
        final Locale locale = localeManager_.getLocale();
        final String pageSpecificName = MessagesUtils.getPageSpecificName(name,
                getPageName());

        String message = null;
        if (pageSpecificName != null) {
            message = getProperty(pageSpecificName, locale);
        }
        if (message == null) {
            message = getProperty(name, locale);
        }
        return message;
    }

    String getPageName() {
        try {
            return MessagesUtils.getPageName((Request) getYmir()
                    .getApplication().getS2Container().getComponent(
                            Request.class));
        } catch (final ComponentNotFoundRuntimeException ex) {
            return null;
        }
    }

    void updateMessages() {
        if (getYmir().isUnderDevelopment()) {
            init();
        }
    }

    protected Ymir getYmir() {
        return YmirContext.getYmir();
    }
}
