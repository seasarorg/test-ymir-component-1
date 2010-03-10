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
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.Globals;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.HttpServletResponseFilterFactory;
import org.seasar.ymir.PathResolver;
import org.seasar.ymir.RedirectionPathResolver;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseHeader;
import org.seasar.ymir.ResponseProcessor;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.Updater;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.util.ResponseUtils;
import org.seasar.ymir.util.YmirUtils;

public class ResponseProcessorImpl implements ResponseProcessor {
    private static final int BUF_SIZE = 4096;

    private Ymir ymir_;

    private Updater[] updaters_ = new Updater[0];

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private PathResolver pathResolver_ = new PathResolverImpl();

    private RedirectionPathResolver redirectionPathResolver_ = new RedirectionPathResolverImpl();

    private HttpServletResponseFilterFactory httpServletResponseFilterFactory_ = new HttpServletResponseFilterFactoryImpl();

    @Binding(bindingType = BindingType.MUST)
    public void setYmir(Ymir ymir) {
        ymir_ = ymir;
    }

    public void setUpdaters(Updater[] updaters) {
        updaters_ = updaters;
    }

    @Binding(value = "@org.seasar.ymir.util.ContainerUtils@findAllComponents(container, @org.seasar.ymir.interceptor.YmirProcessInterceptor@class)", bindingType = BindingType.MUST)
    public void setYmirProcessInterceptors(
            YmirProcessInterceptor[] ymirProcessInterceptors) {
        ymirProcessInterceptors_ = ymirProcessInterceptors;
        YmirUtils.sortYmirProcessInterceptors(ymirProcessInterceptors_);
    }

    @Binding(bindingType = BindingType.MAY)
    public void setPathResolver(PathResolver pathResolver) {
        pathResolver_ = pathResolver;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setRedirectionPathResolver(
            RedirectionPathResolver redirectionPathResolver) {
        redirectionPathResolver_ = redirectionPathResolver;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setHttpServletResponseFilterFactory(
            HttpServletResponseFilterFactory httpServletResponseFilterFactory) {
        httpServletResponseFilterFactory_ = httpServletResponseFilterFactory;
    }

    public HttpServletResponseFilter process(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) throws IOException,
            ServletException {
        // responseProcessingStartedの前に、Responseオブジェクトを最終的な状態にしておく。
        adjustResponse(request, response);

        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            ymirProcessInterceptors_[i].responseProcessingStarted(context,
                    httpRequest, httpResponse, request, response);
        }

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

            String resolved;
            if (response.isSubordinate()) {
                // 通常のforward。
                resolved = response.getPath();
            } else {
                // proceed。
                resolved = resolveRedirectionPath(response.getPath(),
                        httpRequest, httpResponse, request, response, true);
                // proceedのパスはクエリストリングの補正に使われるので、
                // ここで最終的なパスを戻しておく。
                response.setPath(resolved);
            }
            context.getRequestDispatcher(resolved).forward(httpRequest,
                    httpResponse);
            return null;

        case REDIRECT:
            populateHeaders(response, httpResponse);

            httpResponse.sendRedirect(resolveRedirectionPath(
                    response.getPath(), httpRequest, httpResponse, request,
                    response, false));
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

    protected void adjustResponse(Request request, Response response) {
        if (ResponseUtils.isTransitionResponse(response)) {
            response
                    .setPath(pathResolver_.resolve(response.getPath(), request));
        }
    }

    protected String resolveRedirectionPath(String path,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response, boolean proceed) {
        String resolved;
        if (proceed) {
            resolved = redirectionPathResolver_.resolveForProceed(path,
                    httpRequest, httpResponse, request, response);
        } else {
            resolved = redirectionPathResolver_.resolve(path, httpRequest,
                    httpResponse, request, response);
        }
        if (resolved == null) {
            throw new NullPointerException(
                    "Redirection path is null: may logic is wrong");
        }
        if (resolved.indexOf(":") < 0) {
            // 内部パスの場合は必要に応じてエンコードする。
            if (!shouldOmitSessionId()) {
                resolved = httpResponse.encodeRedirectURL(resolved);
            }
        }
        return resolved;
    }

    private boolean shouldOmitSessionId() {
        return PropertyUtils.valueOf(ymir_.getApplication().getProperty(
                Globals.APPKEY_CORE_SESSION_OMITSESSIONID),
                Globals.DEFAULT_CORE_SESSION_OMITSESSIONID);
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
        Dispatcher dispatcher = request.getCurrentDispatch().getDispatcher();
        if ((dispatcher == Dispatcher.REQUEST || dispatcher == Dispatcher.FORWARD)
                && ymir_.isUnderDevelopment() && YmirUtils.isUpdatable(request)) {
            return httpServletResponseFilterFactory_.newUpdaterResponseFilter(
                    httpRequest, httpResponse, updaters_);
        } else {
            return httpServletResponseFilterFactory_
                    .newAsIsResponseFilter(httpResponse);
        }
    }
}
