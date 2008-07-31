package org.seasar.ymir.constraint.impl;

import java.util.Map;

import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Confirmed;
import org.seasar.ymir.test.constraint.ConstraintTestCase;

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
