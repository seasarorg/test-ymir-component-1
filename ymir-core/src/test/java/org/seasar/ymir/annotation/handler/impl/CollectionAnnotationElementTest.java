package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;

import junit.framework.TestCase;

import org.seasar.ymir.annotation.handler.AnnotationElement;

public class CollectionAnnotationElementTest extends TestCase {
    private CollectionAnnotationElement target_ = new CollectionAnnotationElement();

    public void testExpand() throws Exception {
        HoeFuga hoeFuga = new HoeFuga() {
            public Class<? extends Annotation> annotationType() {
                return HoeFuga.class;
            }

            public Hoe[] abc() {
                return new Hoe[] { new Hoe() {
                    public Class<? extends Annotation> annotationType() {
                        return Hoe.class;
                    }

                    public int value() {
                        return 1;
                    }
                }, new Hoe() {
                    public Class<? extends Annotation> annotationType() {
                        return Hoe.class;
                    }

                    public int value() {
                        return 2;
                    }
                } };
            }

            public Fuga value() {
                return new Fuga() {
                    public Class<? extends Annotation> annotationType() {
                        return Fuga.class;
                    }

                    public int value() {
                        return 0;
                    }
                };
            }

            public Hoe[] zzz() {
                return new Hoe[] { new Hoe() {
                    public Class<? extends Annotation> annotationType() {
                        return Hoe.class;
                    }

                    public int value() {
                        return 3;
                    }
                }, new Hoe() {
                    public Class<? extends Annotation> annotationType() {
                        return Hoe.class;
                    }

                    public int value() {
                        return 4;
                    }
                } };
            }
        };

        target_.expand(hoeFuga);
        AnnotationElement[] actual = target_.getExpandedElements();

        assertEquals(5, actual.length);
        int idx = 0;
        assertEquals(1, ((Hoe) actual[idx++].getAnnotation()).value());
        assertEquals(2, ((Hoe) actual[idx++].getAnnotation()).value());
        assertEquals(0, ((Fuga) actual[idx++].getAnnotation()).value());
        assertEquals(3, ((Hoe) actual[idx++].getAnnotation()).value());
        assertEquals(4, ((Hoe) actual[idx++].getAnnotation()).value());
    }
}
