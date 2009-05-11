package org.seasar.ymir.extension.zpt;

import junit.framework.TestCase;

import org.seasar.ymir.extension.zpt.AnalyzerTalTagEvaluator.AnnotationResult;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.TagElement;

public class AnalyzerTalTagEvaluatorTest extends TestCase {

    public void testFindAnnotation1() throws Exception {

        Attribute attr0 = new Attribute("name", "value", "\"");
        Attribute attr1 = new Attribute("tal:annotation", "annotation", "\"");
        Attribute attr2 = new Attribute("tal:attributes", "value", "\"");
        AnalyzerTalTagEvaluator evaluator = new AnalyzerTalTagEvaluator();
        AnnotationResult result = evaluator.findAnnotation(evaluator
                .newContext(), "tag", new Attribute[] { attr0, attr1, attr2, });
        assertEquals("annotation", result.getAnnotation());
        Attribute[] attrs = result.getTheOtherAttributes();
        assertEquals(2, attrs.length);
        assertSame(attr0, attrs[0]);
        assertSame(attr2, attrs[1]);
    }

    public void testFindAnnotation2() throws Exception {

        Attribute attr0 = new Attribute("name", "value", "\"");
        Attribute attr1 = new Attribute("tal:attributes", "value", "\"");
        AnalyzerTalTagEvaluator evaluator = new AnalyzerTalTagEvaluator();
        AnnotationResult result = evaluator.findAnnotation(evaluator
                .newContext(), "tag", new Attribute[] { attr0, attr1, });
        assertNull(result.getAnnotation());
        Attribute[] attrs = result.getTheOtherAttributes();
        assertEquals(2, attrs.length);
        assertSame(attr0, attrs[0]);
        assertSame(attr1, attrs[1]);
    }

    public void testFindAnnotation3() throws Exception {

        Attribute attr0 = new Attribute("name", "value", "\"");
        Attribute attr1 = new Attribute("tal:annotation", "annotation", "\"");
        Attribute attr2 = new Attribute("tal:attributes", "value", "\"");
        Attribute attr3 = new Attribute("tal:annotation", "annotation", "\"");
        AnalyzerTalTagEvaluator evaluator = new AnalyzerTalTagEvaluator();
        try {
            evaluator.findAnnotation(evaluator.newContext(), "tag",
                    new Attribute[] { attr0, attr1, attr2, attr3 });
            fail();
        } catch (EvaluationRuntimeException expected) {
        }
    }

    public void testFindEndEdge() throws Exception {
        AnalyzerTalTagEvaluator target = new AnalyzerTalTagEvaluator();

        assertEquals(2, target.findEndEdge("abcde", 2));
        assertEquals(6, target.findEndEdge("a{bcde", 1));
        assertEquals(6, target.findEndEdge("a}bcde", 1));
        assertEquals(7, target.findEndEdge("a{b{c}d}e", 1));
    }

    public void testIsStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex()
            throws Exception {
        AnalyzerTalTagEvaluator target = new AnalyzerTalTagEvaluator();
        AnalyzerContext context = new AnalyzerContext();
        context.setElement(new TagElement("name", new Attribute[0],
                new Element[0]));

        assertTrue(target
                .isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                        context, "string:entries"));
        assertTrue(target
                .isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                        context, "string:entries[$idx].value"));
        assertTrue(target
                .isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                        context, "string:entries[${idx/hoehoe}].value"));
        assertFalse(target
                .isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                        context, "string:entries[${idx/hoehoe}$fuga].value"));
        assertFalse(target
                .isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                        context, "entries"));
        assertFalse(target
                .isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                        context, "string:entries[$idx.value"));
        assertFalse(target
                .isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                        context, "string:entries$idx.value"));
        assertFalse(target
                .isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                        context, "string:ent${hoe}ries[$idx].value"));
    }
}
