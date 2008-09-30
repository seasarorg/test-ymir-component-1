package org.seasar.ymir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

public class AttributeTest extends TestCase {
    public void test_SerializeとDeserializeができること() throws Exception {
        Attribute target = new Attribute("name", "value");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(target);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                bytes));
        Attribute actual = (Attribute) ois.readObject();
        ois.close();

        assertEquals("name", actual.getName());
        assertEquals("value", actual.getValue());
    }
}
