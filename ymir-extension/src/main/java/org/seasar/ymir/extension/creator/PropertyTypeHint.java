package org.seasar.ymir.extension.creator;

public class PropertyTypeHint {
    private String className_;

    private String propertyName_;

    private String typeName_;

    private boolean collection_;

    public PropertyTypeHint(String className, String propertyName,
            String typeName, boolean collection) {
        className_ = className;
        propertyName_ = propertyName;
        typeName_ = typeName;
        collection_ = collection;
    }

    @Override
    public String toString() {
        return "className=" + className_ + ", propertyName=" + propertyName_
                + ", typeName=" + typeName_ + ", collection=" + collection_;
    }

    public boolean isCollection() {
        return collection_;
    }

    public String getClassName() {
        return className_;
    }

    public String getPropertyName() {
        return propertyName_;
    }

    public String getTypeName() {
        return typeName_;
    }
}
