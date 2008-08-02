package org.seasar.ymir.extension.creator.util.type;

import java.util.List;

import junit.framework.TestCase;

public class TypeTokenTest extends TestCase {
    public void testTokenizeTypes() throws Exception {
        TypeToken target = new TypeToken(
                "Iterator<Map.Entry<List<String>[], List<String>[]>>");
        List<TypeToken> actual = target
                .tokenizeTypes("Map.Entry<List<String>[], List<String>[]>");

        assertEquals(1, actual.size());
        TypeToken token = actual.get(0);
        assertEquals("Map.Entry", token.getBaseName());
        TypeToken[] types = token.getTypes();
        assertEquals(2, types.length);
        int idx = 0;
        assertEquals("List[]", types[idx].getBaseName());
        TypeToken[] ts = types[idx++].getTypes();
        assertEquals(1, ts.length);
        assertEquals("String", ts[0].getBaseName());
        assertEquals("List[]", types[idx].getBaseName());
        ts = types[idx++].getTypes();
        assertEquals(1, ts.length);
        assertEquals("String", ts[0].getBaseName());
    }

    public void testGetAsString() throws Exception {
        TypeToken target = new TypeToken(
                "Iterator<Map.Entry<List<String>[], List<String>[]>>");

        assertEquals("Iterator<Map.Entry<List<String>[], List<String>[]>>",
                target.getAsString());
    }
}
