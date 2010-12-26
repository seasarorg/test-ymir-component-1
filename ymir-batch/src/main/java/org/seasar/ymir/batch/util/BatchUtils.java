package org.seasar.ymir.batch.util;

import java.io.File;
import java.net.URL;

import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.ymir.checkbox.Globals;

public class BatchUtils {
    private BatchUtils() {
    }

    public static File getBuildDir(Class<?> clazz) {
        return getBuildDir(ResourceUtil.getResourcePath(clazz));
    }

    // S2.4.20のResourceUtil.getBuildDir(String)はjarのパスを与えた
    // 場合にjar自身を差すFileオブジェクトを返してしまう不具合があるため
    // 自前で実装している。
    public static File getBuildDir(String path) {
        File dir = null;
        URL url = ResourceUtil.getResource(path);
        if ("file".equals(url.getProtocol())) {
            int num = path.split("/").length;
            dir = new File(ResourceUtil.getFileName(url));
            for (int i = 0; i < num; ++i, dir = dir.getParentFile()) {
            }
        } else {
            dir = new File(JarFileUtil.toJarFilePath(url)).getParentFile();
        }
        return dir;
    }

    public static File getBatchHome() {
        // BatchUtils.classを持つymir-batchにプロジェクト参照されている場合はバッチプロジェクトのホームが
        // 正しく見つからないためこうしている。
        try {
            return getBuildDir(Class.forName(Globals.LANDMARK_CLASSNAME)).getParentFile();
        } catch (ClassNotFoundException ex) {
            return getBuildDir(BatchUtils.class).getParentFile();
        }
    }
}
