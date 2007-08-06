package org.seasar.ymir.extension.creator.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.framework.util.ResourceUtil;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.impl.ConstraintInterceptor;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.PropertyTypeHintBag;
import org.seasar.ymir.extension.creator.TypeDesc;

import com.example.page.SourceCreatorImplTestPageBaseBase;
import com.example.page.TestPageBase;

public class SourceCreatorImplTest extends SourceCreatorImplTestBase {

    public void testGetComponentName() throws Exception {

        String actual = target_.getComponentName("/index.html",
                Request.METHOD_GET);

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
        PropertyTypeHintBag hintBag = new PropertyTypeHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.web.TestPage", "result",
                        "java.lang.Integer", false) });
        target_.gatherClassDescs(classDescMap, new PathMetaDataImpl(
                "/test.html", Request.METHOD_GET, false, "testPage",
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
        assertNotNull(actual[0].getMethodDesc(new MethodDescImpl("_render")));
        assertEquals("Integer", actual[0].getPropertyDesc("result")
                .getTypeDesc().getName());
    }

    public void testGatherClassDescs2_hintとして外部のDtoクラスを指定していても正しく利用されること()
            throws Exception {

        Map<String, ClassDesc> classDescMap = new LinkedHashMap<String, ClassDesc>();
        PropertyTypeHintBag hintBag = new PropertyTypeHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.web.TestPage", "result",
                        "com.outer.dto.EntryDto", false) });
        target_.gatherClassDescs(classDescMap, new PathMetaDataImpl(
                "/test.html", Request.METHOD_GET, false, "testPage",
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
        PropertyTypeHintBag hintBag = new PropertyTypeHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.web.TestPage", "result",
                        "com.outer.dto.EntryDto", false) });
        target_.gatherClassDescs(classDescMap, new PathMetaDataImpl(
                "/test.html", Request.METHOD_GET, false, "testPage",
                "com.example.web.TestPage", null, null, null,
                getSourceCreator().getTemplate("/test.html")), hintBag, null);

        assertNull(classDescMap.get("com.outer.dto.EntryDto"));
    }

    public void testNewClassDesc() throws Exception {

        assertEquals("マッチしたパターンに関連付けられているスーパークラス名がセットされていること",
                "com.example.web.IndexPageBaseBase", target_.newClassDesc(
                        "com.example.web.IndexPage").getSuperclassName());

        assertEquals(
                "マッチするパターンが無くデフォルトのスーパークラス名が指定されている場合はデフォルトのスーパークラス名がセットされていること",
                "com.example.page.TestPageBaseBase", target_.newClassDesc(
                        "com.example.page.TestPage").getSuperclassName());
    }

    public void testMergeWithExistentClass() throws Exception {

        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge3");
        cd.setSuperclass(Merge3BaseBase.class);
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

        target_.mergeWithExistentClass(cd, true);

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

    public void testMergeWithExistentClass2() throws Exception {

        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge2");
        cd.setSuperclass(Merge2BaseBase.class);
        MethodDesc md = new MethodDescImpl("_render");
        md.setReturnTypeDesc(new TypeDescImpl(Void.TYPE));
        md.setParameterDescs(new ParameterDesc[0]);
        cd.setMethodDesc(md);

        target_.mergeWithExistentClass(cd, true);

        assertNull(cd.getMethodDesc(md));
    }

    public void testGetClassDesc_引数が0個で返り値がStringのmethodについてはボディを保存するようなBodyDescが生成されること()
            throws Exception {

        ClassDesc cd = target_.getClassDesc(Class1Base.class,
                "org.seasar.ymir.extension.creator.impl.Class1");
        BodyDesc actual = cd.getMethodDescs()[0].getBodyDesc();
        assertEquals("return \"return value\";", ((Map) actual.getRoot())
                .get("body"));
    }

    public void testFilterResponse() throws Exception {

        assertEquals(
                "<html><head>"
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/prototype/prototype.js\"></script>"
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/scriptaculous/scriptaculous.js\"></script>"
                        + "<script type=\"text/javascript\" src=\"/context/__ymir__/resource/js/sourceCreator.js\"></script>"
                        + "</head></html>", target_
                        .filterResponse("<html><head></head></html>"));
    }

    public void testMergeWithExistentClass_既にギャップクラスがあるがベースクラスがない場合にObjectのメソッドがベースクラスに追加されないこと()
            throws Exception {

        getConfiguration().removeProperty(
                Globals.APPKEY_SOURCECREATOR_SUPERCLASS);

        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge5");

        target_.mergeWithExistentClass(cd, true);

        assertNull(cd.getPropertyDesc("class"));
    }

    public void testMergeWithExistentClass2_validationFailedなどがスーパークラスにある時は自動生成されないこと()
            throws Exception {

        ClassDesc cd = new ClassDescImpl(
                "com.example.page.SourceCreatorImplTestPage");
        cd.setSuperclass(SourceCreatorImplTestPageBaseBase.class);
        MethodDescImpl md = new MethodDescImpl(
                ConstraintInterceptor.ACTION_VALIDATIONFAILED);
        md.setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                Notes.class) });
        cd.setMethodDesc(md);

        target_.mergeWithExistentClass(cd, true);

        assertNull(cd.getMethodDesc(md));
    }

    public void testMergeWithExistentClass3_Baseクラスの親クラス情報は維持されること()
            throws Exception {

        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge3");
        cd.setSuperclass(TestPageBase.class);

        target_.mergeWithExistentClass(cd, true);

        assertEquals("Ymirが親クラスを指定するようになっている場合でも、親クラスが指定されているのであればそれが維持されること",
                "org.seasar.ymir.extension.creator.impl.Merge3BaseBase", cd
                        .getSuperclassName());
    }

    public void testMergeWithExistentClass4_Baseクラスの親クラス情報は維持されること2()
            throws Exception {

        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge6");
        cd.setSuperclass(TestPageBase.class);

        target_.mergeWithExistentClass(cd, true);

        assertNull("Ymirが親クラスを指定するようになっている場合でも、親クラスが指定されていないならばそれが維持されること", cd
                .getSuperclassName());
    }

    public void testMergeWithExistentClass5_Baseクラスのabstract状態が維持されること()
            throws Exception {

        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge7");
        cd.setSuperclass(TestPageBase.class);

        target_.mergeWithExistentClass(cd, true);

        assertTrue(cd.isBaseClassAbstract());
    }

    public void testMergeWithExistentClass6_プロパティのGetterやSetterがスーパークラスにある時は自動生成されないこと()
            throws Exception {

        ClassDesc cd = new ClassDescImpl(
                "org.seasar.ymir.extension.creator.impl.Merge8");
        PropertyDescImpl pd = new PropertyDescImpl("value");
        pd.setMode(PropertyDesc.READ);
        cd.setPropertyDesc(pd);

        target_.mergeWithExistentClass(cd, true);

        assertNull(cd.getPropertyDesc("value"));
    }

    @SuppressWarnings("deprecation")
    public void testCreateAnnotationDesc() throws Exception {
        AnnotationDesc[] ads = target_.createAnnotationDescs(Hoe2.class);
        assertEquals(1, ads.length);
        assertEquals(Deprecated.class.getName(), ads[0].getName());
    }
}
