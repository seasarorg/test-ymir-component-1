package org.seasar.ymir.batch.util;

import java.io.File;
import java.net.URL;

import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.ymir.checkbox.Globals;

public class BatchUtils {
    private static final String NAME_TARGET = "target";

    private static final String PATH_SRC_MAIN_BATCH = "src/main/batch";

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
        File dir;
        try {
            // BatchUtils.classを持つymir-batchにプロジェクト参照されている場合はバッチプロジェクトのホームが
            // 正しく見つからないためこうしている。
            dir = getBuildDir(Class.forName(Globals.LANDMARK_CLASSNAME)).getParentFile();
        } catch (ClassNotFoundException ex) {
            dir = getBuildDir(BatchUtils.class).getParentFile();
        }

        if (NAME_TARGET.equals(dir.getName())) {
            // Maven2でテストを実行している可能性がある。
            // その場合は正しくホームディレクトリを特定できないため補正をする。
            File d = dir.getParentFile();
            if (d != null) {
                d = new File(d, PATH_SRC_MAIN_BATCH);
                if (d.exists() && d.isDirectory()) {
                    dir = d;
                }
            }
        }
        return dir;
    }
}
