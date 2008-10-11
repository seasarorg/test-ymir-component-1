package org.seasar.ymir.extension.creator.impl;

import static org.seasar.ymir.extension.creator.util.DescUtils.getComponentName;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.type.Token;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
import org.seasar.ymir.util.ClassUtils;

public class TypeDescImpl implements TypeDesc {
    private static final String ARRAY_SUFFIX = "[]";

    private static final String PACKAGE_JAVA_LANG = "java.lang.";

    private static final String NULL_VALUE = "null";

    private String name_;

    private ClassDesc classDesc_;

    private boolean array_;

    private boolean explicit_;

    private static final Map<String, String> DEFAULT_VALUE_MAP;

    private static final Map<String, String> WRAPPER_MAP;

    static {
        DEFAULT_VALUE_MAP = new HashMap<String, String>();
        DEFAULT_VALUE_MAP.put("byte", "0");
        DEFAULT_VALUE_MAP.put("short", "0");
        DEFAULT_VALUE_MAP.put("int", "0");
        DEFAULT_VALUE_MAP.put("long", "0");
        DEFAULT_VALUE_MAP.put("float", "0");
        DEFAULT_VALUE_MAP.put("double", "0");
        DEFAULT_VALUE_MAP.put("char", "0");
        DEFAULT_VALUE_MAP.put("boolean", "false");

        WRAPPER_MAP = new HashMap<String, String>();
        WRAPPER_MAP.put("byte", "Byte");
        WRAPPER_MAP.put("short", "Short");
        WRAPPER_MAP.put("int", "Integer");
        WRAPPER_MAP.put("long", "Long");
        WRAPPER_MAP.put("float", "Float");
        WRAPPER_MAP.put("double", "Double");
        WRAPPER_MAP.put("char", "Character");
        WRAPPER_MAP.put("boolean", "Boolean");
    }

    public TypeDescImpl() {
        this(DEFAULT_CLASSDESC);
    }

    public TypeDescImpl(String typeName) {
        this(typeName, false);
    }

    public TypeDescImpl(String typeName, boolean explicit) {
        this(new SimpleClassDesc(DescUtils
                .getNonGenericClassName(getComponentName(typeName.replace('$',
                        '.')))), DescUtils.isArray(typeName), explicit);
        name_ = normalizePackage(typeName.replace('$', '.'));
    }

    String normalizePackage(String typeName) {
        if (typeName == null) {
            return null;
        }

        TypeToken typeToken = new TypeToken(typeName);
        typeToken.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor) {
                String baseName = acceptor.getBaseName();
                StringBuilder sb = new StringBuilder();
                if (baseName.startsWith(PACKAGE_JAVA_LANG)) {
                    sb.append(baseName.substring(PACKAGE_JAVA_LANG.length()));
                } else {
                    sb.append(baseName);
                }
                acceptor.setBaseName(sb.toString());
                return null;
            }
        });
        return typeToken.getAsString();
    }

    public TypeDescImpl(ClassDesc classDesc) {
        this(classDesc, false);
    }

    public TypeDescImpl(ClassDesc classDesc, boolean array) {
        this(classDesc, array, false);
    }

    public TypeDescImpl(ClassDesc classDesc, boolean array, boolean explicit) {
        setClassDesc(classDesc);
        setArray(array);
        setExplicit(explicit);
    }

    public TypeDescImpl(Class<?> clazz) {
        this(clazz, false);
    }

    public TypeDescImpl(Class<?> clazz, boolean explicit) {
        this(new SimpleClassDesc(clazz.getName()), clazz.isArray(), explicit);
    }

    public Object clone() {
        TypeDescImpl cloned;
        try {
            cloned = (TypeDescImpl) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
        if (classDesc_ != null) {
            cloned.classDesc_ = new SimpleClassDesc(classDesc_.getName());
        }
        return cloned;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        TypeDescImpl o = (TypeDescImpl) obj;
        if (!o.getName().equals(getName())) {
            return false;
        }
        return true;
    }

    public String toString() {
        return getName();
    }

    public ClassDesc getClassDesc() {
        return classDesc_;
    }

    public void setClassDesc(ClassDesc classDesc) {
        classDesc_ = classDesc;
        name_ = null;
    }

    public void setClassDesc(String className) {
        setClassDesc(new SimpleClassDesc(className));
    }

    public boolean isArray() {
        return array_;
    }

    public void setArray(boolean array) {
        array_ = array;
    }

    public boolean isExplicit() {
        return explicit_;
    }

    public void setExplicit(boolean explicit) {
        explicit_ = explicit;
    }

    public void transcript(TypeDesc typeDesc) {
        setClassDesc(typeDesc.getClassDesc());
        setArray(typeDesc.isArray());
        name_ = typeDesc.getName();
    }

    public boolean isGeneric() {
        if (name_ != null) {
            return name_.indexOf('<') >= 0;
        } else {
            return false;
        }
    }

    public String getName() {
        if (name_ != null) {
            return normalizePackage(name_);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(normalizePackage(classDesc_.getName()));
            if (array_) {
                sb.append(ARRAY_SUFFIX);
            }
            return sb.toString();
        }
    }

    public String getShortName() {
        if (name_ != null) {
            return getShortTypeName(name_);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(classDesc_.getShortName());
            if (array_) {
                sb.append(ARRAY_SUFFIX);
            }
            return sb.toString();
        }
    }

    public String getShortClassName() {
        if (name_ != null) {
            return getShortClassName(name_);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(classDesc_.getShortName());
            if (array_) {
                sb.append(ARRAY_SUFFIX);
            }
            return sb.toString();
        }
    }

    String getShortTypeName(String typeName) {
        if (typeName == null) {
            return null;
        }

        return getTypeTokenForShorten(typeName).getAsString();
    }

    String getShortClassName(String typeName) {
        if (typeName == null) {
            return null;
        }

        return getTypeTokenForShorten(typeName).getBaseName();
    }

    TypeToken getTypeTokenForShorten(String typeName) {
        TypeToken typeToken = new TypeToken(typeName);
        typeToken.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor) {
                acceptor.setBaseName(ClassUtils.getShortName(acceptor
                        .getBaseName()));
                return null;
            }
        });
        return typeToken;
    }

    public String[] getImportClassNames() {
        if (name_ != null) {
            return getImportClassNames(name_);
        } else {
            return new String[] { classDesc_.getName() };
        }
    }

    String[] getImportClassNames(String typeName) {
        if (typeName == null) {
            return new String[0];
        }

        final Set<String> importClassNameList = new TreeSet<String>();
        TypeToken typeToken = new TypeToken(typeName);
        typeToken.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor) {
                String baseName = acceptor.getBaseName();
                if (baseName.indexOf('.') >= 0) {
                    importClassNameList.add(DescUtils
                            .getComponentName(baseName));
                }
                return null;
            }
        });
        return importClassNameList.toArray(new String[0]);
    }

    public String getDefaultValue() {
        if (array_) {
            return NULL_VALUE;
        } else {
            String name = classDesc_.getName();
            String value = (String) DEFAULT_VALUE_MAP.get(name);
            if (value != null) {
                return value;
            } else {
                return NULL_VALUE;
            }
        }
    }

    public String getInstanceName() {
        if (array_) {
            return classDesc_.getInstanceName() + "s";
        } else {
            return classDesc_.getInstanceName();
        }
    }

    String toWrapperName(String name) {
        if (name == null) {
            return null;
        }
        String wrapperName = WRAPPER_MAP.get(name);
        if (wrapperName != null) {
            return wrapperName;
        } else {
            return name;
        }
    }

    public void replaceClassDesc(ClassDesc classDesc) {
        classDesc_ = classDesc;
    }
}
