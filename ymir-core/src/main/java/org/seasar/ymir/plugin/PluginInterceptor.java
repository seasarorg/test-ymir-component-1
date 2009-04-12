package org.seasar.ymir.plugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.plugin.annotation.PluginAnnotation;

/**
 * アノテーションが付与されているPageクラスまたはアクションメソッドが処理された場合のみ
 * アノテーションが指定するプラグインの処理を織り込むためのインターセプタです。
 * 
 * @author yokota
 * @since 0.9.6
 */
public class PluginInterceptor extends AbstractYmirProcessInterceptor {
    private static final Comparator<Pair<?>> COMPARATOR_PAIR = new Comparator<Pair<?>>() {
        public int compare(Pair<?> o1, Pair<?> o2) {
            return (int) Math.signum(o1.getPlugin().getPriority()
                    - o2.getPlugin().getPriority());
        }
    };

    private static final Pair<?>[] EMPTY_PAIRS = new Pair<?>[0];

    private AnnotationHandler annotationHandler_;

    private ApplicationManager applicationManager_;

    protected Map<Class<?>, Pair<?>[]> pairsByClassMap_;

    protected Map<Method, Pair<?>[]> pairsByMethodMap_;

    private ThreadLocal<Pair<?>[]> pairs_ = new ThreadLocal<Pair<?>[]>();

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        pairsByClassMap_ = cacheManager.newMap();
        pairsByMethodMap_ = cacheManager.newMap();
    }

    @Override
    public PageComponent pageComponentCreated(Request request,
            PageComponent pageComponent) {
        Pair<?>[] pairs = getPairs(pageComponent.getPageClass());
        for (int i = 0; i < pairs.length; i++) {
            pairs[i].pageComponentCreated(request, pageComponent);
        }
        return pageComponent;
    }

    Pair<?>[] getPairs(Class<?> clazz) {
        Pair<?>[] pairs = pairsByClassMap_.get(clazz);
        if (pairs == null) {
            pairs = createPairs(clazz);
            pairsByClassMap_.put(clazz, pairs);
        }
        return pairs;
    }

    @SuppressWarnings("unchecked")
    Pair<?>[] createPairs(AnnotatedElement element) {
        S2Container container = applicationManager_.findContextApplication()
                .getS2Container();
        Annotation[] annotations = annotationHandler_.getMarkedAnnotations(
                element, PluginAnnotation.class);
        Pair<?>[] pairs = new Pair[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            PluginAnnotation plugin = annotations[i].annotationType()
                    .getAnnotation(PluginAnnotation.class);
            pairs[i] = new Pair<Annotation>((Plugin<Annotation>) container
                    .getComponent(plugin.value()), annotations[i]);
        }
        sortPairs(pairs);
        return pairs;
    }

    static void sortPairs(Pair<?>[] pairs) {
        if (pairs != null) {
            Arrays.sort(pairs, COMPARATOR_PAIR);
        }
    }

    @Override
    public Action actionInvoking(Request request, Action action) {
        Pair<?>[] pairs = getPairs(request, action);
        pairs_.set(pairs);
        for (int i = 0; i < pairs.length; i++) {
            action = pairs[i].actionInvoking(request, action);
        }
        return action;
    }

    Pair<?>[] getPairs(Request request, Action action) {
        Pair<?>[] pairsForClass = getPairs(request.getCurrentDispatch()
                .getPageComponent().getPageClass());
        if (action == null) {
            return pairsForClass;
        }

        Method method = action.getMethodInvoker().getMethod();
        if (method == null) {
            return pairsForClass;
        }

        Pair<?>[] pairsForMethod = pairsByMethodMap_.get(method);
        if (pairsForMethod == null) {
            pairsForMethod = createPairs(method);
            pairsByMethodMap_.put(method, pairsForMethod);
        }

        return merge(pairsForClass, pairsForMethod);
    }

    Pair<?>[] merge(Pair<?>[] pair1, Pair<?>[] pair2) {
        Map<Plugin<?>, Pair<?>> map = new IdentityHashMap<Plugin<?>, Pair<?>>();
        for (int i = 0; i < pair1.length; i++) {
            map.put(pair1[i].getPlugin(), pair1[i]);
        }
        for (int i = 0; i < pair2.length; i++) {
            map.put(pair2[i].getPlugin(), pair2[i]);
        }
        Pair<?>[] pairs = map.values().toArray(new Pair[0]);

        sortPairs(pairs);

        return pairs;
    }

    @Override
    public Response actionInvoked(Request request, Response response) {
        Pair<?>[] pairs = getPairs();
        for (int i = 0; i < pairs.length; i++) {
            response = pairs[i].actionInvoked(request, response);
        }
        return response;
    }

    @Override
    public Response responseCreated(Request request, Response response) {
        Pair<?>[] pairs = getPairs();
        for (int i = 0; i < pairs.length; i++) {
            response = pairs[i].responseCreated(request, response);
        }
        return response;
    }

    Pair<?>[] getPairs() {
        Pair<?>[] pairs = pairs_.get();
        if (pairs == null) {
            pairs = EMPTY_PAIRS;
        }
        return pairs;
    }

    @Override
    public void responseProcessingStarted(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) {
        Pair<?>[] pairs = getPairs();
        for (int i = 0; i < pairs.length; i++) {
            pairs[i].responseProcessingStarted(context, httpRequest,
                    httpResponse, request, response);
        }
    }

    @Override
    public void leavingRequest(Request request) {
        Pair<?>[] pairs = getPairs();
        for (int i = 0; i < pairs.length; i++) {
            pairs[i].leavingRequest(request);
        }
    }

    @Override
    public void leftRequest() {
        try {
            Pair<?>[] pairs = getPairs();
            for (int i = 0; i < pairs.length; i++) {
                pairs[i].leftRequest();
            }
        } finally {
            pairs_.set(null);
        }
    }

    static class Pair<A extends Annotation> {
        private Plugin<A> plugin_;

        private A annotation_;

        public Pair(Plugin<A> plugin, A annotation) {
            plugin_ = plugin;
            annotation_ = annotation;
        }

        public Plugin<A> getPlugin() {
            return plugin_;
        }

        public A getAnnotation() {
            return annotation_;
        }

        public double getPriority() {
            return plugin_.getPriority();
        }

        public PageComponent pageComponentCreated(Request request,
                PageComponent pageComponent) {
            return plugin_.pageComponentCreated(request, pageComponent,
                    annotation_);
        }

        public Action actionInvoking(Request request, Action action) {
            return plugin_.actionInvoking(request, action, annotation_);
        }

        public Response actionInvoked(Request request, Response response) {
            return plugin_.actionInvoked(request, response, annotation_);
        }

        public Response responseCreated(Request request, Response response) {
            return plugin_.responseCreated(request, response, annotation_);
        }

        public void responseProcessingStarted(ServletContext context,
                HttpServletRequest httpRequest,
                HttpServletResponse httpResponse, Request request,
                Response response) {
            plugin_.responseProcessingStarted(context, httpRequest,
                    httpResponse, request, response, annotation_);
        }

        public void leavingRequest(Request request) {
            plugin_.leavingRequest(request, annotation_);
        }

        public void leftRequest() {
            plugin_.leftRequest(annotation_);
        }
    }
}
