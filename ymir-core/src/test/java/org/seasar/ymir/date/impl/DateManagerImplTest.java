package org.seasar.ymir.date.impl;

import java.util.Calendar;
import java.util.Date;

import org.seasar.ymir.date.impl.DateManagerImpl;

import junit.framework.TestCase;

public class DateManagerImplTest extends TestCase {
    private DateManagerImpl target_ = new DateManagerImpl();

    public void testGetDate() throws Exception {
        long time1 = System.currentTimeMillis();
        long actual = target_.getDate().getTime();
        long time2 = System.currentTimeMillis();

        assertTrue("現在の時刻が返されること", actual >= time1 && actual <= time2);
    }

    public void testGetDate2() throws Exception {
        target_.setDate(new Date(100));

        Date actual = target_.getDate();
        Date actual2 = target_.getDate();

        assertEquals("設定した値が返されること", 100, actual.getTime());
        assertNotSame("毎回違うインスタンスが返されること", actual, actual2);
    }

    public void testGetCalendar() throws Exception {
        long time1 = System.currentTimeMillis();
        long actual = target_.getCalendar().getTime().getTime();
        long time2 = System.currentTimeMillis();

        assertTrue("現在の時刻が返されること", actual >= time1 && actual <= time2);
    }

    public void testGetCalendar2() throws Exception {
        target_.setDate(new Date(100));

        Calendar actual = target_.getCalendar();
        Calendar actual2 = target_.getCalendar();

        assertEquals("設定した値が返されること", 100, actual.getTime().getTime());
        assertNotSame("毎回違うインスタンスが返されること", actual, actual2);
    }

    public void testGetTime() throws Exception {
        long time1 = System.currentTimeMillis();
        long actual = target_.getTime();
        long time2 = System.currentTimeMillis();

        assertTrue("現在の時刻が返されること", actual >= time1 && actual <= time2);
    }

    public void testGetTime2() throws Exception {
        target_.setDate(new Date(100));

        long actual = target_.getTime();

        assertEquals("設定した値が返されること", 100, actual);
    }
}
