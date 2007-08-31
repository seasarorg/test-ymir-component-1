package org.seasar.ymir.impl;

import java.util.Calendar;
import java.util.Date;

import org.seasar.ymir.DateManager;

public class DateManagerImpl implements DateManager {
    private Date date_;

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        return calendar;
    }

    public Date getDate() {
        return new Date(getTime());
    }

    public long getTime() {
        if (date_ != null) {
            return date_.getTime();
        } else {
            return System.currentTimeMillis();
        }
    }

    public void setDate(Date date) {
        date_ = date;
    }
}
