package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.FormFile;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.NotEmpty;
import org.seasar.ymir.impl.FormFileImpl;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.mock.fileupload.MockFileItem;
import org.seasar.ymir.testing.constraint.ConstraintTestCase;

public class NotEmptyConstraintTest extends
        ConstraintTestCase<NotEmpty, NotEmptyConstraint> {
    @Override
    protected Class<NotEmpty> getAnnotationClass() {
        return NotEmpty.class;
    }

    @Override
    protected NotEmptyConstraint newConstraintComponent() {
        return new NotEmptyConstraint();
    }

    @NotEmpty
    public void setValue(String value) {
    }

    @NotEmpty(completely = true)
    public void setValue2(String value2) {
    }

    @NotEmpty("#values\\[\\d+\\]\\.value")
    public void setValue3(String value3) {
    }

    @NotEmpty(allowWhitespace = false)
    public void setValue4(String value) {
    }

    @NotEmpty(value = "#valu.N")
    public void setValue6(String value) {
    }

    @NotEmpty(allowFullWidthWhitespace = false)
    public void setValue7(String value) {
    }

    @NotEmpty(allowWhitespace = false, allowFullWidthWhitespace = false)
    public void setValue8(String value) {
    }

    @NotEmpty("file")
    public void setFile(FormFile file) {
    }

    public void testValidate_パラメータが指定されていない場合() throws Exception {
        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail("notEmptyはパラメータがない場合はチェックをしないこと");
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
            fail();
        } catch (ValidationFailedException expected) {
            Notes notes = expected.getNotes();
            assertNotNull(notes);
            assertEquals(1, notes.size());
            assertEquals(Constraint.PREFIX_MESSAGEKEY + "notEmpty", notes
                    .getNotes()[0].getValue());
            assertEquals("values[0].value",
                    notes.getNotes()[0].getParameters()[0]);
        }
    }

    public void testValidate_fileタイプのパラメータ名についても正しく動作すること() throws Exception {
        getRequest().getFileParameterMap()
                .put(
                        "file",
                        new FormFile[] { new FormFileImpl(new MockFileItem()
                                .setSize(1)) });
        try {
            confirm(getSetterMethod("file", new Class[] { FormFile.class }));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate_パラメータが半角空白文字である場合() throws Exception {
        getRequest().getParameterMap().put("value", new String[] { " \t\n\r" });

        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate_パラメータが半角空白文字である場合でもallowWhitespaceがfalseの時はバリデーションエラーになること()
            throws Exception {
        getRequest().getParameterMap()
                .put("value4", new String[] { " \t\n\r" });

        try {
            confirm(getSetterMethod("value4"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }

    public void testValidate_正規表現にマッチするパラメータがなくてもバリデーションエラーにならないこと()
            throws Exception {
        getRequest().getParameterMap().put("value6", new String[] { "a" });

        try {
            confirm(getSetterMethod("value6"));
        } catch (ValidationFailedException expected) {
            fail();
        }
    }

    public void testValidate_パラメータが全角空白文字である場合でもallowFullWidthWhitespaceがfalseの時はバリデーションエラーになること()
            throws Exception {
        getRequest().getParameterMap().put("value7", new String[] { "　" });

        try {
            confirm(getSetterMethod("value7"));
            fail();
        } catch (ValidationFailedException expected) {
        }

        getRequest().getParameterMap().put("value7", new String[] { "　 " });

        try {
            confirm(getSetterMethod("value7"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate_パラメータが半角空白文字と全角空白文字からなる文字列である場合でもallowWhitespaceとallowFullWidthWhitespaceがfalseの時はバリデーションエラーになること()
            throws Exception {
        getRequest().getParameterMap().put("value8", new String[] { "　 " });

        try {
            confirm(getSetterMethod("value8"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }
}
