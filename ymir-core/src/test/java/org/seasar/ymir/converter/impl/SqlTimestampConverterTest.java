package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

public class SqlTimestampConverterTest extends TestCase {
    private SqlTimestampConverter target = new SqlTimestampConverter();

    public void testConvertToString() throws Exception {
        Date date = new Date(0L);
        Timestamp timestamp = new Timestamp(date.getTime());

        assertEquals(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").format(date),
                target.convertToString(timestamp, new Annotation[0]));
    }
}
