package org.seasar.ymir.zpt;

import junit.framework.TestCase;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;

public class TernaryTypePrefixHandlerTest extends TestCase {
    private TernaryTypePrefixHandler target_ = new TernaryTypePrefixHandler();

    @Override
    protected void setUp() throws Exception {
        target_.setTalesExpressionEvaluator(new TalesExpressionEvaluator() {
            @Override
            public Object evaluate(TemplateContext context,
                    VariableResolver varResolver, String expression) {
                if ("true".equals(expression)) {
                    return Boolean.TRUE;
                } else if ("false".equals(expression)) {
                    return Boolean.FALSE;
                } else {
                    return expression;
                }
            }
        });
    }

    public void testHandle() throws Exception {
        assertEquals("TRUE", target_.handle(null, null, "true ? TRUE : FALSE"));

        assertEquals("FALSE", target_
                .handle(null, null, "false ? TRUE : FALSE"));
    }

    public void testHandle2_インダイレクション指定あり_type指定あり() throws Exception {
        assertEquals("TRUE", target_.handle(null, null,
                "true/?aaa ? TRUE : type:FALSE"));
    }

    public void testHandle3_文法エラー() throws Exception {
        try {
            target_.handle(null, null, "true ? TRUE FALSE");
            fail();
        } catch (IllegalArgumentException expected) {
        }

        try {
            target_.handle(null, null, "true TRUE : FALSE");
            fail();
        } catch (IllegalArgumentException expected) {
        }

        try {
            target_.handle(null, null, "true");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }
}
