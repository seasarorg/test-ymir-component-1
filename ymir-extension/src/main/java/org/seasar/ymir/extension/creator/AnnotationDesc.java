package org.seasar.ymir.extension.creator;

public interface AnnotationDesc extends Cloneable {
    Object clone();

    String getName();

    String getBody();

    void setBody(String body);

    String getString();

    /**
     * このAnnotationDescが依存している全てのクラスのFQCNを返します。
     * 
     * @return このAnnotationDescが依存している全てのクラスのFQCNの配列。
     * nullを返すことはありません。
     */
    String[] getDependingClassNames();
}
