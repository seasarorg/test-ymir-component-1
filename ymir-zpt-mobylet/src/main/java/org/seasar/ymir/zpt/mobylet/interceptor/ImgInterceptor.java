package org.seasar.ymir.zpt.mobylet.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.mobylet.core.util.ImageUtils;
import org.mobylet.core.util.StringUtils;
import org.mobylet.view.designer.ImageDesigner;
import org.mobylet.view.designer.SingletonDesigner;
import org.seasar.ymir.zpt.TagRenderingInterceptor;
import org.seasar.ymir.zpt.TagRenderingInterceptorChain;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;

/**
 * Mobyletの画像リサイズ機能を使うためのカスタムタグを定義するためのインターセプタです。
 * <p>このインターセプタを適用すると、ZPTテンプレートの中で&lt;m:img&gt;タグを利用することができるようになります。
 * </p>
 * <p>&lt;m:img&gt;タグに指定可能な属性は以下の通りです。</p>
 * <dl>
 *   <dt>magniWidth</dt>
 *   <dd><strong>[必須]</strong> 横幅の割合（～1）。</dd>
 *   <dt>scaleType</dt>
 *   <dd>リサイズ方式。デフォルト値は「FITWIDTH」です。</dd>
 * </dl>
 * 
 * @author sue
 * @author yokota
 */
public class ImgInterceptor implements TagRenderingInterceptor {
    private static final String TAGNAME = "m:img";

    private static final String TAGNAME_IMG = "img";

    private static final String ATTRNAME_MAGNIWIDTH = "magniWidth";

    private static final String ATTRNAME_SCALETYPE = "scaleType";

    private static final String ATTRNAME_SRC = "src";

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
            String magniWidth = null;
            String scaleType = null;
            Attribute srcAttr = null;
            List<Attribute> attrs = new ArrayList<Attribute>();
            for (Attribute attr : attributes) {
                String attrName = attr.getName();
                if (ATTRNAME_MAGNIWIDTH.equals(attrName)) {
                    magniWidth = TagEvaluatorUtils.defilter(attr.getValue());
                } else if (ATTRNAME_SCALETYPE.equals(attrName)) {
                    scaleType = TagEvaluatorUtils.defilter(attr.getValue());
                } else if (ATTRNAME_SRC.equals(attrName)) {
                    srcAttr = attr;
                } else {
                    attrs.add(attr);
                }
            }
            if (srcAttr == null) {
                throw new IllegalArgumentException("tag '" + TAGNAME
                        + "' must have attribute '" + ATTRNAME_SRC + "'");
            } else if (magniWidth == null) {
                throw new IllegalArgumentException("tag '" + TAGNAME
                        + "' must have attribute '" + ATTRNAME_MAGNIWIDTH + "'");
            }

            ImageDesigner designer = SingletonDesigner
                    .getDesigner(ImageDesigner.class);
            String imgSrc = designer.getSrc(TagEvaluatorUtils.defilter(srcAttr
                    .getValue()), StringUtils.isEmpty(magniWidth) ? null
                    : Double.parseDouble(magniWidth), ImageUtils
                    .getScaleType(scaleType));
            attrs.add(new Attribute(ATTRNAME_SRC, TagEvaluatorUtils
                    .filter(imgSrc), srcAttr.getQuote()));

            name = TAGNAME_IMG;
            attributes = attrs.toArray(new Attribute[0]);
        }

        return chain.render(context, name, attributes, body);
    }
}
