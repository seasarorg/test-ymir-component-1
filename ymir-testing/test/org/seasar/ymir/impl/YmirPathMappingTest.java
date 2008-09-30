package org.seasar.ymir.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.Action;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.annotation.handler.impl.AnnotationHandlerImpl;
import org.seasar.ymir.cache.impl.CacheManagerImpl;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.impl.YmirPathMapping.Button;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockApplicationManager;
import org.seasar.ymir.mock.MockRequest;

public class YmirPathMappingTest extends TestCase {
    private YmirPathMapping target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        target_ = new YmirPathMapping("/([a-zA-Z][a-zA-Z0-9]*)\\.(html|do)",
                "${1}Page");
        ActionManagerImpl actionManager = new ActionManagerImpl();
        TypeConversionManager typeConversionManager = new YmirTypeConversionManager();
        actionManager.setTypeConversionManager(typeConversionManager);
        ComponentMetaDataFactoryImpl componentMetaDataFactory = new ComponentMetaDataFactoryImpl();
        CacheManagerImpl cacheManager = new CacheManagerImpl();
        HotdeployManager hotdeployManager = new HotdeployManagerImpl();
        cacheManager.setHotdeployManager(hotdeployManager);
        AnnotationHandlerImpl annotationHandler = new AnnotationHandlerImpl();
        annotationHandler.setCacheManager(cacheManager);
        componentMetaDataFactory.setAnnotationHandler(annotationHandler);
        ApplicationManager applicationManager = new MockApplicationManager();
        applicationManager.setContextApplication(new MockApplication());
        componentMetaDataFactory.setApplicationManager(applicationManager);
        componentMetaDataFactory.setCacheManager(cacheManager);
        actionManager.setComponentMetaDataFactory(componentMetaDataFactory);
        actionManager.setAnnotationHandler(annotationHandler);
        target_.setActionManager(actionManager);
    }

    public void testGetAction_親のボタン用アクション・子のボタン用のアクション・親の通常アクション・子の通常アクションの順で探索すること()
            throws Exception {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("search", new String[] { "" });
        Request request = new MockRequest().setParameterMap(parameterMap);
        VariableResolver resolver = target_.match("/index.html",
                Request.METHOD_POST);
        Parent4Page parent4Page = new Parent4Page();
        Child4Page child4Page = new Child4Page();
        PageComponent pageComponent = new PageComponentImpl(parent4Page,
                Parent4Page.class, new PageComponent[] { new PageComponentImpl(
                        child4Page, Child4Page.class) });

        Action action = target_.getAction(pageComponent, request, resolver);

        assertNotNull(action);
        assertSame(child4Page, action.getTarget());
        assertEquals("_post_search", action.getName());
    }

    public void test_複数パラメータつきアクションのためのActionオブジェクトを生成できること() throws Exception {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("search[1][test][hoe]", new String[] { "" });
        Request request = new MockRequest().setParameterMap(parameterMap);
        VariableResolver resolver = target_.match("/index.html",
                Request.METHOD_POST);
        PageComponent pageComponent = new PageComponentImpl(
                new PathMappingImplTest1Page(), PathMappingImplTest1Page.class,
                new PageComponent[0]);

        Action action = target_.getAction(pageComponent, request, resolver);

        assertNotNull(action);
        assertEquals("_post_search", action.getName());
        MethodInvoker invoker = action.getMethodInvoker();
        Object[] params = invoker.getParameters();
        assertEquals(5, params.length);
        int idx = 0;
        assertEquals(Integer.valueOf(1), params[idx++]);
        assertEquals("test", params[idx++]);
        assertEquals(Integer.valueOf(0), params[idx++]);
        assertEquals(Integer.valueOf(0), params[idx++]);
        assertNull(params[idx++]);
    }

    public void testGetAction_imageタイプのinputタグの名前に対応するアクションが正しく返されること()
            throws Exception {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("image.x", new String[] { "" });
        parameterMap.put("image.y", new String[] { "" });
        Request request = new MockRequest().setParameterMap(parameterMap);
        VariableResolver resolver = target_.match("/index.html",
                Request.METHOD_POST);
        Test6Page test6Page = new Test6Page();
        PageComponent pageComponent = new PageComponentImpl(test6Page,
                Test6Page.class);

        Action action = target_.getAction(pageComponent, request, resolver);

        assertNotNull(action);
        assertEquals("_post_image", action.getName());
    }

    public void testConstructor_Map() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(YmirPathMapping.KEY_ACTIONNAME_TEMPLATE,
                "actionNameTemplateValue");
        map
                .put(YmirPathMapping.KEY_BUTTONNAMEPATTERN,
                        "buttonNamePatternValue");
        map.put(YmirPathMapping.KEY_DENIED, "true");
        map.put(YmirPathMapping.KEY_PAGECOMPONENTNAME_TEMPLATE,
                "pageComponentNameTemplateValue");
        map.put(YmirPathMapping.KEY_PATHINFO_TEMPLATE, "pathInfoTemplateValue");
        map.put(YmirPathMapping.KEY_PATTERN, "patternValue");
        map.put(YmirPathMapping.KEY_PARAMETER_TEMPLATE,
                "queryStringTemplateValue");

        YmirPathMapping target = new YmirPathMapping(
                new HashMap<String, Object>(map));

        assertEquals("actionNameTemplateValue", target.getActionNameTemplate());
        assertEquals("buttonNamePatternValue", target
                .getButtonNamePatternStringForDispatching());
        assertTrue(target.isDenied());
        assertEquals("pageComponentNameTemplateValue", target
                .getPageComponentNameTemplate());
        assertEquals("pathInfoTemplateValue", target.getPathInfoTemplate());
        assertEquals("patternValue", target.getPattern().pattern());
        assertEquals("queryStringTemplateValue", target
                .getQueryStringTemplate());

        map.put(YmirPathMapping.KEY_DENIED, Boolean.TRUE);

        target = new YmirPathMapping(new HashMap<String, Object>(map));

        assertTrue(target.isDenied());
    }

    public void testGetParameterMap() throws Exception {
        YmirPathMapping target = new YmirPathMapping(
                "/article/([^/]*)/([^/]*)\\.html", "articlePage", null,
                "category=${1};sequence=${2}");

        Map<String, String[]> actual = target.getParameterMap(target.match(
                "/article/science&technology/15.html", Request.METHOD_GET));

        assertEquals("science&technology", actual.get("category")[0]);
        assertEquals("15", actual.get("sequence")[0]);
    }

    public void testMapsToPageComponentName() throws Exception {
        YmirPathMapping target = new YmirPathMapping("/([^/]*)/([^/]*)\\.html",
                "${1}Pag${2}e", "", "^_([a-zA-Z][a-zA-Z0-9]*)$");

        assertFalse(target.mapsToPageComponentName(null));
        assertFalse(target.mapsToPageComponentName("saru"));
        assertTrue(target.mapsToPageComponentName("Page"));
        assertTrue(target.mapsToPageComponentName("saruPage"));
    }

    public void testButton1() throws Exception {
        Button button = new Button("abc");
        assertTrue(button.isValid());
        assertEquals("abc", button.getName());
        assertEquals(0, button.getParameters().length);
    }

    public void testButton2() throws Exception {
        Button button = new Button("abc[1][2]");
        assertTrue(button.isValid());
        assertEquals("abc", button.getName());
        assertEquals(2, button.getParameters().length);
        int idx = 0;
        assertEquals("1", button.getParameters()[idx++]);
        assertEquals("2", button.getParameters()[idx++]);
    }

    public void testButton3() throws Exception {
        Button button = new Button("abc*");
        assertFalse(button.isValid());
    }

    public void testButton4() throws Exception {
        Button button = new Button("abc.x");
        assertTrue(button.isValid());
        assertEquals("abc", button.getName());
        assertEquals(0, button.getParameters().length);
    }

    public void testButton5() throws Exception {
        Button button = new Button("abc.y");
        assertTrue(button.isValid());
        assertEquals("abc", button.getName());
        assertEquals(0, button.getParameters().length);
    }

    public void testButton6() throws Exception {
        Button button = new Button("abc.z");
        assertFalse(button.isValid());
    }

    public void testButton7() throws Exception {
        Button button = new Button("abc[1][2].x");
        assertTrue(button.isValid());
        assertEquals("abc", button.getName());
        assertEquals(2, button.getParameters().length);
        int idx = 0;
        assertEquals("1", button.getParameters()[idx++]);
        assertEquals("2", button.getParameters()[idx++]);
    }
}