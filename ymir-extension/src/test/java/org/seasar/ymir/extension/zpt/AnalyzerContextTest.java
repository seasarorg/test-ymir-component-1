package org.seasar.ymir.extension.zpt;

import junit.framework.TestCase;

import org.seasar.ymir.Application;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.PropertyDescImpl;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;
import org.seasar.ymir.extension.creator.impl.TypeDescImpl;
import org.seasar.ymir.mock.MockApplication;

public class AnalyzerContextTest extends TestCase {
    private AnalyzerContext target_;

    private DescPool pool_;

    @Override
    protected void setUp() throws Exception {
        target_ = new AnalyzerContext();
        target_.setSourceCreator(new SourceCreatorImpl() {
            @Override
            public String getFirstRootPackageName() {
                return "com.example";
            }

            @Override
            public String[] getRootPackageNames() {
                return new String[] { getFirstRootPackageName() };
            }

            @Override
            public String getDtoPackageName() {
                return getFirstRootPackageName() + ".dto";
            }

            @Override
            protected ClassLoader getClassLoader() {
                return getClass().getClassLoader();
            }

            @Override
            public Application getApplication() {
                return new MockApplication() {
                    @Override
                    public String getProperty(String key, String defaultValue) {
                        return defaultValue;
                    }

                    @Override
                    public String getRootPackageName() {
                        return "com.example";
                    }
                };
            }

            @Override
            public SourceCreatorSetting getSourceCreatorSetting() {
                return new SourceCreatorSetting(this) {
                    @Override
                    public String findDtoClassName(String propertyName) {
                        return null;
                    }
                };
            }
        });
        pool_ = DescPool.newInstance(target_.getSourceCreator(), null);
    }

    public void testReplaceSimpleDtoTypeToDefaultType1() throws Exception {
        PropertyDesc propertyDesc = new PropertyDescImpl(pool_, "name");
        TypeDesc typeDesc = new TypeDescImpl(pool_, "com.example.dto.HoeDto");
        propertyDesc.setTypeDesc(typeDesc);
        pool_.unregisterClassDesc("com.example.dto.HoeDto");

        target_.replaceSimpleDtoTypeToDefaultType(propertyDesc);

        assertEquals("String", typeDesc.getName());
    }

    public void testReplaceSimpleDtoTypeToDefaultType2() throws Exception {
        PropertyDesc propertyDesc = new PropertyDescImpl(pool_, "name");
        TypeDesc typeDesc = new TypeDescImpl(pool_,
                "java.util.List<com.example.dto.HoeDto>");
        propertyDesc.setTypeDesc(typeDesc);
        pool_.unregisterClassDesc("com.example.dto.HoeDto");

        target_.replaceSimpleDtoTypeToDefaultType(propertyDesc);

        assertEquals("java.util.List<String>", typeDesc.getName());
    }

    public void testReplaceSimpleDtoTypeToDefaultType3() throws Exception {
        PropertyDesc propertyDesc = new PropertyDescImpl(pool_, "name");
        TypeDesc typeDesc = new TypeDescImpl(pool_, "com.example.dto.HoeDto[]");
        propertyDesc.setTypeDesc(typeDesc);
        pool_.unregisterClassDesc("com.example.dto.HoeDto");

        target_.replaceSimpleDtoTypeToDefaultType(propertyDesc);

        assertEquals("String[]", typeDesc.getName());
    }

    public void testReplaceSimpleDtoTypeToDefaultType4() throws Exception {
        PropertyDesc propertyDesc = new PropertyDescImpl(pool_, "name");
        TypeDesc typeDesc = new TypeDescImpl(pool_,
                "java.util.List<com.example.dto.HoeDto[]>[]");
        propertyDesc.setTypeDesc(typeDesc);
        pool_.unregisterClassDesc("com.example.dto.HoeDto");

        target_.replaceSimpleDtoTypeToDefaultType(propertyDesc);

        assertEquals("java.util.List<com.example.dto.HoeDto[]>[]", typeDesc
                .getName());
    }
}
