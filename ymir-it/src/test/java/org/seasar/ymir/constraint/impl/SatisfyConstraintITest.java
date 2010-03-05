package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.message.Notes;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.SatisfyConstraintITestPage;

public class SatisfyConstraintITest extends
        PageTestCase<SatisfyConstraintITestPage> {
    @Override
    protected Class<SatisfyConstraintITestPage> getPageClass() {
        return SatisfyConstraintITestPage.class;
    }

    public void test() throws Exception {
        process(SatisfyConstraintITestPage.class, "value", "");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(1, actual.getNotes().length);
        assertEquals("error.constraint.notEmpty", actual.getNotes()[0]
                .getValue());
    }
}
