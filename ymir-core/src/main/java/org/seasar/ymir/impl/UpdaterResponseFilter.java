package org.seasar.ymir.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.Updater;
import org.seasar.kvasir.util.MimeUtils;
import org.seasar.kvasir.util.io.IOUtils;

public class UpdaterResponseFilter extends HttpServletResponseWrapper implements
        HttpServletResponseFilter {
    private Updater[] updaters_;

    private String type_;

    private ByteArrayOutputStream outputStream_;

    private ServletOutputStream servletOutputStream_;

    private StringWriter writer_;

    private PrintWriter printWriter_;

    private boolean propagateContentType_ = true;

    private String charset_;

    public UpdaterResponseFilter(HttpServletRequest request,
            HttpServletResponse response, Updater[] updaters) {
        super(response);
        updaters_ = updaters;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (shouldBuffering()) {
            if (servletOutputStream_ == null) {
                outputStream_ = new ByteArrayOutputStream();
                servletOutputStream_ = new ServletOutputStream() {
                    public void write(int b) throws IOException {
                        outputStream_.write(b);
                    }
                };
            }
            return servletOutputStream_;
        } else {
            return getResponse().getOutputStream();
        }
    }

    boolean shouldBuffering() {
        return (type_ != null && type_.startsWith("text/") && type_
                .indexOf("html") >= 0);
    }

    public PrintWriter getWriter() throws IOException {
        if (shouldBuffering()) {
            if (printWriter_ == null) {
                writer_ = new StringWriter();
                printWriter_ = new PrintWriter(writer_);
            }
            return printWriter_;
        } else {
            return getResponse().getWriter();
        }
    }

    public void setContentType(String type) {
        type_ = type;
        if (propagateContentType_) {
            getResponse().setContentType(type);
        }
    }

    @Override
    public void setCharacterEncoding(String charset) {
        charset_ = charset;
        if (propagateContentType_) {
            super.setCharacterEncoding(charset);
        }
    }

    @Override
    public String getCharacterEncoding() {
        String charset = charset_;
        if (charset == null) {
            charset = MimeUtils.getCharset(type_);
            if (charset == null) {
                charset = super.getCharacterEncoding();
            }
        }
        return charset;
    }

    public void commit() throws IOException {
        if (shouldBuffering()) {
            String response;
            if (outputStream_ != null) {
                try {
                    outputStream_.flush();
                } catch (IOException ex) {
                    throw new RuntimeException("Can't happen!", ex);
                }
                byte[] bytes = outputStream_.toByteArray();
                try {
                    response = new String(bytes, getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    commit(bytes);
                    return;
                }
            } else if (printWriter_ != null) {
                printWriter_.flush();
                response = writer_.toString();
            } else {
                return;
            }
            commit(filterResponse(response));
        }
    }

    String filterResponse(String response) {
        for (int i = 0; i < updaters_.length; i++) {
            response = updaters_[i].filterResponse(response);
        }
        return response;
    }

    void commit(String response) throws IOException {
        IOUtils.writeString(getResponse().getOutputStream(), response,
                getCharacterEncoding(), false, false);
    }

    void commit(byte[] response) throws IOException {
        IOUtils.writeBytes(getResponse().getOutputStream(), response, false);
    }

    public void setPropagateContentType(boolean propagateContentType) {
        this.propagateContentType_ = propagateContentType;
    }
}
