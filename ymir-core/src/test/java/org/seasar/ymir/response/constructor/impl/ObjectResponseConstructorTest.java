package org.seasar.ymir.response.constructor.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import org.seasar.ymir.Path;
import org.seasar.ymir.Response;
import org.seasar.ymir.response.constructor.ResponseConstructor;

import junit.framework.TestCase;

public class ObjectResponseConstructorTest extends TestCase {
    private ObjectResponseConstructor target_;

    private Class<?> calledClass_;

    protected void setUp() throws Exception {
        super.setUp();
        ResponseConstructorSelectorImpl selector = new ResponseConstructorSelectorImpl();
        selector.add(new StringResponseConstructor() {
            public Response constructResponse(Object page, String returnValue) {
                calledClass_ = getTargetClass();
                return null;
            }
        });
        selector.add(new PathResponseConstructor() {
            public Response constructResponse(Object page, Path returnValue) {
                calledClass_ = getTargetClass();
                return null;
            }
        });
        selector.add(new InputStreamResponseConstructor());
        selector.add(new ResponseConstructor<I>() {
            public Response constructResponse(Object page, I returnValue) {
                return null;
            }

            public Class<I> getTargetClass() {
                return I.class;
            }
        });

        target_ = new ObjectResponseConstructor();
        target_.setResponseConstructorSelector(selector);
    }

    public void testConstructResponse() throws Exception {
        target_.constructResponse(null, "test");
        assertSame(String.class, calledClass_);

        target_.constructResponse(null, new Path());
        assertSame(Path.class, calledClass_);

        target_.constructResponse(null, new Date());
        assertSame("登録されていないクラスのオブジェクトを渡した場合はStringに変換されて処理されること",
                String.class, calledClass_);
    }

    public void testFindResponseConstructor() throws Exception {
        assertNull("Objectクラスに対してはnullを返すこと", target_
                .findResponseConstructor(Object.class));

        assertEquals("指定したクラスに対するResponseConstructorがあればそれを返すこと", Path.class,
                target_.findResponseConstructor(Path.class).getTargetClass());

        assertEquals("スーパークラスのResponseConstructorがあればそれを返すこと", Path.class,
                target_.findResponseConstructor(new Path() {
                    private static final long serialVersionUID = 1L;
                }.getClass()).getTargetClass());

        assertEquals("実装しているインタフェースのResponseConstructorがあればそれを返すこと",
                InputStream.class, target_.findResponseConstructor(
                        FileInputStream.class).getTargetClass());

        assertEquals("スーパークラスが実装しているインタフェースのサブインタフェースも見ること", I.class, target_
                .findResponseConstructor(L.class).getTargetClass());
    }
}
