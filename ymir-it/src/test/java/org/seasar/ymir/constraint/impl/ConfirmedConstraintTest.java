package org.seasar.ymir.constraint.impl;

import java.util.Map;

import org.seasar.ymir.constraint.Globals;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Confirmed;
import org.seasar.ymir.testing.constraint.ConstraintTestCase;

public class ConfirmedConstraintTest extends
        ConstraintTestCase<Confirmed, ConfirmedConstraint> {
    @Override
    protected Class<Confirmed> getAnnotationClass() {
        return Confirmed.class;
    }

    @Override
    protected ConfirmedConstraint newConstraintComponent() {
        return new ConfirmedConstraint();
    }

    @Confirmed("passwordConfirm")
    public void setPassword(String password) {
    }

    @Confirmed(value = "password2Confirm", messageKey = "KEY")
    public void setPassword2(String password2) {
    }

    @Confirmed(value = "password3Confirm", messageKey = "!KEY")
    public void setPassword3(String password3) {
    }

    public void testValidate() throws Exception {
        Map<String, String[]> parameterMap = getRequest().getParameterMap();
        parameterMap.put("password", new String[] { "hoehoe" });
        parameterMap.put("passwordConfirm", new String[] { "hoehoe" });

        try {
            confirm(getSetterMethod("password"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        parameterMap.put("passwordConfirm", new String[] { "fugafuga" });
        try {
            confirm(getSetterMethod("password"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "confirmed", expected
                    .getNotes().getNotes()[0].getValue());
        }

        parameterMap.put("passwordConfirm", new String[] { "" });
        try {
            confirm(getSetterMethod("password"));
            fail();
        } catch (ValidationFailedException expected) {
        }

        parameterMap.remove("passwordConfirm");
        try {
            confirm(getSetterMethod("password"));
        } catch (ValidationFailedException expected) {
            fail("値がないパラメータが指定されている場合はそのパラメータについてはチェックは行なわれないこと");
        }

        parameterMap.put("password2", new String[] { "hoehoe" });
        parameterMap.put("password2Confirm", new String[] { "fugafuga" });
        try {
            confirm(getSetterMethod("password2"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "confirmed.KEY", expected
                    .getNotes().getNotes()[0].getValue());
        }

        parameterMap.put("password3", new String[] { "hoehoe" });
        parameterMap.put("password3Confirm", new String[] { "fugafuga" });
        try {
            confirm(getSetterMethod("password3"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
        }
    }

    public void testValidate5_パラメータが指定されていない場合は何もしないこと() throws Exception {
        try {
            confirm(getSetterMethod("password"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate4_パラメータが空文字列の場合は何もしないこと() throws Exception {
        getRequest().getParameterMap().put("password", new String[] { "" });

        try {
            confirm(getSetterMethod("password"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }
}
