package org.seasar.ymir.extension.creator.util.type;

public class ConstantToken implements Token {
    private String token_;

    public ConstantToken(String token) {
        token_ = token;
    }

    public String getAsString() {
        return token_;
    }

    public Object accept(TokenVisitor visitor) {
        return visitor.visit(this);
    }

    public String getBaseName() {
        return token_;
    }

    public void setBaseName(String baseName) {
        token_ = baseName;
    }
}
