package org.seasar.ymir.extension.creator;

import java.util.Set;

public interface AnnotationDesc extends Cloneable {
    Object clone();

    String getName();

    String getBody();

    String getShortBody();

    void setBody(String body);

    String getAsString();

    String getAsShortString();

    /**
     * このAnnotationDescが依存しているクラスのFQCNを指定されたSetに追加します。
     * 
     * @param set クラス名を追加するためのSet。
     * nullを指定してはいけません。
     */
    void addDependingClassNamesTo(Set<String> set);

    void setTouchedClassNameSet(Set<String> set);
}
