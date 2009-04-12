package org.seasar.ymir.render.html;

import junit.framework.TestCase;


public class TagTest extends TestCase
{
    private static final String LS = System.getProperty("line.separator");


    public void testToString()
        throws Exception
    {
        Tag target = new Tag();
        target.setId("ID");
        target.setStyleClass("CLASS");
        target.setStyle("STYLE");

        assertEquals("< id=\"ID\" class=\"CLASS\" style=\"STYLE\" />", target
            .toString());
    }


    public void testToString2()
        throws Exception
    {
        Tag target = new Tag("CONTENT");
        target.setId("ID");
        target.setStyleClass("CLASS");
        target.setStyle("STYLE");

        assertEquals("< id=\"ID\" class=\"CLASS\" style=\"STYLE\">CONTENT</>",
            target.toString());
    }


    public void testToString3()
        throws Exception
    {
        Tag target = new Tag(new Tag());
        target.setId("ID");
        target.setStyleClass("CLASS");
        target.setStyle("STYLE");

        assertEquals("< id=\"ID\" class=\"CLASS\" style=\"STYLE\">" + LS
            + "  < />" + LS + "</>", target.toString());
    }


    public void testToString4()
        throws Exception
    {
        Tag target = new Tag(new Tag[] { new Tag() });
        target.setId("ID");
        target.setStyleClass("CLASS");
        target.setStyle("STYLE");

        assertEquals("< id=\"ID\" class=\"CLASS\" style=\"STYLE\">" + LS
            + "  < />" + LS + "</>", target.toString());
    }
}
