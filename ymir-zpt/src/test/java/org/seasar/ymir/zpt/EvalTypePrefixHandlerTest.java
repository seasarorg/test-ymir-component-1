package org.seasar.ymir.zpt;

import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

public class EvalTypePrefixHandlerTest extends ZptTestCase {
    private EvalTypePrefixHandler target_ = new EvalTypePrefixHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        target_.setTalesExpressionEvaluator(new YmirTalesExpressionEvaluator());
    }

    public void test_handle() throws Exception {
        Notes notes = new Notes();
        notes.add("category", new Note("key"));
        httpRequest_.setAttribute(RequestProcessor.ATTR_NOTES, notes);
        varResolver_.setVariable("categoryname", "category");

        assertEquals("key", ((Note[]) target_.handle(context_, varResolver_,
                "notes/notes($categoryname)"))[0].getValue());
    }
}
