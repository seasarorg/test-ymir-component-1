package org.seasar.ymir.extension.freemarker;

import java.io.Serializable;
import java.lang.reflect.Array;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.render.Selector;
import org.seasar.ymir.scope.annotation.RequestParameter;

public class Form10DtoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @Meta(name = "bornOf", value = "/list4/index.html")
    protected Selector fruitSelector = new Selector();

    public Form10DtoBase() {
    }

    public Form10DtoBase(Selector fruitSelector) {
        this.fruitSelector = fruitSelector;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append('(');
        append(sb.append("fruitSelector="), fruitSelector);
        sb.append(')');
        return toString(sb);
    }

    protected StringBuilder append(StringBuilder sb, Object obj) {
        if (obj != null && obj.getClass().isArray()) {
            sb.append('{');
            int len = Array.getLength(obj);
            String delim = "";
            for (int i = 0; i < len; i++) {
                sb.append(delim);
                delim = ", ";
                append(sb, Array.get(obj, i));
            }
            sb.append('}');
        } else {
            sb.append(obj);
        }
        return sb;
    }

    protected String toString(StringBuilder sb) {
        return sb.toString();
    }

    @Metas( { @Meta(name = "formProperty", value = "form"),
        @Meta(name = "bornOf", value = "/list4/index.html") })
    @RequestParameter
    public Selector getFruitSelector() {
        return this.fruitSelector;
    }
}
