package org.seasar.ymir.zpt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Globals;
import org.seasar.ymir.YmirContext;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.webapp.ServletVariableResolver;

/**
 * ZPTテンプレート中のタグが持つURL指定用の属性（action, href, src）のうち、現在のページ基準の相対URLであるものについて、
 * URLに必要に応じてセッションIDを付与するためのInterceptorです。
 * 
 * @since 1.0.2
 */
public class SessionIdEmbeddingInterceptor implements TagRenderingInterceptor {
    private static final String ATTR_ACTION = "action";

    private static final String ATTR_HREF = "href";

    private static final String ATTR_SRC = "src";

    private static final String[] SPECIALATTRIBUTEPATTERNSTRINGS = new String[] {
        ATTR_ACTION, ATTR_HREF, ATTR_SRC };

    private static final Pattern PATTERN_OMIT_SESSIONID = Pattern
            .compile(";jsessionid=[^#?]+");

    private static final String REPLACEMENT_OMIT_SESSIONID = "";

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
                String processed = attrValue;
                if (shouldOmitSessionId()) {
                    // セッションIDを除去する。
                    processed = omitSessionId(attrValue);
                } else {
                    // 必要に応じてHttpServletResponse#encodeURL()を使って
                    // URLをエンコードする。
                    if (shouldAddSessionId(attrValue)) {
                        processed = addSessionId(context, attrValue);
                    }
                }
                if (!processed.equals(attrValue)) {
                    attribute = new Attribute(attrName, TagEvaluatorUtils
                            .filter(processed), attribute.getQuote());
                }
            }
            list.add(attribute);
        }
        return chain
                .render(context, name, list.toArray(new Attribute[0]), body);
    }

    private boolean shouldOmitSessionId() {
        return PropertyUtils.valueOf(YmirContext.getYmir().getApplication()
                .getProperty(Globals.APPKEY_CORE_SESSION_OMITSESSIONID),
                Globals.DEFAULT_CORE_SESSION_OMITSESSIONID);
    }

    protected String omitSessionId(String url) {
        return PATTERN_OMIT_SESSIONID.matcher(url).replaceFirst(
                REPLACEMENT_OMIT_SESSIONID);
    }

    protected boolean shouldAddSessionId(String url) {
        // XXX 相対URLだけを対象とすれば十分であると思われるため以下のようにしている。
        // これでまずい場合は対象を広げることを検討する。
        return (url.equals(".") || url.startsWith("./") || url.equals("..") || url
                .startsWith("../"))
                && url.indexOf(";jsessionid=") < 0;
    }

    private String addSessionId(TemplateContext context, String url) {
        HttpServletResponse response = (HttpServletResponse) context
                .getVariableResolver().getVariable(context,
                        ServletVariableResolver.VAR_RESPONSE);
        if (response != null) {
            return response.encodeURL(url);
        } else {
            return url;
        }
    }
}
