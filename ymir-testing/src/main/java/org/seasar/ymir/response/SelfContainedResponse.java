package org.seasar.ymir.response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.seasar.kvasir.util.io.InputStreamFactory;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.impl.AsIsInputStreamFactory;

/**
 * レスポンスヘッダやレスポンスボディの情報を内包するResponseオブジェクトです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class SelfContainedResponse extends ResponseBase {
    public static final String DEFAULT_BINARY_CONTENTTYPE = "application/octet-stream";

    public static final String DEFAULT_ASCII_CONTENTTYPE = "text/html; charset=UTF-8";

    private InputStreamFactory inputStreamFactory_;

    private String string_;

    private String contentType_;

    public SelfContainedResponse() {
    }

    @Deprecated
    public SelfContainedResponse(InputStream inputStream) {
        this(inputStream, DEFAULT_BINARY_CONTENTTYPE);
    }

    @Deprecated
    public SelfContainedResponse(InputStream inputStream, String contentType) {
        setInputStream(inputStream);
        setContentType(contentType);
    }

    public SelfContainedResponse(InputStreamFactory inputStreamFactory) {
        this(inputStreamFactory, DEFAULT_BINARY_CONTENTTYPE);
    }

    public SelfContainedResponse(InputStreamFactory inputStreamFactory,
            String contentType) {
        setInputStreamFactory(inputStreamFactory);
        setContentType(contentType);
    }

    public SelfContainedResponse(String string) {
        this(string, DEFAULT_ASCII_CONTENTTYPE);
    }

    public SelfContainedResponse(String string, String contentType) {
        setString(string);
        setContentType(contentType);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Content-Type: ").append(contentType_).append(", Content: ");
        if (string_ != null) {
            sb.append(string_);
        } else {
            sb.append("(inputStreamFactory)");
        }
        return sb.toString();
    }

    public ResponseType getType() {
        return ResponseType.SELF_CONTAINED;
    }

    public boolean isSubordinate() {
        return false;
    }

    public InputStream getInputStream() {
        InputStream inputStream;
        if (inputStreamFactory_ != null) {
            inputStream = inputStreamFactory_.getInputStream();
            inputStreamFactory_ = null;
        } else if (string_ != null) {
            String charset = getCharacterEncoding();
            if (charset == null) {
                charset = "ISO-8859-1";
            }
            try {
                inputStream = new ByteArrayInputStream(string_
                        .getBytes(charset));
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException("Unknown charset: " + charset, ex);
            }
        } else {
            inputStream = null;
        }
        return inputStream;
    }

    @Deprecated
    public void setInputStream(InputStream inputStream) {
        InputStreamFactory isf;
        if (inputStream != null) {
            isf = new AsIsInputStreamFactory(inputStream);
        } else {
            isf = null;
        }
        setInputStreamFactory0(isf);
        setString0(null);
    }

    public InputStreamFactory getInputStreamFactory() {
        return inputStreamFactory_;
    }

    public void setInputStreamFactory(InputStreamFactory inputStreamFactory) {
        setInputStreamFactory0(inputStreamFactory);
        setString0(null);
    }

    void setInputStreamFactory0(InputStreamFactory inputStreamFactory) {
        if (inputStreamFactory_ != null) {
            InputStream is = inputStreamFactory_.getInputStream();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
        }
        inputStreamFactory_ = inputStreamFactory;
    }

    public String getString() {
        return string_;
    }

    public void setString(String string) {
        setInputStreamFactory0(null);
        setString0(string);
    }

    void setString0(String string) {
        string_ = string;
    }

    public String getContentType() {
        return contentType_;
    }

    public void setContentType(String contentType) {
        contentType_ = contentType;
    }
}
