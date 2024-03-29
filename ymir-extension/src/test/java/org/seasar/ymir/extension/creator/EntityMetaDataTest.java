package org.seasar.ymir.extension.creator;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.impl.ClassDescImpl;
import org.seasar.ymir.extension.creator.mock.MockSourceCreator;

public class EntityMetaDataTest extends TestCase {
    private EntityMetaData target_;

    protected void setUp() throws Exception {
        DescPool pool = DescPool.newInstance(new MockSourceCreator() {
            @Override
            public String getFirstRootPackageName() {
                return "com.example";
            }

            @Override
            public String[] getRootPackageNames() {
                return new String[] { getFirstRootPackageName() };
            }

            @Override
            public String getPagePackageName() {
                return "com.example.web";
            }

            @Override
            public String getDtoPackageName() {
                return "com.example.dto";
            }

            @Override
            public String getDaoPackageName() {
                return "com.example.dao";
            }

            @Override
            public String getDxoPackageName() {
                return "com.example.dxo";
            }

            @Override
            public String getConverterPackageName() {
                return "com.example.converter";
            }

            @Override
            public ClassDesc newClassDesc(DescPool pool, String className,
                    String qualifier, ClassCreationHintBag hintBag) {
                return new ClassDescImpl(pool, className, qualifier);
            }
        }, null);
        target_ = new EntityMetaData(pool, "com.example.dto.sub.TestDto");
    }

    public void testGetEntityName() throws Exception {
        // ## Arrange ##

        // ## Act ##
        String actual = target_.getEntityName();

        // ## Assert ##
        assertEquals("Test", actual);

    }

    public void testGetDtoClassDesc() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ClassDesc actual = target_.getDtoClassDesc();

        // ## Assert ##
        assertEquals("com.example.dto.sub.TestDto", actual.getName());
    }

    public void testGetBeanClassDesc() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ClassDesc actual = target_.getBeanClassDesc();

        // ## Assert ##
        assertEquals("Beanにはサブパッケージを付与しないこと", "com.example.dao.Test", actual
                .getName());
    }

    public void testGetDaoClassDesc() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ClassDesc actual = target_.getDaoClassDesc();

        // ## Assert ##
        assertEquals("Daoにはサブパッケージを付与しないこと", "com.example.dao.TestDao", actual
                .getName());
    }

    public void testGetDxoClassDesc() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ClassDesc actual = target_.getDxoClassDesc();

        // ## Assert ##
        assertEquals("com.example.dxo.sub.TestDxo", actual.getName());
    }

    public void testGetConverterClassDesc() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ClassDesc actual = target_.getConverterClassDesc();

        // ## Assert ##
        assertEquals("com.example.converter.sub.TestConverter", actual
                .getName());
    }

    public void testGetSubPackageName() throws Exception {
        assertEquals("", target_.getSubPackageName("com.example.dto.HoeDto"));

        assertEquals("sub.", target_
                .getSubPackageName("com.example.dto.sub.HoeDto"));
    }
}
