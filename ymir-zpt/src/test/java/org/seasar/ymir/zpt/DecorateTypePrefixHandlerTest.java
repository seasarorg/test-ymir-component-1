package org.seasar.ymir.zpt;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.Globals;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.message.impl.MessagesImpl;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.mock.servlet.MockServletContext;
import org.seasar.ymir.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.token.impl.TokenManagerImpl;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.TagElement;
import net.skirnir.freyja.zpt.ZptTemplateContext;

public class DecorateTypePrefixHandlerTest extends TestCase {
    private DecorateTypePrefixHandler target_ = new DecorateTypePrefixHandler();

    private MockServletContext servletContext = new MockServletContextImpl(
            "/context");

    private MockHttpServletRequest httpRequest = new MockHttpServletRequestImpl(
            servletContext, HttpMethod.GET, "/index.html");

    private YmirVariableResolver varResolver;

    private ZptTemplateContext context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        S2Container container = new S2ContainerImpl();
        container.register(new TokenManagerImpl());
        container.register(MessagesImpl.class, Globals.NAME_MESSAGES);
        varResolver = new YmirVariableResolver(new MockRequest(), httpRequest,
                container);
        context = new ZptTemplateContext();
        context.setElement(new TagElement("input",
                new Attribute[] { new Attribute("class", "number", "\"") },
                new Element[0]));
        context.setTargetName("class");
        Notes notes = new Notes();
        notes.add("hoehoe", new Note("")).add("with", new Note(""));
        httpRequest.setAttribute(RequestProcessor.ATTR_NOTES, notes);
    }

    public void test_値が追加できること() throws Exception {
        assertEquals("number error", target_.handle(context, varResolver,
                " hoehoe  with  error "));
    }

    public void test_既存の値がない場合でも値が設定できること() throws Exception {
        context.setElement(new TagElement("input", new Attribute[0],
                new Element[0]));
        assertEquals("error", target_.handle(context, varResolver,
                " hoehoe  with  error "));
    }

    public void test_条件にマッチしない場合は何もしないこと() throws Exception {
        assertEquals("number", target_.handle(context, varResolver,
                "fugafuga with error"));
    }

    public void test_値が置換できること() throws Exception {
        assertEquals("error", target_.handle(context, varResolver,
                "hoehoe with !error"));
    }

    public void test_値が既に設定されている場合は何もしないこと() throws Exception {
        assertEquals("number error", target_.handle(context, varResolver,
                "hoehoe with error"));
    }

    public void test_条件を省略した場合はNotesがあれば装飾されること() throws Exception {
        assertEquals("number error", target_.handle(context, varResolver,
                "with error"));
    }

    public void test_条件としてwithという単語が使えること() throws Exception {
        assertEquals("number error", target_.handle(context, varResolver,
                "with with error"));
    }

    public void test_追加する値としてwithという単語が使えること() throws Exception {
        assertEquals("number with", target_.handle(context, varResolver,
                "hoehoe with with"));
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること()
            throws Exception {
        try {
            target_.handle(context, varResolver, "");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること2()
            throws Exception {
        try {
            target_.handle(context, varResolver, "hoehoe");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること3()
            throws Exception {
        try {
            target_.handle(context, varResolver, "hoehoe fugafuga");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること4()
            throws Exception {
        try {
            target_.handle(context, varResolver, "hoehoe fugafuga hehehe");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること5()
            throws Exception {
        try {
            target_.handle(context, varResolver, "hoehoe with hehehe fugafuga");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }
}
