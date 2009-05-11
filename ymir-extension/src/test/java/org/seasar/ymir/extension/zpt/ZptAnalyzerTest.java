package org.seasar.ymir.extension.zpt;

import static org.seasar.ymir.extension.Globals.ATTR_UNDECIDEDPARAMETERNAMES;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.Application;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassHint;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.SourceCreatorSetting;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.MethodDescImpl;
import org.seasar.ymir.extension.creator.impl.ParameterDescImpl;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;
import org.seasar.ymir.extension.creator.mapping.PathMappingExtraData;
import org.seasar.ymir.extension.creator.mapping.impl.YmirPathMappingExtraData;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.impl.ApplicationManagerImpl;
import org.seasar.ymir.impl.MatchedPathMappingImpl;
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.impl.YmirPathMapping;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.render.AbstractCandidate;
import org.seasar.ymir.render.Candidate;
import org.seasar.ymir.render.Selector;
import org.seasar.ymir.render.html.Option;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.util.FlexibleList;
import org.seasar.ymir.util.ServletUtils;

import com.example.dto.SaruDto;
import com.example.web.Test47Page;

import net.skirnir.freyja.TemplateContext;

public class ZptAnalyzerTest extends TestCase {
    private static final String CLASSNAME = "com.example.web.IndexPage";

    private PathMapping[] mappings_ = new PathMapping[] {
        new YmirPathMapping("", "_RootPage", "${METHOD}", "", null),
        new YmirPathMapping("/([^/]+)\\.(.+)", "${1}Page", "${METHOD}", "",
                null),
        new YmirPathMapping("/[^/]+/(.+)\\.(.+)", "${1}Page", "${METHOD}", "",
                null), };

    private ZptAnalyzer target_;

    private SourceCreatorImpl sourceCreator_;

    private DescPool pool_;

    private String path_ = "/hoe.html";

    private AnalyzerContext context_;

    private Notes warnings_ = new Notes();

    protected void setUp() throws Exception {
        target_ = new ZptAnalyzer() {
            @Override
            AnalyzerTalTagEvaluator newAnalyzerTalTagEvaluator() {
                return new AnalyzerTalTagEvaluator() {
                    @Override
                    public TemplateContext newContext() {
                        if (context_ != null) {
                            return context_;
                        } else {
                            TemplateContext context = super.newContext();
                            context.getVariableResolver().setVariable("saru",
                                    new SaruDto());
                            return context;
                        }
                    }
                };
            }
        };
        sourceCreator_ = new SourceCreatorImpl() {
            @Override
            public MatchedPathMapping findMatchedPathMapping(String path,
                    HttpMethod method) {
                String normalizedPath = ServletUtils.normalizePath(path);
                for (int i = 0; i < mappings_.length; i++) {
                    VariableResolver resolver = mappings_[i].match(
                            normalizedPath, method);
                    if (resolver != null) {
                        return new MatchedPathMappingImpl(mappings_[i],
                                resolver);
                    }
                }
                return null;
            }

            @Override
            public String getComponentName(String path, HttpMethod method) {
                MatchedPathMapping matched = findMatchedPathMapping(path,
                        method);
                if (matched == null) {
                    return null;
                } else {
                    return matched.getPageComponentName();
                }
            }

            @Override
            public String getClassName(String componentName) {
                if (componentName.equals("_RootPage")) {
                    return "com.example.web._RootPage";
                } else if (componentName.endsWith("Page")) {
                    int underscore = componentName.indexOf('_');
                    if (underscore < 0) {
                        return "com.example.web."
                                + Character
                                        .toUpperCase(componentName.charAt(0))
                                + componentName.substring(1);
                    } else {
                        return "com.example.web."
                                + componentName.substring(0, underscore)
                                + "."
                                + Character.toUpperCase(componentName
                                        .charAt(underscore + 1))
                                + componentName.substring(underscore + 2);
                    }
                } else {
                    return null;
                }
            }

            @Override
            public String getFirstRootPackageName() {
                return "com.example";
            }

            @Override
            public String[] getRootPackageNames() {
                return new String[] { getFirstRootPackageName() };
            }

            @Override
            public Class<?> getClass(String className) {
                if (className == null) {
                    return null;
                }
                if ("byte".equals(className)) {
                    return Byte.TYPE;
                } else if ("short".equals(className)) {
                    return Short.TYPE;
                } else if ("int".equals(className)) {
                    return Integer.TYPE;
                } else if ("long".equals(className)) {
                    return Long.TYPE;
                } else if ("float".equals(className)) {
                    return Float.TYPE;
                } else if ("double".equals(className)) {
                    return Double.TYPE;
                } else if ("char".equals(className)) {
                    return Character.TYPE;
                } else if ("boolean".equals(className)) {
                    return Boolean.TYPE;
                }
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException ex) {
                    return null;
                }
            }
        };
        sourceCreator_.setNamingConvention(new NamingConventionImpl());
        S2Container container = S2ContainerFactory
                .create("org/seasar/ymir/extension/zpt/ZptAnalyzerTest.dicon");
        ApplicationManagerImpl applicationManager = (ApplicationManagerImpl) container
                .getComponent(ApplicationManagerImpl.class);
        MockApplication mockApplication = new MockApplication()
                .setS2Container(container);
        applicationManager.setHotdeployManager(new HotdeployManagerImpl());
        applicationManager.setBaseApplication(mockApplication);
        sourceCreator_.setApplicationManager(applicationManager);
        sourceCreator_
                .setPathMappingExtraDatas(new PathMappingExtraData<?>[] { new YmirPathMappingExtraData() });
        target_.setSourceCreator(sourceCreator_);

        YmirImpl ymir = new YmirImpl();
        ymir.setApplicationManager(applicationManager);
        YmirContext.setYmir(ymir);
    }

    private void act(String methodName, ClassCreationHintBag hintBag) {
        act(methodName, CLASSNAME, hintBag, null);
    }

    private void act(String methodName) {
        act(methodName, CLASSNAME);
    }

    private void act(String methodName, String pageClassName) {
        act(methodName, pageClassName, null);
    }

    private void act(String methodName, String[] ignoreVariables) {
        act(methodName, CLASSNAME, ignoreVariables);
    }

    private void act(final String methodName, String pageClassName,
            String[] ignoreVariables) {
        act(methodName, pageClassName, null, ignoreVariables);
    }

    private void act(final String methodName, String pageClassName,
            ClassCreationHintBag hintBag, String[] ignoreVariables) {
        ServletContext servletContext = new MockServletContextImpl("/context");
        HttpServletRequest request = new MockHttpServletRequestImpl(
                servletContext, "/index.html");
        Class<?> pageClass;
        try {
            pageClass = Class.forName(pageClassName);
        } catch (ClassNotFoundException ex) {
            pageClass = null;
        }
        if (pageClass != null) {
            try {
                request.setAttribute("self", pageClass.newInstance());
            } catch (InstantiationException ignore) {
            } catch (IllegalAccessException ignore) {
            }
        }
        HttpServletResponse response = new MockHttpServletResponseImpl(request);
        MockRequest ymirRequest = new MockRequest();
        MockDispatch dispatch = new MockDispatch();
        dispatch.setPath("/index.html");
        ymirRequest.enterDispatch(dispatch);
        ymirRequest.setContextPath("/context");
        pool_ = DescPool.newInstance(sourceCreator_, hintBag);
        target_.analyze(servletContext, request, response, ymirRequest, path_,
                HttpMethod.GET, new Template() {
                    public InputStream getInputStream() throws IOException {
                        return getClass().getResourceAsStream(
                                "ZptAnalyzerTest_" + methodName + ".zpt");
                    }

                    public boolean exists() {
                        return true;
                    }

                    public String getName() {
                        return "ZptAnalyzerTest_" + methodName + ".zpt";
                    }

                    public OutputStream getOutputStream() throws IOException {
                        return null;
                    }

                    public String getPath() {
                        return path_;
                    }

                    public long lastModified() {
                        return 0;
                    }

                    public String getEncoding() {
                        return "UTF-8";
                    }

                    public boolean isDirectory() {
                        return false;
                    }

                    public boolean mkdirs() {
                        return false;
                    }
                }, pageClassName, ignoreVariables, pool_, warnings_);
    }

    private ClassDesc getClassDesc(String name) {
        if (pool_.contains(name)) {
            return pool_.getClassDesc(name);
        } else {
            return null;
        }
    }

    public void testAnalyze1_TemplateAnalyzerではリクエストメソッドのためのメソッド定義を生成しないこと()
            throws Exception {

        act("testAnalyze1");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        assertNotNull(cd.getPropertyDesc("body"));
        assertNull(cd.getMethodDesc(new MethodDescImpl(pool_, "GET")));
    }

    public void testAnalyze2_フォームパラメータに対してはSetterが作成され参照時にはGetterが生成されること()
            throws Exception {

        act("testAnalyze2");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        PropertyDesc pd = cd.getPropertyDesc("text");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        ClassDesc cd2 = getClassDesc("com.example.web.ActionPage");
        assertNotNull(cd2);
        PropertyDesc pd2 = cd2.getPropertyDesc("text");
        assertNotNull(pd2);
        assertTrue(pd2.isWritable());
    }

    public void testAnalyze3_repeat対象のプロパティの型が配列型になること() throws Exception {

        act("testAnalyze3");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        PropertyDesc pd = cd.getPropertyDesc("entities");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertFalse(pd.getTypeDesc().isExplicit());
        assertEquals("com.example.dto.EntityDto[]", pd.getTypeDesc().getName());
        ClassDesc cd2 = getClassDesc("com.example.dto.EntityDto");
        assertNotNull(cd2);
        PropertyDesc pd2 = cd2.getPropertyDesc("content");
        assertNotNull(pd2);
        assertTrue(pd2.isReadable());
        assertTrue(pd2.isWritable());
    }

    public void testAnalyze4_repeat対象のプロパティの型が適切に決定されること() throws Exception {

        act("testAnalyze4");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        PropertyDesc pd = cd.getPropertyDesc("strings");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertFalse(pd.getTypeDesc().isExplicit());
        assertEquals("プロパティを持たないリピート対象変数はStringの配列になること", "String[]", pd
                .getTypeDesc().getName());

        pd = cd.getPropertyDesc("entities");
        assertEquals("プロパティを持つリピート対象変数はDtoの配列になること",
                "com.example.dto.EntityDto[]", pd.getTypeDesc().getName());
        cd = getClassDesc("com.example.dto.EntityDto");
        assertNotNull("プロパティを持つリピート対象変数の型が生成されていること", cd);
        assertNotNull("Dto型がプロパティを持つこと", cd.getPropertyDesc("content"));
    }

    public void testAnalyze5_ボタンに対応するプロパティは生成されないこと() throws Exception {

        act("testAnalyze5");

        ClassDesc cd = getClassDesc("com.example.web.UpdatePage");
        assertNotNull(cd);
        PropertyDesc pd = cd.getPropertyDesc("text");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertTrue(pd.isWritable());
        pd = cd.getPropertyDesc("select");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertTrue(pd.isWritable());
        pd = cd.getPropertyDesc("textarea");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertTrue(pd.isWritable());
        pd = cd.getPropertyDesc("file");
        assertNotNull(pd);
        assertEquals(FormFile.class.getName(), pd.getTypeDesc().getName());
        assertNull("ボタンに対応するプロパティは生成されないこと", cd.getPropertyDesc("button"));
        assertNull("ボタンに対応するプロパティは生成されないこと", cd.getPropertyDesc("image"));
        assertNull("ボタンに対応するプロパティは生成されないこと", cd.getPropertyDesc("submit"));

        assertNull(cd.getPropertyDesc("text2"));
        assertNull(cd.getPropertyDesc("select2"));
        assertNull(cd.getPropertyDesc("textarea2"));
        assertNull(cd.getPropertyDesc("file2"));
        assertNull(cd.getPropertyDesc("button2"));
        assertNull(cd.getPropertyDesc("image2"));
        assertNull(cd.getPropertyDesc("submit2"));

        MethodDesc md = cd.getMethodDesc(new MethodDescImpl(pool_, "POST"));
        assertNull("YmirPathMappingではnameつきボタンがある場合はPOSTアクションメソッドは生成されないこと", md);
    }

    public void testAnalyze6_YMIR_21_コンポーネント名っぽいものが指定されていてもDtoとみなしてClassDescを生成すること()
            throws Exception {

        act("testAnalyze6");

        ClassDesc cd = getClassDesc("com.example.dto.HoeHoeDto");
        assertNotNull("コンポーネント名っぽいものが指定されていてもDtoとみなしてClassDescを生成すること", cd);
        assertNotNull(cd.getPropertyDesc("body"));
    }

    public void testAnalyze7_href属性からプロパティが自動生成されること() throws Exception {

        act("testAnalyze7");

        ClassDesc actual = getClassDesc("com.example.web.ActionPage");
        assertNotNull(actual);
        MethodDesc md = actual.getMethodDesc(new MethodDescImpl(pool_, "GET"));
        assertNotNull(md);
        PropertyDesc pd = actual.getPropertyDesc("id");
        assertNotNull(pd);
    }

    public void testAnalyze8_src属性からプロパティが自動生成されること() throws Exception {

        act("testAnalyze8");

        ClassDesc actual = getClassDesc("com.example.web.ActionPage");
        assertNotNull(actual);
        MethodDesc md = actual.getMethodDesc(new MethodDescImpl(pool_, "GET"));
        assertNotNull(md);
        PropertyDesc pd = actual.getPropertyDesc("id");
        assertNotNull(pd);
    }

    public void testAnalyze9_formのパラメータに対応するプロパティが生成されること() throws Exception {

        act("testAnalyze9");

        ClassDesc cd = getClassDesc("com.example.web.ActionPage");
        PropertyDesc pd = cd.getPropertyDesc("test");
        assertNotNull("formのパラメータに対応するプロパティが生成されること", pd);
        assertEquals("com.example.dto.TestDto", pd.getTypeDesc().getName());
        assertTrue(pd.isReadable());

        cd = getClassDesc("com.example.dto.TestDto");
        assertNotNull("formのパラメータを保持するためのDtoクラスが生成されること", cd);
        assertNotNull(cd.getPropertyDesc("name"));
        assertNotNull(cd.getPropertyDesc("body"));
    }

    public void testAnalyze10_ファイルパラメータについてはプロパティの型がFormFileになること()
            throws Exception {

        act("testAnalyze10");

        ClassDesc cd = getClassDesc("com.example.dto.TestDto");
        PropertyDesc pd = cd.getPropertyDesc("file");
        assertEquals(FormFile.class.getName(), pd.getTypeDesc().getName());
    }

    public void testAnalyze11_フォームパラメータ名に添え字指定がある場合に適切に自動生成されること()
            throws Exception {

        act("testAnalyze11");

        ClassDesc cd = getClassDesc("com.example.web.ActionPage");
        PropertyDesc pd = cd.getPropertyDesc("tests");
        assertTrue("添字指定がある場合はListになること", pd.getTypeDesc().isCollection());
        assertEquals(List.class.getName(), pd.getTypeDesc()
                .getCollectionClassName());
        assertEquals(
                "添え字指定がある場合は初期値が設定されること",
                "new org.seasar.ymir.util.FlexibleList<com.example.dto.TestDto>()",
                pd.getInitialValue());
        assertEquals("tests配列プロパティのDto型名は単数形になること", "com.example.dto.TestDto",
                pd.getTypeDesc().getComponentClassDesc().getName());
    }

    public void testAnalyze12_添字指定があるファイルパラメータについてはプロパティの型がFormFileの配列になること()
            throws Exception {

        act("testAnalyze12");

        ClassDesc cd = getClassDesc("com.example.dto.TestDto");
        PropertyDesc pd = cd.getPropertyDesc("files");
        assertEquals(List.class.getName(), pd.getTypeDesc()
                .getCollectionClassName());
        assertEquals(
                "new org.seasar.ymir.util.FlexibleList<org.seasar.ymir.FormFile>()",
                pd.getInitialValue());
        assertEquals(FormFile.class.getName(), pd.getTypeDesc()
                .getComponentClassDesc().getName());
    }

    public void testAnalyze13_page式で指定したパラメータ置換が正しく行われてその結果Pageクラスが生成されること()
            throws Exception {

        act("testAnalyze13");

        assertNotNull(getClassDesc("com.example.web.Image0Page"));
    }

    public void testAnalyze15_組み込み変数などVariableResolverから元々取得可能な変数に対応するClassDescは生成されないこと()
            throws Exception {

        act("testAnalyze15");

        assertNull(getClassDesc("com.example.dto.RepeatDto"));
    }

    public void testAnalyze17_submit関連の扱いが正しく行なわれること() throws Exception {

        act("testAnalyze17");

        ClassDesc cd = getClassDesc("com.example.web.UpdatePage");
        assertNotNull(cd);
        assertNull("buttonのnameに対応するプロパティのgetter/setterは生成されないこと", cd
                .getPropertyDesc("button"));
        assertNull("imageのnameに対応するプロパティのgetter/setterは生成されないこと", cd
                .getPropertyDesc("image"));
        assertNull("submitのnameに対応するプロパティのgetter/setterは生成されないこと", cd
                .getPropertyDesc("submit"));
        assertNotNull("対応するメソッドがないボタンがある場合はデフォルトのアクションメソッドが生成されること", cd
                .getMethodDesc(new MethodDescImpl(pool_, "POST")));
        assertNotNull("buttonのnameに対応するアクションメソッドが生成されること", cd
                .getMethodDesc(new MethodDescImpl(pool_, "POST_button")));
        assertNotNull("imageのnameに対応するアクションメソッドが生成されること", cd
                .getMethodDesc(new MethodDescImpl(pool_, "POST_image")));
        assertNotNull("submitのnameに対応するアクションメソッドが生成されること", cd
                .getMethodDesc(new MethodDescImpl(pool_, "POST_submit")));
        assertNotNull("nameが実行時に決まるようなタグでもstring定数なら自動生成対象になること", cd
                .getMethodDesc(new MethodDescImpl(pool_, "POST_submit2")));
        assertNull("nameが実行時に決まるようなタグでパラメータを持つものは無視されること", cd
                .getMethodDesc(new MethodDescImpl(pool_, "POST_submitt")));
    }

    public void testAnalyze18_パスが3段以上でも正しくClassDescを生成できること() throws Exception {

        act("testAnalyze18");

        ClassDesc cd = getClassDesc(CLASSNAME).getPropertyDesc("entry")
                .getTypeDesc().getComponentClassDesc();
        assertEquals("com.example.dto.BodyDto", cd.getPropertyDesc("body")
                .getTypeDesc().getComponentClassDesc().getName());
    }

    public void testAnalyze19_余計なDtoが生成されないこと() throws Exception {

        act("testAnalyze19");

        assertNull(getClassDesc("com.example.dto.CommentsDto"));
    }

    public void testAnalyze20_talConditionの式については生成するプロパティの型がbooleanになること()
            throws Exception {

        act("testAnalyze20");

        PropertyDesc actual = getClassDesc(CLASSNAME)
                .getPropertyDesc("enabled");
        assertNotNull(actual);
        assertEquals("boolean", actual.getTypeDesc().getName());
    }

    public void testAnalyze21_無視するように指定した変数が正しく無視されること() throws Exception {

        act("testAnalyze21", new String[] { "self" });

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNull(cd.getPropertyDesc("self"));
    }

    public void testAnalyze22_同一のnameを持つパラメータが複数存在する場合は配列になること()
            throws Exception {

        act("testAnalyze22");

        ClassDesc cd = getClassDesc("com.example.web.ActionPage");
        PropertyDesc pd = cd.getPropertyDesc("check");
        assertTrue(pd.getTypeDesc().isCollection());
        assertNull(pd.getTypeDesc().getCollectionClassName());
    }

    public void testAnalyze23_同一のnameを持つパラメータがラジオボタンである場合はコレクションにならないこと()
            throws Exception {

        act("testAnalyze23");

        ClassDesc cd = getClassDesc("com.example.web.ActionPage");
        PropertyDesc pd = cd.getPropertyDesc("radio");
        assertFalse(pd.getTypeDesc().isCollection());
    }

    public void testAnalyze24_コレクションに対するsize呼び出しがあっても正しく自動生成されること()
            throws Exception {

        act("testAnalyze24");

        ClassDesc cd = getClassDesc(CLASSNAME);
        PropertyDesc pd = cd.getPropertyDesc("comment");
        assertTrue(pd.getTypeDesc().isCollection());
        assertEquals("com.example.dto.CommentDto", pd.getTypeDesc()
                .getComponentClassDesc().getName());
        assertNull(getClassDesc("com.example.dto.CommentDto").getPropertyDesc(
                "size"));
    }

    public void testAnalyze25_notesはYmir内蔵のNotesクラスと解釈されること() throws Exception {

        act("testAnalyze25");

        assertNull(getClassDesc("com.example.dto.NotesDto"));
    }

    public void testAnalyze26_明示的に型が指定されているプロパティについては自動的にマッピングされるDto型が生成されないこと()
            throws Exception {

        act("testAnalyze26");

        ClassDesc cd = getClassDesc(CLASSNAME);
        PropertyDesc pd = cd.getPropertyDesc("results");
        assertEquals(Note.class.getName(), pd.getTypeDesc()
                .getComponentClassDesc().getName());
        assertNull(getClassDesc(Note.class.getName()));
        assertNull(getClassDesc("com.example.dto.ResultDto"));
    }

    public void testAnalyze27_入力値を復元するタイプのinputタグがある場合にプロパティの型が配列になってしまわないこと()
            throws Exception {

        act("testAnalyze27", "com.example.web.HoePage");

        ClassDesc cd = getClassDesc("com.example.web.HoePage");
        PropertyDesc pd = cd.getPropertyDesc("value");
        assertFalse(pd.getTypeDesc().isCollection());
    }

    public void testAnalyze28_既にクラスがある状態で再生成されるとプロパティの既存の型が保存されること()
            throws Exception {

        act("testAnalyze28");

        ClassDesc cd = getClassDesc(CLASSNAME);
        PropertyDesc pd = cd.getPropertyDesc("param2");
        assertEquals("Integer[]", pd.getTypeDesc().getName());
    }

    public void testAnalyze29_サブアプリケーション以下のPageクラスについて既にクラスがある状態で再生成されるとプロパティの既存の型が保存されること()
            throws Exception {

        path_ = "/sub_index.html";
        act("testAnalyze29", "com.example.web.sub.IndexPage");

        ClassDesc cd = getClassDesc("com.example.web.sub.IndexPage");
        PropertyDesc pd = cd.getPropertyDesc("param2");
        assertEquals("Integer[]", pd.getTypeDesc().getName());
    }

    public void testAnalyze30_condition系のプロパティが実はDtoだった時に正しくDtoと解釈されること()
            throws Exception {

        act("testAnalyze30");

        ClassDesc cd = getClassDesc(CLASSNAME);
        PropertyDesc pd = cd.getPropertyDesc("information");
        assertEquals("com.example.dto.InformationDto", pd.getTypeDesc()
                .getName());
        pd = pd.getTypeDesc().getComponentClassDesc()
                .getPropertyDesc("entries");
        assertEquals("com.example.dto.EntryDto[]", pd.getTypeDesc().getName());
    }

    public void testAnalyze31_サブアプリケーションに属するPageについてはDTOもサブアプリケーションに生成されること()
            throws Exception {

        act("testAnalyze31", "com.example.web.sub.TestPage");

        ClassDesc cd = getClassDesc("com.example.web.sub.TestPage");
        PropertyDesc pd = cd.getPropertyDesc("entries");
        assertEquals("com.example.dto.sub.EntryDto[]", pd.getTypeDesc()
                .getName());
    }

    public void testAnalyze32_サブアプリケーションに属するPageについてはDTOもサブアプリケーションに生成されること()
            throws Exception {

        act("testAnalyze32");

        ClassDesc cd = getClassDesc("com.example.web.sub.TestPage");
        PropertyDesc pd = cd.getPropertyDesc("entry");
        assertEquals("com.example.dto.sub.EntryDto", pd.getTypeDesc().getName());
    }

    public void testAnalyze33_optionタグがrepeat指定されている場合でYmirのRenderClassを利用する設定の場合は対象プロパティの型がOptionの配列になること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze33", "com.example.web.sub.TestPage");

        ClassDesc cd = getClassDesc("com.example.web.sub.TestPage");
        PropertyDesc pd = cd.getPropertyDesc("entries");
        assertEquals(Option.class.getName() + "[]", pd.getTypeDesc().getName());
    }

    public void testAnalyze34_DTOのためのgetterが定義済みで返り値の型が決まっている場合は新たにDTOを生成しようとしないこと()
            throws Exception {

        act("testAnalyze34", "com.example.web.Test34Page");

        ClassDesc cd = getClassDesc("com.example.web.Test34Page");
        PropertyDesc pd = cd.getPropertyDesc("options");
        assertEquals(Option.class.getName() + "[]", pd.getTypeDesc().getName());
        assertNull(getClassDesc("com.example.dto.OptionDto"));
    }

    public void testAnalyze35_repeatタグの一時変数に対して呼び出されたプロパティはクラスにも追加されること()
            throws Exception {
        act("testAnalyze35", "com.example.web.Test35Page");

        ClassDesc cd = getClassDesc("com.example.web.Test35Page");
        PropertyDesc pd = cd.getPropertyDesc("entries");
        assertEquals("com.example.dto.Entry2Dto[]", pd.getTypeDesc().getName());
        cd = pd.getTypeDesc().getComponentClassDesc();
        assertNotNull(cd.getPropertyDesc("value"));
    }

    public void testAnalyze36_名前に大文字小文字の違いしかない2つのプロパティを使った場合にExceptionがスローされてしまう問題を再現するためのテストケース()
            throws Exception {
        act("testAnalyze36", "com.example.web.Test36Page");

        ClassDesc cd = getClassDesc("com.example.web.Test36Page");
        PropertyDesc pd = cd.getPropertyDesc("comment").getTypeDesc()
                .getComponentClassDesc().getPropertyDesc("entries");
        assertEquals("com.example.dto.EntryDto[]", pd.getTypeDesc().getName());
    }

    public void testAnalyze37_パスの途中に同じ文字列が2度現れてもStackOverflowにならないこと()
            throws Exception {

        try {
            act("testAnalyze37");
        } catch (StackOverflowError e) {
            fail();
        }
    }

    public void testAnalyze38_括弧などを含むプロパティは自動生成の対象外になること_本当は引数つきgetterなどを生成して欲しいところだが()
            throws Exception {

        act("testAnalyze38");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNull(cd.getPropertyDesc("arrayValue"));
        assertNull(cd.getPropertyDesc("arrayValue[1]"));
        assertNull(cd.getPropertyDesc("mapValue"));
        assertNull(cd.getPropertyDesc("mapValue(1)"));
    }

    public void testAnalyze39_ルートパッケージ外のクラスのプロパティを指定してもExceptionがスローされないこと()
            throws Exception {

        try {
            act("testAnalyze39");
        } catch (Throwable t) {
            fail();
        }

        assertNull(getClassDesc("org.seasar.ymir.dto.NameDto"));
    }

    public void testAnalyze40_action属性がシャープである場合は自動生成の対象としないこと()
            throws Exception {

        act("testAnalyze40");

        assertNull(getClassDesc("com.example.web.HoePage"));
    }

    public void testAnalyze41_talConditionに書いたものがhrefにも出てくる時はbooleanにならないこと()
            throws Exception {

        act("testAnalyze41");

        ClassDesc cd = getClassDesc(CLASSNAME);

        assertEquals("String", cd.getPropertyDesc("value").getTypeDesc()
                .getName());
    }

    public void testAnalyze42_repeatについてDTO名は配列を返すプロパティ名からではなく一時変数名から名づけられること()
            throws Exception {

        act("testAnalyze42");

        assertNotNull(getClassDesc("com.example.dto.RepeatEntryDto"));
        assertEquals("com.example.dto.RepeatEntryDto", getClassDesc(CLASSNAME)
                .getPropertyDesc("entryList").getTypeDesc()
                .getComponentClassDesc().getName());
    }

    public void testAnalyze43_hrefに書かれたリクエストパラメータのsetterが追加されること()
            throws Exception {

        act("testAnalyze43");

        PropertyDesc pd = getClassDesc(CLASSNAME).getPropertyDesc("value");
        assertNotNull(pd);
        AnnotationDesc[] ads = pd.getAnnotationDescsOnSetter();
        assertEquals("[#YMIR-187]", 1, ads.length);
        assertEquals("[#YMIR-187]", RequestParameter.class.getName(), ads[0]
                .getName());
    }

    public void testAnalyze44_talConditionに書いたものがtalContentにも出てくる時はbooleanにならないこと()
            throws Exception {

        act("testAnalyze44");

        ClassDesc cd = getClassDesc(CLASSNAME);

        assertEquals("String", cd.getPropertyDesc("value").getTypeDesc()
                .getName());
    }

    public void testAnalyze45_配列型のnameの添え字部分だけが実行時に決定されるinputタグが自動生成の対象になること()
            throws Exception {

        act("testAnalyze45");

        ClassDesc cd = getClassDesc(CLASSNAME);
        PropertyDesc pd = cd.getPropertyDesc("entries");
        assertNotNull(pd);
        assertEquals("java.util.List<com.example.dto.EntryDto>", pd
                .getTypeDesc().getName());

        assertNull("副作用で添え字部分以外の部分が実行時に決定されるinputタグが自動生成対象になったりしていないこと", cd
                .getPropertyDesc("entry"));
    }

    public void testAnalyze46_配列型のnameの添え字部分だけが実行時に決定されるsubmitタイプのinputタグに対応するアクションが生成されること()
            throws Exception {

        act("testAnalyze46");

        ClassDesc cd = getClassDesc("com.example.web.UpdatePage");
        MethodDesc md = new MethodDescImpl(pool_, "POST_action");
        md.setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(pool_,
                Integer.TYPE, "index") });
        md = cd.getMethodDesc(md);
        assertNotNull(md);
    }

    public void testAnalyze47_インナークラスをプロパティなどの型として持つクラスの自動生成が正しく行なえること()
            throws Exception {

        act("testAnalyze47", Test47Page.class.getName());

        ClassDesc cd = getClassDesc(Test47Page.class.getName());
        assertEquals(Test47Page.class.getName() + ".Internal", cd
                .getPropertyDesc("property").getTypeDesc().getName());
    }

    public void testAnalyze48_name属性を持つformについてはSetterがDtoへのSetterとなること()
            throws Exception {

        sourceCreator_
                .getApplication()
                .setProperty(
                        SourceCreatorSetting.APPKEY_SOURCECREATOR_FEATURE_CREATEFORMDTO_ENABLE,
                        String.valueOf(true));

        act("testAnalyze48");

        ClassDesc cd = getClassDesc(CLASSNAME);
        PropertyDesc pd = cd.getPropertyDesc("form");
        assertNotNull(pd);
        assertEquals("com.example.dto.FormDto", pd.getTypeDesc().getName());
        assertEquals(PropertyDesc.NONE, pd.getMode());
        assertTrue(pd.hasMeta(Globals.META_NAME_PROPERTY));
        assertEquals("form", pd.getMetaFirstValue(Globals.META_NAME_PROPERTY));
        pd = cd.getPropertyDesc("value");
        assertNotNull(pd);
        assertTrue(pd.isWritable());
        assertTrue(pd.hasMetaOnSetter(Globals.META_NAME_FORMPROPERTY));
        assertEquals("form", pd
                .getMetaFirstValueOnSetter(Globals.META_NAME_FORMPROPERTY));
    }

    public void testAnalyze49_RequestParameterアノテーションが付与されること()
            throws Exception {
        act("testAnalyze49");

        ClassDesc cd = getClassDesc(CLASSNAME);
        PropertyDesc pd = cd.getPropertyDesc("value");
        assertNotNull(pd);
        assertNotNull(pd.getAnnotationDescOnSetter(RequestParameter.class
                .getName()));
        pd = cd.getPropertyDesc("aaa");
        assertNotNull(pd);
        assertNotNull(pd.getAnnotationDescOnGetter(RequestParameter.class
                .getName()));
    }

    public void testAnalyze50_talAttributesの中でtalDefineされた変数を使ってもExceptionがスローされないこと()
            throws Exception {

        try {
            act("testAnalyze50");
        } catch (Exception ex) {
            fail();
        }
    }

    public void testAnalyze51_YMIR_84_buttonタグを正しく解釈できること() throws Exception {

        act("testAnalyze51");

        ClassDesc cd = getClassDesc("com.example.web.UpdatePage");
        assertNotNull(cd);
        assertNull(
                "dispatchingByRequestParameterがtrueであるようなPathMappingにactionのパスがマッチする場合はbuttonのnameに対応するプロパティのgetter/setterは生成されないこと",
                cd.getPropertyDesc("button"));
        assertNull(
                "dispatchingByRequestParameterがtrueであるようなPathMappingにactionのパスがマッチする場合はbutton（type=submit）のnameに対応するプロパティのgetter/setterは生成されないこと",
                cd.getPropertyDesc("submit"));
        assertNotNull(
                "dispatchingByRequestParameterがtrueであるようなPathMappingにactionのパスがマッチする場合はbuttonのnameに対応するアクションメソッドが生成されること",
                cd.getMethodDesc(new MethodDescImpl(pool_, "POST_button")));
        assertNotNull(
                "dispatchingByRequestParameterがtrueであるようなPathMappingにactionのパスがマッチする場合はbutton（type=submit）のnameに対応するアクションメソッドが生成されること",
                cd.getMethodDesc(new MethodDescImpl(pool_, "POST_submit")));

        cd = getClassDesc(CLASSNAME);
        assertNull("formの外にあるbuttonタグは無視されること", cd.getPropertyDesc("button2"));
        assertNull("formの外にあるbuttonタグは無視されること", cd
                .getMethodDesc(new MethodDescImpl(pool_, "POST_button2")));
        assertNull("formの外にあるbuttonタグ（type=submit）は無視されること", cd
                .getPropertyDesc("submit2"));
        assertNull("formの外にあるbuttonタグ（type=submit）は無視されること", cd
                .getMethodDesc(new MethodDescImpl(pool_, "POST_submit2")));
    }

    public void testAnalyze52_YMIR_180_formのname属性で指定した名前と同じ名前のプロパティのGetterが生成されること()
            throws Exception {

        sourceCreator_
                .getApplication()
                .setProperty(
                        SourceCreatorSetting.APPKEY_SOURCECREATOR_FEATURE_CREATEFORMDTO_ENABLE,
                        String.valueOf(true));

        act("testAnalyze52");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        PropertyDesc pd = cd.getPropertyDesc("form");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
    }

    public void testAnalyze53_hrefに書かれたパスにリクエストパラメータがなくてもPageクラスが生成されること()
            throws Exception {

        act("testAnalyze53");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
    }

    public void testAnalyze54_formにパラメータがなくてもPageクラスが生成されること() throws Exception {

        act("testAnalyze54");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
    }

    public void testAnalyze55_omitTagの式については生成するプロパティの型がbooleanになること()
            throws Exception {

        act("testAnalyze55");

        PropertyDesc actual = getClassDesc(CLASSNAME)
                .getPropertyDesc("enabled");
        assertNotNull(actual);
        assertEquals("boolean", actual.getTypeDesc().getName());
    }

    public void testAnalyze56_YMIR_197_name属性を持つformについてformのDTOがGetされている場合はDTOのGetterが生成されること()
            throws Exception {

        sourceCreator_
                .getApplication()
                .setProperty(
                        SourceCreatorSetting.APPKEY_SOURCECREATOR_FEATURE_CREATEFORMDTO_ENABLE,
                        String.valueOf(true));

        act("testAnalyze56");

        ClassDesc cd = getClassDesc(CLASSNAME);
        PropertyDesc pd = cd.getPropertyDesc("form");
        assertNotNull(pd);
        assertEquals("com.example.dto.FormDto", pd.getTypeDesc().getName());
        assertTrue(pd.isReadable());
    }

    public void testAnalyze57_YMIR_198_プロパティの値が使われない場合でもプロパティの型はHintで指定された型になること()
            throws Exception {

        ClassCreationHintBag hintBag = new ClassCreationHintBag(
                new PropertyTypeHint[] {
                    new PropertyTypeHint("com.example.web.IndexPage", "hoes",
                            "com.example.dto.HoeDto[]"),
                    new PropertyTypeHint("com.example.web.IndexPage", "number",
                            "java.lang.Integer") }, null);

        act("testAnalyze57", hintBag);

        ClassDesc cd = getClassDesc(CLASSNAME);
        PropertyDesc pd = cd.getPropertyDesc("hoes");
        assertNotNull(pd);
        assertEquals("com.example.dto.HoeDto[]", pd.getTypeDesc().getName());
        pd = cd.getPropertyDesc("number");
        assertNotNull(pd);
        assertEquals("java.lang.Integer", pd.getTypeDesc()
                .getComponentClassDesc().getName());
    }

    public void testAnalyze58_submit関連のタグについて正しく自動生成されること() throws Exception {

        act("testAnalyze58");

        ClassDesc updateCd = getClassDesc("com.example.web.UpdatePage");
        MethodDesc md = updateCd
                .getMethodDesc(new MethodDescImpl(pool_, "POST"));
        assertNull("submit等がない場合はPOSTが作られないこと", md);

        ClassDesc update2Cd = getClassDesc("com.example.web.Update2Page");
        md = update2Cd.getMethodDesc(new MethodDescImpl(pool_, "GET"));
        assertNull("[#YMIR-207] HTTPメソッドがGETである場合でもGETが作られないこと", md);

        ClassDesc indexCd = getClassDesc("com.example.web.IndexPage");
        md = indexCd.getMethodDesc(new MethodDescImpl(pool_, "GET"));
        assertNull(
                "nameつきsubmitしかない場合でもポストバックの時はGETが作られるが、HTMLテンプレートの解析処理では作られない",
                md);

        ClassDesc update3Cd = getClassDesc("com.example.web.Update3Page");
        md = update3Cd.getMethodDesc(new MethodDescImpl(pool_, "POST"));
        assertNull("nameを持つsubmit等がある場合はPOSTが作られないこと", md);

        ClassDesc update4Cd = getClassDesc("com.example.web.Update4Page");
        md = update4Cd.getMethodDesc(new MethodDescImpl(pool_, "POST"));
        assertNotNull("適切なnameを持つsubmit等がない場合はPOSTが作られること", md);

        ClassDesc update5Cd = getClassDesc("com.example.web.Update5Page");
        md = update5Cd.getMethodDesc(new MethodDescImpl(pool_, "POST"));
        assertNotNull("nameを持たないsubmit等がある場合はPOSTが作られること", md);

        ClassDesc update6Cd = getClassDesc("com.example.web.Update6Page");
        md = update6Cd.getMethodDesc(new MethodDescImpl(pool_, "POST"));
        assertNotNull(
                "適切なnameを持つsubmit等があっても適切なnameを持つsubmit等がない場合はPOSTが作られること", md);

        ClassDesc update7Cd = getClassDesc("com.example.web.Update7Page");
        md = update7Cd.getMethodDesc(new MethodDescImpl(pool_, "POST"));
        assertNotNull(
                "適切なnameを持つsubmit等があってもnameを持たないsubmit等がある場合はPOSTが作られること", md);
    }

    public void testAnalyze59_YMIR_243_fileパラメータの型を明示的に指定することができること()
            throws Exception {

        ClassCreationHintBag bag = new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(CLASSNAME,
                        "file", "java.lang.String") }, new ClassHint[0]);
        act("testAnalyze59", CLASSNAME, bag, null);

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        PropertyDesc pd = cd.getPropertyDesc("file");
        assertNotNull(pd);
        assertEquals("String", pd.getTypeDesc().getName());
    }

    public void testAnalyze60_YMIR_269_Java式を使っても例外が発生しないこと() throws Exception {

        try {
            act("testAnalyze60");
        } catch (Throwable t) {
            fail();
        }
    }

    public void testAnalyze61_典型的なname動的生成のテンプレートでnameが自動生成に使われること()
            throws Exception {

        context_ = new AnalyzerContext() {
            @Override
            public void setRepeatedPropertyGeneratedAsList(
                    boolean repeatedPropertyGeneratedAsList) {
                super.setRepeatedPropertyGeneratedAsList(true);
            }
        };

        act("testAnalyze61");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        PropertyDesc pd = cd.getPropertyDesc("items");
        assertNotNull(pd);
        assertEquals("java.util.List<com.example.dto.ItemDto>", pd
                .getTypeDesc().getName());

        cd = getClassDesc("com.example.dto.ItemDto");
        assertNotNull(cd);
        pd = cd.getPropertyDesc("value");
        assertNotNull(pd);
    }

    public void testAnalyze62_YMIR_279_リピートされるプロパティをListとして自動生成する機能が正しく機能すること()
            throws Exception {

        context_ = new AnalyzerContext() {
            @Override
            public void setRepeatedPropertyGeneratedAsList(
                    boolean repeatedPropertyGeneratedAsList) {
                super.setRepeatedPropertyGeneratedAsList(true);
            }
        };

        act("testAnalyze62");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        PropertyDesc pd = cd.getPropertyDesc("strings");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertFalse(pd.getTypeDesc().isExplicit());
        assertEquals("プロパティを持たないリピート対象変数はStringのListになること",
                "java.util.List<String>", pd.getTypeDesc().getName());

        pd = cd.getPropertyDesc("entities");
        assertEquals("プロパティを持つリピート対象変数はDtoのListになること",
                "java.util.List<com.example.dto.EntityDto>", pd.getTypeDesc()
                        .getName());
        assertEquals("表示系のListの初期値はArrayListであること",
                "new java.util.ArrayList<com.example.dto.EntityDto>()", pd
                        .getInitialValue());
        cd = getClassDesc("com.example.dto.EntityDto");
        assertNotNull("プロパティを持つリピート対象変数の型が生成されていること", cd);
        assertNotNull("Dto型がプロパティを持つこと", cd.getPropertyDesc("content"));
    }

    public void testAnalyze63_YMIR_279_リピートされるプロパティをListとして自動生成する機能が正しく機能すること_Option()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze63");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        PropertyDesc pd = cd.getPropertyDesc("options");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertFalse(pd.getTypeDesc().isExplicit());
        assertEquals("デフォルトではリピート対象変数がOptionの配列になること", Option.class.getName()
                + "[]", pd.getTypeDesc().getName());

        context_ = new AnalyzerContext() {
            @Override
            public void setRepeatedPropertyGeneratedAsList(
                    boolean repeatedPropertyGeneratedAsList) {
                super.setRepeatedPropertyGeneratedAsList(true);
            }
        };
        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "net.skirnir.freyja.render.*");

        act("testAnalyze63");

        cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
        pd = cd.getPropertyDesc("options");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertFalse(pd.getTypeDesc().isExplicit());
        assertEquals("リピート対象変数がOptionのListになること", "java.util.List<"
                + Option.class.getName() + ">", pd.getTypeDesc().getName());
    }

    public void testAnalyze64_YMIR_288_postアクションの戻り値の型を変更可能であること()
            throws Exception {

        Application application = sourceCreator_.getApplication();
        application
                .setProperty(
                        SourceCreatorSetting.APPKEYPREFIX_SOURCECREATOR_ACTION_RETURNTYPE
                                + "GET", "org.seasar.ymir.Response");
        application
                .setProperty(
                        SourceCreatorSetting.APPKEYPREFIX_SOURCECREATOR_ACTION_RETURNTYPE
                                + "POST", "java.lang.String");

        act("testAnalyze64");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);

        MethodDesc md = cd.getMethodDesc(new MethodDescImpl(pool_, "GET"));
        assertNotNull(md);
        assertEquals("org.seasar.ymir.Response", md.getReturnTypeDesc()
                .getName());

        md = cd.getMethodDesc(new MethodDescImpl(pool_, "POST"));
        assertNotNull(md);
        assertEquals("String", md.getReturnTypeDesc().getName());

        md = cd.getMethodDesc(new MethodDescImpl(pool_, "POST_button"));
        assertNotNull(md);
        assertEquals("String", md.getReturnTypeDesc().getName());
    }

    public void testAnalyze65_paramSelfを認識すること() throws Exception {

        act("testAnalyze65");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);

        PropertyDesc pd = cd.getPropertyDesc("text");
        assertNotNull(pd);
    }

    public void testAnalyze66_リピートされているパラメータに対応するプロパティはコレクションになること()
            throws Exception {

        act("testAnalyze66");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);

        PropertyDesc pd = cd.getPropertyDesc("fruits");
        assertNotNull(pd);
        assertTrue(pd.getTypeDesc().isCollection());

        pd = cd.getPropertyDesc("one");
        assertNotNull(pd);
        assertFalse("ラジオボタンは例外", pd.getTypeDesc().isCollection());
    }

    public void testAnalyze67_conditionで参照されているプロパティであってもcondition以外でも参照されていたら型がStringになること()
            throws Exception {

        act("testAnalyze67");

        assertEquals("最初に非conditionで参照、次にconditionで参照されている場合", "String",
                getClassDesc(CLASSNAME).getPropertyDesc("text").getTypeDesc()
                        .getName());
        assertEquals("最初にconditionで参照、次に非conditionで参照されている場合", "String",
                getClassDesc(CLASSNAME).getPropertyDesc("text2").getTypeDesc()
                        .getName());
    }

    public void testAnalyze68_conditionで参照されている場合でもPageのプロパティの型とFormDtoのプロパティ型が一致すること()
            throws Exception {

        act("testAnalyze68");

        assertEquals("String", getClassDesc(CLASSNAME).getPropertyDesc("text")
                .getTypeDesc().getName());
        assertEquals("String", getClassDesc("com.example.dto.FormDto")
                .getPropertyDesc("text").getTypeDesc().getName());
    }

    public void testAnalyze69_Pageのプロパティ型を変えた場合にFormDtoのプロパティ型も変更されること()
            throws Exception {

        ClassCreationHintBag bag = new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(CLASSNAME,
                        "text", "Object") }, new ClassHint[0]);
        act("testAnalyze69", bag);

        assertEquals("Object", getClassDesc(CLASSNAME).getPropertyDesc("text")
                .getTypeDesc().getName());
        assertEquals("Object", getClassDesc("com.example.dto.FormDto")
                .getPropertyDesc("text").getTypeDesc().getName());
    }

    public void testAnalyze70_Pageオブジェクトへの参照がない場合でも空のPageが生成されること()
            throws Exception {

        act("testAnalyze70");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);
    }

    public void testAnalyze71_YMIR_301() throws Exception {

        act("testAnalyze71");

        ClassDesc cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);

        String[] names = (String[]) cd
                .getAttribute(ATTR_UNDECIDEDPARAMETERNAMES);
        assertNotNull(names);
        assertEquals("パラメータかボタン名かあいまいなものが検出されること", "edit", names[0]);

        ClassHint classHint = new ClassHint(CLASSNAME);
        classHint.setParameterRole("edit", ParameterRole.BUTTON);
        ClassCreationHintBag bag = new ClassCreationHintBag(
                new PropertyTypeHint[0], new ClassHint[] { classHint });
        act("testAnalyze71", bag);

        cd = getClassDesc(CLASSNAME);
        assertNotNull(cd);

        names = (String[]) cd.getAttribute(ATTR_UNDECIDEDPARAMETERNAMES);
        assertNull(names);
        assertNull("プロパティが生成されないこと", cd.getPropertyDesc("edit"));
        assertNotNull("ボタンに対応するメソッドが生成されること", cd
                .getMethodDesc(new MethodDescImpl(pool_, "GET_edit")));
    }

    public void testAnalyze72_相対パスを正しく解釈できること() throws Exception {

        act("testAnalyze72");

        assertNotNull(getClassDesc(CLASSNAME).getPropertyDesc("a"));
        assertNotNull(getClassDesc("com.example.web.Index2Page")
                .getPropertyDesc("a"));
    }

    public void testAnalyze73_添え字つきパラメータの扱いが正しいこと() throws Exception {

        act("testAnalyze73");

        PropertyDesc pd = getClassDesc(CLASSNAME).getPropertyDesc("entries");
        assertNotNull(pd);
        assertTrue(pd.isReadable());
        assertEquals("form", pd.getMetaFirstValueOnGetter("formProperty"));

        pd = getClassDesc("com.example.dto.FormDto").getPropertyDesc("entries");
        assertNotNull(pd);
        TypeDesc td = pd.getTypeDesc();
        assertEquals("com.example.dto.EntryDto", td.getComponentClassDesc()
                .getName());
        assertTrue(td.isCollection());
        assertEquals(List.class.getName(), td.getCollectionClassName());
        assertEquals(FlexibleList.class.getName(), td
                .getCollectionImplementationClassName());

        pd = getClassDesc("com.example.dto.EntryDto").getPropertyDesc("name");
        assertNotNull(pd);
        td = pd.getTypeDesc();
        assertEquals(String.class.getName(), td.getComponentClassDesc()
                .getName());
        assertFalse(td.isCollection());
    }

    public void testAnalyze74_YMIR_328_プロパティ名と子プロパティ名によってプロパティの型推論が行なわれること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze74");

        PropertyDesc onePd = getClassDesc("com.example.dto.FormDto")
                .getPropertyDesc("oneSelector");
        assertNotNull(onePd);
        assertEquals(
                "[inputタグのname属性] 名前からSelectorクラスと推論。またselectedValueプロパティを持つのでそのまま確定。",
                Selector.class.getName(), onePd.getTypeDesc().getName());

        PropertyDesc twoPd = getClassDesc("com.example.dto.FormDto")
                .getPropertyDesc("twoSelector");
        assertNotNull(twoPd);
        assertEquals(
                "[Path式] 名前からSelectorクラスと推論。またselectedValueプロパティを持つのでそのまま確定。",
                Selector.class.getName(), twoPd.getTypeDesc().getName());

        PropertyDesc threePd = getClassDesc("com.example.dto.FormDto")
                .getPropertyDesc("threeSelector");
        assertNotNull(threePd);
        assertEquals(
                "[inputタグのname属性] 名前からSelectorクラスと推論するが、hogeプロパティを持たないのでDtoと推論",
                "com.example.dto.ThreeSelectorDto", threePd.getTypeDesc()
                        .getName());

        PropertyDesc fourPd = getClassDesc("com.example.dto.FormDto")
                .getPropertyDesc("fourSelector");
        assertNotNull(fourPd);
        assertEquals("[Path式] 名前からSelectorクラスと推論するが、hogeプロパティを持たないのでDtoと推論",
                "com.example.dto.FourSelectorDto", fourPd.getTypeDesc()
                        .getName());

        PropertyDesc fivePd = getClassDesc("com.example.dto.FormDto")
                .getPropertyDesc("fiveSelector");
        assertNotNull(fivePd);
        assertEquals(
                "名前からSelectorクラスと推論。またselectedValueプロパティを持つが、Dtoが既に存在するのでDtoと推論。",
                "com.example.dto.FiveSelectorDto", fivePd.getTypeDesc()
                        .getName());
    }

    public void testAnalyze75_boolean型の属性に対応するプロパティの型がbooleanになること()
            throws Exception {

        act("testAnalyze75");

        PropertyDesc pd = getClassDesc("com.example.dto.FormDto")
                .getPropertyDesc("checked");
        assertEquals("boolean", pd.getTypeDesc().getName());
    }

    public void testAnalyze76_renderクラスのインタフェース型のプロパティに対応するDto型は残されること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze76");

        ClassDesc actual = getClassDesc("com.example.dto.CandidateDto");
        assertNotNull(actual);
    }

    public void testAnalyze77_renderクラスでインタフェース型を返すプロパティに対応するDto型の名前はインタフェース名から推論されること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze77");

        ClassDesc actual = getClassDesc("com.example.dto.CandidateDto");
        assertNotNull(actual);
    }

    public void testAnalyze78_Dtoの循環参照でStackOverflowにならないことと() throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        try {
            act("testAnalyze78");
        } catch (StackOverflowError er) {
            fail();
        }
    }

    public void testAnalyze79_既存の自動生成対象クラスがあるとプロパティが検出されない問題の確認()
            throws Exception {

        act("testAnalyze79");

        PropertyDesc pd = getClassDesc(CLASSNAME).getPropertyDesc("param1");
        assertNotNull(pd);
    }

    public void testAnalyze80_インタフェース型を返す既存DTOクラスのプロパティであってもグループ名がない場合はrepeat変数で受けている場合はrepeat変数名によって実装クラス名が決定されること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        // 正しいサブパッケージにDTOが生成されない不具合があったので、サブパッケージに属するPageクラスでテストするようにしている。 
        act("testAnalyze80", "com.example.web.sub.IndexPage");

        assertNull(getClassDesc("com.example.dto.sub.CandidateDto"));
        assertNotNull(getClassDesc("com.example.dto.sub.EntryDto"));
    }

    public void testAnalyze81_パス式とリクエストパラメータ名が同じプロパティを指す場合にプロパティ型が別々に判断されてしまわないこと()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze81");

        assertNull(getClassDesc("com.example.dto.CandidateDto"));
        assertNull(getClassDesc("com.example.dto.EntryDto"));
        assertNotNull(getClassDesc("com.example.dto.EntryCandidateDto"));
    }

    public void testAnalyze82_インタフェースを返す既存DTOクラスのプロパティに対応する自動生成DTO型はそのインタフェースを実装していること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze82");

        assertNull(getClassDesc("com.example.dto.CandidateDto"));
        ClassDesc actual = getClassDesc("com.example.dto.EntryDto");
        assertNotNull(actual);
        TypeDesc[] tds = actual.getInterfaceTypeDescs();
        assertEquals(1, tds.length);
        assertEquals(Candidate.class.getName(), tds[0].getName());
        assertEquals("インタフェースの抽象実装クラスがあればそれが設定されていること", AbstractCandidate.class
                .getName(), actual.getSuperclassName());
    }

    public void testAnalyze83_インタフェースを返す既存DTOクラスのプロパティをrepeat変数で受けている場合にはグループ名がrepeat変数名よりも優先されること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze83");

        assertNull(getClassDesc("com.example.dto.CandidateDto"));
        assertNull(getClassDesc("com.example.dto.EntDto"));
        assertNull(getClassDesc("com.example.dto.EntryDto"));
        assertNotNull(getClassDesc("com.example.dto.EntryCandidateDto"));
    }

    public void testAnalyze84_インタフェースを返す既存DTOクラスのプロパティをrepeat変数で受けている場合にはグループ名がなければrepeat変数名がインタフェース名よりも優先されること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze84");

        ClassDesc actual = getClassDesc("com.example.dto.CandidateDto");
        assertNotNull(actual);
        assertNotNull(actual.getPropertyDesc("label"));
        assertEquals(1, actual.getPropertyDescs().length);
        assertNotNull(getClassDesc("com.example.dto.EntDto"));
        assertNull(getClassDesc("com.example.dto.EntryDto"));
        assertNull(getClassDesc("com.example.dto.EntryCandidateDto"));
    }

    public void testAnalyze85_再生成の際にインタフェースを返す既存DTOクラスのプロパティの型を正しく推論できること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze85");

        assertNull(getClassDesc("com.example.dto.CandidateDto"));
        assertNull(getClassDesc("com.example.dto.EntDto"));
        assertNull(getClassDesc("com.example.dto.EntryDto"));
        assertNotNull(getClassDesc("com.example.dto.EntryCandidateDto"));
    }

    public void testAnalyze86_ヒントでインタフェースを返す既存DTOクラスのプロパティの型を指定した場合に正しく推論できること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze86", new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.dto.FormDto", "entrySelector",
                        Selector.class.getName()) }, null));

        assertNull(getClassDesc("com.example.dto.CandidateDto"));
        assertNull(getClassDesc("com.example.dto.EntDto"));
        assertNull(getClassDesc("com.example.dto.EntryDto"));
        assertNotNull(getClassDesc("com.example.dto.EntryCandidateDto"));
    }

    public void testAnalyze87_インタフェースを返す既存DTOクラスが入れ子になっている場合に正しく推論できること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze87", new ClassCreationHintBag(
                new PropertyTypeHint[] { new PropertyTypeHint(
                        "com.example.dto.FormDto", "entrySelector",
                        Selector.class.getName()) }, null));

        assertNull(getClassDesc("com.example.dto.CandidateDto"));
        assertNull(getClassDesc("com.example.dto.EntDto"));
        assertNull(getClassDesc("com.example.dto.SubEntDto"));
        assertNotNull(getClassDesc("com.example.dto.EntryCandidateDto"));
        assertNotNull(getClassDesc("com.example.dto.SubEntryCandidateDto"));
    }

    public void testAnalyze88_FormDtoは空でも生成されること() throws Exception {

        act("testAnalyze88");

        assertNotNull(getClassDesc("com.example.dto.FormDto"));
        assertEquals("com.example.dto.FormDto", getClassDesc(CLASSNAME)
                .getPropertyDesc("form").getTypeDesc().getComponentClassDesc()
                .getName());
    }

    public void testAnalyze89_インタフェース型の実装型である自動生成DTOの親クラスはプロパティファイルで指定されていれば指定されたものになること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");
        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEYPREFIX_SOURCECREATOR_SUPERCLASS
                        + "com\\.example\\.dto\\.EntryDto",
                "com.example.dto.Saru");

        act("testAnalyze89");

        assertEquals("com.example.dto.Saru", getClassDesc(
                "com.example.dto.EntryDto").getSuperclassName());
    }

    public void testAnalyze90_インタフェース型の実装型である自動生成DTOの親クラスはもともとの指定が維持されること()
            throws Exception {

        sourceCreator_.getApplication().setProperty(
                SourceCreatorSetting.APPKEY_SOURCECREATOR_DTOSEARCHPATH,
                "org.seasar.ymir.render.*");

        act("testAnalyze90");

        assertEquals("com.example.dto.Entry90DtoBaseBase", getClassDesc(
                "com.example.dto.Entry90Dto").getSuperclassName());
    }
}
