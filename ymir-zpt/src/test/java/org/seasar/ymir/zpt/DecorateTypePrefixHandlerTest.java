package org.seasar.ymir.zpt;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.Globals;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.message.impl.MessagesImpl;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.mock.MockYmir;
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

    private MockServletContext servletContext_ = new MockServletContextImpl(
            "/context");

    private MockHttpServletRequest httpRequest_ = new MockHttpServletRequestImpl(
            servletContext_, HttpMethod.GET, "/index.html");

    private YmirVariableResolver varResolver_;

    private ZptTemplateContext context_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        S2Container container = new S2ContainerImpl();
        container.register(new TokenManagerImpl());
        container.register(MessagesImpl.class, Globals.NAME_MESSAGES);

        MockYmir ymir = new MockYmir();
        MockApplication application = new MockApplication();
        application.setS2Container(container);
        ymir.setApplication(application);
        YmirContext.setYmir(ymir);
        target_.setTalesExpressionEvaluator(new YmirTalesExpressionEvaluator());

        varResolver_ = new YmirVariableResolver(new MockRequest(),
                httpRequest_, container);
        varResolver_.setVariable("true", Boolean.TRUE);
        varResolver_.setVariable("false", Boolean.FALSE);
        context_ = new ZptTemplateContext();
        context_.setElement(new TagElement("input",
                new Attribute[] { new Attribute("class", "number", "\"") },
                new Element[0]));
        context_.setTargetName("class");
        context_.setProcessingAttributeName("tal:attributes");
        Notes notes = new Notes();
        notes.add("hoehoe", new Note("")).add("with", new Note(""));
        httpRequest_.setAttribute(RequestProcessor.ATTR_NOTES, notes);
    }

    public void test_値が追加できること() throws Exception {
        assertEquals("number error", target_.handle(context_, varResolver_,
                " notes/notes  with  error "));
    }

    public void test_既存の値がない場合でも値が設定できること() throws Exception {
        context_.setElement(new TagElement("input", new Attribute[0],
                new Element[0]));
        assertEquals("error", target_.handle(context_, varResolver_,
                " notes/contains(hoehoe)  with  error "));
    }

    public void test_条件にマッチしない場合は何もしないこと() throws Exception {
        assertEquals("number", target_.handle(context_, varResolver_,
                "notes/contains(fugafuga) with error"));
    }

    public void test_値が置換できること() throws Exception {
        assertEquals("error", target_.handle(context_, varResolver_,
                "notes/contains(hoehoe) with !error"));
    }

    public void test_値が既に設定されている場合は何もしないこと() throws Exception {
        assertEquals("number error", target_.handle(context_, varResolver_,
                "notes/contains(hoehoe) with error"));
    }

    public void test_条件としてwithという単語が使えること() throws Exception {
        assertEquals("number", target_.handle(context_, varResolver_,
                "with with error"));
    }

    public void test_追加する値としてwithという単語が使えること() throws Exception {
        assertEquals("number with", target_.handle(context_, varResolver_,
                "notes/contains(hoehoe) with with"));
    }

    public void test_空白を含む式を条件として記述できること() throws Exception {
        assertEquals("number with", target_.handle(context_, varResolver_,
                "false | true with with"));
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること()
            throws Exception {
        try {
            target_.handle(context_, varResolver_, "");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること2()
            throws Exception {
        try {
            target_.handle(context_, varResolver_, "hoehoe");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること3()
            throws Exception {
        try {
            target_.handle(context_, varResolver_, "hoehoe fugafuga");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること4()
            throws Exception {
        try {
            target_.handle(context_, varResolver_, "hoehoe fugafuga hehehe");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_文法エラーの場合はIllegalArgumentExceptionがスローされること5()
            throws Exception {
        try {
            target_.handle(context_, varResolver_,
                    "hoehoe with hehehe fugafuga");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_talAttributesの中でない場合はIllegalArgumentExceptionがスローされること5()
            throws Exception {
        context_.setProcessingAttributeName(null);
        try {
            target_.handle(context_, varResolver_, "hoehoe with error");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void test_class属性以外の属性に対して使用した場合はIllegalArgumentExceptionがスローされること5()
            throws Exception {
        context_.setTargetName("id");
        try {
            target_.handle(context_, varResolver_, "hoehoe with error");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }
}
