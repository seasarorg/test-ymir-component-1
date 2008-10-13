package org.seasar.ymir.extension.creator.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.impl.ConstraintInterceptor;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassHint;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;

import com.example.page.SourceCreatorImplTestPageBaseBase;
import com.example.page.TestPageBase;

public class SourceCreatorImplTest extends SourceCreatorImplTestBase {
    public void testGetComponentName() throws Exception {
        String actual = target_.getComponentName("/index.html", HttpMethod.GET);

        assertEquals("indexPage", actual);
    }

    public void testGetClassName1() throws Exception {
        assertNull(target_.getClassName(null));
    }

    public void testGetClassName2() throws Exception {
        String actual = target_.getClassName("indexPage");

        assertEquals("com.example.web.IndexPage", actual);
    }

    public void testGetClassName3() throws Exception {
        String actual = target_.getClassName("hoehoePage");

        assertEquals("com.example.web.HoehoePage", actual);
    }

    public void testGetClassName4() throws Exception {
        assertNull(target_.getClassName("hoehoe"));
    }

    public void testWriteSourceFile1() throws Exception {
        ClassDesc classDesc = constructClassDesc();
        File testPage = new File(ResourceUtil.getBuildDir(getClass()),
                classDesc.getName().replace('.', '/') + ".java");
        File testPageBase = new File(ResourceUtil.getBuildDir(getClass()),
                classDesc.getName().replace('.', '/') + "Base.java");

        testPage.delete();

        target_.writeSourceFile(classDesc, null);

        assertTrue(testPage.exists());
        assertTrue(testPageBase.exists());
    }

    public void testWriteSourceFile2() throws Exception {
        ClassDesc classDesc = constructClassDesc();
        File testPage = new File(ResourceUtil.getBuildDir(getClass()),
                classDesc.getName().replace('.', '/') + ".java");
        File testPageBase = new File(ResourceUtil.getBuildDir(getClass()),
                classDesc.getName().replace('.', '/') + "Base.java");

        testPage.getParentFile().mkdirs();
        testPageBase.getParentFile().mkdirs();
        OutputStream os = new FileOutputStream(testPage);
        os.write(32);
        os.close();
        os = new FileOutputStream(testPageBase);
        os.write(32);
        os.close();

        target_.writeSourceFile(classDesc, null);

        String actual = IOUtils.readString(new FileInputStream(testPage),
                "UTF-8", false);
        assertEquals(" ", actual);
        actual = IOUtils.readString(new FileInputStream(testPageBase), "UTF-8",
                false);
        assertFalse(" ".equals(actual));
    }

    public void testGatherClassDescs() throws Exception {
        Map<String, ClassDesc> classDescMap = new LinkedHashMap<String, ClassDesc>();
        ClassCreationHintBag hintBag = new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.web.TestPage", "result",
                        "java.lang.Integer", false) }, null);
        target_.gatherClassDescs(classDescMap, new PathMetaDataImpl(
                "/test.html", HttpMethod.GET, false, "testPage",
                "com.example.web.TestPage", null, null, null,
                getSourceCreator().getTemplate("/test.html")), hintBag, null);
        ClassDesc[] actual = (ClassDesc[]) classDescMap.values().toArray(
                new ClassDesc[0]);

        assertNotNull(actual);
        assertEquals(2, actual.length);
        assertEquals("com.example.web.TestPage", actual[0].getName());
        assertEquals("com.example.dto.EntityDto", actual[1].getName());
        MethodDesc md = actual[0].getMethodDesc(new MethodDescImpl("_get"));
        assertNotNull(md);
        assertEquals(TypeDesc.TYPE_VOID, md.getReturnTypeDesc().getName());
        assertNotNull(actual[0].getMethodDesc(new MethodDescImpl("_prerender")));
        assertEquals("Integer", actual[0].getPropertyDesc("result")
                .getTypeDesc().getName());
        md = new MethodDescImpl("_validationFailed");
        md.setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                Notes.class) });
        assertNotNull(actual[0].getMethodDesc(md));
        md = new MethodDescImpl("_permissionDenied");
        md.setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                PermissionDeniedException.class) });
        md = actual[0].getMethodDesc(md);
        assertNotNull(md);
        assertNotNull(md.getAnnotationDesc(Begin.class.getName()));
    }

    public void testGatherClassDescs2_hintとして外部のDtoクラスを指定していても正しく利用されること()
            throws Exception {
        Map<String, ClassDesc> classDescMap = new LinkedHashMap<String, ClassDesc>();
        ClassCreationHintBag hintBag = new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.web.TestPage", "result",
                        "com.outer.dto.EntryDto", false) }, null);
        target_.gatherClassDescs(classDescMap, new PathMetaDataImpl(
                "/test.html", HttpMethod.GET, false, "testPage",
                "com.example.web.TestPage", null, null, null,
                getSourceCreator().getTemplate("/test.html")), hintBag, null);
        ClassDesc[] actual = (ClassDesc[]) classDescMap.values().toArray(
                new ClassDesc[0]);

        assertEquals("com.outer.dto.EntryDto", actual[0].getPropertyDesc(
                "result").getTypeDesc().getName());
    }

    public void testGatherClassDescs2_外部のDtoクラスは自動生成対象にならないこと()
            throws Exception {
        Map<String, ClassDesc> classDescMap = new LinkedHashMap<String, ClassDesc>();
        ClassCreationHintBag hintBag = new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.web.TestPage", "result",
                        "com.outer.dto.EntryDto", false) }, null);
        target_.gatherClassDescs(classDescMap, new PathMetaDataImpl(
                "/test.html", HttpMethod.GET, false, "testPage",
                "com.example.web.TestPage", null, null, null,
                getSourceCreator().getTemplate("/test.html")), hintBag, null);

        assertNull(classDescMap.get("com.outer.dto.EntryDto"));
    }

    public void testNewClassDesc() throws Exception {
        assertEquals("マッチしたパターンに関連付けられているスーパークラス名がセットされていること",
                "com.example.web.HndexPageBaseBase", target_.newClassDesc(
                        "com.example.web.HndexPage", null).getSuperclassName());

        assertEquals(
                "マッチするパターンが無くデフォルトのスーパークラス名が指定されている場合はデフォルトのスーパークラス名がセットされていること",
                "com.example.page.TestPageBaseBase", target_.newClassDesc(
                        "com.example.page.TestPage", null).getSuperclassName());

        assertEquals("マッチしたパターンに関連付けられているスーパークラス名よりも実際のスーパークラス名が優先されること",
                "com.example.web.IndexPageBaseBase", target_.newClassDesc(
                        "com.example.web.IndexPage", null).getSuperclassName());
    }

    public void testNewClassDesc_hintが指定されていればスーパークラス名がhintを基に設定されること()
            throws Exception {
        assertEquals("com.example.web.PageBase", target_.newClassDesc(
                "com.example.web.IndexPage",
                new ClassCreationHintBag(null,
                        new ClassHint[] { new ClassHint(
                                "com.example.web.IndexPage",
                                "com.example.web.PageBase") }))
                .getSuperclassName());
    }

    public void testAdjustByExistentClass() throws Exception {
        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge3Page");
        cd.setSuperclassName(Merge3PageBaseBase.class.getName());
        PropertyDesc pd = new PropertyDescImpl("hoe");
        pd.addMode(PropertyDesc.READ);
        pd.addMode(PropertyDesc.WRITE);
        cd.setPropertyDesc(pd);
        MethodDesc addMd = new MethodDescImpl("add");
        addMd.setReturnTypeDesc(new TypeDescImpl(Integer.TYPE));
        addMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(Integer.TYPE),
            new ParameterDescImpl(Integer.TYPE) });
        cd.setMethodDesc(addMd);
        MethodDesc subMd = new MethodDescImpl("sub");
        subMd.setReturnTypeDesc(new TypeDescImpl(Integer.TYPE));
        subMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(Integer.TYPE),
            new ParameterDescImpl(Integer.TYPE) });
        cd.setMethodDesc(subMd);
        MethodDesc timesMd = new MethodDescImpl("times");
        timesMd.setReturnTypeDesc(new TypeDescImpl(Integer.TYPE));
        timesMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(Integer.TYPE),
            new ParameterDescImpl(Integer.TYPE) });
        cd.setMethodDesc(timesMd);
        MethodDesc divMd = new MethodDescImpl("div");
        divMd.setReturnTypeDesc(new TypeDescImpl(Integer.TYPE));
        divMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(Integer.TYPE),
            new ParameterDescImpl(Integer.TYPE) });
        cd.setMethodDesc(divMd);
        MethodDesc incMd = new MethodDescImpl("inc");
        incMd.setReturnTypeDesc(new TypeDescImpl(Integer.TYPE));
        incMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(Integer.TYPE),
            new ParameterDescImpl(Integer.TYPE) });
        cd.setMethodDesc(incMd);

        target_.adjustByExistentClass(cd);

        assertEquals(2, cd.getPropertyDescs().length);
        assertFalse("スーパークラスだけが持っているプロパティのアクセッサは除去されること", cd.getPropertyDesc(
                "hoe").isReadable());
        assertTrue("スーパークラスが持っていないプロパティのアクセッサは除去されないこと", cd.getPropertyDesc(
                "hoe").isWritable());
        assertTrue("スーパークラスが持っている場合でも、Baseが持っていればプロパティのアクセッサは除去されないこと", cd
                .getPropertyDesc("fuga").isWritable());
        assertEquals(3, cd.getMethodDescs().length);
        assertNull("スーパークラスだけが持っているメソッドは除去されること", cd.getMethodDesc(addMd));
        assertNotNull("スーパークラスが持っている場合でも、Baseが持っていればメソッドは除去されないこと", cd
                .getMethodDesc(incMd));
        assertNull("サブクラスだけが持っているメソッドは除去されること", cd.getMethodDesc(subMd));
        assertNotNull("サブクラスが持っている場合でも、Baseが持っていればメソッドは除去されないこと", cd
                .getMethodDesc(divMd));
    }

    public void testAdjustByExistentClass2() throws Exception {
        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge2Page");
        cd.setSuperclassName(Merge2PageBaseBase.class.getName());
        MethodDesc md = new MethodDescImpl("_render");
        md.setReturnTypeDesc(new TypeDescImpl(Void.TYPE));
        md.setParameterDescs(new ParameterDesc[0]);
        cd.setMethodDesc(md);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getMethodDesc(md));
    }

    public void testGetClassDesc_bodyアノテーションが付与されている場合はボディが復元されること()
            throws Exception {
        ClassDesc cd = target_.getClassDesc(Class1Base.class);

        BodyDesc actual = cd.getMethodDescs()[0].getBodyDesc();

        assertEquals("return \"return value\";", actual.getRoot().get("body"));
    }

    public void testGetClassDesc_プロパティのGetterとSetterに付与したAnnotationが保持されること()
            throws Exception {
        ClassDesc cd = target_.getClassDesc(Class2Base.class);

        AnnotationDesc[] ads = cd.getPropertyDesc("value")
                .getAnnotationDescsForGetter();
        assertEquals(1, ads.length);
        assertEquals(Out.class.getName(), ads[0].getName());

        ads = cd.getPropertyDesc("value").getAnnotationDescsForSetter();
        assertEquals(1, ads.length);
        assertEquals(In.class.getName(), ads[0].getName());
    }

    public void testGetClassDesc_YMIR_191_フォームDtoのfieldが保存されること()
            throws Exception {
        ClassDesc cd = target_.getClassDesc(Class3Base.class);

        PropertyDesc pd = cd.getPropertyDesc("form");
        assertNotNull(pd);
        assertNotNull(pd.getAnnotationDesc(Meta.class.getName()));
    }

    public void testFilterResponse() throws Exception {
        assertEquals(
                "<html><head>"
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/prototype/prototype.js\"></script>"
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/scriptaculous/scriptaculous.js\"></script>"
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/sourceCreator.js\"></script>"
                        + "</head><body><div id=\"__ymir__controlPanel\">"
                        + "<form action=\"/context/path\" method=\"post\">"
                        + "<input type=\"hidden\" name=\"__ymir__task\" value=\"systemConsole\" />"
                        + "<input type=\"hidden\" name=\"__ymir__method\" value=\"GET\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"a%26%3F\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"b\" />"
                        + "<input type=\"hidden\" name=\"bbb\" value=\"c\" />"
                        + "<input type=\"submit\" value=\"[TO SYSTEM CONSOLE]\" />"
                        + "</form></div></body></html>",
                target_
                        .filterResponse("<html><head></head><body></body></html>"));
    }

    public void testAdjustByExistentClass_既にギャップクラスがあるがベースクラスがない場合にObjectのメソッドがベースクラスに追加されないこと()
            throws Exception {
        getConfiguration().removeProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_SUPERCLASS);

        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge5Page");

        target_.adjustByExistentClass(cd);

        assertNull(cd.getPropertyDesc("class"));
    }

    public void testAdjustByExistentClass2_validationFailedなどがスーパークラスにある時は自動生成されないこと()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(
                "com.example.page.SourceCreatorImplTestPage");
        cd.setSuperclassName(SourceCreatorImplTestPageBaseBase.class.getName());
        MethodDescImpl md = new MethodDescImpl(
                ConstraintInterceptor.ACTION_VALIDATIONFAILED);
        md.setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                Notes.class) });
        cd.setMethodDesc(md);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getMethodDesc(md));
    }

    public void testAdjustByExistentClass3_Baseクラスの親クラス情報は維持されること()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge3Page");
        cd.setSuperclassName(TestPageBase.class.getName());

        target_.adjustByExistentClass(cd);

        assertEquals("もともとの親クラスが維持されること", TestPageBase.class.getName(), cd
                .getSuperclassName());
    }

    public void testAdjustByExistentClass5_Baseクラスのabstract状態が維持されること()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge7Page");
        cd.setSuperclassName(TestPageBase.class.getName());

        target_.adjustByExistentClass(cd);

        assertTrue(cd.isBaseClassAbstract());
    }

    public void testAdjustByExistentClass6_プロパティのGetterやSetterがスーパークラスにある時は自動生成されないこと()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge8Page");
        PropertyDescImpl pd = new PropertyDescImpl("value");
        pd.setMode(PropertyDesc.READ);
        cd.setPropertyDesc(pd);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getPropertyDesc("value"));
    }

    public void testAdjustByExistentClass9_BaseクラスのAnnotationが上書きされること()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge9Page");
        cd.setAnnotationDesc(new MetaAnnotationDescImpl("meta",
                new String[] { "newValue" }, new Class[0]));

        target_.adjustByExistentClass(cd);

        MetaAnnotationDesc actual = (MetaAnnotationDesc) cd
                .getAnnotationDesc(Meta.class.getName());
        assertNotNull(actual);
        assertEquals("newValue", actual.getValue("meta"));
    }

    public void testAdjustByExistentClass10_Baseクラスのメソッドの返り値型()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge10Page");
        MethodDesc getMd = new MethodDescImpl("_get");
        getMd.setReturnTypeDesc(new TypeDescImpl(Void.TYPE));
        getMd.setParameterDescs(new ParameterDesc[0]);
        cd.setMethodDesc(getMd);
        MethodDesc postMd = new MethodDescImpl("_post");
        postMd.setReturnTypeDesc(new TypeDescImpl(Void.TYPE));
        postMd.setParameterDescs(new ParameterDesc[0]);
        cd.setMethodDesc(postMd);

        target_.adjustByExistentClass(cd);

        assertEquals("Gapクラスに同じメソッドがある場合は返り値型が同じになること", "String", cd
                .getMethodDesc(getMd).getReturnTypeDesc().getName());
        assertEquals("Superクラスに同じメソッドがある場合は返り値型が同じになること", "String", cd
                .getMethodDesc(postMd).getReturnTypeDesc().getName());
    }

    public void testAdjustByExistentClass11_FormDtoフィールドがsuperクラスにある場合はプロパティが除去されれること()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge11Page");
        PropertyDesc pd = new PropertyDescImpl("hoehoe");
        cd.setPropertyDesc(pd);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("property",
                new String[] { "hoehoe" }, new Class[0]));
        cd
                .setSuperclassName("org.seasar.ymir.extension.creator.impl.Merge11PageBaseBase");

        target_.adjustByExistentClass(cd);

        assertNull(cd.getPropertyDesc("hoehoe"));
    }

    public void testGetBeginAnnotation() throws Exception {
        Begin actual = target_.getBeginAnnotation();

        assertNotNull(actual);
    }

    public void testCreateControlPanelFormHTML() throws Exception {
        MockRequest request = new MockRequest();
        request.setParameterValues("aaa", new String[] { "a&?", "b" });
        request.setParameter("bbb", "c");
        request.setParameter(SourceCreator.PARAM_PREFIX + "hoehoe", "c");
        request.setMethod(HttpMethod.GET);
        MockDispatch dispatch = new MockDispatch();
        dispatch.setAbsolutePath("/context/path");
        request.enterDispatch(dispatch);

        String actual = target_.createControlPanelFormHTML(request);

        assertEquals(
                "<form action=\"/context/path\" method=\"post\"><input type=\"hidden\" name=\"__ymir__task\" value=\"systemConsole\" /><input type=\"hidden\" name=\"__ymir__method\" value=\"GET\" /><input type=\"hidden\" name=\"aaa\" value=\"a%26%3F\" /><input type=\"hidden\" name=\"aaa\" value=\"b\" /><input type=\"hidden\" name=\"bbb\" value=\"c\" /><input type=\"submit\" value=\"[TO SYSTEM CONSOLE]\" /></form>",
                actual);
    }

    public void testIsFormDtoFieldPresent() throws Exception {
        assertFalse(target_.isFormDtoFieldPresent(new ClassDescImpl(
                Object.class.getName()), "hoehoe"));

        assertFalse(target_.isFormDtoFieldPresent(new ClassDescImpl(
                Merge10Page.class.getName()), "hoehoe"));

        assertTrue(target_.isFormDtoFieldPresent(new ClassDescImpl(
                Merge11Page.class.getName()), "hoehoe"));
    }

    public void testGetClassDesc_YMIR_226_aNameのようなプロパティを正しく検出できること1()
            throws Exception {
        ClassDesc actual = target_.getClassDesc(new Object() {
            private String aName_;

            public String getAName() {
                return aName_;
            }

            public void setAName(String aName) {
                aName_ = aName;
            }
        }.getClass(), true);

        assertNull(actual.getPropertyDesc("AName"));
        assertNotNull(actual.getPropertyDesc("aName"));
    }

    public void testGetClassDesc_YMIR_226_aNameのようなプロパティを正しく検出できること2()
            throws Exception {
        ClassDesc actual = target_.getClassDesc(new Object() {
            @org.seasar.ymir.annotation.Meta(name = "property", value = "hoehoe")
            protected HoehoeDto hoehoe_ = new HoehoeDto();

            @org.seasar.ymir.annotation.Meta(name = "formProperty", value = "hoehoe")
            public String getAName() {
                return hoehoe_.getAName();
            }

            @org.seasar.ymir.annotation.Meta(name = "formProperty", value = "hoehoe")
            public void setAName(String aName) {
                hoehoe_.setAName(aName);
            }
        }.getClass(), true);

        assertNull(actual.getPropertyDesc("AName"));
        assertNotNull(actual.getPropertyDesc("aName"));
    }

    public void testNewClassDesc_superclassがトラバースされること() throws Exception {
        Configuration configuration = (Configuration) container_
                .getComponent(Configuration.class);
        configuration.setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_SUPERCLASS,
                "com.example.web.TraversePageBase");

        assertEquals("com.example.web.aaa1.bbb1.TraversePageBase", target_
                .newClassDesc("com.example.web.aaa1.bbb1.Traverse1Page", null)
                .getSuperclassName());

        assertEquals("com.example.web.aaa1.TraversePageBase", target_
                .newClassDesc("com.example.web.aaa1.bbb2.Traverse2Page", null)
                .getSuperclassName());

        assertEquals("com.example.web.TraversePageBase", target_.newClassDesc(
                "com.example.web.aaa2.bbb.TraversePage", null)
                .getSuperclassName());
    }

    public void testAdjustByExistentClass_Baseにあるメソッドのアノテーションとボディが保持されること()
            throws Exception {
        ClassDesc classDesc = target_.getClassDesc(AdjustPage.class);
        target_.adjustByExistentClass(classDesc);

        MethodDesc actual = classDesc.getMethodDesc(new MethodDescImpl("_get"));
        assertNotNull(actual);
        assertNotNull(actual.getMetaFirstValue(Globals.META_NAME_SOURCE));
        assertNotNull(actual.getBodyDesc());
    }
}
