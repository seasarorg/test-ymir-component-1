package org.seasar.ymir.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.log.Logger;
import org.seasar.kvasir.util.collection.I18NProperties;
import org.seasar.kvasir.util.collection.I18NPropertiesBuilder;
import org.seasar.ymir.LocaleManager;
import org.seasar.ymir.Messages;
import org.seasar.ymir.Request;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.util.MessagesUtils;

public class MessagesImpl implements Messages {

    private final Logger logger_ = Logger.getLogger(getClass());

    private final List<String> path_ = new ArrayList<String>();

    private I18NProperties messages_;

    private LocaleManager localeManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setLocaleManager(final LocaleManager localeManager) {
        localeManager_ = localeManager;
    }

    public void addPath(final String path) {
        path_.add(path);
    }

    @InitMethod
    public void init() {
        if (path_.isEmpty()) {
            logger_.warn("no message path specified");
        }
        final I18NPropertiesBuilder propertiesBuilder = new I18NPropertiesBuilder();
        for (final String path : path_) {
            propertiesBuilder.addPath(path);
        }
        messages_ = propertiesBuilder.build();
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
