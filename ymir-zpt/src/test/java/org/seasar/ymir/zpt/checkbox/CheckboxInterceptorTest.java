package org.seasar.ymir.zpt.checkbox;

import org.seasar.ymir.zpt.TagRenderingInterceptorChain;

import junit.framework.TestCase;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;

public class CheckboxInterceptorTest extends TestCase {
    private CheckboxInterceptor target_ = new CheckboxInterceptor() {
        @Override
        String getKey() {
            return "_";
        }
    };

    public void test_render() throws Exception {
        assertEquals(
                "<input type=\"checkbox\" name=\"check1\" value=\"true\" /><input type=\"hidden\" name=\"_\" value=\"check1\" />",
                target_.render(null, "input", new Attribute[] {
                    new Attribute("type", "checkbox", "\""),
                    new Attribute("name", "check1", "\""),
                    new Attribute("value", "true", "\"") }, null,
                        new TagRenderingInterceptorChain() {
                            public String render(TemplateContext context,
                                    String name, Attribute[] attributes,
                                    String body) {
                                return TagEvaluatorUtils.evaluate(context,
                                        name, attributes, body);
                            }
                        }));

        assertEquals("<input name=\"check1\" value=\"true\" />", target_
                .render(null, "input", new Attribute[] {
                    new Attribute("name", "check1", "\""),
                    new Attribute("value", "true", "\"") }, null,
                        new TagRenderingInterceptorChain() {
                            public String render(TemplateContext context,
                                    String name, Attribute[] attributes,
                                    String body) {
                                return TagEvaluatorUtils.evaluate(context,
                                        name, attributes, body);
                            }
                        }));

        assertEquals("<input type=\"text\" name=\"check1\" value=\"true\" />",
                target_.render(null, "input", new Attribute[] {
                    new Attribute("type", "text", "\""),
                    new Attribute("name", "check1", "\""),
                    new Attribute("value", "true", "\"") }, null,
                        new TagRenderingInterceptorChain() {
                            public String render(TemplateContext context,
                                    String name, Attribute[] attributes,
                                    String body) {
                                return TagEvaluatorUtils.evaluate(context,
                                        name, attributes, body);
                            }
                        }));

        assertEquals("<input type=\"checkbox\" value=\"true\" />", target_
                .render(null, "input", new Attribute[] {
                    new Attribute("type", "checkbox", "\""),
                    new Attribute("value", "true", "\"") }, null,
                        new TagRenderingInterceptorChain() {
                            public String render(TemplateContext context,
                                    String name, Attribute[] attributes,
                                    String body) {
                                return TagEvaluatorUtils.evaluate(context,
                                        name, attributes, body);
                            }
                        }));
    }
}
