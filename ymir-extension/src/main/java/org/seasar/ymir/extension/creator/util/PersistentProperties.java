package org.seasar.ymir.extension.creator.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.kvasir.util.io.IOUtils;

public class PersistentProperties {
    private static final Log log_ = LogFactory
            .getLog(PersistentProperties.class);

    private File file_;

    private MapProperties prop_ = new MapProperties(
            new TreeMap<String, String>());

    public PersistentProperties(File file) {
        file_ = file;

        load();
    }

    void load() {
        if (file_.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file_);
                prop_.load(new BufferedInputStream(fis));
            } catch (IOException ex) {
                log_.error("Can't read properties: " + file_);
            } finally {
                IOUtils.closeQuietly(fis);
            }
        }
    }

    public MapProperties getProperties() {
        return prop_;
    }

    public void setProperty(String name, String value) {
        prop_.setProperty(name, value);
    }

    public void save() {
        if (!file_.exists()) {
            file_.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file_);
            prop_.store(new BufferedOutputStream(fos), "ISO-8859-1");
            fos = null;
        } catch (IOException ex) {
            log_.error("Can't write properties: " + file_);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }
}
