package org.seasar.ymir.extension.creator.util.type;

import java.util.ArrayList;
import java.util.List;

import org.seasar.ymir.extension.creator.util.GenericsUtils;
import org.seasar.ymir.util.ClassUtils;

public class TypeToken implements Token {
    private BaseToken name_;

    private TypeToken[] types_;

    public TypeToken(String token) {
        tokenize(token);
    }

    void tokenize(String string) {
        String suffix;
        if (string.endsWith(SUFFIX_ARRAY)) {
            suffix = SUFFIX_ARRAY;
            string = string.substring(0,
                    string.length() - SUFFIX_ARRAY.length()).trim();
        } else {
            suffix = "";
        }

        int idx = string.indexOf(BEGIN_TYPESPEC);
        if (idx < 0) {
            name_ = new BaseToken(string + suffix);
            types_ = new TypeToken[0];
        } else {
            name_ = new BaseToken(string.substring(0, idx) + suffix);
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

                if (end < string.length() - SUFFIX_ARRAY.length()
                        && SUFFIX_ARRAY.equals(string.substring(end, end
                                + SUFFIX_ARRAY.length()))) {
                    end += SUFFIX_ARRAY.length();
                }

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

    @Override
    public String toString() {
        return getAsString();
    }

    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(GenericsUtils.getComponentName(name_.getBaseName()));
        if (types_.length > 0) {
            String delim = String.valueOf(BEGIN_TYPESPEC);
            for (TypeToken type : types_) {
                sb.append(delim).append(type.getAsString());
                delim = ", ";
            }
            sb.append(END_TYPESPEC);
        }
        if (ClassUtils.isArray(name_.getBaseName())) {
            sb.append(SUFFIX_ARRAY);
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public <R> R accept(TokenVisitor<?> visitor, Object... parameters) {
        R result = (R) visitor.visit(name_);
        if (result != null) {
            return result;
        }
        for (TypeToken type : types_) {
            result = (R) visitor.visit(type);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public BaseToken getName() {
        return name_;
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

    public String getComponentName() {
        return name_.getComponentName();
    }

    public boolean isArray() {
        return name_.isArray();
    }
}
