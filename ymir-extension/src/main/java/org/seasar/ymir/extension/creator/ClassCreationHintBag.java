package org.seasar.ymir.extension.creator;

import java.util.HashMap;
import java.util.Map;

public class ClassCreationHintBag {
    private Map<String, Map<String, PropertyTypeHint>> propertyTypeHintMap_ = new HashMap<String, Map<String, PropertyTypeHint>>();

    private Map<String, ClassHint> classHintMap_ = new HashMap<String, ClassHint>();

    public ClassCreationHintBag(PropertyTypeHint[] propertyTypeHints,
            ClassHint[] classHints) {
        if (propertyTypeHints != null) {
            for (int i = 0; i < propertyTypeHints.length; i++) {
                Map<String, PropertyTypeHint> map = propertyTypeHintMap_
                        .get(propertyTypeHints[i].getClassName());
                if (map == null) {
                    map = new HashMap<String, PropertyTypeHint>();
                    propertyTypeHintMap_.put(propertyTypeHints[i]
                            .getClassName(), map);
                }
                map.put(propertyTypeHints[i].getPropertyName(),
                        propertyTypeHints[i]);
            }
        }
        if (classHints != null) {
            for (int i = 0; i < classHints.length; i++) {
                classHintMap_.put(classHints[i].getClassName(), classHints[i]);
            }
        }
    }

    public PropertyTypeHint getPropertyTypeHint(String className,
            String propertyName) {
        Map<String, PropertyTypeHint> map = propertyTypeHintMap_.get(className);
        if (map == null) {
            return null;
        }
        return map.get(propertyName);
    }

    public ClassHint getClassHint(String className) {
        return classHintMap_.get(className);
    }
}
