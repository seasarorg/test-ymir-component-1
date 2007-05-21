package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.constraint.ConstraintTestCase;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Length;

public class LengthConstraintTest extends
        ConstraintTestCase<Length, LengthConstraint> {
    @Override
    protected Class<Length> getAnnotationClass() {
        return Length.class;
    }

    @Override
    protected LengthConstraint newConstraintComponent() {
        return new LengthConstraint();
    }

    @Length(max = 10)
    public void setValue(String value) {
    }

    @Length(min = 10)
    public void setValue2(String value) {
    }

    @Length(10)
    public void setValue3(String value) {
    }

    public void testValidate_最大長が指定されている場合() throws Exception {
        getRequest().getParameterMap().put("value",
                new String[] { "1234567890" });

        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value",
                new String[] { "12345678901" });
        try {
            confirm(getSetterMethod("value"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }

    public void testValidate2_最小長が指定されている場合() throws Exception {
        getRequest().getParameterMap().put("value2",
                new String[] { "1234567890" });

        try {
            confirm(getSetterMethod("value2"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value2",
                new String[] { "123456789" });
        try {
            confirm(getSetterMethod("value2"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }

    public void testValidate3_最大長が指定されている場合() throws Exception {
        getRequest().getParameterMap().put("value3",
                new String[] { "1234567890" });

        try {
            confirm(getSetterMethod("value3"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value3",
                new String[] { "12345678901" });
        try {
            confirm(getSetterMethod("value3"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }
}
