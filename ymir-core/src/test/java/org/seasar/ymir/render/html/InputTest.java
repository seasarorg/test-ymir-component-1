package org.seasar.ymir.render.html;

import junit.framework.TestCase;


public class InputTest extends TestCase
{
    public void testToString()
        throws Exception
    {
        Input target = new Input();
        target.setId("ID");
        target.setType("TYPE");
        target.setName("NAME");
        target.setInputValue("VALUE");
        target.setChecked(true);
        target.setDisabled(true);

        assertEquals(
            "<input id=\"ID\" type=\"TYPE\" name=\"NAME\" value=\"VALUE\" checked=\"checked\" disabled=\"disabled\" />",
            target.toString());
    }
}
