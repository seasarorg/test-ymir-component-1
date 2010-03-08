package org.seasar.ymir.zpt;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.TagElement;

public class HoeTemplateParsingInterceptor implements
        TemplateParsingInterceptor {
    private static final String[] SPECIALATTRIBUTEPATTERNSTRINGS = new String[] { "id" };

    public String[] getSpecialAttributePatternStrings() {
        return SPECIALATTRIBUTEPATTERNSTRINGS;
    }

    public String[] getSpecialTagPatternStrings() {
        return null;
    }

    public Element[] tagElementCreated(TagElement tagElement,
            TemplateParsingInterceptorChain chain)
            throws IllegalSyntaxException {
        Attribute[] attributes = tagElement.getAttributes();
        String id = null;
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getName().equals("id")) {
                id = attributes[i].getValue();
            }
        }
        return chain.tagElementCreated(new TagElement(id, new Attribute[0],
                tagElement.getBodyElements()));
    }
}
