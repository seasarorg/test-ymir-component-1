package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.Notes;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintTestCase;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Matched;

public class MatchedConstraintTest extends
        ConstraintTestCase<Matched, MatchedConstraint> {
    @Override
    protected Class<Matched> getAnnotationClass() {
        return Matched.class;
    }

    @Override
    protected MatchedConstraint newConstraintComponent() {
        return new MatchedConstraint();
    }

    @Matched("[\\d]+(-[\\d]+)+")
    public void setValue(String value) {
    }

    public void testValidate() throws Exception {
        getRequest().getParameterMap().put("value",
                new String[] { "090-1111-2222" });

        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value", new String[] { "-10-" });
        try {
            confirm(getSetterMethod("value"));
            fail();
        } catch (ValidationFailedException expected) {
            Notes notes = expected.getNotes();
            assertNotNull(notes);
            assertEquals(1, notes.size());
            assertEquals(Constraint.PREFIX_MESSAGEKEY + "matched", notes
                    .getNotes()[0].getValue());
        }
    }
}
