package org.seasar.ymir.extension.creator;

import junit.framework.TestCase;

import org.seasar.ymir.Application;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;
import org.seasar.ymir.extension.creator.impl.TypeDescImpl;
import org.seasar.ymir.mock.MockApplication;

public class DescValidatorTest extends TestCase {
    private ClassDescSet classDescSet_ = new ClassDescSet();

    private DescPool pool_;

    @Override
    protected void setUp() throws Exception {
        pool_ = DescPool.newInstance(new SourceCreatorImpl() {
            @Override
            public Application getApplication() {
                return new MockApplication();
            }

            @Override
            protected ClassLoader getClassLoader() {
                return getClass().getClassLoader();
            }
        }, null);
    }

    public void testValidate1() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(pool_, Void.TYPE);

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate2() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(pool_, "int");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate3() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(pool_, "String");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate4() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(pool_, "java.lang.String");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate5() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(pool_, "java.lang.String[]");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate6() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(pool_, "java.lang.Hoehoe");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertFalse(actual);
    }

    public void testValidate7() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(pool_,
                "java.util.List<java.lang.String>");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }
}
