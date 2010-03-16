package org.seasar.ymir.scaffold.maintenance.zpt.interceptor;

import java.util.Date;

import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.scaffold.maintenance.enm.Action;
import org.seasar.ymir.scaffold.maintenance.web.EntityBean;
import org.seasar.ymir.scaffold.maintenance.web.MaintenancePage;
import org.seasar.ymir.scaffold.util.ScaffoldUtils;
import org.seasar.ymir.zpt.MutableTagElement;
import org.seasar.ymir.zpt.TagRenderingInterceptor;
import org.seasar.ymir.zpt.TagRenderingInterceptorChain;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.ConstantElement;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;

public class MaintenanceInterceptor implements TagRenderingInterceptor {
    private static final String PREFIX_YS = "ys:";

    private static final String PREFIX_MAINTENANCE = "maintenance-";

    private static final String YS_EXPAND = PREFIX_YS + PREFIX_MAINTENANCE
            + "expand";

    private static final String EXPAND_ALL = "all";

    private static final String[] SPECIALATTRIBUTEPATTERNSTRINGS = new String[] { "^"
            + PREFIX_YS + PREFIX_MAINTENANCE };

    private static final String[] SPECIALTAGPATTERNSTRINGS = new String[0];

    @Binding(bindingType = BindingType.MUST)
    protected ApplicationManager applicationManager;

    @Binding(bindingType = BindingType.MUST)
    protected YmirNamingConvention ymirNamingConvention;

    public String[] getSpecialAttributePatternStrings() {
        return SPECIALATTRIBUTEPATTERNSTRINGS;
    }

    public String[] getSpecialTagPatternStrings() {
        return SPECIALTAGPATTERNSTRINGS;
    }

    public String render(TemplateContext context, String name,
            Attribute[] attributes, String body,
            TagRenderingInterceptorChain chain) {
        MutableTagElement element = MutableTagElement.newInstance(name,
                attributes);
        element.setColumnNumber(context.getElement().getColumnNumber());
        element.setLineNumber(context.getElement().getLineNumber());

        // ys:maintanence-属性がレンダリングされないように除去しておく。
        try {
            element.removeAttributes(SPECIALATTRIBUTEPATTERNSTRINGS, YS_EXPAND);
        } catch (IllegalSyntaxException ex) {
            throw new EvaluationRuntimeException(ex);
        }

        if (EXPAND_ALL.equals(element
                .getDefilteredOriginalAttributeValue(YS_EXPAND))) {
            String columnName = element
                    .getDefilteredOriginalAttributeValue("name");
            if (columnName == null) {
                throw new EvaluationRuntimeException(
                        "Attribute 'name' must be specified", element);
            }
            MaintenancePage self = (MaintenancePage) context
                    .getVariableResolver().getVariable(context, "self");
            buildInputElement(element, self.getEntityBean(), self.getAction(),
                    columnName);
        }

        return chain.render(context, element.getName(),
                element.getAttributes(), TagEvaluatorUtils.evaluateElements(
                        context, element.getBodyElements()));
    }

    protected void buildInputElement(MutableTagElement element,
            EntityBean entityBean, Action action, String columnName) {
        ColumnInfo columnInfo = entityBean.getColumnInfo(columnName);
        if (columnInfo == null) {
            return;
        }

        Class<?> type = columnInfo.getPropertyType();
        boolean passwordColumn = entityBean.isPasswordColumn(columnName);
        if (!passwordColumn && type == String.class
                && "CLOB".equals(columnInfo.getColumnDbType())) {
            element.setName("textarea");
            element.removeAttribute("type");
            element.addAttribute("cols", 64);
            element.addAttribute("rows", 10);
            Attribute valueAttr = element.getOriginalAttribute("value");
            String value = valueAttr != null ? ScaffoldUtils
                    .adjustContentForTextarea(valueAttr.getValue()) : "";
            element
                    .setBodyElements(new Element[] { new ConstantElement(value) });
        } else {
            element.setName("input");
            element.addAttribute("type", passwordColumn ? "password" : "text");

            if (passwordColumn && action == Action.EDIT) {
                element.removeAttribute("value");
            }

            int size;
            if (type == String.class) {
                int columnSize = columnInfo.getColumnSize();
                if (columnSize < 500) {
                    // columnSizeが非常に大きい場合にはそのまま埋め込んでもあまり現実的でないため、
                    // 制限を設けている。
                    size = (int) Math.min(columnSize * 1.5, 64);
                    element.addAttribute("maxlength", columnSize);
                } else {
                    size = 64;
                }
            } else if (Date.class.isAssignableFrom(type)) {
                size = 20;
            } else {
                size = 20;
            }
            element.addAttribute("size", size);
        }
    }
}
