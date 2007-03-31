package org.seasar.ymir.beanutils;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.impl.FormFileImpl;

public class FormFileArrayConverterTest extends TestCase {

    public void testBeanUtilsConverter() throws Exception {

        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new FormFileArrayConverter(),
                FormFile[].class);
        BeanUtilsBean bean = new BeanUtilsBean(convertUtilsBean,
                new PropertyUtilsBean());
        Map<String, FormFile[]> map = new HashMap<String, FormFile[]>();
        FormFile[] files = new FormFile[] { new FormFileImpl(null),
            new FormFileImpl(null) };
        map.put("files", files);

        TestBean testBean = new TestBean();
        bean.copyProperties(testBean, map);

        assertSame(files, testBean.getFiles());
    }
}
