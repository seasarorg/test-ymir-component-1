package org.seasar.ymir.constraint.impl;

import java.util.Map;

import org.seasar.ymir.constraint.Globals;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Datetime;
import org.seasar.ymir.testing.constraint.ConstraintTestCase;

public class DatetimeConstraintTest extends
        ConstraintTestCase<Datetime, DatetimeConstraint> {
    @Override
    protected Class<Datetime> getAnnotationClass() {
        return Datetime.class;
    }

    @Override
    protected DatetimeConstraint newConstraintComponent() {
        return new DatetimeConstraint();
    }

    @Datetime
    public void setDatetime(String datetime) {
    }

    @Datetime(messageKey = "KEY")
    public void setDatetime2(String datetime2) {
    }

    @Datetime(messageKey = "!KEY")
    public void setDatetime3(String datetime3) {
    }

    public void testValidate() throws Exception {
        Map<String, String[]> parameterMap = getRequest().getParameterMap();
        parameterMap.put("datetime", new String[] { "2000-01-01 00:00:00" });

        try {
            confirm(getSetterMethod("datetime"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        parameterMap.put("datetime", new String[] { "XXX" });
        try {
            confirm(getSetterMethod("datetime"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "datetime", expected
                    .getNotes().getNotes()[0].getValue());
        }

        parameterMap.put("datetime2", new String[] { "XXX" });
        try {
            confirm(getSetterMethod("datetime2"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "datetime.KEY", expected
                    .getNotes().getNotes()[0].getValue());
        }

        parameterMap.put("datetime3", new String[] { "XXX" });
        try {
            confirm(getSetterMethod("datetime3"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
        }
    }

    public void testValidate5_パラメータが指定されていない場合は何もしないこと() throws Exception {
        try {
            confirm(getSetterMethod("datetime"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate4_パラメータが空文字列の場合は何もしないこと() throws Exception {
        getRequest().getParameterMap().put("datetime", new String[] { "" });

        try {
            confirm(getSetterMethod("datetime"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }
}
