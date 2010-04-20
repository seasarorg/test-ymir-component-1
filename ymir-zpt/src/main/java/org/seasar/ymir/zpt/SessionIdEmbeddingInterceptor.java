package org.seasar.ymir.zpt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.util.ServletUtils;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.webapp.ServletVariableResolver;

/**
 * ZPTテンプレート中のタグが持つURL指定用の属性（action, href, src）のうち、相対URLであるものについて、
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
                if (attrValue != null) {
                    String processed = attrValue;
                    if (shouldOmitSessionId()) {
                        // セッションIDを除去する。
                        processed = omitSessionId(processed);
                    } else {
                        // 必要に応じてHttpServletResponse#encodeURL()を使って
                        // URLをエンコードする。
                        if (shouldAddSessionId(processed)
                                && !ServletUtils.isSessionIdEmbedded(processed)) {
                            processed = addSessionId(context, processed);
                        }
                    }
                    processed = resolveURL(context, processed);
                    if (!processed.equals(attrValue)) {
                        attribute = new Attribute(attrName, TagEvaluatorUtils
                                .filter(processed), attribute.getQuote());
                    }
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
        return ServletUtils.omitSessionId(url);
    }

    protected boolean shouldAddSessionId(String url) {
        // XXX 相対URLだけを対象とすれば十分であると思われるため以下のようにしている。
        // これでまずい場合は対象を広げることを検討する。
        if (url.indexOf(':') >= 0) {
            return false;
        }

        String stripped = ServletUtils.stripParameters(url);
        return stripped.endsWith(".html") || stripped.endsWith(".do")
                || stripped.endsWith(".zpt");

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

    protected String resolveURL(TemplateContext context, String url) {
        VariableResolver resolver = context.getVariableResolver();
        HttpServletRequest httpRequest = (HttpServletRequest) resolver
                .getVariable(context, ServletVariableResolver.VAR_REQUEST);
        HttpServletResponse httpResponse = (HttpServletResponse) resolver
                .getVariable(context, ServletVariableResolver.VAR_RESPONSE);
        Request request = (Request) resolver.getVariable(context,
                YmirVariableResolver.NAME_YMIRREQUEST);
        return resolveURL(url, httpRequest, httpResponse, request);
    }

    /**
     * 指定されたURLを最終的なURLに加工します。
     * <p>必要に応じてこのメソッドをサブクラスでオーバライドして下さい。
     * </p>
     * <p>このメソッドの中で必要に応じて相対URLを絶対URLに加工したり
     * 強制的にセッションIDを埋め込んだりすることができます。
     * </p>
     * 
     * @param url URL。nullが渡されることはありません。
     * @param httpResponse 現在のHttpServletRequestオブジェクト。 
     * @param httpRequest 現在のHttpServletResponseオブジェクト。
     * @param request 現在のRequestオブジェクト。
     * @return 加工後のURL。
     * @since 1.0.7
     */
    protected String resolveURL(String url, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request) {
        return url;
    }
}
