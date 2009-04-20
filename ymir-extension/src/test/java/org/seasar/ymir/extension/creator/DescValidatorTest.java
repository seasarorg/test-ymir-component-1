package org.seasar.ymir.extension.creator;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.impl.TypeDescImpl;

public class DescValidatorTest extends TestCase {
    private ClassDescSet classDescSet_ = new ClassDescSet();

    public void testValidate1() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(null, Void.TYPE);

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate2() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(null, "int");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate3() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(null, "String");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate4() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(null, "java.lang.String");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate5() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(null, "java.lang.String[]");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }

    public void testValidate6() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(null, "java.lang.Hoehoe");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertFalse(actual);
    }

    public void testValidate7() throws Exception {
        // ## Arrange ##
        TypeDesc target = new TypeDescImpl(null,
                "java.util.List<java.lang.String>");

        // ## Act ##
        boolean actual = DescValidator.validate(target, classDescSet_)
                .isValid();

        // ## Assert ##
        assertTrue(actual);
    }
}
