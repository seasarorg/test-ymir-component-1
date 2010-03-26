package org.seasar.ymir.zpt;

import java.util.ArrayList;
import java.util.List;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;

/**
 * ZPTテンプレート中のタグが持つURL指定用の属性（action, href, src）を書き換えるためのInterceptorです。
 * <p>テンプレート中のURLを加工したい場合は{@link #filterURL(String)}を実装したサブクラスを作成して
 * <code>ymir++.dicon</code>などに追加して下さい。
 * </p>
 * 
 * @since 1.0.7
 */
public abstract class URLInterceptorBase implements TagRenderingInterceptor {
    private static final String ATTR_ACTION = "action";

    private static final String ATTR_HREF = "href";

    private static final String ATTR_SRC = "src";

    private static final String[] SPECIALATTRIBUTEPATTERNSTRINGS = new String[] {
        ATTR_ACTION, ATTR_HREF, ATTR_SRC };

    public String[] getSpecialAttributePatternStrings() {
        return SPECIALATTRIBUTEPATTERNSTRINGS;
    }

    public String[] getSpecialTagPatternStrings() {
        return null;
    }

    public String render(TemplateContext context, String name,
            Attribute[] attributes, String body,
            TagRenderingInterceptorChain chain) {
        List<Attribute> list = new ArrayList<Attribute>();
        for (Attribute attribute : attributes) {
            String attrName = attribute.getName();
            if (ATTR_ACTION.equals(attrName) || ATTR_HREF.equals(attrName)
                    || ATTR_SRC.equals(attrName)) {
                String attrValue = TagEvaluatorUtils.defilter(attribute
                        .getValue());
                if (attrName != null) {
                    String filtered = filterURL(attrValue);
                    if (!filtered.equals(attrValue)) {
                        attribute = new Attribute(attrName, TagEvaluatorUtils
                                .filter(filtered), attribute.getQuote());
                    }
                }
            }
            list.add(attribute);
        }
        return chain
                .render(context, name, list.toArray(new Attribute[0]), body);
    }

    /**
     * URLを加工するメソッドです。
     * 
     * @param url URL。nullが渡されることはありません。
     * @return 加工後のURL。
     */
    abstract protected String filterURL(String url);
}
