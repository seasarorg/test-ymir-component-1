package org.seasar.ymir.render.html;

import junit.framework.TestCase;


public class OptgroupTest extends TestCase
{
    private static final String LS = System.getProperty("line.separator");


    public void testToString()
        throws Exception
    {
        Option option1 = new Option();
        option1.setContent("CONTENT1");
        Option option2 = new Option();
        option2.setContent("CONTENT2");
        Optgroup target = new Optgroup("LABEL", new Option[] {
            option1, option2 });
        target.setId("ID");

        assertEquals("<optgroup id=\"ID\" label=\"LABEL\">" + LS
            + "  <option>CONTENT1</option>" + LS
            + "  <option>CONTENT2</option>" + LS + "</optgroup>", target
            .toString());
    }
}
