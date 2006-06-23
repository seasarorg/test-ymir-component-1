package org.seasar.cms.framework.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.cms.framework.Response;
import org.seasar.cms.framework.ResponseProcessor;
import org.seasar.cms.framework.util.ServletUtils;

public class DefaultResponseProcessor implements ResponseProcessor {

    private static final int BUF_SIZE = 4096;

    public boolean process(ServletContext context,
        HttpServletRequest httpRequest, HttpServletResponse httpResponse,
        Response response) throws IOException, ServletException {

        switch (response.getType()) {
        case Response.TYPE_PASSTHROUGH:
            return true;

        case Response.TYPE_FORWARD:
            context.getRequestDispatcher(response.getPath()).forward(
                httpRequest, httpResponse);
            return false;

        case Response.TYPE_REDIRECT:
            String path = response.getPath();
            if (path.startsWith("/")) {
                path = ServletUtils.getContextPath(httpRequest) + path;
                if ("".equals(path)) {
                    path = "/";
                }
            }
            httpResponse.sendRedirect(path);
            return false;

        case Response.TYPE_SELF_CONTAINED:
            String contentType = response.getContentType();
            if (contentType != null) {
                httpResponse.setContentType(contentType);
            }
            InputStream is = null;
            OutputStream os = null;
            try {
                is = response.getInputStream();
                os = httpResponse.getOutputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                BufferedOutputStream bos = new BufferedOutputStream(os);

                byte[] buf = new byte[BUF_SIZE];
                int len;
                while ((len = bis.read(buf)) >= 0) {
                    bos.write(buf, 0, len);
                }
                bos.flush();
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
            return false;

        case Response.TYPE_VOID:
            return false;

        case Response.TYPE_STATUS:
            response.setStatus(response.getStatus());
            return false;

        default:
            throw new RuntimeException("Unknown response type:"
                + response.getType());
        }
    }
}
