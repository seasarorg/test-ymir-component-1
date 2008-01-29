package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Out;

public class PageMetaDataImplTest extends TestCase {
    private PageMetaDataImpl targetNonStrict_ = new PageMetaDataImpl(
            Hoe2Page.class, null) {
        @Override
        protected boolean isStrictInjection(S2Container container) {
            return false;
        }

        @Override
        void registerForInjectionFromScope(In in, Method method) {
        }

        @Override
        void registerForOutjectionToScope(Out out, Method method) {
        }
    };

    private PageMetaDataImpl targetStrict_ = new PageMetaDataImpl(
            Hoe2Page.class, null) {
        @Override
        protected boolean isStrictInjection(S2Container container) {
            return true;
        }

        @Override
        void registerForInjectionFromScope(In in, Method method) {
        }

        @Override
        void registerForOutjectionToScope(Out out, Method method) {
        }
    };

    public void testNonStrictInjectionの時にInアノテーションが付与されたメソッドがプロテクトされること()
            throws Exception {
        assertTrue(targetNonStrict_.isProtected("hoehoe"));
    }

    public void testNonStrictInjectionの時にOutアノテーションが付与されたメソッドがプロテクトされないこと()
            throws Exception {
        assertFalse(targetNonStrict_.isProtected("hoehoe2"));
    }

    public void testNonStrictInjectionの時にBindingアノテーションが付与されたメソッドがプロテクトされること()
            throws Exception {
        assertTrue(targetNonStrict_.isProtected("hoehoe3"));
    }

    public void testNonStrictInjectionの時にProtectedアノテーションが付与されたメソッドがプロテクトされること()
            throws Exception {
        assertTrue(targetNonStrict_.isProtected("hoehoe4"));
    }

    public void testNonStrictInjectionの時にFormFileのSetterメソッドがプロテクトされないこと()
            throws Exception {
        assertFalse(targetNonStrict_.isProtected("hoehoe5"));
    }

    public void testNonStrictInjectionの時にFormFileの配列のSetterメソッドがプロテクトされないこと()
            throws Exception {
        assertFalse(targetNonStrict_.isProtected("hoehoe6"));
    }

    public void testNonStrictInjectionの時にインタフェースを引数とするSetterメソッドがプロテクトされること()
            throws Exception {
        assertTrue(targetNonStrict_.isProtected("hoehoe7"));
    }

    public void testStrictInjectionの時にRequestParameterアノテーションが付与されていないメソッドがプロテクトされること()
            throws Exception {
        assertTrue(targetStrict_.isProtected("fugafuga"));
        assertTrue(targetStrict_.isProtected("fugafuga2"));
        assertTrue(targetStrict_.isProtected("fugafuga3"));
        assertTrue(targetStrict_.isProtected("fugafuga4"));
        assertTrue(targetStrict_.isProtected("fugafuga5"));
        assertTrue(targetStrict_.isProtected("fugafuga6"));
        assertTrue(targetStrict_.isProtected("fugafuga7"));
    }

    public void testStrictInjectionの時にRequestParameterアノテーションが付与されたメソッドがプロテクトされないこと()
            throws Exception {
        assertFalse(targetStrict_.isProtected("fugafuga8"));
    }

    public void testネストしたパラメータについて正しくプロテクトされること() throws Exception {
        assertTrue(targetStrict_.isProtected("aaa[0].bbb[1].ccc"));

        assertFalse(targetStrict_.isProtected("bbb[0].ccc[1].ddd"));
    }
}
