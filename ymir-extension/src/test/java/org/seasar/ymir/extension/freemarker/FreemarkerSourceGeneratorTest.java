package org.seasar.ymir.extension.freemarker;

import java.util.HashMap;

import org.seasar.ymir.Application;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.impl.ConstraintInterceptor;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.impl.AnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.BodyDescImpl;
import org.seasar.ymir.extension.creator.impl.ClassDescImpl;
import org.seasar.ymir.extension.creator.impl.MethodDescImpl;
import org.seasar.ymir.extension.creator.impl.ParameterDescImpl;
import org.seasar.ymir.extension.creator.impl.PropertyDescImpl;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;
import org.seasar.ymir.extension.creator.impl.ThrowsDescImpl;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.test.TestCaseBase;

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

    }

    protected void setUp() throws Exception {
        super.setUp();
        sourceCreator_ = new FSourceCreatorImpl() {
            {
                setNamingConvention(new YmirNamingConvention());
            }

            @Override
            protected ClassLoader getClassLoader() {
                return getClass().getClassLoader();
            }

            @Override
            public String getRootPackageName() {
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
        };
        target_ = new FreemarkerSourceGenerator();
        target_.setSourceCreator(sourceCreator_);
    }

    private ClassDesc prepareClassDesc() {
        ClassDesc classDesc = new ClassDescImpl("com.example.page.TestPage");
        classDesc.setSuperclassName(TestPageBaseBase.class.getName());
        PropertyDesc propertyDesc = new PropertyDescImpl("param1");
        propertyDesc.setTypeDesc("boolean");
        propertyDesc.setMode(PropertyDesc.READ);
        classDesc.setPropertyDesc(propertyDesc);
        propertyDesc = new PropertyDescImpl("param2");
        propertyDesc.setMode(PropertyDesc.WRITE);
        classDesc.setPropertyDesc(propertyDesc);
        // 順番をアルファベット順でないようにしているのは、プロパティやメソッドがアルファベット順に
        // 生成されることを検証するため。
        propertyDesc = new PropertyDescImpl("param4");
        propertyDesc.setMode(PropertyDesc.READ | PropertyDesc.WRITE);
        propertyDesc.setTypeDesc("java.lang.Integer[]", true);
        classDesc.setPropertyDesc(propertyDesc);
        propertyDesc = new PropertyDescImpl("param3");
        propertyDesc.setMode(PropertyDesc.READ | PropertyDesc.WRITE);
        classDesc.setPropertyDesc(propertyDesc);

        return classDesc;
    }

    public void testGenerateGapSource() throws Exception {
        ClassDesc classDesc = prepareClassDesc();
        classDesc.setName("com.example.dto.TestDto");

        String actual = target_.generateGapSource(classDesc);

        assertEquals(
                readResource(getClass(), "testGenerateGapSource.expected"),
                actual);
    }

    public void testGenerateBaseSource_Page() throws Exception {
        ClassDesc classDesc = prepareClassDesc();
        MethodDesc methodDesc = new MethodDescImpl("_get");
        methodDesc.setReturnTypeDesc(String.class.getName());
        methodDesc.setBodyDesc(new BodyDescImpl(
                "return \"redirect:/path/to/redirect.html\";"));
        classDesc.setMethodDesc(methodDesc);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page.expected"), actual);
    }

    public void testGenerateBaseSource_Page2() throws Exception {
        ClassDesc classDesc = new SourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(String className, ClassType type,
                    ClassCreationHintBag bag) {
                return new ClassDescImpl(className);
            }
        }.getClassDesc(HoePageBase.class);
        classDesc.setName("org.seasar.ymir.extension.freemarker.HoePage");

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page2.expected"), actual);
    }

    public void testGenerateBaseSource_Page3() throws Exception {
        ClassDesc classDesc = new SourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(String className, ClassType type,
                    ClassCreationHintBag bag) {
                return new ClassDescImpl(className);
            }
        }.getClassDesc(Hoe3PageBase.class);
        classDesc.setName("org.seasar.ymir.extension.freemarker.Hoe3Page");

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page3.expected"), actual);
    }

    @Begin
    public void testGenerateBaseSource_Page4() throws Exception {
        ClassDesc classDesc = new ClassDescImpl("com.example.web.TestPage");
        MethodDesc methodDesc = new MethodDescImpl("_permissionDenied");
        methodDesc.setThrowsDesc(new ThrowsDescImpl()
                .addThrowable(PermissionDeniedException.class));
        methodDesc
                .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                        PermissionDeniedException.class, "ex") });
        methodDesc.setBodyDesc(new BodyDescImpl(
                ConstraintInterceptor.ACTION_PERMISSIONDENIED,
                new HashMap<String, Object>()));
        methodDesc.setAnnotationDesc(new AnnotationDescImpl(
                FreemarkerSourceGeneratorTest.class.getDeclaredMethod(
                        "testGenerateBaseSource_Page4", new Class<?>[0])
                        .getAnnotation(Begin.class)));
        classDesc.setMethodDesc(methodDesc);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page4.expected"), actual);
    }

    public void testGenerateBaseSource_Page5() throws Exception {
        ClassDesc classDesc = new SourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(String className, ClassType type,
                    ClassCreationHintBag bag) {
                return new ClassDescImpl(className);
            }
        }.getClassDesc(Hoe5PageBase.class);
        classDesc.setName("org.seasar.ymir.extension.freemarker.Hoe5Page");

        ClassDesc generated = new ClassDescImpl(
                "org.seasar.ymir.extension.freemarker.Hoe5Page");
        MethodDesc methodDesc = new MethodDescImpl("_permissionDenied");
        methodDesc.setThrowsDesc(new ThrowsDescImpl()
                .addThrowable(PermissionDeniedException.class));
        methodDesc
                .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                        PermissionDeniedException.class, "ex") });
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
        ClassDesc classDesc = prepareClassDesc();
        classDesc.setName("com.example.dto.TestDto");

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Dto.expected"), actual);
    }

    public void testGenerateGapSource_Page() throws Exception {

        ClassDesc classDesc = prepareClassDesc();
        MethodDesc methodDesc = new MethodDescImpl("_get");
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
        ClassDesc classDesc = new SourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(String className, ClassType type,
                    ClassCreationHintBag bag) {
                return new ClassDescImpl(className);
            }
        }.getClassDesc(Hoe6PageBase.class);
        classDesc.setName("org.seasar.ymir.extension.freemarker.Hoe6Page");

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page6.expected"), actual);
    }

    public void testGenerateBaseSource_Converter() throws Exception {
        ClassDesc classDesc = sourceCreator_.createConverterClassDesc(
                new ClassDescImpl(HoeDto.class.getName()), new String[] {
                    Hoe.class.getName() + "<java.util.List>",
                    "java.lang.Object" }, null);
        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Converter.expected"), actual);
    }

    public void testGenerateBaseSource_Converter2_中身が空の場合() throws Exception {
        ClassDesc classDesc = sourceCreator_.createConverterClassDesc(
                new ClassDescImpl(HoeDto.class.getName()),
                new String[] { "java.lang.Object" }, null);
        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Converter2.expected"), actual);
    }
}
