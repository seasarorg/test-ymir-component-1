package org.seasar.ymir.response.scheme.impl;

import java.io.InputStream;

import org.seasar.ymir.Response;
import org.seasar.ymir.impl.AsIsInputStreamFactory;
import org.seasar.ymir.mime.MimeTypeDetector;
import org.seasar.ymir.response.SelfContainedResponse;
import org.seasar.ymir.response.scheme.Strategy;

public class ResourceStrategy implements Strategy {
    public static final String SCHEME = "resource";

    private MimeTypeDetector detector_;

    public void setMimeTypeDetector(MimeTypeDetector detector) {
        detector_ = detector;
    }

    public String getScheme() {
        return SCHEME;
    }

    public Response constructResponse(String path, Object component) {
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(path);
        if (in == null) {
            throw new IllegalArgumentException("Resource does not exist: path="
                    + path + ", classLoader="
                    + Thread.currentThread().getContextClassLoader());
        }

        String contentType = SelfContainedResponse.DEFAULT_BINARY_CONTENTTYPE;
        if (detector_ != null) {
            String mimeType = detector_.getMimeType(path);
            if (mimeType != null) {
                if (mimeType.startsWith("text/")) {
                    contentType = mimeType + "; charset=UTF-8";
                } else {
                    contentType = mimeType;
                }
            }
        }
        return new SelfContainedResponse(new AsIsInputStreamFactory(in),
                contentType);
    }
}
