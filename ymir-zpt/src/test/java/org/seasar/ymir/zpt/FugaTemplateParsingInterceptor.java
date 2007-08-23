package org.seasar.ymir.zpt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.skirnir.freyja.ConstantElement;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.TagElement;

public class FugaTemplateParsingInterceptor implements
        TemplateParsingInterceptor {
    private static final String[] SPECIALTAGPATTERNSTRINGS = new String[] { ".*" };

    public String[] getSpecialAttributePatternStrings() {
        return null;
    }

    public String[] getSpecialTagPatternStrings() {
        return SPECIALTAGPATTERNSTRINGS;
    }

    public Element[] tagElementCreated(TagElement tagElement,
            TemplateParsingInterceptorChain chain) {
        List<Element> elementList = new ArrayList<Element>();
        elementList.addAll(Arrays.asList(chain.tagElementCreated(tagElement)));
        elementList.add(new ConstantElement(((ConstantElement) tagElement
                .getBodyElements()[0]).getConstant()));
        return elementList.toArray(new Element[0]);
    }
}
