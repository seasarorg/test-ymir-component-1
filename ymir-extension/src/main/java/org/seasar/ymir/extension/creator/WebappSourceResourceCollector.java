package org.seasar.ymir.extension.creator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Webアプリケーションのソースツリーのルート以下にあるリソースを収集するためのクラスです。
 * 
 * @author YOKOTA Takehiko
 */
public class WebappSourceResourceCollector<R> {
    private File webappSourceRoot_;

    private Rule<R> rule_;

    public WebappSourceResourceCollector(File webappSourceRoot, Rule<R> rule) {
        webappSourceRoot_ = webappSourceRoot;
        rule_ = rule;
    }

    public List<R> collect() {
        List<R> resourceList = new ArrayList<R>();
        collect("", resourceList);
        return resourceList;
    }

    protected void collect(String path, List<R> resourceList) {
        File dir;
        if (path.length() == 0) {
            dir = webappSourceRoot_;
        } else {
            dir = new File(webappSourceRoot_, path);
        }
        File[] childFiles = dir.listFiles();
        for (int i = 0; i < childFiles.length; i++) {
            String childName = childFiles[i].getName();
            String childPath = path + "/" + childName;
            if (shouldIgnore(childPath, childName)) {
                continue;
            }
            if (childFiles[i].isDirectory()) {
                collect(childPath, resourceList);
            } else {
                rule_.add(childPath, resourceList);
            }
        }
    }

    protected boolean shouldIgnore(String path, String name) {
        if (path.equals("/WEB-INF/classes") || path.equals("/WEB-INF/lib")
                || path.equals("/WEB-INF/web.xml") || path.equals("/META-INF")) {
            return true;
        }

        if ("CVS".equals(name) || ".svn".equals(name) || "_svn".equals(name)) {
            return true;
        }

        return false;
    }

    /**
     * リソース収集のためのルールを表すインタフェースです。
     *
     * @param <R> リソースの型。
     * @author YOKOTA Takehiko
     */
    public static interface Rule<R> {
        /**
         * 指定されたパスに対応するリソースが収集すべきリソースである場合に、
         * 指定されたListにリソースオブジェクトを追加します。
         * 
         * @param path Webアプリケーションのソースツリーのルート相対のパス。
         * @param resourceList リソースオブジェクトを格納するためのListオブジェクト。
         */
        void add(String path, List<R> resourceList);
    }
}
