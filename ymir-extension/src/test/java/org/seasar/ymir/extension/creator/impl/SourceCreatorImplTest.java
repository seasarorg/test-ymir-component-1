package org.seasar.ymir.extension.creator.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

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
import org.seasar.ymir.extension.creator.Born;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassDescBag;
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
import org.seasar.ymir.extension.creator.ThrowsDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.zpt.ParameterRole;
import org.seasar.ymir.id.action.GetAction;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.annotation.RequestParameter;

import com.example.page.SourceCreatorImplTestPageBaseBase;
import com.example.page.TestPageBase;
import com.example.web.SourceCreatorImplTest2Page;
import com.example.web.SourceCreatorImplTest3Page;

public class SourceCreatorImplTest extends SourceCreatorImplTestBase {
    private static final String LS = "\r\n";

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

        target_.prepareForUpdating(classDesc);
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

        target_.prepareForUpdating(classDesc);
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
        target_.gatherClassDescs(pool, null, true, null, new PathMetaDataImpl(
                "/test.html", HttpMethod.GET, false, "testPage",
                "com.example.web.TestPage", null, null, null,
                getSourceCreator().getTemplate("/test.html")));
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
        target_.gatherClassDescs(pool, null, true, null, new PathMetaDataImpl(
                "/test.html", HttpMethod.GET, false, "testPage",
                "com.example.web.TestPage", null, null, null,
                getSourceCreator().getTemplate("/test.html")));

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
        target_.gatherClassDescs(pool, null, true, null, new PathMetaDataImpl(
                "/test.html", HttpMethod.GET, false, "testPage",
                "com.example.web.TestPage", null, null, null,
                getSourceCreator().getTemplate("/test.html")));

        assertFalse(pool.contains("com.outer.dto.EntryDto"));
    }

    public void testNewClassDesc() throws Exception {
        assertEquals("マッチしたパターンに関連付けられているスーパークラス名がセットされていること",
                "com.example.web.HndexPageBaseBase", target_.newClassDesc(
                        pool_, "com.example.web.HndexPage", null)
                        .getSuperclassName());

        assertEquals(
                "マッチするパターンが無くデフォルトのスーパークラス名が指定されている場合はデフォルトのスーパークラス名がセットされていること",
                "com.example.page.TestPageBaseBase", target_.newClassDesc(
                        pool_, "com.example.page.TestPage", null)
                        .getSuperclassName());

        assertEquals("マッチしたパターンに関連付けられているスーパークラス名よりも実際のスーパークラス名が優先されること",
                "com.example.web.IndexPageBaseBase", target_.newClassDesc(
                        pool_, "com.example.web.IndexPage", null)
                        .getSuperclassName());
    }

    public void testNewClassDesc_hintが指定されていればスーパークラス名がhintを基に設定されること()
            throws Exception {
        ClassHint classHint = new ClassHint("com.example.web.IndexPage");
        classHint.setSuperclassName("com.example.web.PageBase");
        assertEquals("com.example.web.PageBase", target_.newClassDesc(pool_,
                "com.example.web.IndexPage",
                new ClassCreationHintBag(null, new ClassHint[] { classHint }))
                .getSuperclassName());
    }

    public void testNewClassDesc_メソッドのボディが正しく復元されること() throws Exception {
        ClassDesc actual = target_.newClassDesc(pool_, Hoe5.class, true);
        BodyDesc bodyDesc = actual.getMethodDescs("_get")[0].getBodyDesc();
        assertNotNull(bodyDesc);
        assertEquals(BodyDesc.KEY_ASIS, bodyDesc.getKey());
        Map<String, Object> root = bodyDesc.getRoot();
        assertNotNull(root);
        assertEquals("return new PassthroughResponse();", root
                .get(BodyDesc.PROP_BODY));
        assertEquals(1, bodyDesc.getDependingClassNames().length);
        assertEquals(PassthroughResponse.class.getName(), bodyDesc
                .getDependingClassNames()[0]);
    }

    public void testAdjustByExistentClass() throws Exception {
        ClassDesc cd = pool_
                .getClassDesc("org.seasar.ymir.extension.creator.impl.Merge3Page");
        cd.setSuperclassName(Merge3PageBaseBase.class.getName());
        PropertyDesc pd = new PropertyDescImpl(pool_, "hoe");
        pd.addMode(PropertyDesc.READ);
        pd.addMode(PropertyDesc.WRITE);
        cd.setPropertyDesc(pd);
        MethodDesc addMd = new MethodDescImpl(pool_, "add");
        addMd.setReturnTypeDesc(new TypeDescImpl(pool_, Integer.TYPE));
        addMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool_, Integer.TYPE),
            new ParameterDescImpl(pool_, Integer.TYPE) });
        cd.setMethodDesc(addMd);
        MethodDesc subMd = new MethodDescImpl(pool_, "sub");
        subMd.setReturnTypeDesc(new TypeDescImpl(pool_, Integer.TYPE));
        subMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool_, Integer.TYPE),
            new ParameterDescImpl(pool_, Integer.TYPE) });
        cd.setMethodDesc(subMd);
        MethodDesc timesMd = new MethodDescImpl(pool_, "times");
        timesMd.setReturnTypeDesc(new TypeDescImpl(pool_, Integer.TYPE));
        timesMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool_, Integer.TYPE),
            new ParameterDescImpl(pool_, Integer.TYPE) });
        cd.setMethodDesc(timesMd);
        MethodDesc divMd = new MethodDescImpl(pool_, "div");
        divMd.setReturnTypeDesc(new TypeDescImpl(pool_, Integer.TYPE));
        divMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool_, Integer.TYPE),
            new ParameterDescImpl(pool_, Integer.TYPE) });
        cd.setMethodDesc(divMd);
        MethodDesc incMd = new MethodDescImpl(pool_, "inc");
        incMd.setReturnTypeDesc(new TypeDescImpl(pool_, Integer.TYPE));
        incMd.setParameterDescs(new ParameterDesc[] {
            new ParameterDescImpl(pool_, Integer.TYPE),
            new ParameterDescImpl(pool_, Integer.TYPE) });
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
        ClassDesc cd = pool_
                .getClassDesc("org.seasar.ymir.extension.creator.impl.Merge2Page");
        cd.setSuperclassName(Merge2PageBaseBase.class.getName());
        MethodDesc md = new MethodDescImpl(pool_, "_render");
        md.setReturnTypeDesc(new TypeDescImpl(pool_, Void.TYPE));
        md.setParameterDescs(new ParameterDesc[0]);
        cd.setMethodDesc(md);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getMethodDesc(md));
    }

    public void testGetClassDesc_bodyアノテーションが付与されている場合はボディが復元されること()
            throws Exception {
        ClassDesc cd = target_.newClassDesc(pool_, Class1Base.class, true);

        BodyDesc actual = cd.getMethodDescs()[0].getBodyDesc();

        assertEquals("return \"return value\";", actual.getRoot().get("body"));
    }

    public void testGetClassDesc_プロパティのGetterとSetterに付与したAnnotationが保持されること()
            throws Exception {
        ClassDesc cd = target_.newClassDesc(pool_, Class2Base.class, true);

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
        ClassDesc cd = target_.newClassDesc(pool_, Class3Base.class, true);

        PropertyDesc pd = cd.getPropertyDesc("form");
        assertNotNull(pd);
        assertNotNull(pd.getAnnotationDesc(Meta.class.getName()));
    }

    public void testFilterResponse() throws Exception {
        assertEquals(
                "<html><head>"
                        + "<link type=\"text/css\" rel=\"stylesheet\" href=\"/context/__ymir__/resource/js/jquery/css/smoothness/jquery-ui-1.8.custom.css\" />"
                        + LS
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/jquery/jquery-1.4.2.min.js\"></script>"
                        + LS
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/jquery/jquery-ui-1.8.custom.min.js\"></script>"
                        + LS
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/jquery/editable/jquery.editable.ex-1.3.3.js\"></script>"
                        + LS
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/sourceCreator.js\"></script>"
                        + LS
                        + "</head><body><div id=\"__ymir__inPlaceEditor\">BODY</div>"
                        + "<div id=\"__ymir__controlPanel\">"
                        + LS
                        + "<form action=\"/context/path\" method=\"post\">"
                        + "<input type=\"hidden\" name=\"__ymir__task\" value=\"systemConsole\" />"
                        + "<input type=\"hidden\" name=\"__ymir__method\" value=\"GET\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"a%26%3F\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"b\" />"
                        + "<input type=\"hidden\" name=\"bbb\" value=\"c\" />"
                        + "<input type=\"submit\" value=\"SYSTEM CONSOLE\" />"
                        + "</form>"
                        + LS
                        + "<form action=\"/context/path\" method=\"post\">"
                        + "<input type=\"hidden\" name=\"__ymir__task\" value=\"updateClasses\" />"
                        + "<input type=\"hidden\" name=\"__ymir__method\" value=\"GET\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"a%26%3F\" />"
                        + "<input type=\"hidden\" name=\"aaa\" value=\"b\" />"
                        + "<input type=\"hidden\" name=\"bbb\" value=\"c\" />"
                        + "<input type=\"submit\" value=\"UPDATE CLASSES\" />"
                        + "</form>" + LS + "</div>" + "</body></html>",
                target_
                        .filterResponse("<html><head></head><body>BODY</body></html>"));
    }

    public void testAdjustByExistentClass_既にギャップクラスがあるがベースクラスがない場合にObjectのメソッドがベースクラスに追加されないこと()
            throws Exception {
        getConfiguration().removeProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_SUPERCLASS);

        ClassDesc cd = pool_
                .getClassDesc("org.seasar.ymir.extension.creator.impl.Merge5Page");

        target_.adjustByExistentClass(cd);

        assertNull(cd.getPropertyDesc("class"));
    }

    public void testAdjustByExistentClass2_validationFailedなどがスーパークラスにある時は自動生成されないこと()
            throws Exception {
        ClassDesc cd = pool_
                .getClassDesc("com.example.page.SourceCreatorImplTestPage");
        cd.setSuperclassName(SourceCreatorImplTestPageBaseBase.class.getName());
        MethodDescImpl md = new MethodDescImpl(pool_,
                ConstraintInterceptor.ACTION_VALIDATIONFAILED);
        md.setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(pool_,
                Notes.class) });
        cd.setMethodDesc(md);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getMethodDesc(md));
    }

    public void testAdjustByExistentClass3_親クラスが明示的に指定されていればBaseクラスの現在の親クラス情報よりも優先されること()
            throws Exception {
        ClassDesc cd = pool_
                .getClassDesc("org.seasar.ymir.extension.creator.impl.Merge3Page");
        cd.setSuperclassName(TestPageBase.class.getName());

        target_.adjustByExistentClass(cd);

        assertEquals("明示的な指定が優先されること", TestPageBase.class.getName(), cd
                .getSuperclassName());
    }

    public void testAdjustByExistentClass5_Baseクラスのabstract状態が維持されること()
            throws Exception {
        ClassDesc cd = pool_
                .getClassDesc("org.seasar.ymir.extension.creator.impl.Merge7Page");
        cd.setSuperclassName(TestPageBase.class.getName());

        target_.adjustByExistentClass(cd);

        assertTrue(cd.isAbstract());
    }

    public void testAdjustByExistentClass6_プロパティのGetterやSetterがスーパークラスにある時は自動生成されないこと()
            throws Exception {
        ClassDesc cd = pool_
                .getClassDesc("org.seasar.ymir.extension.creator.impl.Merge8Page");
        PropertyDescImpl pd = new PropertyDescImpl(pool_, "value");
        pd.setMode(PropertyDesc.READ);
        cd.setPropertyDesc(pd);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getPropertyDesc("value"));
    }

    public void testAdjustByExistentClass9_BaseクラスのAnnotationが上書きされること()
            throws Exception {
        ClassDesc cd = pool_
                .getClassDesc("org.seasar.ymir.extension.creator.impl.Merge9Page");
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
        ClassDesc cd = pool_
                .getClassDesc("org.seasar.ymir.extension.creator.impl.Merge10Page");
        MethodDesc getMd = new MethodDescImpl(pool_, "_get");
        getMd.setReturnTypeDesc(new TypeDescImpl(pool_, Void.TYPE));
        getMd.setParameterDescs(new ParameterDesc[0]);
        cd.setMethodDesc(getMd);
        MethodDesc postMd = new MethodDescImpl(pool_, "_post");
        postMd.setReturnTypeDesc(new TypeDescImpl(pool_, Void.TYPE));
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
        ClassDesc cd = pool_
                .getClassDesc("org.seasar.ymir.extension.creator.impl.Merge11Page");
        PropertyDesc pd = new PropertyDescImpl(pool_, "hoehoe");
        cd.setPropertyDesc(pd);
        pd.setAnnotationDesc(new MetaAnnotationDescImpl("property",
                new String[] { "hoehoe" }, new Class[0]));
        cd
                .setSuperclassName("org.seasar.ymir.extension.creator.impl.Merge11PageBaseBase");

        target_.adjustByExistentClass(cd);

        assertNull(cd.getPropertyDesc("hoehoe"));
    }

    public void testAdjustByExistentClass12_Gapクラスに同名のシグネチャが異なるアクションメソッドがある場合はBaseクラスにアクションメソッドが生成されないこと()
            throws Exception {
        ClassDesc cd = pool_.getClassDesc(Merge12Page.class);
        MethodDesc md = new MethodDescImpl(pool_, "_get");
        md.setAttribute(Globals.ATTR_ACTION, Boolean.TRUE);
        cd.setMethodDesc(md);

        target_.adjustByExistentClass(cd);

        assertNull(cd.getMethodDesc(md));
    }

    public void testAdjustByExistentClass13_Baseにあるメソッドのアノテーションとボディが保持されること()
            throws Exception {
        ClassDesc classDesc = target_.newClassDesc(pool_, AdjustPage.class
                .getName(), null);
        classDesc.setBornOf("/login.html");
        MethodDescImpl md = new MethodDescImpl(pool_, "_get");
        md.setReturnTypeDesc(Void.TYPE);
        classDesc.setMethodDesc(md);

        target_.adjustByExistentClass(classDesc);

        MethodDesc actual = classDesc.getMethodDesc(md);
        assertNotNull(actual);
        assertNotNull(actual.getMetaFirstValue(Globals.META_NAME_SOURCE));
        assertNotNull(actual.getBodyDesc());
        assertEquals("String", actual.getReturnTypeDesc().getName());
    }

    public void testAdjustByExistentClass14_Baseにあるプロパティの初期値情報が正しくマージされること()
            throws Exception {
        ClassDesc classDesc = target_.newClassDesc(pool_, Adjust4Page.class,
                false);
        classDesc.getPropertyDesc("list").getTypeDesc()
                .setCollectionImplementationClassName(null);
        target_.adjustByExistentClass(classDesc);

        PropertyDesc actual = classDesc.getPropertyDesc("list");
        assertNotNull(actual);
        assertEquals("new org.seasar.ymir.util.FlexibleList<String>()", actual
                .getInitialValue());
    }

    public void testAdjustByExistentClass15_Baseにあるプロパティの初期値情報よりもClassDescのプロパティの型情報が優先されること()
            throws Exception {
        ClassDesc classDesc = target_.newClassDesc(pool_, Adjust5Page.class,
                false);
        classDesc.getPropertyDesc("list").getTypeDesc().setComponentClassDesc(
                pool_.getClassDesc(String.class));
        classDesc.getPropertyDesc("list").getTypeDesc()
                .setCollectionImplementationClassName(null);
        target_.adjustByExistentClass(classDesc);

        PropertyDesc actual = classDesc.getPropertyDesc("list");
        assertNotNull(actual);
        assertEquals("new org.seasar.ymir.util.FlexibleList<String>()", actual
                .getInitialValue());
    }

    public void testAdjustByExistentClass16_由来が同じプロパティのうち生成されたClassDescに含まれていないものが削除されること()
            throws Exception {
        ClassDesc classDesc = pool_.getClassDesc(Adjust2Page.class);
        classDesc.setBornOf("/adjust2.html");
        classDesc.addPropertyDesc("param4", PropertyDesc.READ
                | PropertyDesc.WRITE);

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

    public void testAdjustByExistentClass17_由来が同じメソッドのうち生成されたClassDescに含まれていないものが削除されること()
            throws Exception {
        ClassDesc classDesc = pool_.getClassDesc(Adjust3Page.class);
        classDesc.setBornOf("/adjust3.html");
        classDesc.setMethodDesc(new MethodDescImpl(pool_, "_get_write"));

        target_.adjustByExistentClass(classDesc);

        MethodDesc md = classDesc.getMethodDesc(new MethodDescImpl(pool_,
                "_get"));
        assertNotNull(md);

        assertNull(classDesc.getMethodDesc(new MethodDescImpl(pool_,
                "_get_output")));

        md = classDesc.getMethodDesc(new MethodDescImpl(pool_, "_get_list"));
        assertNotNull(md);

        md = classDesc.getMethodDesc(new MethodDescImpl(pool_, "_get_write"));
        assertNotNull(md);
    }

    public void testAdjustByExistentClass18_由来が同じプロパティのうち生成されたClassDescにも含まれているものが正しく残ること()
            throws Exception {
        ClassDesc classDesc = pool_.getClassDesc(Adjust2Page.class);
        classDesc.setBornOf("/adjust2.html");
        classDesc.addPropertyDesc("param6", PropertyDesc.READ);

        target_.adjustByExistentClass(classDesc);

        PropertyDesc pd = classDesc.getPropertyDesc("param6");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertFalse(pd.isWritable());

        String[] value = pd.getMetaValueOnGetter(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(2, value.length);
    }

    public void testAdjustByExistentClass19_オーバライドされたメソッドが消えないこと()
            throws Exception {
        pool_.setBornOf("/adjust6.html");
        ClassDesc classDesc = pool_.getClassDesc(Adjust6Page.class);
        MethodDesc methodDesc = new MethodDescImpl(pool_, "_get");
        methodDesc.setAttribute(Globals.ATTR_ACTION, Boolean.TRUE);
        classDesc.setMethodDesc(methodDesc);

        target_.adjustByExistentClass(classDesc);

        MethodDesc actual = classDesc.getMethodDesc(methodDesc);
        assertNotNull(actual);

        String[] value = actual.getMetaValue(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(1, value.length);
    }

    public void testAdjustByExistentClass20_BaseクラスのプロパティにbornOfがついていない場合に正しく残ること()
            throws Exception {
        pool_.setBornOf("/adjust20.html");
        ClassDesc classDesc = pool_.getClassDesc(Adjust20Page.class);
        classDesc.addPropertyDesc("name", PropertyDesc.READ
                | PropertyDesc.WRITE);

        target_.adjustByExistentClass(classDesc);

        PropertyDesc actual = classDesc.getPropertyDesc("name");
        assertNotNull(actual);

        String[] value = actual.getMetaValue(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(1, value.length);
        int idx = 0;
        assertEquals("/adjust20.html", value[idx++]);

        value = actual.getMetaValueOnGetter(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(1, value.length);
        idx = 0;
        assertEquals("/adjust20.html", value[idx++]);

        value = actual.getMetaValueOnSetter(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(1, value.length);
        idx = 0;
        assertEquals("/adjust20.html", value[idx++]);
    }

    public void testAdjustByExistentClass21_BaseクラスのプロパティにbornOfがついている場合に正しく残ること()
            throws Exception {
        pool_.setBornOf("/adjust21.html");
        ClassDesc classDesc = pool_.getClassDesc(Adjust21Page.class);
        classDesc.addPropertyDesc("name", PropertyDesc.READ
                | PropertyDesc.WRITE);

        target_.adjustByExistentClass(classDesc);

        PropertyDesc actual = classDesc.getPropertyDesc("name");
        assertNotNull(actual);

        String[] value = actual.getMetaValue(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(2, value.length);
        int idx = 0;
        assertEquals("/adjust.html", value[idx++]);
        assertEquals("/adjust21.html", value[idx++]);

        value = actual.getMetaValueOnGetter(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(2, value.length);
        idx = 0;
        assertEquals("/adjust.html", value[idx++]);
        assertEquals("/adjust21.html", value[idx++]);

        value = actual.getMetaValueOnSetter(Globals.META_NAME_BORNOF);
        assertNotNull(value);
        assertEquals(2, value.length);
        idx = 0;
        assertEquals("/adjust.html", value[idx++]);
        assertEquals("/adjust21.html", value[idx++]);
    }

    public void testAdjustByExistentClass22_GapクラスとBaseクラスのメソッドにthrowsがついているが生成されたClassDescのメソッドにはthrowsがついていない場合にthrowsが残ること()
            throws Exception {
        pool_.setBornOf("/adjust22.html");
        ClassDesc classDesc = pool_.getClassDesc(Adjust22Page.class);
        classDesc.setMethodDesc(new MethodDescImpl(pool_, "_get"));

        target_.adjustByExistentClass(classDesc);

        MethodDesc actual = classDesc.getMethodDesc(new MethodDescImpl(pool_,
                "_get"));
        assertNotNull(actual);

        ThrowsDesc throwsDesc = actual.getThrowsDesc();
        assertNotNull(throwsDesc);

        String[] throwableClassNames = throwsDesc.getThrowableClassNames();
        assertEquals(1, throwableClassNames.length);
        int idx = 0;
        assertEquals(IOException.class.getName(), throwableClassNames[idx++]);
    }

    public void testAdjustByExistentClass23_Baseクラスに由来パスが異なりかつアクションインタフェースがないアクションクラスがある場合に生成されたアクションインタフェースの情報が消えないこと()
            throws Exception {
        pool_.setBornOf("/adjust23.html");
        ClassDesc classDesc = pool_.getClassDesc(Adjust23Page.class);
        MethodDescImpl methodDesc = new MethodDescImpl(pool_, "_get");
        target_.setActionInfo(methodDesc, GetAction.class, "");
        classDesc.setMethodDesc(methodDesc);

        target_.adjustByExistentClass(classDesc);

        MethodDesc actual = classDesc.getMethodDesc(methodDesc);
        assertNotNull(actual);

        assertEquals(Boolean.TRUE, actual.getAttribute(Globals.ATTR_ACTION));
        assertEquals(GetAction.class, actual
                .getAttribute(Globals.ATTR_ACTION_INTERFACE));
        assertEquals("", actual.getAttribute(Globals.ATTR_ACTION_KEY));
    }

    public void testAdjustByExistentClass24_ある画面から生成した結果Baseクラスのメソッドの返り値がvoidでない場合に別の画面から生成しても返り値がvoidにならないこと()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        pool.setBornOf("/index.html");
        ClassDesc classDesc = pool.getClassDesc(Adjust24Page.class);
        classDesc.setMethodDesc(new MethodDescImpl(pool, "_get"));

        target_.adjustByExistentClass(classDesc);

        MethodDesc actual = classDesc.getMethodDesc(new MethodDescImpl(pool,
                "_get"));
        assertNotNull(actual);
        assertEquals("由来が違うならvoidでない方が優先されること", "String", actual
                .getReturnTypeDesc().getName());

        pool = DescPool.newInstance(target_, null);
        pool.setBornOf("/index.html");
        classDesc = pool.getClassDesc(Adjust24Page.class);
        MethodDesc methodDesc = new MethodDescImpl(pool, "_get_two");
        methodDesc.setReturnTypeDesc(String.class);
        classDesc.setMethodDesc(methodDesc);

        target_.adjustByExistentClass(classDesc);

        actual = classDesc.getMethodDesc(new MethodDescImpl(pool, "_get_two"));
        assertNotNull(actual);
        assertEquals("由来が違うならvoidでない方が優先されること2", "String", actual
                .getReturnTypeDesc().getName());

        pool = DescPool.newInstance(target_, null);
        pool.setBornOf("/adjust24.html");
        classDesc = pool.getClassDesc(Adjust24Page.class);
        classDesc.setMethodDesc(new MethodDescImpl(pool, "_get"));

        target_.adjustByExistentClass(classDesc);

        actual = classDesc.getMethodDesc(new MethodDescImpl(pool, "_get"));
        assertNotNull(actual);
        assertEquals("由来が同じなら置き換わること", "void", actual.getReturnTypeDesc()
                .getName());

        pool = DescPool.newInstance(target_, null);
        pool.setBornOf("/adjust24.html");
        classDesc = pool.getClassDesc(Adjust24Page.class);
        classDesc.setMethodDesc(new MethodDescImpl(pool, "_get_string"));

        target_.adjustByExistentClass(classDesc);

        actual = classDesc
                .getMethodDesc(new MethodDescImpl(pool, "_get_string"));
        assertNotNull(actual);
        assertNotNull("由来が同じでもBaseがボディを持っている場合は置き換わらないこと", actual.getBodyDesc());
        assertEquals("由来が同じでもBaseがボディを持っている場合は置き換わらないこと", "String", actual
                .getReturnTypeDesc().getName());

        pool = DescPool.newInstance(target_, null);
        pool.setBornOf("/adjust24.html");
        classDesc = pool.getClassDesc(Adjust24Page.class);
        classDesc.setMethodDesc(new MethodDescImpl(pool, "_post"));

        target_.adjustByExistentClass(classDesc);

        actual = classDesc.getMethodDesc(new MethodDescImpl(pool, "_post"));
        assertNotNull(actual);
        assertEquals("由来が同じでもGapでオーバライドしている場合は置き換わらないこと", "String", actual
                .getReturnTypeDesc().getName());
    }

    @SuppressWarnings("unchecked")
    public void testAdjustByExistentClass25_パラメータ定数が正しくマージされること()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        pool.setBornOf("/adjust25.html");
        ClassDesc classDesc = pool.getClassDesc(Adjust25Page.class);
        DescUtils.addParameter(classDesc.addPropertyDesc("param1",
                PropertyDesc.WRITE), "param1.value2");

        target_.adjustByExistentClass(classDesc);

        Born<String>[] parameters = (Born<String>[]) classDesc.getPropertyDesc(
                "param1").getAttribute(Globals.ATTR_PARAMETERS);
        assertNotNull(parameters);
        assertEquals(2, parameters.length);
        int idx = 0;
        assertEquals("param1.value2", parameters[idx].getElement());
        assertEquals("/adjust25.html", parameters[idx].getBornOf()[0]);
        assertEquals("/fuga.html", parameters[idx].getBornOf()[1]);
        idx++;
        assertEquals("param1.value3", parameters[idx].getElement());
        assertEquals("/fuga.html", parameters[idx].getBornOf()[0]);
        idx++;
    }

    public void testAdjustByExistentClass26_formのアクセッサがsuperクラスにある場合に除去されること()
            throws Exception {
        DescPool pool = DescPool.newInstance(target_, null);
        pool.setBornOf("/adjust26.html");
        ClassDesc classDesc = pool.getClassDesc(Adjust26Page.class);
        PropertyDesc propertyDesc = classDesc.addPropertyDesc("message",
                PropertyDesc.READ | PropertyDesc.WRITE);
        propertyDesc.setAnnotationDescOnGetter(new MetaAnnotationDescImpl(
                "formProperty", new String[] { "adjust26Form" }));
        propertyDesc.setAnnotationDescOnSetter(new MetaAnnotationDescImpl(
                "formProperty", new String[] { "adjust26Form" }));
        propertyDesc.setAnnotationDescOnSetter(new AnnotationDescImpl(
                RequestParameter.class.getName()));
        propertyDesc = classDesc.addPropertyDesc("adjust26Form",
                PropertyDesc.NONE);
        propertyDesc.setTypeDesc(Adjust26FormDto.class);
        propertyDesc.setAnnotationDesc(new MetaAnnotationDescImpl("property",
                new String[] { "adjust26Form" }));

        target_.adjustByExistentClass(classDesc);

        assertNull(classDesc.getPropertyDesc("adjust26Form"));
    }

    public void testGetBeginAnnotation() throws Exception {
        Begin actual = target_.getBeginAnnotation();

        assertNotNull(actual);
    }

    public void testCreateButtonHTML() throws Exception {
        MockRequest request = new MockRequest();
        request.setParameterValues("aaa", new String[] { "a&?", "b" });
        request.setParameter("bbb", "c");
        request.setParameter(SourceCreator.PARAM_PREFIX + "hoehoe", "c");
        request.setMethod(HttpMethod.GET);
        MockDispatch dispatch = new MockDispatch();
        dispatch.setAbsolutePath("/context/path");
        request.enterDispatch(dispatch);

        String actual = target_.createButtonHTML(request, "systemConsole",
                "SYSTEM CONSOLE");

        assertEquals(
                "<form action=\"/context/path\" method=\"post\"><input type=\"hidden\" name=\"__ymir__task\" value=\"systemConsole\" /><input type=\"hidden\" name=\"__ymir__method\" value=\"GET\" /><input type=\"hidden\" name=\"aaa\" value=\"a%26%3F\" /><input type=\"hidden\" name=\"aaa\" value=\"b\" /><input type=\"hidden\" name=\"bbb\" value=\"c\" /><input type=\"submit\" value=\"SYSTEM CONSOLE\" /></form>",
                actual);
    }

    public void testIsFormDtoFieldPresent() throws Exception {
        assertFalse(target_.isFormDtoFieldPresent(pool_
                .getClassDesc(Object.class), "hoehoe"));

        assertFalse(target_.isFormDtoFieldPresent(pool_
                .getClassDesc(Merge10Page.class), "hoehoe"));

        assertTrue(target_.isFormDtoFieldPresent(pool_
                .getClassDesc(Merge11Page.class), "hoehoe"));
    }

    public void testGetClassDesc_YMIR_226_aNameのようなプロパティを正しく検出できること1()
            throws Exception {
        ClassDesc actual = target_.newClassDesc(pool_, new Object() {
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
        ClassDesc actual = target_.newClassDesc(pool_, new Object() {
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
                .newClassDesc(pool_, "com.example.web.aaa1.bbb1.Traverse1Page",
                        null).getSuperclassName());

        assertEquals("com.example.web.aaa1.TraversePageBase", target_
                .newClassDesc(pool_, "com.example.web.aaa1.bbb2.Traverse2Page",
                        null).getSuperclassName());

        assertEquals("com.example.web.TraversePageBase", target_.newClassDesc(
                null, "com.example.web.aaa2.bbb.TraversePage", null)
                .getSuperclassName());
    }

    public void testUpdateClass_Page() throws Exception {
        pool_.setBornOf("/sourceCreatorImplTest2.html");
        ClassDesc classDesc = pool_
                .getClassDesc(SourceCreatorImplTest2Page.class);
        MethodDesc methodDesc = target_.newActionMethodDesc(pool_,
                "/sourceCreatorImplTest2.html", HttpMethod.GET,
                new ActionSelectorSeedImpl());
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
        ClassDescSet set = new ClassDescSet();
        ClassDesc pageCd = pool_.getClassDesc("com.example.web.IndexPage");
        set.add(pageCd);
        ClassDesc hoeDtoCd = pool_.getClassDesc("com.example.dto.HoeDto");
        set.add(hoeDtoCd);
        ClassDesc fugaDtoCd = pool_.getClassDesc("com.example.dto.FugaDto");
        set.add(fugaDtoCd);
        ClassDesc hoeConverterCd = pool_
                .getClassDesc("com.example.converter.HoeConverter");
        set.add(hoeConverterCd);
        ClassDesc fugaConverterCd = pool_
                .getClassDesc("com.example.converter.FugaConverter");
        set.add(fugaConverterCd);

        PropertyDesc pd = new PropertyDescImpl(pool_, "hoes");
        pageCd.setPropertyDesc(pd);
        pd.setTypeDesc(pool_
                .newTypeDesc("java.util.List<com.example.dto.HoeDto>"));

        pd = new PropertyDescImpl(pool_, "fugas");
        hoeDtoCd.setPropertyDesc(pd);
        pd.setTypeDesc(pool_
                .newTypeDesc("java.util.List<com.example.dto.FugaDto>"));

        target_.addConverterSetterToPageClassDesc(pageCd, set);

        assertNotNull(pageCd.getPropertyDesc("hoeConverter"));
        assertNotNull(pageCd.getPropertyDesc("fugaConverter"));
    }

    public void testAddConverterSetterToPageClassDesc_循環参照時にStackOverflowがでないこと()
            throws Exception {
        ClassDescSet set = new ClassDescSet();
        ClassDesc pageCd = pool_.getClassDesc("com.example.web.IndexPage");
        set.add(pageCd);
        ClassDesc hoeDtoCd = pool_.getClassDesc("com.example.dto.HoeDto");
        set.add(hoeDtoCd);
        ClassDesc hoeConverterCd = pool_
                .getClassDesc("com.example.converter.HoeConverter");
        set.add(hoeConverterCd);

        PropertyDesc pd = new PropertyDescImpl(pool_, "hoe");
        pageCd.setPropertyDesc(pd);
        pd.setTypeDesc(pool_.newTypeDesc("com.example.dto.HoeDto"));

        pd = new PropertyDescImpl(pool_, "hoe");
        hoeDtoCd.setPropertyDesc(pd);
        pd.setTypeDesc(pool_.newTypeDesc("com.example.dto.HoeDto"));

        try {
            target_.addConverterSetterToPageClassDesc(pageCd, set);
        } catch (StackOverflowError e) {
            fail();
        }
    }

    public void testAddConverterSetterToPageClassDesc_循環参照時にStackOverflowがでないこと2()
            throws Exception {
        ClassDescSet set = new ClassDescSet();
        ClassDesc pageCd = pool_.getClassDesc("com.example.web.IndexPage");
        set.add(pageCd);
        ClassDesc hoeDtoCd = pool_.getClassDesc("com.example.dto.HoeDto");
        set.add(hoeDtoCd);

        PropertyDesc pd = new PropertyDescImpl(pool_, "hoe");
        pageCd.setPropertyDesc(pd);
        pd.setTypeDesc(pool_.newTypeDesc("com.example.dto.HoeDto"));

        pd = new PropertyDescImpl(pool_, "hoe");
        hoeDtoCd.setPropertyDesc(pd);
        pd.setTypeDesc(pool_.newTypeDesc("com.example.dto.HoeDto"));

        try {
            target_.addConverterSetterToPageClassDesc(pageCd, set);
        } catch (StackOverflowError e) {
            fail();
        }
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

    public void test_findPropertyClassName() throws Exception {
        assertEquals("com.example.dto.EntryDto", target_.findPropertyClassName(
                "entry", "com.example.web.IndexPage"));

        assertEquals("com.example.dto.sub.EntryDto",
                target_.findPropertyClassName("entry",
                        "com.example.web.sub.IndexPage"));

        try {
            target_.findPropertyClassName("entry", null);
            fail();
        } catch (NullPointerException expected) {
        }

        try {
            assertEquals("com.example.dto.sub.EntryDto", target_
                    .findPropertyClassName("entry",
                            "org.seasar.ymir.render.Selector"));
            fail();
        } catch (IllegalArgumentException excepted) {
        }
    }

    public void test_inferPropertyClassName() throws Exception {
        assertEquals("com.example.dto.sub.NameDto", target_
                .inferPropertyClassName("name", "com.example.web.sub.SubPage"));

        try {
            assertEquals("com.example.dto.NameDto", target_
                    .inferPropertyClassName("name",
                            "net.kankeinai.package.SubPage"));
            fail();
        } catch (IllegalArgumentException excepted) {
        }

        assertEquals("[#YMIR-202]既存クラスが上位にあればそれを返すこと",
                "com.example.dto.sub.Name2Dto", target_.inferPropertyClassName(
                        "name2", "com.example.web.sub.sub.SubPage"));
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

    public void test_inferParameterRole_インクルードされているクラスも見ること() throws Exception {
        assertEquals(ParameterRole.PARAMETER, target_.inferParameterRole(
                "/index.html", HttpMethod.GET, Hoe6Page.class.getName(),
                "param", null));

        assertEquals(ParameterRole.BUTTON, target_.inferParameterRole(
                "/index.html", HttpMethod.GET, Hoe6Page.class.getName(), "go",
                null));
    }

    public void test_updateClasses_YMIR_342_存在しない親クラスを指定してもエラーにならないこと()
            throws Exception {
        pool_.setBornOf("/sourceCreatorImplTest3.html");
        ClassDesc classDesc = pool_
                .getClassDesc(SourceCreatorImplTest3Page.class);
        String superclassName = classDesc.getName() + "Super";
        classDesc.setSuperclassName(superclassName);

        ClassDescBag bag = new ClassDescBag();
        bag.addAsCreated(classDesc);

        File actualFile = new File(getSourceDir(), superclassName.replace('.',
                '/')
                + ".java");
        actualFile.delete();

        target_.updateClasses(bag);

        String actual = IOUtils.readString(new FileInputStream(actualFile),
                "UTF-8", false);

        assertEquals(readResource(getClass(),
                "test_updateClasses_YMIR_342.expected"), actual);
    }

    public void test_writeEmptyBaseSourceFileIfNotExists_Page()
            throws Exception {
        ClassDesc classDesc = target_.newClassDesc(pool_,
                "com.example.web.sub.PageBase", null);
        classDesc.setSuperclassName("com.example.web.PageBase");
        File actualFile = new File(getSourceDir(), classDesc.getName().replace(
                '.', '/')
                + ".java");
        actualFile.delete();

        target_.prepareForUpdating(classDesc);
        target_.writeEmptyBaseSourceFileIfNotExists(classDesc);

        String actual = IOUtils.readString(new FileInputStream(actualFile),
                "UTF-8", false);

        assertEquals(readResource(getClass(),
                "test_writeEmptyBaseSourceFileIfNotExists_Page.expected"),
                actual);
    }

    public void test_writeEmptyBaseSourceFileIfNotExists_Dto() throws Exception {
        ClassDesc classDesc = target_.newClassDesc(pool_,
                "com.example.web.sub.DtoBase", null);
        classDesc.setSuperclassName("com.example.web.DtoBase");
        File actualFile = new File(getSourceDir(), classDesc.getName().replace(
                '.', '/')
                + ".java");
        actualFile.delete();

        target_.prepareForUpdating(classDesc);
        target_.writeEmptyBaseSourceFileIfNotExists(classDesc);

        String actual = IOUtils.readString(new FileInputStream(actualFile),
                "UTF-8", false);

        assertEquals(readResource(getClass(),
                "test_writeEmptyBaseSourceFileIfNotExists_Dto.expected"),
                actual);
    }
}
