package org.seasar.ymir.zpt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TemplateContext;

public class TagRenderingInterceptorChainImpl implements
        TagRenderingInterceptorChain {
    private TagRenderingInterceptor tagRenderingInterceptor_;

    private TagRenderingInterceptorChain chain_;

    private Pattern[] tagPatterns_;

    private Pattern[] attrPatterns_;

    public TagRenderingInterceptorChainImpl(
            TagRenderingInterceptor tagRenderingInterceptor,
            TagRenderingInterceptorChain chain) {
        tagRenderingInterceptor_ = tagRenderingInterceptor;
        chain_ = chain;

        tagPatterns_ = compilePatterns(tagRenderingInterceptor
                .getSpecialTagPatternStrings());
        attrPatterns_ = compilePatterns(tagRenderingInterceptor
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

    public String render(TemplateContext context, String name,
            Attribute[] attributes, String body) {
        if (shouldProcess(name, attributes)) {
            return tagRenderingInterceptor_.render(context, name, attributes,
                    body, chain_);
        } else {
            return chain_.render(context, name, attributes, body);
        }
    }

    boolean shouldProcess(String name, Attribute[] attributes) {
        if (isSpecialTag(name)) {
            return true;
        }
        for (int i = 0; i < attributes.length; i++) {
            if (isSpecialAttribute(attributes[i].getName())) {
                return true;
            }
        }
        return false;
    }
}
