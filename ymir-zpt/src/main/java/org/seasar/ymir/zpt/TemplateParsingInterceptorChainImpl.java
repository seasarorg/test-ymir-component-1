package org.seasar.ymir.zpt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.TagElement;

public class TemplateParsingInterceptorChainImpl implements
        TemplateParsingInterceptorChain {
    private TemplateParsingInterceptor templateParsingInterceptor_;

    private TemplateParsingInterceptorChain chain_;

    private Pattern[] tagPatterns_;

    private Pattern[] attrPatterns_;

    public TemplateParsingInterceptorChainImpl(
            TemplateParsingInterceptor templateParsingInterceptor,
            TemplateParsingInterceptorChain chain) {
        templateParsingInterceptor_ = templateParsingInterceptor;
        chain_ = chain;

        tagPatterns_ = compilePatterns(templateParsingInterceptor
                .getSpecialTagPatternStrings());
        attrPatterns_ = compilePatterns(templateParsingInterceptor
                .getSpecialAttributePatternStrings());

    }

    Pattern[] compilePatterns(String[] patternStrings) {
        if (patternStrings == null) {
            return new Pattern[0];
        }

        Pattern[] patterns = new Pattern[patternStrings.length];
        for (int i = 0; i < patternStrings.length; i++) {
            try {
                patterns[i] = Pattern.compile(patternStrings[i]);
            } catch (PatternSyntaxException ex) {
                throw (IllegalArgumentException) new IllegalArgumentException(
                        "Syntax error: " + patternStrings[i]).initCause(ex);
            }
        }
        return patterns;
    }

    boolean isSpecialTag(String tagName) {
        for (int i = 0; i < tagPatterns_.length; i++) {
            Matcher m = tagPatterns_[i].matcher(tagName);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    boolean isSpecialAttribute(String attrName) {
        for (int i = 0; i < attrPatterns_.length; i++) {
            Matcher m = attrPatterns_[i].matcher(attrName);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    public Element[] tagElementCreated(TagElement tagElement) {
        if (shouldProcess(tagElement)) {
            return templateParsingInterceptor_.tagElementCreated(tagElement,
                    chain_);
        } else {
            return chain_.tagElementCreated(tagElement);
        }
    }

    boolean shouldProcess(TagElement tagElement) {
        if (isSpecialTag(tagElement.getName())) {
            return true;
        }
        Attribute[] attributes = tagElement.getAttributes();
        for (int i = 0; i < attributes.length; i++) {
            if (isSpecialAttribute(attributes[i].getName())) {
                return true;
            }
        }
        return false;
    }
}
