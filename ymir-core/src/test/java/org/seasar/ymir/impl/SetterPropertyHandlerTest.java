package org.seasar.ymir.impl;

import junit.framework.TestCase;

public class SetterPropertyHandlerTest extends TestCase {
    public void testGetPropertyType() throws Exception {
        SetterPropertyHandler target = new SetterPropertyHandler(new Hoe(),
                Hoe.class.getMethod("setFuga", new Class[] { String.class }));

        assertEquals(String.class, target.getPropertyType());
    }

    static class Hoe {
        public void setFuga(String fuga) {
        }
    }
}
