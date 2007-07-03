package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.constraint.ConstraintTestCase;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Required;

public class RequiredConstraintTest extends
        ConstraintTestCase<Required, RequiredConstraint> {
    @Override
    protected Class<Required> getAnnotationClass() {
        return Required.class;
    }

    @Override
    protected RequiredConstraint newConstraintComponent() {
        return new RequiredConstraint();
    }

    @Required
    public void setValue(String value) {
    }

    @Required(completely = true)
    public void setValue2(String value2) {
    }

    public void testValidate_パラメータが指定されていない場合() throws Exception {
        getRequest().getParameterMap().put("value", new String[] { "" });

        try {
            confirm(getSetterMethod("value"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }

    public void testValidate_パラメータが空文字列である場合() throws Exception {
        getRequest().getParameterMap().put("value", new String[] { "" });

        try {
            confirm(getSetterMethod("value"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }

    public void testValidate_パラメータが空文字列でない場合() throws Exception {
        getRequest().getParameterMap().put("value", new String[] { "a" });

        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate_パラメータの値が複数あってどれか1つでも空文字列でない場合はエラーにならないこと()
            throws Exception {
        getRequest().getParameterMap().put("value", new String[] { "", "a" });

        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate_completelyがtrueの時はパラメータの値が複数あってどれか1つでも空文字列でない場合はエラーになること()
            throws Exception {
        getRequest().getParameterMap().put("value2", new String[] { "", "a" });

        try {
            confirm(getSetterMethod("value2"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }
}
