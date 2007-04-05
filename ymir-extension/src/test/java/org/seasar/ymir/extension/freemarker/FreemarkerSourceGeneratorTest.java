package org.seasar.ymir.extension.freemarker;

import java.util.HashMap;

import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.YmirTestCase;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.impl.BodyDescImpl;
import org.seasar.ymir.extension.creator.impl.ClassDescImpl;
import org.seasar.ymir.extension.creator.impl.MethodDescImpl;
import org.seasar.ymir.extension.creator.impl.ParameterDescImpl;
import org.seasar.ymir.extension.creator.impl.PropertyDescImpl;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;
import org.seasar.ymir.extension.creator.impl.ThrowsDescImpl;
import org.seasar.ymir.extension.creator.mock.MockSourceCreator;

import com.example.page.TestPageBaseBase;

public class FreemarkerSourceGeneratorTest extends YmirTestCase {

    private FreemarkerSourceGenerator target_;

    protected void setUp() throws Exception {

        super.setUp();
        target_ = new FreemarkerSourceGenerator();
        target_.setSourceCreator(new MockSourceCreator() {

            public String getPagePackageName() {
                return "com.example.web";
            }

            public String getDtoPackageName() {
                return "com.example.dto";
            }

            public String getDaoPackageName() {
                return "com.example.dao";
            }

            public String getDxoPackageName() {
                return "com.example.dxo";
            }
        });
    }

    private ClassDesc prepareClassDesc() {

        ClassDesc classDesc = new ClassDescImpl("com.example.page.TestPage");
        classDesc.setSuperclass(TestPageBaseBase.class);
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
            public ClassDesc newClassDesc(String className) {
                return new ClassDescImpl(className);
            }
        }.getClassDesc(HoePageBase.class,
                "org.seasar.ymir.extension.freemarker.HoePage");

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page2.expected"), actual);
    }

    public void testGenerateBaseSource_Page3() throws Exception {

        ClassDesc classDesc = new SourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(String className) {
                return new ClassDescImpl(className);
            }
        }.getClassDesc(Hoe3PageBase.class,
                "org.seasar.ymir.extension.freemarker.Hoe3Page");

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page3.expected"), actual);
    }

    public void testGenerateBaseSource_Page4() throws Exception {

        ClassDesc classDesc = new ClassDescImpl("com.example.web.TestPage");
        MethodDesc methodDesc = new MethodDescImpl("_permissionDenied");
        methodDesc.setThrowsDesc(new ThrowsDescImpl()
                .addThrowable(PermissionDeniedException.class));
        methodDesc
                .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                        PermissionDeniedException.class, "ex") });
        methodDesc.setBodyDesc(new BodyDescImpl(
                RequestProcessor.ACTION_PERMISSIONDENIED,
                new HashMap<String, Object>()));
        classDesc.setMethodDesc(methodDesc);

        String actual = target_.generateBaseSource(classDesc);

        assertEquals(readResource(getClass(),
                "testGenerateBaseSource_Page4.expected"), actual);
    }

    public void testGenerateBaseSource_Page5() throws Exception {

        ClassDesc classDesc = new SourceCreatorImpl() {
            @Override
            public ClassDesc newClassDesc(String className) {
                return new ClassDescImpl(className);
            }
        }.getClassDesc(Hoe5PageBase.class,
                "org.seasar.ymir.extension.freemarker.Hoe5Page");

        ClassDesc generated = new ClassDescImpl(
                "org.seasar.ymir.extension.freemarker.Hoe5Page");
        MethodDesc methodDesc = new MethodDescImpl("_permissionDenied");
        methodDesc.setThrowsDesc(new ThrowsDescImpl()
                .addThrowable(PermissionDeniedException.class));
        methodDesc
                .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                        PermissionDeniedException.class, "ex") });
        methodDesc.setBodyDesc(new BodyDescImpl(
                RequestProcessor.ACTION_PERMISSIONDENIED,
                new HashMap<String, Object>()));
        generated.setMethodDesc(methodDesc);

        classDesc.merge(generated);

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

        String actual = target_.generateSource(new BodyDescImpl("test"));

        assertEquals("test", actual);
    }
}
