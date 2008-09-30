package org.seasar.ymir.response;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseHeader;
import org.seasar.ymir.ResponseType;

abstract public class ResponseBase implements Response {
    private static final String PREFIX_CHARSET = "charset=";

    private int status_ = STATUS_UNDEFINED;

    private List<ResponseHeader> headerList_ = new ArrayList<ResponseHeader>();

    public void setType(ResponseType type) {
        throw new UnsupportedOperationException();
    }

    public void setSubordinate(boolean subordinate) {
        throw new UnsupportedOperationException();
    }

    public String getPath() {
        throw new UnsupportedOperationException();
    }

    public void setPath(String path) {
        throw new UnsupportedOperationException();
    }

    public InputStream getInputStream() {
        throw new UnsupportedOperationException();
    }

    public void setInputStream(InputStream is) {
        throw new UnsupportedOperationException();
    }

    public String getContentType() {
        return null;
    }

    public void setContentType(String contentType) {
        throw new UnsupportedOperationException();
    }

    public String getCharacterEncoding() {
        String contentType = getContentType();
        if (contentType != null) {
            int semi = contentType.indexOf(';');
            if (semi >= 0) {
                String sub = contentType.substring(semi + 1).trim();
                if (sub.toLowerCase().startsWith(PREFIX_CHARSET)) {
                    return sub.substring(PREFIX_CHARSET.length()).trim();
                }
            }
        }
        return null;
    }

    public int getStatus() {
        return status_;
    }

    public void setStatus(int status) {
        status_ = status;
    }

    public void addDateHeader(String name, long value) {
        headerList_.add(new ResponseHeader(name, value, true));
    }

    public void addHeader(String name, String value) {
        headerList_.add(new ResponseHeader(name, value, true));
    }

    public void addIntHeader(String name, int value) {
        headerList_.add(new ResponseHeader(name, value, true));
    }

    public void setDateHeader(String name, long value) {
        headerList_.add(new ResponseHeader(name, value));
    }

    public void setHeader(String name, String value) {
        headerList_.add(new ResponseHeader(name, value));
    }

    public void setIntHeader(String name, int value) {
        headerList_.add(new ResponseHeader(name, value));
    }

    public ResponseHeader[] getResponseHeaders() {
        return headerList_.toArray(new ResponseHeader[0]);
    }

    public boolean containsHeader(String name) {
        for (Iterator<ResponseHeader> itr = headerList_.iterator(); itr
                .hasNext();) {
            ResponseHeader header = itr.next();
            if (name.equals(header.getName())) {
                return true;
            }
        }
        return false;
    }
}
