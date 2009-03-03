package org.seasar.ymir.zpt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.impl.YmirImpl;

import net.skirnir.freyja.webapp.FreyjaServlet;

public class ZptYmir extends YmirImpl {
    @Override
    protected Object backupForInclusion(AttributeContainer attributeContainer) {
        return new Backupped(super.backupForInclusion(attributeContainer),
                attributeContainer
                        .getAttribute(FreyjaServlet.ATTR_RESPONSECONTENTTYPE),
                attributeContainer
                        .getAttribute(FreyjaServlet.ATTR_VARIABLERESOLVER));
    }

    @Override
    protected void processResponse(ServletContext servletContext,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response, FilterChain chain)
            throws IOException, ServletException {
        if (response.getType() == ResponseType.PASSTHROUGH
                || response.getType() == ResponseType.FORWARD) {
            String contentType = response.getContentType();
            if (contentType != null) {
                httpRequest.setAttribute(
                        FreyjaServlet.ATTR_RESPONSECONTENTTYPE, contentType);
            }

            httpRequest.setAttribute(FreyjaServlet.ATTR_VARIABLERESOLVER,
                    new YmirVariableResolver(request, httpRequest,
                            getApplication().getS2Container()));
        }

        super.processResponse(servletContext, httpRequest, httpResponse,
                request, response, chain);
    }

    @Override
    protected void restoreForInclusion(AttributeContainer attributeContainer,
            Object backupped) {
        Backupped b = (Backupped) backupped;
        attributeContainer.setAttribute(FreyjaServlet.ATTR_RESPONSECONTENTTYPE,
                b.getResponseContentType());
        attributeContainer.setAttribute(FreyjaServlet.ATTR_VARIABLERESOLVER, b
                .getVariableResolver());
        super.restoreForInclusion(attributeContainer, b.getBackupped());
    }

    static class Backupped {
        private Object backupped_;

        private Object responseContentType_;

        private Object variableResolver_;

        Backupped(Object backupped, Object responseContentType,
                Object variableResolver) {
            backupped_ = backupped;
            responseContentType_ = responseContentType;
            variableResolver_ = variableResolver;
        }

        Object getBackupped() {
            return backupped_;
        }

        Object getResponseContentType() {
            return responseContentType_;
        }

        Object getVariableResolver() {
            return variableResolver_;
        }
    }
}
