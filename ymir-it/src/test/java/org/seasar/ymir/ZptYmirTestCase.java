package org.seasar.ymir;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.mock.servlet.MockServletConfig;
import org.seasar.framework.mock.servlet.MockServletConfigImpl;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.mock.servlet.MockHttpServletResponse;
import org.seasar.ymir.testing.YmirTestCase;
import org.seasar.ymir.zpt.YmirTagEvaluator;
import org.seasar.ymir.zpt.YmirTagRenderer;
import org.seasar.ymir.zpt.YmirTalesExpressionEvaluator;
import org.seasar.ymir.zpt.YmirTemplateParser;

import net.skirnir.freyja.webapp.FreyjaServlet;

abstract public class ZptYmirTestCase extends YmirTestCase {
    private FreyjaServlet servlet_;

    private MockServletConfig config_;

    @Override
    public void setUp() {
        super.setUp();

        servlet_ = new FreyjaServlet();
        config_ = new MockServletConfigImpl();
        config_.setServletContext(getServletContext());
        config_.setInitParameter("templateParser", YmirTemplateParser.class
                .getName());
        config_.setInitParameter("tagEvaluator", YmirTagEvaluator.class
                .getName());
        config_.setInitParameter("expressionEvaluator",
                YmirTalesExpressionEvaluator.class.getName());
        config_
                .setInitParameter("tagRenderer", YmirTagRenderer.class
                        .getName());
        config_.setInitParameter("templateRoot", "");
        config_.setInitParameter("pageEncoding", getCharacterEncoding());
        config_.setInitParameter("requestEncoding", getCharacterEncoding());
        config_.setInitParameter("responseEncoding", getCharacterEncoding());
        config_.setInitParameter("contentType", "text/html; charset="
                + getCharacterEncoding());

        try {
            servlet_.init(config_);
        } catch (ServletException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected ResponseFilter renderResponse() throws ServletException,
            IOException {
        MockHttpServletRequest request = getHttpServletRequest();
        MockHttpServletResponse response = getHttpServletResponse();
        int status = response.getStatus();
        if (status != 0 && status != HttpServletResponse.SC_OK) {
            throw new IllegalStateException(
                    "Should not render response because response status is bad ("
                            + status + ")");
        }
        ResponseFilter filter = new ResponseFilter(request, response);
        servlet_.service(request, filter);
        return filter;
    }

    protected String getTextResource(String name) {
        return IOUtils.readString(getClass().getClassLoader()
                .getResourceAsStream(
                        getClass().getName().replace('.', '/').concat("_")
                                .concat(name)), "UTF-8", true);
    }
}
