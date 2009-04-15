package org.seasar.ymir.zpt.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.zpt.tales.TalesUtils;

public class Parameters implements Iterable<Parameters.Parameter> {
    private static final char DELIMITER = '!';

    private List<Parameter> parameterList_;

    public Parameters(String statement) throws IllegalSyntaxException {
        parameterList_ = Collections.unmodifiableList(tokenize(statement));
    }

    List<Parameter> tokenize(String statement) throws IllegalSyntaxException {
        List<Parameter> list = new ArrayList<Parameter>();

        int len = statement.length();
        if (len == 0) {
            return list;
        }

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len;) {
            char ch = statement.charAt(i);
            if (ch == '$') {
                if (i + 1 == len) {
                    // 末尾の$。
                    throw (IllegalSyntaxException) new IllegalSyntaxException(
                            "Unexpected '$' operator").setColumnNumber(i + 1);
                }

                sb.append(ch);

                ch = statement.charAt(++i);
                if (ch == '{') {
                    int start = i;
                    i = balancedIndexOf(statement, '}', start + 1);
                    if (i < 0) {
                        throw (IllegalSyntaxException) new IllegalSyntaxException(
                                "'}' not found").setColumnNumber(len);
                    }
                    sb.append(statement.substring(start, ++i));
                } else if (ch == '_' || ch >= 'a' && ch <= 'z' || ch >= 'A'
                        && ch <= 'Z') {
                    int start = i;
                    for (i = start + 1; i < len; i++) {
                        char ch2 = statement.charAt(i);
                        if (ch2 >= '0' && ch2 <= '9' || ch2 == '_'
                                || ch2 >= 'a' && ch2 <= 'z' || ch2 >= 'A'
                                && ch2 <= 'Z') {
                            ;
                        } else {
                            break;
                        }
                    }
                    sb.append(statement.substring(start, i));
                } else {
                    throw (IllegalSyntaxException) new IllegalSyntaxException(
                            "Unexpected character '" + ch + "' after '$'")
                            .setColumnNumber(i + 1);
                }

            } else if (ch == DELIMITER) {
                if (i + 1 < len && statement.charAt(i + 1) == DELIMITER) {
                    // ,,。
                    sb.append(DELIMITER);
                    i += 2;
                } else {
                    list.add(new Parameter(sb.toString()));
                    sb.delete(0, sb.length());
                    i++;
                }
            } else {
                sb.append(ch);
                i++;
            }
        }
        list.add(new Parameter(sb.toString()));

        return list;
    }

    private static int balancedIndexOf(String str, char ch, int idx) {
        int n = str.length();
        int depth = 0;
        for (int i = idx; i < n; i++) {
            char c = str.charAt(i);
            if (depth == 0 && c == ch) {
                return i;
            }
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
            }
        }
        return -1;
    }

    public static class Parameter {
        private String expression_;

        public Parameter(String expression) {
            expression_ = expression;
        }

        public Object evaluate(TemplateContext context) {
            return TalesUtils.resolve(context, expression_, context
                    .getVariableResolver(), context.getExpressionEvaluator());
        }

        public String getExpression() {
            return expression_;
        }
    }

    public Iterator<Parameter> iterator() {
        return parameterList_.iterator();
    }

    public Parameter[] getParameters() {
        return parameterList_.toArray(new Parameter[0]);
    }

    public List<Object> getEvaluatedParameterList(TemplateContext context) {
        List<Object> list = new ArrayList<Object>(parameterList_.size());
        for (Parameter parameter : parameterList_) {
            list.add(parameter.evaluate(context));
        }
        return list;
    }

    public List<Parameter> getParameterList() {
        return parameterList_;
    }

    public Object[] getEvaluatedParameters(TemplateContext context) {
        return getEvaluatedParameterList(context).toArray();
    }
}
