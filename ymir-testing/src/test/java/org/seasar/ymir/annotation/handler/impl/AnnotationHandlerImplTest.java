package org.seasar.ymir.annotation.handler.impl;

import java.util.Arrays;
import java.util.Comparator;

import junit.framework.TestCase;

import org.seasar.ymir.cache.impl.CacheManagerImpl;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.impl.Hoe;
import org.seasar.ymir.impl.HoeAlias;
import org.seasar.ymir.impl.HoeAliass;
import org.seasar.ymir.impl.HoeHolder;

public class AnnotationHandlerImplTest extends TestCase {
    private AnnotationHandlerImpl target_ = new AnnotationHandlerImpl();

    @Override
    protected void setUp() throws Exception {
        CacheManagerImpl cacheManager = new CacheManagerImpl();
        cacheManager.setHotdeployManager(new HotdeployManagerImpl());
        target_.setCacheManager(cacheManager);
    }

    @HoeAliass(@HoeAlias)
    public void testIsAnnotationPresent() throws Exception {
        assertTrue(target_.isAnnotationPresent(getClass().getMethod(
                "testIsAnnotationPresent", new Class[0]), Hoe.class));
    }

    public void testGetAnnotations() throws Exception {
        Hoe[] actual = target_.getAnnotations(HoeHolder.class, Hoe.class);
        Arrays.sort(actual, new Comparator<Hoe>() {
            public int compare(Hoe o1, Hoe o2) {
                return o1.value().compareTo(o2.value());
            }
        });

        assertEquals(5, actual.length);
        int idx = 0;
        assertEquals("1", actual[idx++].value());
        assertEquals("2", actual[idx++].value());
        assertEquals("3", actual[idx++].value());
        assertEquals("4", actual[idx++].value());
        assertEquals("5", actual[idx++].value());
    }

    public void testGetParameterAnnotations() throws Exception {
        Hoe[] actual = target_.getParameterAnnotations(HoeHolder.class
                .getMethod("hoe", new Class<?>[] { String.class }), 0,
                Hoe.class);
        Arrays.sort(actual, new Comparator<Hoe>() {
            public int compare(Hoe o1, Hoe o2) {
                return o1.value().compareTo(o2.value());
            }
        });

        assertEquals(5, actual.length);
        int idx = 0;
        assertEquals("1", actual[idx++].value());
        assertEquals("2", actual[idx++].value());
        assertEquals("3", actual[idx++].value());
        assertEquals("4", actual[idx++].value());
        assertEquals("5", actual[idx++].value());
    }
}
