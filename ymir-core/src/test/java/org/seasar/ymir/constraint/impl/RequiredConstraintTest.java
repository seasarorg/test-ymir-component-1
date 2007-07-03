package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.Notes;
import org.seasar.ymir.constraint.Constraint;
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

    @Required("#values\\[\\d+\\]\\.value")
    public void setValue3(String value3) {
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

    public void testValidate_パラメータ名が正規表現で指定されている場合に正しく動作すること() throws Exception {
        getRequest().getParameterMap().put("value3", new String[] { "a" });
        getRequest().getParameterMap().put("values[0].value",
                new String[] { "" });
        getRequest().getParameterMap().put("values[1].value",
                new String[] { "a" });

        try {
            confirm(getSetterMethod("value3"));
        } catch (ValidationFailedException expected) {
            Notes notes = expected.getNotes();
            assertNotNull(notes);
            assertEquals(1, notes.size());
            assertEquals(Constraint.PREFIX_MESSAGEKEY + "required", notes
                    .getNotes()[0].getValue());
            assertEquals("values[0].value",
                    notes.getNotes()[0].getParameters()[0]);
        }
    }
}
