package org.seasar.ymir.extension.creator.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.seasar.ymir.extension.creator.Template;

public class NullTemplate implements Template {
    public static final NullTemplate INSTANCE = new NullTemplate();

    private NullTemplate() {
    }

    public boolean exists() {
        return false;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(new byte[0]);
    }

    public String getName() {
        return null;
    }

    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    public String getPath() {
        return null;
    }

    public long lastModified() {
        return 0;
    }

    public String getEncoding() {
        return "UTF-8";
    }

    public boolean isDirectory() {
        return false;
    }

    public boolean mkdirs() {
        return false;
    }
}
