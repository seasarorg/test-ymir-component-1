package org.seasar.ymir.date.impl;

import java.util.Calendar;
import java.util.Date;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.annotation.ForTesting;
import org.seasar.ymir.date.DateManager;
import org.seasar.ymir.locale.LocaleManager;

public class DateManagerImpl implements DateManager {
    private LocaleManager localeManager_;

    private Date date_;

    @Binding(bindingType = BindingType.MUST)
    public void setLocaleManager(LocaleManager localeManager) {
        localeManager_ = localeManager;
    }

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.setTimeZone(localeManager_.getTimeZone());
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

    @ForTesting
    public void setDate(Date date) {
        date_ = date;
    }
}
