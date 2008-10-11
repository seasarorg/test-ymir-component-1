package org.seasar.ymir.extension.creator;

import junit.framework.TestCase;

public class ClassTypeTest extends TestCase {
    public void testTypeOfClass() throws Exception {
        assertNull(ClassType.typeOfClass((String) null));

        assertEquals(ClassType.PAGE, ClassType
                .typeOfClass("com.example.web.HoePage"));

        assertEquals(ClassType.PAGE, ClassType
                .typeOfClass("com.example.web.HoePageBase"));

        assertEquals(ClassType.BEAN, ClassType
                .typeOfClass("com.example.dao.Hoe"));
    }
}
