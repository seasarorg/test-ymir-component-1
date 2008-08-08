package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;

import junit.framework.TestCase;

import org.seasar.ymir.annotation.In;
import org.seasar.ymir.annotation.Ins;
import org.seasar.ymir.annotation.handler.AnnotationElement;
import org.seasar.ymir.scope.impl.ApplicationScope;
import org.seasar.ymir.scope.impl.SessionScope;

public class CollectionAnnotationElementTest extends TestCase {
    private CollectionAnnotationElement target_ = new CollectionAnnotationElement();

    public void testExpand() throws Exception {
        Ins ins = new Ins() {
            public In[] value() {
                return new In[] { new In() {
                    public String[] actionName() {
                        return new String[0];
                    }

                    public boolean injectWhereNull() {
                        return false;
                    }

                    public String name() {
                        return "";
                    }

                    public Class<?> scopeClass() {
                        return Object.class;
                    }

                    public String scopeName() {
                        return "";
                    }

                    public Class<? extends Annotation> annotationType() {
                        return In.class;
                    }

                    public Class<?> value() {
                        return SessionScope.class;
                    }
                }, new In() {
                    public String[] actionName() {
                        return new String[0];
                    }

                    public boolean injectWhereNull() {
                        return false;
                    }

                    public String name() {
                        return "";
                    }

                    public Class<?> scopeClass() {
                        return Object.class;
                    }

                    public String scopeName() {
                        return "";
                    }

                    public Class<? extends Annotation> annotationType() {
                        return In.class;
                    }

                    public Class<?> value() {
                        return ApplicationScope.class;
                    }
                } };
            }

            public Class<? extends Annotation> annotationType() {
                return Ins.class;
            }
        };

        target_.expand(ins);
        AnnotationElement[] actual = target_.getExpandedElements();

        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals(SessionScope.class, ((In) actual[idx++].getAnnotation())
                .value());
        assertEquals(ApplicationScope.class, ((In) actual[idx++]
                .getAnnotation()).value());
    }
}
