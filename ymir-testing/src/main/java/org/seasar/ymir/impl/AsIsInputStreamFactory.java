package org.seasar.ymir.impl;

import java.io.InputStream;

import org.seasar.kvasir.util.io.InputStreamFactory;

public class AsIsInputStreamFactory implements InputStreamFactory {
    private InputStream is_;

    public AsIsInputStreamFactory(InputStream is) {
        is_ = is;
    }

    public InputStream getInputStream() {
        return is_;
    }
}
