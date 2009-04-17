package org.seasar.ymir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.seasar.kvasir.util.MimeUtils;
import org.seasar.kvasir.util.io.IOUtils;

public class ResponseFilter extends HttpServletResponseWrapper {
    private String type_;

    private ByteArrayOutputStream outputStream_;

    private ServletOutputStream servletOutputStream_;

    private StringWriter writer_;

    private PrintWriter printWriter_;

    private int sc_;

    public ResponseFilter(HttpServletRequest request,
            HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (servletOutputStream_ == null) {
            outputStream_ = new ByteArrayOutputStream();
            servletOutputStream_ = new ServletOutputStream() {
                public void write(int b) throws IOException {
                    outputStream_.write(b);
                }
            };
        }
        return servletOutputStream_;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (printWriter_ == null) {
            writer_ = new StringWriter();
            printWriter_ = new PrintWriter(writer_);
        }
        return printWriter_;
    }

    @Override
    public void setContentType(String type) {
        type_ = type;
        getResponse().setContentType(type);
    }

    public String getResponseBodyAsString() {
        if (type_ == null || !type_.startsWith("text/")) {
            throw new IllegalArgumentException("Response body is not text");
        }

        if (outputStream_ != null) {
            try {
                outputStream_.flush();
            } catch (IOException ex) {
                throw new RuntimeException("Can't happen!", ex);
            }
            byte[] bytes = outputStream_.toByteArray();
            try {
                return new String(bytes, getCharacterEncoding());
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        } else if (printWriter_ != null) {
            printWriter_.flush();
            return writer_.toString();
        } else {
            return null;
        }
    }

    String getResponseCharset() {
        String charset = MimeUtils.getCharset(type_);
        if (charset == null) {
            charset = "ISO-8859-1";
        }
        return charset;
    }

    void commit(byte[] response) throws IOException {
        IOUtils.writeBytes(getResponse().getOutputStream(), response, false);
    }

    public int getStatus() {
        return sc_;
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        sc_ = sc;
    }

    @Override
    public void sendError(int sc) throws IOException {
        super.sendError(sc);
        sc_ = sc;
    }
}
