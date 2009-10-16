package org.seasar.ymir.zpt.mobylet.interceptor;

import org.mobylet.core.Carrier;
import org.mobylet.view.designer.EmojiDesigner;
import org.mobylet.view.designer.SingletonDesigner;
import org.seasar.ymir.zpt.TagRenderingInterceptor;
import org.seasar.ymir.zpt.TagRenderingInterceptorChain;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TemplateContext;

/**
 * Mobyletの絵文字機能を使うためのカスタムタグを定義するためのインターセプタです。
 * <p>このインターセプタを適用すると、ZPTテンプレートの中で&lt;m:emoji&gt;タグを利用することができるようになります。
 * </p>
 * <p>&lt;m:emoji&gt;タグに指定可能な属性は以下の通りです。</p>
 * <dl>
 *   <dt>name</dt>
 *   <dd>絵文字の名前。</dd>
 *   <dt>carrier</dt>
 *   <dd>キャリア名。デフォルト値は「DOCOMO」です。</dd>
 * </dl>
 * 
 * @author sue
 * @author yokota
 */
public class EmojiInterceptor implements TagRenderingInterceptor {
    private static final String TAGNAME = "m:emoji";

    private static final String ATTRNAME_NAME = "name";

    private static final String ATTRNAME_CARRIER = "carrier";

    private static final String[] SPECIAL_TAG_PATTERN_STRINGS = new String[] { "^"
            + TAGNAME + "$" };

    public String[] getSpecialAttributePatternStrings() {
        return null;
    }

    public String[] getSpecialTagPatternStrings() {
        return SPECIAL_TAG_PATTERN_STRINGS;
    }

    public String render(TemplateContext context, String name,
            Attribute[] attributes, String body,
            TagRenderingInterceptorChain chain) {
        if (name.equals(TAGNAME)) {
            String emojiName = null;
            Carrier carrier = Carrier.DOCOMO;
            for (Attribute attr : attributes) {
                String attrName = attr.getName();
                if (ATTRNAME_NAME.equals(attrName)) {
                    emojiName = attr.getValue();
                } else if (ATTRNAME_CARRIER.equals(attrName)) {
                    carrier = Carrier.valueOf(attr.getValue());
                }
            }
            if (emojiName == null || emojiName.length() == 0) {
                return "";
            }

            return SingletonDesigner.getDesigner(EmojiDesigner.class).get(
                    emojiName, carrier);
        }

        return chain.render(context, name, attributes, body);
    }
}
