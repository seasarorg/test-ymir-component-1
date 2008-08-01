package org.seasar.ymir.extension.creator.util.type;

import java.util.List;

import junit.framework.TestCase;

public class TypeTokenTest extends TestCase {
    public void testTokenizeTypes() throws Exception {
        TypeToken target = new TypeToken(
                "Iterator<Map.Entry<String, String[]>>");
        List<TypeToken> actual = target
                .tokenizeTypes("Map.Entry<String, String[]>");

        assertEquals(1, actual.size());
        int idx = 0;
        TypeToken token = actual.get(idx++);
        assertEquals("Map.Entry", token.getBaseName());
        TypeToken[] types = token.getTypes();
        assertEquals(2, types.length);
        idx = 0;
        assertEquals("String", types[idx++].getBaseName());
        assertEquals("String[]", types[idx++].getBaseName());
    }

}
