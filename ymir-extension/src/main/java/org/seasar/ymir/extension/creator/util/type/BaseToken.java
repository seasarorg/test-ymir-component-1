package org.seasar.ymir.extension.creator.util.type;

public class BaseToken implements Token {
    private String token_;

    public BaseToken(String token) {
        token_ = token;
    }

    @Override
    public String toString() {
        return token_;
    }

    public String getAsString() {
        return token_;
    }

    @SuppressWarnings("unchecked")
    public <R> R accept(TokenVisitor<?> visitor, Object... parameters) {
        return (R) visitor.visit(this);
    }

    public String getBaseName() {
        return token_;
    }

    public void setBaseName(String baseName) {
        token_ = baseName;
    }

    public String getComponentName() {
        if (token_.endsWith(SUFFIX_ARRAY)) {
            return token_.substring(0, token_.length() - SUFFIX_ARRAY.length());
        } else {
            return token_;
        }
    }

    public boolean isArray() {
        return token_.endsWith(SUFFIX_ARRAY);
    }
}
