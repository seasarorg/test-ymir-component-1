package org.seasar.ymir.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.util.ServletUtils;

/**
 * @since 1.0.7
 */
public class JavaResourceServlet extends HttpServlet {
    private static final String INITPARAM_WEBROOTPACKAGEPATH = "webrootPackagePath";

    private static final String INITPRARM_ENCODING = "encoding";

    private static final String DEFAULT_WEBROOTPACKAGEPATH = "org/seasar/ymir/webroot";

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MIMETYPE = "application/octet-stream";

    private String webrootPackagePath_;

    private String encoding_;

    @Override
    public void init() throws ServletException {
        super.init();

        String webrootPackagePath = getInitParameter(INITPARAM_WEBROOTPACKAGEPATH);
        if (webrootPackagePath == null) {
            webrootPackagePath = DEFAULT_WEBROOTPACKAGEPATH;
        }
        if (webrootPackagePath.endsWith("/")) {
            webrootPackagePath = webrootPackagePath.substring(0,
                    webrootPackagePath.length() - 1/*= "/".length() */);
        }
        webrootPackagePath_ = webrootPackagePath;

        encoding_ = getInitParameter(INITPRARM_ENCODING);
        if (encoding_ == null) {
            encoding_ = "UTF-8";
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = ServletUtils.getPath(req);
        InputStream is = getServletContext().getResourceAsStream(path);
        if (is == null) {
            is = getClassLoader().getResourceAsStream(
                    webrootPackagePath_ + path);
            if (is == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        try {
            String contentType = getServletContext().getMimeType(path);
            if (contentType == null) {
                contentType = DEFAULT_MIMETYPE;
            }
            if (contentType.startsWith("text/")) {
                contentType += ";charset=" + encoding_;
            }
            resp.setContentType(contentType);

            IOUtils.pipe(is, resp.getOutputStream(), false, false);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    protected ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }
}
