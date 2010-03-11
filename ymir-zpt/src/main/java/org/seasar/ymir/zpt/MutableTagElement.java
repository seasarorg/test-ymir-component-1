package org.seasar.ymir.zpt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.TagElement;
import net.skirnir.freyja.TagEvaluatorUtils;

/**
 * @since 1.0.7
 */
public class MutableTagElement extends TagElement {
    private static final Attribute[] ATTRIBUTES_EMPTY = new Attribute[0];

    private String name_;

    private Map<String, Attribute> originalAttrMap_ = new LinkedHashMap<String, Attribute>();

    private Map<String, Attribute> attrMap_ = new LinkedHashMap<String, Attribute>();

    private Element[] bodyElements_;

    public static MutableTagElement newInstance() {
        return new MutableTagElement();
    }

    public static MutableTagElement newInstance(String name) {
        return new MutableTagElement(name);
    }

    public static MutableTagElement newInstance(String name,
            Attribute[] attributes) {
        return new MutableTagElement(name, attributes);
    }

    public static MutableTagElement newInstance(String name,
            Element[] bodyElements) {
        return new MutableTagElement(name, bodyElements);
    }

    public static MutableTagElement newInstance(String name,
            Attribute[] attributes, Element[] bodyElements) {
        return new MutableTagElement(name, attributes, bodyElements);
    }

    public static MutableTagElement toMutable(TagElement element) {
        if (element == null || element instanceof MutableTagElement) {
            return (MutableTagElement) element;
        } else {
            return new MutableTagElement(element);
        }
    }

    protected MutableTagElement() {
        super(null, null, null);
    }

    protected MutableTagElement(TagElement element) {
        super(null, null, null);

        name_ = element.getName();
        for (Attribute attr : element.getAttributes()) {
            String lattrName = attr.getName().toLowerCase();
            originalAttrMap_.put(lattrName, attr);
            attrMap_.put(lattrName, attr);
        }
        bodyElements_ = element.getBodyElements();
        setColumnNumber(element.getColumnNumber());
        setLineNumber(element.getLineNumber());
    }

    protected MutableTagElement(String name) {
        this(name, ATTRIBUTES_EMPTY, null);
    }

    protected MutableTagElement(String name, Attribute[] attributes) {
        this(name, attributes, null);
    }

    protected MutableTagElement(String name, Element[] bodyElements) {
        this(name, ATTRIBUTES_EMPTY, bodyElements);
    }

    protected MutableTagElement(String name, Attribute[] attributes,
            Element[] bodyElements) {
        super(null, null, null);
        setName(name);
        if (attributes != null) {
            for (Attribute attr : attributes) {
                String lattrName = attr.getName().toLowerCase();
                originalAttrMap_.put(lattrName, attr);
                attrMap_.put(lattrName, attr);
            }
        }
        setBodyElements(bodyElements);
    }

    @Override
    public String getName() {
        return name_;
    }

    public void setName(String name) {
        name_ = name;
    }

    public Attribute getOriginalAttribute(String attrName) {
        return originalAttrMap_.get(attrName.toLowerCase());
    }

    public String getDefilteredOriginalAttributeValue(String attrName) {
        Attribute attr = getOriginalAttribute(attrName);
        if (attr == null) {
            return null;
        } else {
            return TagEvaluatorUtils.defilter(attr.getValue());
        }
    }

    public void addAttribute(Attribute attr) {
        attrMap_.put(attr.getName().toLowerCase(), attr);
    }

    public void addAttribute(String name, Object defilteredValue) {
        String filteredValue;
        if (defilteredValue == null) {
            filteredValue = null;
        } else {
            filteredValue = TagEvaluatorUtils
                    .filter(defilteredValue.toString());
        }

        addAttribute(new Attribute(name, filteredValue));
    }

    public void removeAttribute(String attrName) {
        attrMap_.remove(attrName.toLowerCase());
    }

    public void removeAttributes(String[] attrNamePatterns,
            String... validAttrNames) throws IllegalSyntaxException {
        List<Pattern> patterns = new ArrayList<Pattern>();
        for (String attrNamePattern : attrNamePatterns) {
            patterns.add(Pattern.compile(attrNamePattern));
        }

        Set<String> validNames = new HashSet<String>(Arrays
                .asList(validAttrNames));
        for (Iterator<Map.Entry<String, Attribute>> itr = attrMap_.entrySet()
                .iterator(); itr.hasNext();) {
            Map.Entry<String, Attribute> entry = itr.next();
            for (Pattern pattern : patterns) {
                String name = entry.getKey();
                if (pattern.matcher(name).find()) {
                    itr.remove();

                    if (!validNames.contains(name)) {
                        Attribute attr = entry.getValue();
                        throw (IllegalSyntaxException) new IllegalSyntaxException(
                                "Unknown attribute: " + name).setLineNumber(
                                attr.getLineNumber()).setColumnNumber(
                                attr.getColumnNumber());
                    }
                }
            }
        }
    }

    @Override
    public Attribute[] getAttributes() {
        return attrMap_.values().toArray(ATTRIBUTES_EMPTY);
    }

    public Attribute getAttribute(String attrName) {
        return attrMap_.get(attrName.toLowerCase());
    }

    public String getDefilteredAttributeValue(String attrName) {
        Attribute attr = getAttribute(attrName);
        if (attr == null) {
            return null;
        } else {
            return TagEvaluatorUtils.defilter(attr.getValue());
        }
    }

    @Override
    public Element[] getBodyElements() {
        return bodyElements_;
    }

    public void setBodyElements(Element[] bodyElements) {
        bodyElements_ = bodyElements;
    }
}
