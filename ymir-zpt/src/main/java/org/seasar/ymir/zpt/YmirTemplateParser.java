package org.seasar.ymir.zpt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.YmirContext;

import net.skirnir.freyja.Element;
import net.skirnir.freyja.TagElement;
import net.skirnir.freyja.impl.TemplateParserImpl;

public class YmirTemplateParser extends TemplateParserImpl {
    private TemplateParsingInterceptorChain chain_;

    @Override
    public void setPatternStrings(String[] tagPatternStrings,
            String[] attrPatternStrings) {
        TemplateParsingInterceptor[] templateParsingInterceptors = gatherTemplateParsingInterceptors();

        Set<String> tagPatternStringSet = new HashSet<String>();
        Set<String> attributePatternStringSet = new HashSet<String>();
        if (tagPatternStrings != null) {
            tagPatternStringSet.addAll(Arrays.asList(tagPatternStrings));
        }
        if (attrPatternStrings != null) {
            attributePatternStringSet.addAll(Arrays.asList(attrPatternStrings));
        }
        for (int i = 0; i < templateParsingInterceptors.length; i++) {
            String[] strings = templateParsingInterceptors[i]
                    .getSpecialTagPatternStrings();
            if (strings != null) {
                tagPatternStringSet.addAll(Arrays.asList(strings));
            }
            strings = templateParsingInterceptors[i]
                    .getSpecialAttributePatternStrings();
            if (strings != null) {
                attributePatternStringSet.addAll(Arrays.asList(strings));
            }
        }

        super.setPatternStrings(tagPatternStringSet.toArray(new String[0]),
                attributePatternStringSet.toArray(new String[0]));

        chain_ = newChain(templateParsingInterceptors, 0);
    }

    private TemplateParsingInterceptorChain newChain(
            TemplateParsingInterceptor[] templateParsingInterceptors, int idx) {
        if (idx < templateParsingInterceptors.length) {
            return new TemplateParsingInterceptorChainImpl(
                    templateParsingInterceptors[idx], newChain(
                            templateParsingInterceptors, idx + 1));
        } else {
            return new DefaultAdapter();
        }
    }

    TemplateParsingInterceptor[] gatherTemplateParsingInterceptors() {
        return (TemplateParsingInterceptor[]) getS2Container()
                .findAllComponents(TemplateParsingInterceptor.class);
    }

    S2Container getS2Container() {
        return YmirContext.getYmir().getApplication().getS2Container();
    }

    @Override
    protected void addElement(TagInfo tagInfo, TagElement element) {
        Element[] elements = chain_.tagElementCreated(element);
        for (int i = 0; i < elements.length; i++) {
            tagInfo.addElement(elements[i]);
        }
    }

    class DefaultAdapter implements TemplateParsingInterceptorChain {
        public Element[] tagElementCreated(TagElement tagElement) {
            return new Element[] { tagElement };
        }
    }
}
