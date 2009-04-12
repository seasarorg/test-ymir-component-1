package org.seasar.ymir.render;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import junit.framework.TestCase;

public class SelectorTest extends TestCase {
    private Selector target_ = new Selector();

    public void test_getSelectedValues_candidatesがない場合() throws Exception {
        String[] actual = target_.getSelectedValues();

        assertNotNull(actual);
        assertEquals(0, actual.length);

        target_.setSelectedValues("1", "2");
        actual = target_.getSelectedValues();

        assertNotNull(actual);
        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals("1", actual[idx++]);
        assertEquals("2", actual[idx++]);
    }

    public void test_getSelectedValues_candidatesがある場合() throws Exception {
        target_.setCandidates(new StringCandidate("1"), new StringCandidate(
                "2", true));
        String[] actual = target_.getSelectedValues();

        assertNotNull(actual);
        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("2", actual[idx++]);

        target_.setSelectedValues("1", "3");
        actual = target_.getSelectedValues();

        assertNotNull(actual);
        assertEquals(1, actual.length);
        idx = 0;
        assertEquals("1", actual[idx++]);
    }

    public void test_getSelectedValues_setSelectedValuesしてからcandidatesをセットした場合()
            throws Exception {
        target_.setSelectedValues("1", "3");
        target_.setCandidates(new StringCandidate("1"), new StringCandidate(
                "2", true));
        String[] actual = target_.getSelectedValues();

        assertNotNull(actual);
        assertEquals(1, actual.length);
        int idx = 0;
        assertEquals("candidatesに最初設定されていたフラグは無視されること", "1", actual[idx++]);
    }
}
