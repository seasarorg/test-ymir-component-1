package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.FittedOnTypeConstraintITest2Page;
import com.example.web.FittedOnTypeConstraintITestPage;

public class FittedOnTypeConstraintITest extends
        PageTestCase<FittedOnTypeConstraintITestPage> {
    @Override
    protected Class<FittedOnTypeConstraintITestPage> getPageClass() {
        return FittedOnTypeConstraintITestPage.class;
    }

    public void test_int() throws Exception {
        process(FittedOnTypeConstraintITestPage.class, "value", "abc");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(1, actual.getNotes().length);
        Note note = actual.getNotes()[0];
        assertEquals("error.constraint.fittedOnType.int", note.getValue());
        assertEquals("value", note.getParameters()[0]);
    }

    public void test_メソッドにも付与できること() throws Exception {
        process(FittedOnTypeConstraintITest2Page.class, "value", "abc");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(1, actual.getNotes().length);
        Note note = actual.getNotes()[0];
        assertEquals("error.constraint.fittedOnType.int", note.getValue());
        assertEquals("value", note.getParameters()[0]);
    }

    public void test_Date() throws Exception {
        process(FittedOnTypeConstraintITestPage.class, "date", "20100501");

        Notes actual = getNotes();

        assertNotNull(actual);
        assertEquals(1, actual.getNotes().length);
        Note note = actual.getNotes()[0];
        assertEquals("Dateのためのキーがメッセージとして存在しない場合はデフォルトのキーが使われること",
                "error.constraint.fittedOnType", note.getValue());
        assertEquals("date", note.getParameters()[0]);
    }

    public void test_Date型ではDatetimeアノテーションとバッティングしないこと() throws Exception {
        process(FittedOnTypeConstraintITestPage.class, "date2", "20100501");

        Notes actual = getNotes();

        assertNull(actual);
    }
}
