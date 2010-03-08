package org.seasar.ymir.zpt;

import net.skirnir.freyja.Element;
import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.TagElement;

public interface TemplateParsingInterceptorChain {
    Element[] tagElementCreated(TagElement tagElement)
            throws IllegalSyntaxException;
}
