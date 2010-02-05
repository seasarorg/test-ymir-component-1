package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.constraint.Globals;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Matched;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.testing.constraint.ConstraintTestCase;

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

    @Matched(value = "[\\d]+(-[\\d]+)+", messageKey = "KEY")
    public void setValue2(String value2) {
    }

    @Matched(value = "[\\d]+(-[\\d]+)+", messageKey = "!KEY")
    public void setValue3(String value3) {
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
            assertEquals(Globals.PREFIX_MESSAGEKEY + "matched", notes
                    .getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value2", new String[] { "-10-" });
        try {
            confirm(getSetterMethod("value2"));
            fail();
        } catch (ValidationFailedException expected) {
            Notes notes = expected.getNotes();
            assertNotNull(notes);
            assertEquals(1, notes.size());
            assertEquals(Globals.PREFIX_MESSAGEKEY + "matched.KEY", notes
                    .getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value3", new String[] { "-10-" });
        try {
            confirm(getSetterMethod("value3"));
            fail();
        } catch (ValidationFailedException expected) {
            Notes notes = expected.getNotes();
            assertNotNull(notes);
            assertEquals(1, notes.size());
            assertEquals("KEY", notes.getNotes()[0].getValue());
        }
    }
}
