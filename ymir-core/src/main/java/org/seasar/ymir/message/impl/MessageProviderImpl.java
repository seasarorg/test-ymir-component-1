package org.seasar.ymir.message.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.kvasir.util.collection.I18NProperties;
import org.seasar.kvasir.util.collection.I18NPropertiesBuilder;
import org.seasar.ymir.message.MessageProvider;

public class MessageProviderImpl implements MessageProvider {
    private final Log log_ = LogFactory.getLog(MessageProviderImpl.class);

    private final List<String> path_ = new ArrayList<String>();

    private I18NProperties messages_;

    public void addPath(final String path) {
        path_.add(path);
    }

    @InitMethod
    public void init() {
        if (path_.isEmpty()) {
            log_.warn("no message path specified");
        }
        final I18NPropertiesBuilder propertiesBuilder = new I18NPropertiesBuilder();
        for (final String path : path_) {
            propertiesBuilder.addPath(path);
        }
        messages_ = propertiesBuilder.build();
    }

    public String getMessageValue(String name, Locale locale) {
        return messages_.getProperty(name, locale);
    }

    public String getMessageValue(String name) {
        return messages_.getProperty(name);
    }
}
