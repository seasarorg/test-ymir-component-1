package org.seasar.ymir.extension.creator.impl;

import java.io.File;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;

public class LazyPathMetaData implements PathMetaData {
    private SourceCreator sourceCreator_;

    private String path_;

    private HttpMethod method_;

    private String forwardPath_;

    private boolean deniedLoaded_;

    private boolean denied_;

    private String componentName_;

    private boolean componentNameLoaded_;

    private String className_;

    private boolean classNameLoaded_;

    private File sourceFile_;

    private boolean sourceFileLoaded_;

    private File baseSourceFile_;

    private boolean baseSourceFileLoaded_;

    private Template template_;

    private boolean templateLoaded_;

    public LazyPathMetaData(SourceCreator sourceCreator, String path,
            HttpMethod method, String forwardPath) {
        sourceCreator_ = sourceCreator;
        path_ = path;
        method_ = method;
        forwardPath_ = strip(forwardPath);
    }

    String strip(String path) {
        if (path == null) {
            return null;
        }
        int index = path.indexOf('?');
        if (index < 0) {
            return stripPathParameter(path);
        } else {
            return stripPathParameter(path.substring(0, index));
        }
    }

    String stripPathParameter(String path) {
        if (path == null) {
            return null;
        }
        int index = path.indexOf(';');
        if (index < 0) {
            return path;
        } else {
            return path.substring(0, index);
        }
    }

    public HttpMethod getMethod() {
        return method_;
    }

    public String getPath() {
        return path_;
    }

    public String getForwardPath() {
        return forwardPath_;
    }

    public boolean isDenied() {
        if (!deniedLoaded_) {
            denied_ = sourceCreator_.isDenied(path_, method_);
            deniedLoaded_ = true;
        }
        return denied_;
    }

    public String getComponentName() {
        // forward先のパスに対応するクラスがあればforward先のパスからコンポーネント名を
        // 組み立てる。そうでなければリクエストパスからコンポーネント名を組み立てる。
        // 基本的にYmirではforward先のテンプレート中の動的要素のレンダリングのための準備を
        // forward元のPageクラスで行なうように考えており、自動生成でもforward先のテンプレート中の
        // 動的要素に対応するプロパティはforward元のPageクラスに生成するようにしているが、
        // forward先テンプレートのレンダリングをforward元から独立させたい場合のことも考えて、
        // forward先パスに対応するPageを手動で作っておいた場合はそちらにforward先のテンプレート中
        // の動的要素に対応するプロパティを生成するようにしている。そのための仕組み。
        if (!componentNameLoaded_) {
            String componentName = sourceCreator_.getComponentName(
                    forwardPath_, method_);
            if (sourceCreator_.getClass(sourceCreator_
                    .getClassName(componentName)) != null) {
                componentName_ = componentName;
            } else {
                componentName_ = sourceCreator_
                        .getComponentName(path_, method_);
            }
            componentNameLoaded_ = true;
        }
        return componentName_;
    }

    public String getClassName() {
        if (!classNameLoaded_) {
            className_ = sourceCreator_.getClassName(getComponentName());
            classNameLoaded_ = true;
        }
        return className_;
    }

    public File getBaseSourceFile() {
        if (!baseSourceFileLoaded_) {
            baseSourceFile_ = sourceCreator_.getSourceFile(getClassName()
                    + "Base");
            baseSourceFileLoaded_ = true;
        }
        return baseSourceFile_;
    }

    public File getSourceFile() {
        if (!sourceFileLoaded_) {
            sourceFile_ = sourceCreator_.getSourceFile(getClassName());
            sourceFileLoaded_ = true;
        }
        return sourceFile_;
    }

    public Template getTemplate() {
        if (!templateLoaded_) {
            if (forwardPath_ != null) {
                template_ = sourceCreator_.getTemplate(forwardPath_);
            } else {
                template_ = null;
            }
            templateLoaded_ = true;
        }
        return template_;
    }
}
