package org.seasar.ymir.zpt;

import java.util.Calendar;
import java.util.Date;

import net.skirnir.freyja.ExpressionEvaluator;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.ZptTemplateContext;

public class FormatTypePrefixHandlerTest extends ZptTestCase {
    private FormatTypePrefixHandler target_ = new FormatTypePrefixHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        varResolver_.setVariable("date", new Date(0L));
        varResolver_.setVariable("number", Integer.valueOf(500000));

        context_ = new ZptTemplateContext() {
            @Override
            public VariableResolver getVariableResolver() {
                return varResolver_;
            }

            @Override
            public ExpressionEvaluator getExpressionEvaluator() {
                return new ExpressionEvaluator() {
                    public Object evaluate(TemplateContext context,
                            VariableResolver varResolver, String expression) {
                        return varResolver.getVariable(context, expression);
                    }

                    public boolean isTrue(Object obj) {
                        return false;
                    }
                };
            }
        };
    }

    public void test_handle() throws Exception {
        assertEquals("500,000", target_.handle(context_, context_
                .getVariableResolver(), "{0}!${number}"));

        assertEquals("1970/01/01 00:00:00", target_.handle(context_, context_
                .getVariableResolver(), "{0,date,yyyy/MM/dd HH:mm:ss}!${date}"));
    }

    public void test_数値の自動変換が正しく働くこと() throws Exception {
        varResolver_.setVariable("string", "500000");

        assertEquals("500,000", target_.handle(context_, context_
                .getVariableResolver(), "{0,number}!${string}"));
    }

    public void test_数値の自動変換が正しく働くこと2() throws Exception {
        varResolver_.setVariable("string", "hoehoe");

        assertEquals("hoehoe", target_.handle(context_, context_
                .getVariableResolver(), "{0,number}!${string}"));
    }

    public void test_日付の自動変換が正しく働くこと() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(0L));
        varResolver_.setVariable("calendar", calendar);

        assertEquals("1970/01/01 00:00:00", target_.handle(context_, context_
                .getVariableResolver(),
                "{0,date,yyyy/MM/dd HH:mm:ss}!${calendar}"));
    }

    public void test_日付の自動変換が正しく働くこと2() throws Exception {
        varResolver_.setVariable("number", Integer.valueOf(0));

        assertEquals("1970/01/01 00:00:00", target_.handle(context_, context_
                .getVariableResolver(),
                "{0,date,yyyy/MM/dd HH:mm:ss}!${number}"));
    }

    public void test_日付の自動変換が正しく働くこと3() throws Exception {
        varResolver_.setVariable("string", "0");

        assertEquals("1970/01/01 00:00:00", target_.handle(context_, context_
                .getVariableResolver(),
                "{0,date,yyyy/MM/dd HH:mm:ss}!${string}"));
    }

    public void test_日付の自動変換が正しく働くこと4() throws Exception {
        varResolver_.setVariable("string", "hoehoe");

        assertEquals("hoehoe", target_.handle(context_, context_
                .getVariableResolver(),
                "{0,date,yyyy/MM/dd HH:mm:ss}!${string}"));
    }
}
