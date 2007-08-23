package org.seasar.ymir.zpt;

import java.io.StringReader;

import junit.framework.TestCase;

import net.skirnir.freyja.ConstantElement;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.TagElement;

public class YmirTemplateParserTest extends TestCase {
    public void testParse() throws Exception {
        YmirTemplateParser target = new YmirTemplateParser() {
            @Override
            TemplateParsingInterceptor[] gatherTemplateParsingInterceptors() {
                return new TemplateParsingInterceptor[] {
                    new HoeTemplateParsingInterceptor(),
                    new FugaTemplateParsingInterceptor() };
            }
        };

        Element[] actual = target
                .parse(new StringReader("<p id=\"a\">BODY</p>"));

        assertEquals(2, actual.length);
        assertTrue(actual[0] instanceof TagElement);
        assertEquals("a", ((TagElement) actual[0]).getName());
        assertTrue(actual[1] instanceof ConstantElement);
        assertEquals("BODY", ((ConstantElement) actual[1]).getConstant());
    }
}
