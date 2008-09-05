package org.seasar.ymir.extension.creator.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.seasar.ymir.extension.creator.Template;

public class FileTemplate implements Template {
    private String path_;

    private File file_;

    private String encoding_;

    public FileTemplate(String path, File file, String encoding) {
        path_ = path;
        file_ = file;
        encoding_ = encoding;
    }

    public boolean exists() {
        return (file_.exists() && !file_.isDirectory());
    }

    public InputStream getInputStream() throws IOException {
        if (file_.exists() && file_.isDirectory()) {
            throw new FileNotFoundException(
                    "Can't open inputStream: is directory: " + file_);
        }
        return new FileInputStream(file_);
    }

    public String getPath() {
        return path_;
    }

    public long lastModified() {
        return file_.lastModified();
    }

    public String getName() {
        return file_.getName();
    }

    public OutputStream getOutputStream() throws IOException {
        if (file_.exists() && file_.isDirectory()) {
            throw new FileNotFoundException(
                    "Can't open outputStream: is directory: " + file_);
        }
        file_.getParentFile().mkdirs();
        return new FileOutputStream(file_);
    }

    public String getEncoding() {
        return encoding_;
    }

    public boolean isDirectory() {
        return file_.isDirectory();
    }
}
