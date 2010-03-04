package org.seasar.ymir.message.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.kvasir.util.collection.I18NProperties;
import org.seasar.kvasir.util.collection.I18NPropertiesBuilder;
import org.seasar.kvasir.util.el.EvaluationException;
import org.seasar.kvasir.util.el.TextTemplateEvaluator;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.kvasir.util.el.impl.SimpleTextTemplateEvaluator;
import org.seasar.ymir.Request;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.locale.LocaleManager;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.util.MessagesUtils;

public class MessagesImpl implements Messages {
    private static final String SUFFIX_PAGE = "Page";

    private final Log log_ = LogFactory.getLog(MessagesImpl.class);

    private final List<String> path_ = new ArrayList<String>();

    private I18NProperties messages_;

    private TextTemplateEvaluator evaluator_ = new SimpleTextTemplateEvaluator();

    private VariableResolver variableResolverAdapter_ = new VariableResolver() {
        public Object getValue(Object key) {
            String value = getProperty0((String) key);
            if (value != null) {
                return value;
            } else {
                return "";
            }
        }
    };

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
            log_.warn("no message path specified");
        }
        final I18NPropertiesBuilder propertiesBuilder = new I18NPropertiesBuilder();
        for (final String path : path_) {
            propertiesBuilder.addPath(path);
        }
        messages_ = propertiesBuilder.build();
    }

    public String getProperty(final String name) {
        updateMessages();
        return getProperty0(name);
    }

    protected String getProperty0(final String name) {
        return resolveValue(messages_.getProperty(name));
    }

    protected String resolveValue(String value) {
        try {
            return evaluator_.evaluateAsString(value, variableResolverAdapter_);
        } catch (EvaluationException ex) {
            log_.warn("Can't evaluate: " + value, ex);
            return value;
        }
    }

    public String getProperty(final String name, final Locale locale) {
        updateMessages();
        return getProperty0(name, locale);
    }

    protected String getProperty0(final String name, final Locale locale) {
        return resolveValue(messages_.getProperty(name, locale), locale);
    }

    protected String resolveValue(String value, final Locale locale) {
        try {
            return evaluator_.evaluateAsString(value, new VariableResolver() {
                public Object getValue(Object key) {
                    String value = getProperty0((String) key, locale);
                    if (value != null) {
                        return value;
                    } else {
                        return "";
                    }
                }
            });
        } catch (EvaluationException ex) {
            log_.warn("Can't evaluate: " + value + ": locale=" + locale, ex);
            return value;
        }
    }

    public String getMessage(final String name) {
        updateMessages();
        final Locale locale = localeManager_.getLocale();
        for (String messageNameCnadidate : MessagesUtils
                .getMessageNameCandidates(name,
                        getPageNameCandidates(getPageNames()))) {
            String message = getProperty0(messageNameCnadidate, locale);
            if (message != null) {
                return message;
            }
        }
        return null;
    }

    String[] getPageNames() {
        try {
            return MessagesUtils.getPageNames((Request) getYmir()
                    .getApplication().getS2Container().getComponent(
                            Request.class));
        } catch (final ComponentNotFoundRuntimeException ex) {
            return null;
        }
    }

    protected String[] getPageNameCandidates(String[] pageNames) {
        Set<String> candidates = new LinkedHashSet<String>();
        if (pageNames != null) {
            for (String pageName : pageNames) {
                gatherPageNameCandidates(pageName, candidates);
            }
        }
        return candidates.toArray(new String[0]);
    }

    protected void gatherPageNameCandidates(String pageName,
            Set<String> candidates) {
        if (pageName != null && pageName.length() > 0) {
            candidates.add(pageName);
            if (pageName.endsWith(SUFFIX_PAGE)) {
                candidates.add(pageName.substring(0, pageName.length()
                        - SUFFIX_PAGE.length()));
            }

            for (int i = pageName.length() - 1; i >= 1; i--) {
                char ch = pageName.charAt(i);
                if (ch == '_') {
                    String name = pageName.substring(0, i);
                    if (!name.endsWith("_")) {
                        candidates.add(name);
                    }
                }
            }
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
