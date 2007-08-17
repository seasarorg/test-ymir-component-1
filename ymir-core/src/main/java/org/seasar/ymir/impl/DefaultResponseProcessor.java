package org.seasar.ymir.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.RedirectionPathResolver;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseHeader;
import org.seasar.ymir.ResponseProcessor;
import org.seasar.ymir.Updater;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.util.YmirUtils;

public class DefaultResponseProcessor implements ResponseProcessor {

    private static final int BUF_SIZE = 4096;

    private Ymir ymir_;

    private Updater[] updaters_ = new Updater[0];

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private RedirectionPathResolver redirectionPathResolver_ = new DefaultRedirectionPathResolver();

    @Binding(bindingType = BindingType.MUST)
    public void setYmir(Ymir ymir) {
        ymir_ = ymir;
    }

    public void setUpdaters(Updater[] updaters) {
        updaters_ = updaters;
    }

    public void setYmirProcessInterceptors(
            YmirProcessInterceptor[] ymirProcessInterceptors) {
        ymirProcessInterceptors_ = ymirProcessInterceptors;
        YmirUtils.sortYmirProcessInterceptors(ymirProcessInterceptors_);
    }

    @Binding(bindingType = BindingType.MAY)
    public void setRedirectionPathResolver(
            RedirectionPathResolver responsePathNormalizer) {
        redirectionPathResolver_ = responsePathNormalizer;
    }

    public HttpServletResponseFilter process(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) throws IOException,
            ServletException {
        if (response.getStatus() != Response.STATUS_UNDEFINED) {
            httpResponse.setStatus(response.getStatus());
        }
        if (response.getContentType() != null) {
            httpResponse.setContentType(response.getContentType());
        }

        switch (response.getType()) {
        case PASSTHROUGH:
            populateHeaders(response, httpResponse);
            return constructResponseFilter(httpRequest, httpResponse, request);

        case FORWARD:
            populateHeaders(response, httpResponse);
            context.getRequestDispatcher(response.getPath()).forward(
                    httpRequest, httpResponse);
            return null;

        case REDIRECT:
            String resolved = redirectionPathResolver_.resolve(response
                    .getPath(), request);
            if (resolved == null) {
                throw new NullPointerException(
                        "Redirection path is null: may logic is wrong");
            }
            if (resolved.startsWith("/")) {
                // 内部パスの場合はエンコードする。
                resolved = encodeRedirectURL(httpResponse, resolved);
            }
            httpResponse.sendRedirect(resolved);
            return null;

        case SELF_CONTAINED:
            populateHeaders(response, httpResponse);

            InputStream is = null;
            OutputStream os = null;
            try {
                is = response.getInputStream();
                if (is != null) {
                    os = httpResponse.getOutputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    BufferedOutputStream bos = new BufferedOutputStream(os);

                    byte[] buf = new byte[BUF_SIZE];
                    int len;
                    while ((len = bis.read(buf)) >= 0) {
                        bos.write(buf, 0, len);
                    }
                    bos.flush();
                }
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        ;
                    }
                }
                // httpResponseのoutputStreamのクローズはservletContainerに
                // 任せて良い（と思う）。
            }
            return null;

        case VOID:
            return null;

        default:
            throw new RuntimeException("Unknown response type:"
                    + response.getType());
        }
    }

    protected String encodeRedirectURL(HttpServletResponse httpResponse,
            String url) {
        if (url == null) {
            return null;
        }

        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            url = ymirProcessInterceptors_[i].encodingRedirectURL(url);
        }
        return httpResponse.encodeRedirectURL(url);
    }

    protected void populateHeaders(Response response,
            HttpServletResponse httpResponse) {
        ResponseHeader[] headers = response.getResponseHeaders();
        for (int i = 0; i < headers.length; i++) {
            String name = headers[i].getName();
            Object value = headers[i].getValue();
            boolean add = headers[i].isAdd();
            if (value instanceof Integer) {
                if (add) {
                    httpResponse.addIntHeader(name, ((Integer) value)
                            .intValue());
                } else {
                    httpResponse.setIntHeader(name, ((Integer) value)
                            .intValue());
                }
            } else if (value instanceof Long) {
                if (add) {
                    httpResponse
                            .addDateHeader(name, ((Long) value).longValue());
                } else {
                    httpResponse
                            .setDateHeader(name, ((Long) value).longValue());
                }
            } else {
                if (add) {
                    httpResponse.addHeader(name, String.valueOf(value));
                } else {
                    httpResponse.setHeader(name, String.valueOf(value));
                }
            }
        }
    }

    HttpServletResponseFilter constructResponseFilter(
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request) {
        if (Request.DISPATCHER_REQUEST.equals(request.getCurrentDispatch()
                .getDispatcher())
                && ymir_.isUnderDevelopment()) {
            return new UpdaterResponseFilter(httpRequest, httpResponse,
                    updaters_);
        } else {
            return new AsIsResponseFilter(httpResponse);
        }
    }
}
