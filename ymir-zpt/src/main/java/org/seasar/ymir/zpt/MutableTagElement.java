package org.seasar.ymir.zpt;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
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

    @SuppressWarnings("unchecked")
    protected MutableTagElement(TagElement element) {
        super(null, null, null);

        name_ = element.getName();
        for (Attribute attr : element.getAttributes()) {
            String lattrName = attr.getName().toLowerCase();
            originalAttrMap_.put(lattrName, attr);
            attrMap_.put(lattrName, attr);
        }
        bodyElements_ = element.getBodyElements();
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
            for (Attribute attribute : attributes) {
                addAttribute(attribute);
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

    public void removeAttributes(String attrNamePattern,
            String... validAttrNames) throws IllegalSyntaxException {
        Pattern pattern = Pattern.compile(attrNamePattern.toLowerCase());
        for (Iterator<Map.Entry<String, Attribute>> itr = attrMap_.entrySet()
                .iterator(); itr.hasNext();) {
            Map.Entry<String, Attribute> entry = itr.next();
            if (pattern.matcher(entry.getKey()).find()) {
                itr.remove();

                boolean valid = false;
                for (String validAttrName : validAttrNames) {
                    if (entry.getKey().equals(validAttrName)) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    throw new IllegalSyntaxException("Unknown attribute: "
                            + entry.getKey());
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
