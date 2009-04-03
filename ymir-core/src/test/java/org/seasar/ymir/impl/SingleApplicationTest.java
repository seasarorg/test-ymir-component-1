package org.seasar.ymir.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.cms.pluggable.impl.ConfigurationImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

public class SingleApplicationTest extends TestCase {
    private SingleApplication target_ = new SingleApplication(
            new MockServletContextImpl("/context"), new ConfigurationImpl(),
            (Class<?>[]) null, null, null, null);

    public void testSave_projectStatusは保存されないこと() throws Exception {
        target_.setProperty(Configuration.KEY_PROJECTSTATUS,
                Configuration.PROJECTSTATUS_DEVELOP);
        target_.setProperty("KEY", "VALUE");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        target_.save(baos, null);

        Properties prop = new Properties();
        prop.load(new ByteArrayInputStream(baos.toByteArray()));

        assertEquals("VALUE", prop.getProperty("KEY"));
        assertNull(prop.getProperty(Configuration.KEY_PROJECTSTATUS));
    }
}
