package org.seasar.ymir.render.html;

import java.util.Arrays;

import junit.framework.TestCase;


public class SelectTest extends TestCase
{
    public void testSetValues_選択状態が洗い替えされること()
        throws Exception
    {
        Select target = new Select(
            new Option[] { new Option("value1"),
                new Option("value2").setSelected(true) });

        target.setSelectedValues(new String[] { "value1", "value3" });

        String[] actual = target.getSelectedValues();

        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("value1", actual[idx++]);
        assertFalse(target.getOption("value2").isSelected());
    }


    public void test_setValues_StackOverflowを起こさないこと()
        throws Exception
    {
        Select target = new Select(new Option[] { new Option("1") });

        try {
            target.setSelectedValues(new Integer[] { new Integer(1) });
        } catch (StackOverflowError er) {
            fail();
        }

        try {
            target.setSelectedValues(Arrays.asList(new Integer[] { new Integer(
                1) }));
        } catch (StackOverflowError er) {
            fail();
        }
    }


    public void test_後からモデルを構築できること()
        throws Exception
    {
        Select target = new Select();

        target.setSelectedValues(new String[] { "value1", "value3" });
        target.setOptions(new Option[] { new Option("value1"),
            new Option("value2").setSelected(true) });

        String[] actual = target.getSelectedValues();

        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("value1", actual[idx++]);
        assertFalse(target.getOption("value2").isSelected());
    }
}
