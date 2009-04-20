package org.seasar.ymir.extension.freemarker;

import java.util.HashMap;

import org.seasar.ymir.Application;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.constraint.ConstraintInterceptor;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.impl.BodyDescImpl;
import org.seasar.ymir.extension.creator.impl.ClassDescImpl;
import org.seasar.ymir.extension.creator.impl.MethodDescImpl;
import org.seasar.ymir.extension.creator.impl.ParameterDescImpl;
import org.seasar.ymir.extension.creator.impl.PropertyDescImpl;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;
import org.seasar.ymir.extension.creator.impl.ThrowsDescImpl;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockYmir;
import org.seasar.ymir.testing.TestCaseBase;

import com.example.dao.Hoe;
import com.example.dto.HoeDto;
import com.example.page.TestPageBaseBase;

public class FreemarkerSourceGeneratorTest extends TestCaseBase {
    private FSourceCreatorImpl sourceCreator_;

    private FreemarkerSourceGenerator target_;

    public static class FSourceCreatorImpl extends SourceCreatorImpl {
        @Override
        public ClassDesc createConverterClassDesc(ClassDesc dtoCd,
                String[] pairTypeNames, ClassCreationHintBag hintBag) {
            return super
                    .createConverterClassDesc(dtoCd, pairTypeNames, hintBag);
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
                public String getFieldPrefix() {
                    return "";
                }

                @Override
                public String getFieldSpecialPrefix() {
                    return "";
                }

                @Override
                public String getFieldSuffix() {
                    return "_";
                }
            };
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        sourceCreator_ = new FSourceCreatorImpl() {
            {
                setNamingConvention(new YmirNamingConvention());
            }

            @Override
            public String[] getRootPackageNames() {
                return new String[] { getFirstRootPackageName() };
            }

            @Override
            public String getFirstRootPackageName() {
                return "com.example";
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
        };
        target_ = new FreemarkerSourceGenerator();
        target_.setSourceCreator(sourceCreator_);

        YmirContext.setYmir(new MockYmir());
    }

    private ClassDesc prepareClassDesc(String className) {
        ClassDesc classDesc = new ClassDescImpl(null, className);
        classDesc.setSuperclassName(TestPageBaseBase.class.getName());
        PropertyDesc propertyDesc = new PropertyDescImpl(null, "param1");
        propertyDesc.setTypeDesc(Boolean.TYPE);
        propertyDesc.setMode(PropertyDesc.READ);
        classDesc.setPropertyDesc(propertyDesc);
        propertyDesc = new PropertyDescImpl(null, "param2");
        propertyDesc.setMode(PropertyDesc.WRITE);
        classDesc.setPropertyDesc(propertyDesc);
        // 順番をアルファベット順でないようにしているのは、プロパティやメソッドがアルファベット順に
        // 生成されることを検証するため。（Dtoではコンストラクタのみアルファベット順）
        propertyDesc = new PropertyDescImpl(null, "param4");
        propertyDesc.setMode(PropertyDesc.READ | PropertyDesc.WRITE);
        propertyDesc.setTypeDesc(Integer[].class).setExplicit(true);
        classDesc.setPropertyDesc(propertyDesc);
        propertyDesc = new PropertyDescImpl(null, "param3");
        propertyDesc.setMode(PropertyDesc.READ | PropertyDesc.WRITE);
        classDesc.setPropertyDesc(propertyDesc);

        return classDesc;
    }

    public void testGenerateGapSource() throws Exception {
        ClassDesc classDesc = prepareClassDesc("com.example.dto.TestDto");

        String actual = target_.generateGapSource(classDesc);

        assertEquals(
                readResource(getClass(), "testGenerateGapSource.expected"),
                actual);
    }

    public void testGenerateBaseSource_Page() throws Exception {
        ClassDesc classDesc = prepareClassDesc("com.example.page.TestPage");
        MethodDesc methodDesc = new MethodDescImpl(null, "_get");
        classDesc.setAttribute(Globals.ATTR_ACTION,
                new MethodDesc[] { methodDesc });
        methodDesc.setReturnTypeDesc(String.class.getName());
        methodDesc.setBodyDesc(new BodyDescImpl(
                "return \"redirect:/path/to/redirect.html\";"));
        classDesc.setMethodDesc(methodDesc);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page.expected"), actual);
    }

    public void testGenerateBaseSource_Page2() throws Exception {
        ClassDesc classDesc = new FSourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(DescPool pool, String className,
                    ClassCreationHintBag bag) {
                if (className.equals(HoePageBase.class.getName())) {
                    className = "org.seasar.ymir.extension.freemarker.HoePage";
                }
                return new ClassDescImpl(pool, className);
            }
        }.newClassDesc(HoePageBase.class, true);
        classDesc.setAttribute(Globals.ATTR_ACTION,
                new MethodDesc[] { classDesc.getMethodDesc(new MethodDescImpl(
                        null, "_get")) });

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page2.expected"), actual);
    }

    public void testGenerateBaseSource_Page3() throws Exception {
        ClassDesc classDesc = new FSourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(DescPool pool, String className,
                    ClassCreationHintBag bag) {
                if (className.equals(Hoe3PageBase.class.getName())) {
                    className = "org.seasar.ymir.extension.freemarker.Hoe3Page";
                }
                return new ClassDescImpl(pool, className);
            }
        }.newClassDesc(Hoe3PageBase.class, true);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page3.expected"), actual);
    }

    public void testGenerateBaseSource_Page4() throws Exception {
        ClassDesc classDesc = new ClassDescImpl(null,
                "com.example.web.TestPage");
        MethodDesc methodDesc = new MethodDescImpl(null, "_permissionDenied");
        methodDesc.setThrowsDesc(new ThrowsDescImpl()
                .addThrowable(PermissionDeniedException.class));
        methodDesc
                .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                        null, PermissionDeniedException.class, "ex") });
        methodDesc.setBodyDesc(new BodyDescImpl(
                ConstraintInterceptor.ACTION_PERMISSIONDENIED,
                new HashMap<String, Object>()));
        classDesc.setMethodDesc(methodDesc);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page4.expected"), actual);
    }

    public void testGenerateBaseSource_Page5() throws Exception {
        ClassDesc classDesc = new FSourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(DescPool pool, String className,
                    ClassCreationHintBag bag) {
                if (className.equals(Hoe5PageBase.class.getName())) {
                    className = "org.seasar.ymir.extension.freemarker.Hoe5Page";
                }
                return new ClassDescImpl(pool, className);
            }
        }.newClassDesc(Hoe5PageBase.class, true);

        ClassDesc generated = new ClassDescImpl(null,
                "org.seasar.ymir.extension.freemarker.Hoe5Page");
        MethodDesc methodDesc = new MethodDescImpl(null, "_permissionDenied");
        methodDesc.setThrowsDesc(new ThrowsDescImpl()
                .addThrowable(PermissionDeniedException.class));
        methodDesc
                .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                        null, PermissionDeniedException.class, "ex") });
        methodDesc.setBodyDesc(new BodyDescImpl(
                ConstraintInterceptor.ACTION_PERMISSIONDENIED,
                new HashMap<String, Object>()));
        generated.setMethodDesc(methodDesc);

        classDesc.merge(generated, true);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page5.expected"), actual);
    }

    public void testGenerateBaseSource_Dto() throws Exception {
        ClassDesc classDesc = prepareClassDesc("com.example.dto.TestDto");

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Dto.expected"), actual);
    }

    public void testGenerateGapSource_Page() throws Exception {

        ClassDesc classDesc = prepareClassDesc("com.example.page.TestPage");
        MethodDesc methodDesc = new MethodDescImpl(null, "_get");
        methodDesc.setReturnTypeDesc(String.class.getName());
        methodDesc.setBodyDesc(new BodyDescImpl(
                "return \"redirect:/path/to/redirect.html\";"));
        classDesc.setMethodDesc(methodDesc);

        String actual = target_.generateGapSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateGapSource_Page.expected"), actual);
    }

    public void testGenerateSource_BodyDesc() throws Exception {
        String actual = target_.generateBodySource(new BodyDescImpl("test"));

        assertEquals("test", actual);
    }

    public void testGenerateBaseSource_Page6() throws Exception {
        ClassDesc classDesc = new FSourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(DescPool pool, String className,
                    ClassCreationHintBag bag) {
                if (className.equals(Hoe6PageBase.class.getName())) {
                    className = "org.seasar.ymir.extension.freemarker.Hoe6Page";
                }
                return new ClassDescImpl(pool, className);
            }
        }.newClassDesc(Hoe6PageBase.class, true);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page6.expected"), actual);
    }

    public void testGenerateBaseSource_Converter() throws Exception {
        ClassDesc classDesc = sourceCreator_.createConverterClassDesc(
                new ClassDescImpl(null, HoeDto.class.getName()), new String[] {
                    Hoe.class.getName() + "<java.util.List>",
                    "java.lang.Object" }, null);
        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Converter.expected"), actual);
    }

    public void testGenerateGapSource_Converter() throws Exception {
        ClassDesc classDesc = sourceCreator_.createConverterClassDesc(
                new ClassDescImpl(null, HoeDto.class.getName()), new String[] {
                    Hoe.class.getName() + "<java.util.List>",
                    "java.lang.Object" }, null);
        String actual = target_.generateGapSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateGapSource_Converter.expected"), actual);
    }

    public void testGenerateBaseSource_Converter2_中身が空の場合() throws Exception {
        ClassDesc classDesc = sourceCreator_.createConverterClassDesc(
                new ClassDescImpl(null, HoeDto.class.getName()),
                new String[] { "java.lang.Object" }, null);
        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Converter2.expected"), actual);
    }

    public void test_YMIR_309_GenerateBaseSource_Page7_プリミティブ型の引数を持つメソッドの引数名が適切に生成されること()
            throws Exception {
        ClassDesc classDesc = new ClassDescImpl(null,
                "com.example.page.TestPage");
        MethodDesc methodDesc = new MethodDescImpl(null, "_get");
        classDesc.setAttribute(Globals.ATTR_ACTION,
                new MethodDesc[] { methodDesc });
        methodDesc.setReturnTypeDesc("void");
        methodDesc
                .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                        null, Integer.TYPE) });
        classDesc.setMethodDesc(methodDesc);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page7.expected"), actual);
    }

    public void test_YMIR_310_GenerateBaseSource_Page8_同一型の引数を複数持つメソッドの引数名が適切に生成されること()
            throws Exception {
        ClassDesc classDesc = new ClassDescImpl(null,
                "com.example.page.TestPage");
        MethodDesc methodDesc = new MethodDescImpl(null, "_get");
        classDesc.setAttribute(Globals.ATTR_ACTION,
                new MethodDesc[] { methodDesc });
        methodDesc.setReturnTypeDesc("void");
        methodDesc.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(null, String.class),
            new ParameterDescImpl(null, String.class) });
        classDesc.setMethodDesc(methodDesc);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page8.expected"), actual);
    }
}
