package org.seasar.ymir.extension.creator.util.type;

import org.seasar.ymir.Acceptor;

public interface Token extends Acceptor<TokenVisitor<?>> {
    char BEGIN_TYPESPEC = '<';

    char END_TYPESPEC = '>';

    char COMMA = ',';

    String SUFFIX_ARRAY = "[]";

    String getBaseName();

    String getComponentName();

    boolean isArray();

    String getAsString();

    void setBaseName(String baseName);
}
