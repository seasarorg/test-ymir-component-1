package org.seasar.ymir.extension.creator.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.constraint.ConstraintInterceptor;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescSet;
import org.seasar.ymir.extension.creator.ClassHint;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;

import com.example.page.SourceCreatorImplTestPageBaseBase;
import com.example.page.TestPageBase;
import com.example.web.SourceCreatorImplTest2Page;

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
        File testPage = new File(getSourceDir(), classDesc.getName().replace(
                '.', '/')
                + ".java");
        File testPageBase = new File(getSourceDir(), classDesc.getName()
                .replace('.', '/')
                + "Base.java");

        testPage.delete();

        target_.writeSourceFile(classDesc, null);

        assertTrue(testPage.exists());
        assertTrue(testPageBase.exists());
    }

    public void testWriteSourceFile2_Baseは上書きされるがGapは上書きされないこと()
            throws Exception {
        ClassDesc classDesc = constructClassDesc();
        File testPage = new File(getSourceDir(), classDesc.getName().replace(
                '.', '/')
                + ".java");
        File testPageBase = new File(getSourceDir(), classDesc.getName()
                .replace('.', '/')
                + "Base.java");

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
        ClassCreationHintBag hintBag = new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.web.TestPage", "result",
                        "java.lang.Integer") }, null);
        DescPool pool = DescPool.newInstance(target_, hintBag);
        target_.gatherClassDescs(pool,
                new PathMetaDataImpl("/test.html", HttpMethod.GET, false,
                        "testPage", "com.example.web.TestPage", null, null,
                        null, getSourceCreator().getTemplate("/test.html")),
                null);
        ClassDesc[] actual = pool.getGeneratedClassDescs().toArray(
                new ClassDesc[0]);
        Arrays.sort(actual, new Comparator<ClassDesc>() {
            public int compare(ClassDesc o1, ClassDesc o2) {
                return o2.getName().compareTo(o1.getName());
            }
        });

        assertNotNull(actual);
        assertEquals(2, actual.length);
        assertEquals("com.example.web.TestPage", actual[0].getName());
        assertEquals("com.example.dto.EntityDto", actual[1].getName());
        MethodDesc md = actual[0]
                .getMethodDesc(new MethodDescImpl(pool, "_get"));
        assertNotNull(md);
        assertEquals(Void.TYPE.getName(), md.getReturnTypeDesc().getName());
        assertNotNull(actual[0].getMethodDesc(new MethodDescImpl(pool,
                "_prerender")));
        assertEquals("Integer", actual[0].getPropertyDesc("result")
                .getTypeDesc().getName());
        md = new MethodDescImpl(pool, "_validationFailed");
        md.setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(pool,
                Notes.class) });
        assertNotNull(actual[0].getMethodDesc(md));
        md = new MethodDescImpl(pool, "_permissionDenied");
        md.setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(pool,
                PermissionDeniedException.class) });
        md = actual[0].getMethodDesc(md);
        assertNotNull(md);
    }

    public void testGatherClassDescs2_hintとして外部のDtoクラスを指定していても正しく利用されること()
            throws Exception {
        ClassCreationHintBag hintBag = new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.web.TestPage", "result",
                        "com.outer.dto.EntryDto") }, null);
        DescPool pool = DescPool.newInstance(target_, hintBag);
        target_.gatherClassDescs(pool,
                new PathMetaDataImpl("/test.html", HttpMethod.GET, false,
                        "testPage", "com.example.web.TestPage", null, null,
                        null, getSourceCreator().getTemplate("/test.html")),
                null);

        assertTrue(pool.contains("com.example.web.TestPage"));
        assertEquals("com.outer.dto.EntryDto", pool.getClassDesc(
                "com.example.web.TestPage").getPropertyDesc("result")
                .getTypeDesc().getName());
    }

    public void testGatherClassDescs2_外部のDtoクラスは自動生成対象にならないこと()
            throws Exception {
        ClassCreationHintBag hintBag = new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.web.TestPage", "result",
                        "com.outer.dto.EntryDto") }, null);
        DescPool pool = DescPool.newInstance(target_, hintBag);
        target_.gatherClassDescs(pool,
                new PathMetaDataImpl("/test.html", HttpMethod.GET, false,
                        "testPage", "com.example.web.TestPage", null, null,
                        null, getSourceCreator().getTemplate("/test.html")),
                null);

        assertFalse(pool.contains("com.outer.dto.EntryDto"));
    }

    public void testNewClassDesc() throws Exception {
        assertEquals("マッチしたパターンに関連付けられているスーパークラス名がセットされていること",
                "com.example.web.HndexPageBaseBase", target_.newClassDesc(
                        DescPool.newInstance(target_, null),
                        "com.example.web.HndexPage", null).getSuperclassName());

        assertEquals(
                "マッチするパターンが無くデフォルトのスーパークラス名が指定されている場合はデフォルトのスーパークラス名がセットされていること",
                "com.example.page.TestPageBaseBase", target_.newClassDesc(
                        DescPool.newInstance(target_, null),
                        "com.example.page.TestPage", null).getSuperclassName());

        assertEquals("マッチしたパターンに関連付けられているスーパークラス名よりも実際のスーパークラス名が優先されること",
                "com.example.web.IndexPageBaseBase", target_.newClassDesc(
                        DescPool.newInstance(target_, null),
                        "com.example.web.IndexPage", null).getSuperclassName());
    }

    public void testNewClassDesc_hintが指定されていればスーパークラス名がhintを基に設定されること()
            throws Exception {
        ClassHint classHint = new ClassHint("com.example.web.IndexPage");
        classHint.setSuperclassName("com.example.web.PageBase");
        assertEquals("com.example.web.PageBase", target_.newClassDesc(
                DescPool.newInstance(target_, null),
                "com.example.web.IndexPage",
                new ClassCreationHintBag(null, new ClassHint[] { classHint }))
                .getSuperclassName());
    }

    public void testAdjustByExistentClass() throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc cd = new ClassDescImpl(pool,
                "org.seasar.ymir.extension.creator.impl.Merge3Page");
        cd.setSuperclassName(Merge3PageBaseBase.class.getName());
        PropertyDesc pd = new PropertyDescImpl(pool, "hoe");
        pd.addMode(PropertyDesc.READ);
        pd.addMode(PropertyDesc.WRITE);
        cd.setPropertyDesc(pd);
        MethodDesc addMd = new MethodDescImpl(pool, "add");
        addMd.setReturnTypeDesc(new TypeDescImpl(pool, Integer.TYPE));
        addMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool, Integer.TYPE),
            new ParameterDescImpl(pool, Integer.TYPE) });
        cd.setMethodDesc(addMd);
        MethodDesc subMd = new MethodDescImpl(pool, "sub");
        subMd.setReturnTypeDesc(new TypeDescImpl(pool, Integer.TYPE));
        subMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool, Integer.TYPE),
            new ParameterDescImpl(pool, Integer.TYPE) });
        cd.setMethodDesc(subMd);
        MethodDesc timesMd = new MethodDescImpl(pool, "times");
        timesMd.setReturnTypeDesc(new TypeDescImpl(pool, Integer.TYPE));
        timesMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool, Integer.TYPE),
            new ParameterDescImpl(pool, Integer.TYPE) });
        cd.setMethodDesc(timesMd);
        MethodDesc divMd = new MethodDescImpl(pool, "div");
        divMd.setReturnTypeDesc(new TypeDescImpl(pool, Integer.TYPE));
        divMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool, Integer.TYPE),
            new ParameterDescImpl(pool, Integer.TYPE) });
        cd.setMethodDesc(divMd);
        MethodDesc incMd = new MethodDescImpl(pool, "inc");
        incMd.setReturnTypeDesc(new TypeDescImpl(pool, Integer.TYPE));
        incMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool, Integer.TYPE),
            new ParameterDescImpl(pool, Integer.TYPE) });
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
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc cd = new ClassDescImpl(pool,
                "org.seasar.ymir.extension.creator.impl.Merge2Page");
        cd.setSuperclassName(Merge2PageBaseBase.class.getName());
        MethodDesc md = new MethodDescImpl(pool, "_render");
        md.setReturnTypeDesc(new TypeDescImpl(pool, Void.TYPE));
        md.setParameterDescs(new ParameterDesc[0]);
        cd.setMethodDesc(md);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getMethodDesc(md));
    }

    public void testGetClassDesc_bodyアノテーションが付与されている場合はボディが復元されること()
            throws Exception {
        ClassDesc cd = target_.newClassDesc(
                DescPool.newInstance(target_, null), Class1Base.class, true);

        BodyDesc actual = cd.getMethodDescs()[0].getBodyDesc();

        assertEquals("return \"return value\";", actual.getRoot().get("body"));
    }

    public void testGetClassDesc_プロパティのGetterとSetterに付与したAnnotationが保持されること()
            throws Exception {
        ClassDesc cd = target_.newClassDesc(
                DescPool.newInstance(target_, null), Class2Base.class, true);

        AnnotationDesc[] ads = cd.getPropertyDesc("value")
                .getAnnotationDescsOnGetter();
        assertEquals(1, ads.length);
        assertEquals(Out.class.getName(), ads[0].getName());

        ads = cd.getPropertyDesc("value").getAnnotationDescsOnSetter();
        assertEquals(1, ads.length);
        assertEquals(In.class.getName(), ads[0].getName());
    }

    public void testGetClassDesc_YMIR_191_フォームDtoのfieldが保存されること()
            throws Exception {
        ClassDesc cd = target_.newClassDesc(
                DescPool.newInstance(target_, null), Class3Base.class, true);

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
                        + "</head><body><div id=\"__ymir__inPlaceEditor\">BODY</div>"
                        + "<div id=\"__ymir__controlPanel\">"
                        + "<form action=\"/context/path\" method=\"post\">"
                        + "<input type=\"hidden\" name=\"__ymir__task\" value=\"systemConsole\" />"
                        + "<input type=\"hidden\" name=\"__ymir__method\" value=\"GET\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"a%26%3F\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"b\" />"
                        + "<input type=\"hidden\" name=\"bbb\" value=\"c\" />"
                        + "<input type=\"submit\" value=\"SYSTEM CONSOLE\" />"
                        + "</form>"
                        + "<form action=\"/context/path\" method=\"post\">"
                        + "<input type=\"hidden\" name=\"__ymir__task\" value=\"updateClasses\" />"
                        + "<input type=\"hidden\" name=\"__ymir__method\" value=\"GET\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"a%26%3F\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"b\" />"
                        + "<input type=\"hidden\" name=\"bbb\" value=\"c\" />"
                        + "<input type=\"submit\" value=\"UPDATE CLASSES\" />"
                        + "</form></div></body></html>",
                target_
                        .filterResponse("<html><head></head><body>BODY</body></html>"));
    }

    public void testAdjustByExistentClass_既にギャップクラスがあるがベースクラスがない場合にObjectのメソッドがベースクラスに追加されないこと()
            throws Exception {
        getConfiguration().removeProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_SUPERCLASS);

        ClassDesc cd = new ClassDescImpl(DescPool.newInstance(target_, null),
                "org.seasar.ymir.extension.creator.impl.Merge5Page");

        target_.adjustByExistentClass(cd);

        assertNull(cd.getPropertyDesc("class"));
    }

    public void testAdjustByExistentClass2_validationFailedなどがスーパークラスにある時は自動生成されないこと()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc cd = new ClassDescImpl(pool,
                "com.example.page.SourceCreatorImplTestPage");
        cd.setSuperclassName(SourceCreatorImplTestPageBaseBase.class.getName());
        MethodDescImpl md = new MethodDescImpl(pool,
                ConstraintInterceptor.ACTION_VALIDATIONFAILED);
        md.setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(pool,
                Notes.class) });
        cd.setMethodDesc(md);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getMethodDesc(md));
    }

    public void testAdjustByExistentClass3_Baseクラスの親クラス情報は維持されること()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(DescPool.newInstance(target_, null),
                "org.seasar.ymir.extension.creator.impl.Merge3Page");
        cd.setSuperclassName(TestPageBase.class.getName());

        target_.adjustByExistentClass(cd);

        assertEquals("もともとの親クラスが維持されること", TestPageBase.class.getName(), cd
                .getSuperclassName());
    }

    public void testAdjustByExistentClass5_Baseクラスのabstract状態が維持されること()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(DescPool.newInstance(target_, null),
                "org.seasar.ymir.extension.creator.impl.Merge7Page");
        cd.setSuperclassName(TestPageBase.class.getName());

        target_.adjustByExistentClass(cd);

        assertTrue(cd.isAbstract());
    }

    public void testAdjustByExistentClass6_プロパティのGetterやSetterがスーパークラスにある時は自動生成されないこと()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc cd = new ClassDescImpl(pool,
                "org.seasar.ymir.extension.creator.impl.Merge8Page");
        PropertyDescImpl pd = new PropertyDescImpl(pool, "value");
        pd.setMode(PropertyDesc.READ);
        cd.setPropertyDesc(pd);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getPropertyDesc("value"));
    }

    public void testAdjustByExistentClass9_BaseクラスのAnnotationが上書きされること()
            throws Exception {
        ClassDesc cd = new ClassDescImpl(DescPool.newInstance(target_, null),
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
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc cd = new ClassDescImpl(pool,
                "org.seasar.ymir.extension.creator.impl.Merge10Page");
        MethodDesc getMd = new MethodDescImpl(pool, "_get");
        getMd.setReturnTypeDesc(new TypeDescImpl(pool, Void.TYPE));
        getMd.setParameterDescs(new ParameterDesc[0]);
        cd.setMethodDesc(getMd);
        MethodDesc postMd = new MethodDescImpl(pool, "_post");
        postMd.setReturnTypeDesc(new TypeDescImpl(pool, Void.TYPE));
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
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc cd = new ClassDescImpl(pool,
                "org.seasar.ymir.extension.creator.impl.Merge11Page");
        PropertyDesc pd = new PropertyDescImpl(pool, "hoehoe");
        cd.setPropertyDesc(pd);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("property",
                new String[] { "hoehoe" }, new Class[0]));
        cd
                .setSuperclassName("org.seasar.ymir.extension.creator.impl.Merge11PageBaseBase");

        target_.adjustByExistentClass(cd);

        assertNull(cd.getPropertyDesc("hoehoe"));
    }

    public void test_adjustByExistentClass11_Gapクラスに同名のアクションメソッドがある場合はBaseクラスにアクションメソッドが生成されないこと()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc cd = new ClassDescImpl(pool, Merge12Page.class.getName());
        MethodDesc md = new MethodDescImpl(pool, "_get");
        md.setAttribute(Globals.ATTR_ACTION, Boolean.TRUE);
        cd.setMethodDesc(md);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getMethodDesc(md));
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
                "<form action=\"/context/path\" method=\"post\"><input type=\"hidden\" name=\"__ymir__task\" value=\"systemConsole\" /><input type=\"hidden\" name=\"__ymir__method\" value=\"GET\" /><input type=\"hidden\" name=\"aaa\" value=\"a%26%3F\" /><input type=\"hidden\" name=\"aaa\" value=\"b\" /><input type=\"hidden\" name=\"bbb\" value=\"c\" /><input type=\"submit\" value=\"SYSTEM CONSOLE\" /></form>",
                actual);
    }

    public void testIsFormDtoFieldPresent() throws Exception {
        assertFalse(target_.isFormDtoFieldPresent(new ClassDescImpl(DescPool
                .newInstance(target_, null), Object.class.getName()), "hoehoe"));

        assertFalse(target_.isFormDtoFieldPresent(new ClassDescImpl(DescPool
                .newInstance(target_, null), Merge10Page.class.getName()),
                "hoehoe"));

        assertTrue(target_.isFormDtoFieldPresent(new ClassDescImpl(DescPool
                .newInstance(target_, null), Merge11Page.class.getName()),
                "hoehoe"));
    }

    public void testGetClassDesc_YMIR_226_aNameのようなプロパティを正しく検出できること1()
            throws Exception {
        ClassDesc actual = target_.newClassDesc(DescPool.newInstance(target_,
                null), new Object() {
            private String aName_;

            @SuppressWarnings("unused")
            public String getAName() {
                return aName_;
            }

            @SuppressWarnings("unused")
            public void setAName(String aName) {
                aName_ = aName;
            }
        }.getClass(), true);

        assertNull(actual.getPropertyDesc("AName"));
        assertNotNull(actual.getPropertyDesc("aName"));
    }

    public void testGetClassDesc_YMIR_226_aNameのようなプロパティを正しく検出できること2()
            throws Exception {
        ClassDesc actual = target_.newClassDesc(DescPool.newInstance(target_,
                null), new Object() {
            @org.seasar.ymir.annotation.Meta(name = "property", value = "hoehoe")
            protected HoehoeDto hoehoe_ = new HoehoeDto();

            @SuppressWarnings("unused")
            @org.seasar.ymir.annotation.Meta(name = "formProperty", value = "hoehoe")
            public String getAName() {
                return hoehoe_.getAName();
            }

            @SuppressWarnings("unused")
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
                .newClassDesc(DescPool.newInstance(target_, null),
                        "com.example.web.aaa1.bbb1.Traverse1Page", null)
                .getSuperclassName());

        assertEquals("com.example.web.aaa1.TraversePageBase", target_
                .newClassDesc(DescPool.newInstance(target_, null),
                        "com.example.web.aaa1.bbb2.Traverse2Page", null)
                .getSuperclassName());

        assertEquals("com.example.web.TraversePageBase", target_.newClassDesc(
                null, "com.example.web.aaa2.bbb.TraversePage", null)
                .getSuperclassName());
    }

    public void testAdjustByExistentClass_Baseにあるメソッドのアノテーションとボディが保持されること()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc classDesc = target_
                .newClassDesc(pool, AdjustPage.class, true);
        target_.adjustByExistentClass(classDesc);

        MethodDesc actual = classDesc.getMethodDesc(new MethodDescImpl(pool,
                "_get"));
        assertNotNull(actual);
        assertNotNull(actual.getMetaFirstValue(Globals.META_NAME_SOURCE));
        assertNotNull(actual.getBodyDesc());
    }

    public void testAdjustByExistentClass_Baseにあるプロパティの初期値情報が正しくマージされること()
            throws Exception {
        ClassDesc classDesc = target_.newClassDesc(DescPool.newInstance(
                target_, null), Adjust4Page.class, false);
        classDesc.getPropertyDesc("list").getTypeDesc()
                .setCollectionImplementationClassName(null);
        target_.adjustByExistentClass(classDesc);

        PropertyDesc actual = classDesc.getPropertyDesc("list");
        assertNotNull(actual);
        assertEquals("new org.seasar.ymir.util.FlexibleList<String>()", actual
                .getInitialValue());
    }

    public void testAdjustByExistentClass_Baseにあるプロパティの初期値情報よりもClassDescのプロパティの型情報が優先されること()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc classDesc = target_.newClassDesc(pool, Adjust5Page.class,
                false);
        classDesc.getPropertyDesc("list").getTypeDesc().setComponentClassDesc(
                new ClassDescImpl(pool, String.class.getName()));
        classDesc.getPropertyDesc("list").getTypeDesc()
                .setCollectionImplementationClassName(null);
        target_.adjustByExistentClass(classDesc);

        PropertyDesc actual = classDesc.getPropertyDesc("list");
        assertNotNull(actual);
        assertEquals("new org.seasar.ymir.util.FlexibleList<String>()", actual
                .getInitialValue());
    }

    public void testAdjustByExistentClass2_由来が同じプロパティのうち生成されたClassDescに含まれていないものが削除されること()
            throws Exception {
        ClassDesc classDesc = new ClassDescImpl(DescPool.newInstance(target_,
                null), Adjust2Page.class.getName());
        classDesc.setBornOf("/adjust2.html");
        classDesc.addProperty("param4", PropertyDesc.READ | PropertyDesc.WRITE);

        target_.adjustByExistentClass(classDesc);

        PropertyDesc pd = classDesc.getPropertyDesc("param1");
        assertNotNull(pd);
        assertFalse(pd.isReadable());
        assertTrue(pd.isWritable());

        pd = classDesc.getPropertyDesc("param2");
        assertNotNull(pd);
        assertFalse(pd.isReadable());
        assertFalse(pd.isWritable());

        pd = classDesc.getPropertyDesc("param3");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertTrue(pd.isWritable());

        pd = classDesc.getPropertyDesc("param4");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertTrue(pd.isWritable());

        assertNull(classDesc.getPropertyDesc("param5"));
    }

    public void testAdjustByExistentClass3_由来が同じメソッドのうち生成されたClassDescに含まれていないものが削除されること()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc classDesc = new ClassDescImpl(pool, Adjust3Page.class
                .getName());
        classDesc.setBornOf("/adjust3.html");
        classDesc.setMethodDesc(new MethodDescImpl(pool, "_get_write"));

        target_.adjustByExistentClass(classDesc);

        MethodDesc md = classDesc
                .getMethodDesc(new MethodDescImpl(pool, "_get"));
        assertNotNull(md);

        assertNull(classDesc.getMethodDesc(new MethodDescImpl(pool,
                "_get_output")));

        md = classDesc.getMethodDesc(new MethodDescImpl(pool, "_get_list"));
        assertNotNull(md);

        md = classDesc.getMethodDesc(new MethodDescImpl(pool, "_get_write"));
        assertNotNull(md);
    }

    public void testAdjustByExistentClass4_由来が同じプロパティのうち生成されたClassDescにも含まれているものが正しく残ること()
            throws Exception {
        ClassDesc classDesc = new ClassDescImpl(DescPool.newInstance(target_,
                null), Adjust2Page.class.getName());
        classDesc.setBornOf("/adjust2.html");
        classDesc.addProperty("param6", PropertyDesc.READ);

        target_.adjustByExistentClass(classDesc);

        PropertyDesc pd = classDesc.getPropertyDesc("param6");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertFalse(pd.isWritable());

        String[] value = pd.getMetaValueOnGetter(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(2, value.length);
    }

    public void testAdjustByExistentClass5_オーバライドされたメソッドが消えないこと()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDesc classDesc = new ClassDescImpl(pool, Adjust6Page.class
                .getName());
        classDesc.setBornOf("/adjust6.html");
        MethodDesc methodDesc = new MethodDescImpl(pool, "_get");
        classDesc.setMethodDesc(methodDesc);

        target_.adjustByExistentClass(classDesc);

        MethodDesc actual = classDesc.getMethodDesc(methodDesc);
        assertNotNull(actual);

        String[] value = actual.getMetaValue(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(1, value.length);
    }

    public void testUpdateClass_Page() throws Exception {
        ClassDesc classDesc = new ClassDescImpl(DescPool.newInstance(target_,
                null), SourceCreatorImplTest2Page.class.getName());
        MethodDesc methodDesc = target_.newActionMethodDesc(classDesc,
                "/sourceCreatorImplTest2.html", HttpMethod.GET);
        classDesc.setBornOf("/sourceCreatorImplTest2.html");
        classDesc.setMethodDesc(methodDesc);

        File testPageBase = new File(getSourceDir(), classDesc.getName()
                .replace('.', '/')
                + "Base.java");

        target_.updateClass(classDesc);

        String actual = IOUtils.readString(new FileInputStream(testPageBase),
                "UTF-8", false);

        assertEquals(readResource(getClass(), "testUpdateClass_Page.expected"),
                actual);
    }

    public void testAddConverterSetterToPageClassDesc_YMIR_325()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        ClassDescSet set = new ClassDescSet();
        ClassDesc pageCd = pool.getClassDesc("com.example.web.IndexPage");
        set.add(pageCd);
        ClassDesc hoeDtoCd = pool.getClassDesc("com.example.dto.HoeDto");
        set.add(hoeDtoCd);
        ClassDesc fugaDtoCd = pool.getClassDesc("com.example.dto.FugaDto");
        set.add(fugaDtoCd);
        ClassDesc hoeConverterCd = pool
                .getClassDesc("com.example.converter.HoeConverter");
        set.add(hoeConverterCd);
        ClassDesc fugaConverterCd = pool
                .getClassDesc("com.example.converter.FugaConverter");
        set.add(fugaConverterCd);

        PropertyDesc pd = new PropertyDescImpl(pool, "hoes");
        pageCd.setPropertyDesc(pd);
        pd.setTypeDesc(pool
                .newTypeDesc("java.util.List<com.example.dto.HoeDto>"));

        pd = new PropertyDescImpl(pool, "fugas");
        hoeDtoCd.setPropertyDesc(pd);
        pd.setTypeDesc(pool
                .newTypeDesc("java.util.List<com.example.dto.FugaDto>"));

        target_.addConverterSetterToPageClassDesc(pageCd, set);

        assertNotNull(pageCd.getPropertyDesc("hoeConverter"));
        assertNotNull(pageCd.getPropertyDesc("fugaConverter"));
    }

    public void test_isDtoClass() throws Exception {
        assertFalse(target_.isDtoClass(null));
        assertFalse(target_.isDtoClass("java.lang.Object"));
        assertTrue(target_.isDtoClass("com.example.dto.HoeDto"));
        assertTrue(target_.isDtoClass("org.seasar.ymir.render.Selector"));
    }

    public void test_getMethods_Objectクラスが持つメソッドをオーバライドしている場合それが除外されること()
            throws Exception {
        assertEquals(0, target_.getMethods(Hoe3Base.class, true).length);
        assertEquals(0, target_.getMethods(Hoe3.class, false).length);
    }
}
