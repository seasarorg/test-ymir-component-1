package org.seasar.ymir.zpt;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.StringUtils;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.zpt.ZptUtils;

public class TalAttributes {
    private Map<String, Statement> statementMap_ = new LinkedHashMap<String, Statement>();

    public static class Statement {
        private String varname_;

        private String expression_;

        public Statement(String varname, String expression) {
            varname_ = varname;
            expression_ = expression;
        }

        public String getVarname() {
            return varname_;
        }

        public void setVarname(String varname) {
            varname_ = varname;
        }

        public String getExpression() {
            return expression_;
        }

        public void setExpression(String expression) {
            expression_ = expression;
        }
    }

    public static TalAttributes newInstance() {
        return new TalAttributes();
    }

    public static TalAttributes valueOf(Attribute attribute)
            throws IllegalSyntaxException {
        if (attribute == null) {
            return null;
        } else {
            return new TalAttributes(attribute);
        }
    }

    public static TalAttributes valueOf(String defiltered)
            throws IllegalSyntaxException {
        if (defiltered == null) {
            return null;
        } else {
            return new TalAttributes(defiltered);
        }
    }

    protected TalAttributes() {
    }

    protected TalAttributes(Attribute attribute) throws IllegalSyntaxException {
        try {
            initialize(TagEvaluatorUtils.defilter(attribute.getValue()));
        } catch (IllegalSyntaxException ex) {
            throw (IllegalSyntaxException) ex.setLineNumber(
                    attribute.getLineNumber()).setColumnNumber(
                    attribute.getColumnNumber());
        }
    }

    protected TalAttributes(String string) throws IllegalSyntaxException {
        initialize(string);
    }

    private void initialize(String string) throws IllegalSyntaxException {
        for (String statement : ZptUtils.parseStatements(string)) {
            int sp = statement.indexOf(' ');
            if (sp < 0) {
                throw new IllegalSyntaxException("Syntax error: " + statement);
            }
            String varname = statement.substring(0, sp).trim();
            if (statementMap_.put(varname, new Statement(varname, StringUtils
                    .trimLeft(statement.substring(sp + 1)))) != null) {
                throw new IllegalSyntaxException(
                        "Duplicate variable name found: " + varname);
            }
        }
    }

    public Collection<Statement> getStatements() {
        return statementMap_.values();
    }

    public Statement getStatement(String varname) {
        return statementMap_.get(varname);
    }

    public void addStatement(String varname, String expression) {
        statementMap_.put(varname, new Statement(varname, expression));
    }

    public void removeStatement(String varname) {
        statementMap_.remove(varname);
    }

    public String toFilteredString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (Statement statement : getStatements()) {
            sb.append(delim).append(statement.getVarname()).append(" ").append(
                    statement.getExpression().replace(";", ";;"));
            delim = "; ";
        }
        return TagEvaluatorUtils.filter(sb.toString());
    }
}
