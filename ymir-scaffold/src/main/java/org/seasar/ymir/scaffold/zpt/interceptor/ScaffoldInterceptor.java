package org.seasar.ymir.scaffold.zpt.interceptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Request;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.scaffold.util.ScaffoldUtils;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.zpt.MutableTagElement;
import org.seasar.ymir.zpt.TemplateParsingInterceptor;
import org.seasar.ymir.zpt.TemplateParsingInterceptorChain;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.ConstantElement;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.TagElement;

public class ScaffoldInterceptor implements TemplateParsingInterceptor {
    private static final Attribute[] ATTRIBUTES_EMPTY = new Attribute[0];

    private static final Element[] ELEMENTS_EMPTY = new Element[0];

    private static final String PREFIX_YS = "ys:";

    private static final String YS_EXPAND = PREFIX_YS + "expand";

    private static final String EXPAND_FORM = "form";

    private static final String YS_INCLUDES = PREFIX_YS + "includes";

    private static final String YS_EXCLUDES = PREFIX_YS + "excludes";

    private static final String[] SPECIALATTRIBUTEPATTERNSTRINGS = new String[] { "^"
            + PREFIX_YS };

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

    public Element[] tagElementCreated(TagElement tagElement,
            TemplateParsingInterceptorChain chain)
            throws IllegalSyntaxException {
        MutableTagElement mutableElement = MutableTagElement
                .toMutable(tagElement);
        // ys:属性がレンダリングされないように除去しておく。
        mutableElement.removeAttributes(SPECIALATTRIBUTEPATTERNSTRINGS[0],
                YS_EXPAND, YS_INCLUDES, YS_EXCLUDES);

        String expand = mutableElement
                .getDefilteredOriginalAttributeValue(YS_EXPAND);
        if (EXPAND_FORM.equals(expand)) {
            return processToExpandForm(mutableElement, chain);
        }

        return chain.tagElementCreated(mutableElement);
    }

    private Element[] processToExpandForm(MutableTagElement element,
            TemplateParsingInterceptorChain chain)
            throws IllegalSyntaxException {
        String tagName = element.getName();
        if ("form".equals(tagName)) {
            // form。

            // action属性がなければ生成する。
            if (element.getAttribute("action") == null) {
                element.addAttribute("action", getRequest().getAbsolutePath());
            }

            // method属性がなければ生成する。
            if (element.getAttribute("method") == null) {
                element.addAttribute("method", "post");
            }

            Entity entity;
            String name = element.getDefilteredAttributeValue("name");
            if (name == null) {
                entity = getEntity();
                element.addAttribute("name", entity.getTablePropertyName());
            } else {
                entity = getEntity(name);
            }

            Map<String, ColumnInfo> columnInfoMap = getColumnInfoMap(entity);

            Element[] bodies = element.getBodyElements();
            if (bodies == null || bodies.length == 0 || bodies.length == 1
                    && bodies[0] instanceof ConstantElement) {
                // formのボディがテキストだけならフォーム項目を生成する。
                element.setBodyElements(new Element[] { new TagElement("table",
                        ATTRIBUTES_EMPTY,
                        buildTableFormBodyElements(extractColumnInfos(element,
                                columnInfoMap))), });
            } else {
                // formのボディがテキストだけでないならフォーム項目の生成はしない。
                element.setBodyElements(expandElements(element
                        .getBodyElements(), entity, columnInfoMap));
            }
        }
        return chain.tagElementCreated(element);
    }

    private Element[] expandElements(Element[] elements, Entity entity,
            Map<String, ColumnInfo> columnInfoMap)
            throws IllegalSyntaxException {
        if (elements == null) {
            return null;
        }
        List<Element> expanded = new ArrayList<Element>();
        for (Element element : elements) {
            if (!(element instanceof TagElement)) {
                // TagElementでないので展開不要。
                expanded.add(element);
                continue;
            }

            if (!(element instanceof MutableTagElement)) {
                // Mutableでないのでbodyの展開だけを行なう。
                TagElement tagElement = (TagElement) element;
                expanded.add(new TagElement(tagElement.getName(), tagElement
                        .getAttributes(), expandElements(tagElement
                        .getBodyElements(), entity, columnInfoMap)));
                continue;
            }

            MutableTagElement tagElement = (MutableTagElement) element;

            if (!EXPAND_FORM.equals(tagElement
                    .getDefilteredOriginalAttributeValue(YS_EXPAND))) {
                // ys:expand="form"属性がついていないものについてはbodyの展開だけを行なう。
                tagElement.setBodyElements(expandElements(tagElement
                        .getBodyElements(), entity, columnInfoMap));
                expanded.add(tagElement);
                continue;
            }

            // ys:expand="form"属性がついているタグを展開する。

            String tagName = tagElement.getName();
            if ("input".equals(tagName) || "textarea".equals(tagName)
                    || "select".equals(tagName)) {
                // input系のタグ。

                // nameはEntityのプロパティ名を表す（必須）。
                String name = tagElement.getDefilteredAttributeValue("name");
                if (name == null) {
                    throw new IllegalSyntaxException(
                            "'name' attribute must be specified");
                }

                ColumnInfo columnInfo = columnInfoMap.get(name);
                if (columnInfo == null) {
                    throw new IllegalSyntaxException(
                            "Unknown column name for entity '"
                                    + entity.getTablePropertyName() + "': "
                                    + name);
                }

                buildInputElement(tagElement, columnInfo);
                expanded.add(tagElement);
                continue;
            }

            // フォーム項目表示用のタグ。

            List<ColumnInfo> columnInfos = extractColumnInfos(tagElement,
                    columnInfoMap);

            if ("table".equals(tagName)) {
                // <table ys:expand="">
                tagElement
                        .setBodyElements(buildTableFormBodyElements(columnInfos));
                expanded.add(tagElement);
            } else {
                if ("ul".equals(tagName) || "ol".equals(tagName)) {
                    // <ul ys:expand="">, <ol ys:expand="">
                    tagElement
                            .setBodyElements(buildListFormBodyElements(columnInfos));
                    expanded.add(tagElement);
                } else if ("dl".equals(tagName)) {
                    // <dl ys:expand="">
                    tagElement
                            .setBodyElements(buildDefinitionFormBodyElements(columnInfos));
                    expanded.add(tagElement);
                } else {
                    // その他。
                    tagElement
                            .setBodyElements(buildSimpleFormBodyElements(columnInfos));
                    expanded.add(tagElement);
                }
                expanded
                        .add(new TagElement("p", ATTRIBUTES_EMPTY,
                                new Element[] {
                                    buildSubmitElement("do_" + getActionName(),
                                            " 登録 "),
                                    new ConstantElement("&nbsp;"),
                                    buildSubmitElement("cancel", "キャンセル"), }));
            }
        }
        return expanded.toArray(ELEMENTS_EMPTY);
    }

    private List<ColumnInfo> extractColumnInfos(MutableTagElement element,
            Map<String, ColumnInfo> columnInfoMap)
            throws IllegalSyntaxException {
        String[] includes = null;
        String includesStr = element
                .getDefilteredOriginalAttributeValue(YS_INCLUDES);
        if (includesStr != null) {
            includes = includesStr.split("\\s+");
        }
        String[] excludes = null;
        String excludesStr = element
                .getDefilteredOriginalAttributeValue(YS_EXCLUDES);
        if (excludesStr != null) {
            excludes = excludesStr.split("\\s+");
        }
        return ScaffoldUtils.extractColumnsList(columnInfoMap, includes,
                excludes, "id", "versionNo");
    }

    private Element[] buildSimpleFormBodyElements(List<ColumnInfo> columnInfos) {
        List<Element> bodies = new ArrayList<Element>();

        for (ColumnInfo columnInfo : columnInfos) {
            bodies.add(new ConstantElement("<p>" + getFilteredLabel(columnInfo)
                    + ": "));
            bodies.add(buildInputElement(columnInfo));
            bodies.add(new ConstantElement("</p>"));
        }

        return bodies.toArray(ELEMENTS_EMPTY);
    }

    private Element[] buildDefinitionFormBodyElements(
            List<ColumnInfo> columnInfos) {
        List<Element> bodies = new ArrayList<Element>();

        for (ColumnInfo columnInfo : columnInfos) {
            bodies.add(new ConstantElement("<dt>"
                    + getFilteredLabel(columnInfo) + "</dt><dd>"));
            bodies.add(buildInputElement(columnInfo));
            bodies.add(new ConstantElement("</dd>"));
        }

        return bodies.toArray(ELEMENTS_EMPTY);
    }

    private Element[] buildListFormBodyElements(List<ColumnInfo> columnInfos) {
        List<Element> bodies = new ArrayList<Element>();

        for (ColumnInfo columnInfo : columnInfos) {
            bodies.add(new ConstantElement("<li>"
                    + getFilteredLabel(columnInfo) + ": "));
            bodies.add(buildInputElement(columnInfo));
            bodies.add(new ConstantElement("</li>"));
        }

        return bodies.toArray(ELEMENTS_EMPTY);
    }

    private Element[] buildTableFormBodyElements(List<ColumnInfo> columnInfos) {
        List<Element> bodies = new ArrayList<Element>();

        bodies.add(new ConstantElement("<tbody>"));
        for (ColumnInfo columnInfo : columnInfos) {
            bodies.add(new ConstantElement("<tr><th>"
                    + getFilteredLabel(columnInfo) + "</th><td>"));
            bodies.add(buildInputElement(columnInfo));
            bodies.add(new ConstantElement("</td></tr>"));
        }
        bodies.add(new ConstantElement("<tr><th>&nbsp;</td><td>"));
        bodies.add(buildSubmitElement("do_" + getActionName(), " 登録 "));
        bodies.add(new ConstantElement("&nbsp;"));
        bodies.add(buildSubmitElement("cancel", "キャンセル"));
        bodies.add(new ConstantElement("</td></tr></tbody>"));

        return bodies.toArray(ELEMENTS_EMPTY);
    }

    private TagElement buildSubmitElement(String actionName, String label) {
        return new TagElement("input", new Attribute[] {
            new Attribute("type", "submit"), new Attribute("name", actionName),
            new Attribute("value", label), }, null);
    }

    protected MutableTagElement buildInputElement(ColumnInfo columnInfo) {
        MutableTagElement element = MutableTagElement.newInstance();
        String name = columnInfo.getPropertyName();
        Class<?> type = columnInfo.getPropertyType();
        if (columnInfo.isPrimary()) {
            element.setName("input");
            element.addAttribute("type", "hidden");
        } else if (type == String.class && columnInfo.getColumnSize() >= 200) {
            element.setName("textarea");
        } else {
            element.setName("input");
            element.addAttribute("type", "text");
        }
        element.addAttribute("name", name);
        buildInputElement(element, columnInfo);
        return element;
    }

    protected void buildInputElement(MutableTagElement element,
            ColumnInfo columnInfo) {
        String tagName = element.getName();
        String name = columnInfo.getPropertyName();
        Class<?> type = columnInfo.getPropertyType();
        if ("textarea".equals(tagName)) {
            element.addAttribute("tal:content", "param-self/" + name);
            if (element.getAttribute("cols") == null) {
                element.addAttribute("cols", 64);
            }
            if (element.getAttribute("rows") == null) {
                element.addAttribute("rows", 10);
            }
        } else if ("input".equals(tagName)) {
            element.addAttribute("tal:attributes", "value param-self/" + name);
            if ("text".equals(element.getDefilteredAttributeValue("type"))) {
                int size;
                if (type == String.class) {
                    size = columnInfo.getColumnSize() * 2;
                    if (element.getAttribute("maxlength") == null) {
                        element.addAttribute("maxlength", columnInfo
                                .getColumnSize());
                    }
                } else if (Date.class.isAssignableFrom(type)) {
                    size = 20;
                } else {
                    size = 32;
                }
                if (element.getAttribute("size") == null) {
                    element.addAttribute("size", size);
                }
            }
        }
    }

    protected String getFilteredLabel(ColumnInfo columnInfo) {
        String label = columnInfo.getColumnAlias();
        if (label == null) {
            label = columnInfo.getPropertyName();
        }
        return label;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Entity> findEntityClass(String name) {
        String capitalized = BeanUtils.capitalize(name);
        for (String rootPackageName : ymirNamingConvention
                .getRootPackageNames()) {
            String className = rootPackageName + ".dbflute.exentity."
                    + capitalized;
            try {
                return (Class<? extends Entity>) ClassUtils.forName(className);
            } catch (ClassNotFoundException ignore) {
            }
        }
        return null;
    }

    private String getEntityName() {
        String path = getRequest().getPath();
        if (!path.startsWith("/")) {
            return null;
        }
        int slash = path.indexOf("/", 1);
        if (slash < 0) {
            return path.substring(1);
        } else {
            return path.substring(1, slash);
        }
    }

    private Entity getEntity() throws IllegalSyntaxException {
        String name = getEntityName();
        if (name == null) {
            throw new IllegalSyntaxException(
                    "Cannot infer entity name from path: "
                            + getRequest().getPath());
        }
        return getEntity(name);
    }

    private Entity getEntity(String name) throws IllegalSyntaxException {
        Class<? extends Entity> entityClass = findEntityClass(name);
        if (entityClass == null) {
            throw new IllegalSyntaxException(
                    "Entity class corresponding to name '" + name
                            + "' not found");
        }
        try {
            return ClassUtils.newInstance(entityClass);
        } catch (Throwable t) {
            throw new IllegalSyntaxException("Cannot instanciate entity: "
                    + entityClass.getName());
        }
    }

    private Map<String, ColumnInfo> getColumnInfoMap(Entity entity) {
        Map<String, ColumnInfo> columnInfoMap = new LinkedHashMap<String, ColumnInfo>();
        for (ColumnInfo columnInfo : entity.getDBMeta().getColumnInfoList()) {
            columnInfoMap.put(columnInfo.getPropertyName(), columnInfo);
        }
        return columnInfoMap;
    }

    private String getActionName() {
        String path = getRequest().getPath();
        String actionName;
        int slash = path.lastIndexOf("/");
        if (slash < 0) {
            actionName = path;
        } else {
            actionName = path.substring(slash + 1);
        }
        int dot = actionName.indexOf(".");
        if (dot >= 0) {
            actionName = actionName.substring(0, dot);
        }
        return actionName;
    }

    private Request getRequest() {
        return (Request) applicationManager.findContextApplication()
                .getS2Container().getComponent(Request.class);
    }
}
