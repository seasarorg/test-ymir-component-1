package org.seasar.ymir.impl;

import junit.framework.TestCase;

import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;

public class RedirectionPathResolverImplTest extends TestCase {

    private RedirectionPathResolverImpl target_ = new RedirectionPathResolverImpl();

    public void testResolve1() throws Exception {

        assertEquals("/", target_.resolve("", new MockRequest()
                .setContextPath("")));
    }

    public void testResolve2() throws Exception {

        assertEquals("/hoe.do", target_.resolve("/hoe.do", new MockRequest()
                .setContextPath("")));
    }

    public void testResolve3() throws Exception {

        assertEquals("/?hoe=fuga", target_.resolve("?hoe=fuga",
                new MockRequest().setContextPath("")));
    }

    public void testResolve4() throws Exception {

        assertEquals("/;jsessionid=XXXX", target_.resolve(";jsessionid=XXXX",
                new MockRequest().setContextPath("")));
    }

    public void testResolve5() throws Exception {

        assertEquals("/context/", target_.resolve("", new MockRequest()
                .setContextPath("/context")));
    }

    public void testResolve6() throws Exception {

        assertEquals("/context/hoe.do", target_.resolve("/hoe.do",
                new MockRequest().setContextPath("/context")));
    }

    public void testResolve7() throws Exception {

        assertEquals("/context/?hoe=fuga", target_.resolve("?hoe=fuga",
                new MockRequest().setContextPath("/context")));
    }

    public void testResolve8() throws Exception {

        assertEquals("/context/;jsessionid=XXXX", target_.resolve(
                ";jsessionid=XXXX", new MockRequest()
                        .setContextPath("/context")));
    }

    public void testResolve9() throws Exception {

        // 以前は単に""となるようになっていたが、これだとリクエストURL相対のルート、
        // すなわちリクエストされたファイルの親ディレクトリURL（例：リクエストURLが.../dir/file.html
        // なら.../dir）と解釈されてしまうため、きちんとドメイン相対パスに変換するようにしている。
        MockRequest request = new MockRequest().setContextPath("/context");
        request.enterDispatch(new MockDispatch().setPath("/index.html"));
        assertEquals("/context/index.html", target_.resolve(".", request));
    }

    public void testResolve10() throws Exception {

        MockRequest request = new MockRequest().setContextPath("/context");
        request.enterDispatch(new MockDispatch().setPath("/index.html"));
        assertEquals("/context/index.html?hoe=fuga", target_.resolve(
                ".?hoe=fuga", request));
    }

    public void testResolve11() throws Exception {

        MockRequest request = new MockRequest().setContextPath("/context");
        request.enterDispatch(new MockDispatch().setPath("/index.html"));
        assertEquals("/context/index.html;jsessionid=XXXX", target_.resolve(
                ".;jsessionid=XXXX", request));
    }

    public void testResolve12() throws Exception {

        assertEquals(".dummy", target_.resolve(".dummy", new MockRequest()
                .setContextPath("/context")));
    }
}
