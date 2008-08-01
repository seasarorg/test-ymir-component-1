package org.seasar.ymir.extension.creator.util.type;

import java.util.ArrayList;
import java.util.List;

public class TypeToken implements Token {
    private String token_;

    private ConstantToken name_;

    private TypeToken[] types_;

    public TypeToken(String token) {
        token_ = token;

        tokenize(token);
    }

    void tokenize(String string) {
        int idx = string.indexOf(BEGIN_TYPESPEC);
        if (idx < 0) {
            name_ = new ConstantToken(string);
            types_ = new TypeToken[0];
        } else {
            name_ = new ConstantToken(string.substring(0, idx));
            types_ = tokenizeTypes(
                    string.substring(idx + 1, string.length() - 1)).toArray(
                    new TypeToken[0]);
        }
    }

    List<TypeToken> tokenizeTypes(String string) {
        List<TypeToken> list = new ArrayList<TypeToken>();

        int beginTypeSpec = string.indexOf(BEGIN_TYPESPEC);
        int comma = string.indexOf(COMMA);
        if (comma >= 0) {
            if (beginTypeSpec >= 0 && beginTypeSpec < comma) {
                int end = skipSpace(string,
                        skipTypeSpec(string, beginTypeSpec) + 1);
                if (end < string.length() && string.charAt(end) == COMMA) {
                    comma = end;
                } else {
                    comma = -1;
                }
            }
        }

        if (comma >= 0) {
            list.add(new TypeToken(string.substring(0, comma).trim()));
            list.addAll(tokenizeTypes(string.substring(comma + 1).trim()));
        } else {
            list.add(new TypeToken(string.trim()));
        }

        return list;
    }

    int skipSpace(String string, int begin) {
        for (int i = begin; i < string.length(); i++) {
            if (string.charAt(i) != ' ') {
                return i;
            }
        }
        return string.length();
    }

    int skipTypeSpec(String string, int begin) {
        for (int i = begin + 1; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (ch == BEGIN_TYPESPEC) {
                i = skipTypeSpec(string, i);
            } else if (ch == END_TYPESPEC) {
                return i;
            }
        }
        throw new IllegalArgumentException("Illegal format: " + string);
    }

    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name_.getAsString());
        if (types_.length > 0) {
            String delim = String.valueOf(BEGIN_TYPESPEC);
            for (TypeToken type : types_) {
                sb.append(delim).append(type.getAsString());
                delim = ", ";
            }
            sb.append(END_TYPESPEC);
        }

        return sb.toString();
    }

    public Object accept(TokenVisitor visitor) {
        Object result = visitor.visit(name_);
        if (result != null) {
            return result;
        }
        for (TypeToken type : types_) {
            result = visitor.visit(type);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public String getBaseName() {
        return name_.getBaseName();
    }

    public void setBaseName(String baseName) {
        name_.setBaseName(baseName);
    }

    public TypeToken[] getTypes() {
        return types_;
    }
}
