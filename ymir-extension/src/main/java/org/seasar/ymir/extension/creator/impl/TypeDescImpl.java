package org.seasar.ymir.extension.creator.impl;

import static org.seasar.ymir.extension.creator.util.DescUtils.getComponentName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

    private ClassDesc componentClassDesc_;

    private boolean collection_;

    private String collectionClassName_;

    private boolean explicit_;

    private static final Map<String, String> DEFAULT_VALUE_MAP;

    private static final Map<String, String> WRAPPER_MAP;

    private static final String PACKAGEPREFIX_FREYJA_RENDER_CLASS = "net.skirnir.freyja.render.";

    private static final String SUFFIX_DTO = "Dto";

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
        setName(typeName);
        explicit_ = explicit;
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

    public TypeDescImpl(ClassDesc classDesc, boolean collection) {
        this(classDesc, collection, false);
    }

    public TypeDescImpl(ClassDesc classDesc, boolean collection,
            boolean explicit) {
        setComponentClassDesc(classDesc);
        collection_ = collection;
        explicit_ = explicit;
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
        if (componentClassDesc_ != null) {
            cloned.componentClassDesc_ = (ClassDesc) componentClassDesc_
                    .clone();
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

    public ClassDesc getComponentClassDesc() {
        return componentClassDesc_;
    }

    public void setComponentClassDesc(ClassDesc classDesc) {
        componentClassDesc_ = classDesc;
        name_ = null;
    }

    public void setName(String typeName) {
        setName(typeName, null);
    }

    public void setName(String typeName, Map<String, ClassDesc> classDescMap) {
        componentClassDesc_ = null;
        collectionClassName_ = null;
        collection_ = DescUtils.isArray(typeName);
        name_ = normalizePackage(typeName.replace('$', '.'));

        String componentClassName;
        if (collection_) {
            // 配列である場合はコレクションクラスであっても普通のクラスとして扱う。
            componentClassName = getComponentName(typeName.replace('$', '.'));
        } else {
            // 配列でない場合はコレクションクラスである場合についての処理を行なう。
            TypeToken token = new TypeToken(typeName);
            try {
                collection_ = Collection.class.isAssignableFrom(Class
                        .forName(token.getBaseName()));
            } catch (ClassNotFoundException ignore) {
            }
            if (collection_) {
                collectionClassName_ = token.getBaseName();
                TypeToken[] types = token.getTypes();
                if (types.length == 0) {
                    componentClassName = Object.class.getName();
                } else {
                    componentClassName = types[0].getBaseName();
                }
            } else {
                // 配列でもコレクションでもないのでそのまま扱う。
                componentClassName = typeName;
            }
        }

        String className = DescUtils.getNonGenericClassName(componentClassName);
        if (classDescMap != null) {
            componentClassDesc_ = classDescMap.get(className);
        }
        if (componentClassDesc_ == null) {
            componentClassDesc_ = new SimpleClassDesc(className);
        }
    }

    public boolean isCollection() {
        return collection_;
    }

    public void setCollection(boolean collection) {
        collection_ = collection;
        name_ = null;
    }

    public String getCollectionClassName() {
        return collectionClassName_;
    }

    public void setCollectionClassName(String collectionClassName) {
        collectionClassName_ = collectionClassName;
        name_ = null;
    }

    public boolean isExplicit() {
        return explicit_;
    }

    public void setExplicit(boolean explicit) {
        explicit_ = explicit;
    }

    public void transcript(TypeDesc typeDesc) {
        setComponentClassDesc(typeDesc.getComponentClassDesc());
        collection_ = typeDesc.isCollection();
        collectionClassName_ = typeDesc.getCollectionClassName();
        name_ = typeDesc.getName();
    }

    public boolean isGeneric() {
        if (name_ != null) {
            return name_.indexOf('<') >= 0;
        } else {
            return false;
        }
    }

    public String getCompleteName() {
        if (name_ != null) {
            return name_;
        } else {
            StringBuilder sb = new StringBuilder();
            if (collection_) {
                if (collectionClassName_ != null) {
                    sb.append(collectionClassName_).append("<");
                }
            }
            sb.append(componentClassDesc_.getName());
            if (collection_) {
                if (collectionClassName_ != null) {
                    sb.append(">");
                } else {
                    sb.append(ARRAY_SUFFIX);
                }
            }
            return sb.toString();
        }
    }

    public String getName() {
        if (name_ != null) {
            return normalizePackage(name_);
        } else {
            StringBuilder sb = new StringBuilder();
            if (collection_) {
                if (collectionClassName_ != null) {
                    sb.append(normalizePackage(collectionClassName_)).append(
                            "<");
                }
            }
            sb.append(normalizePackage(componentClassDesc_.getName()));
            if (collection_) {
                if (collectionClassName_ != null) {
                    sb.append(">");
                } else {
                    sb.append(ARRAY_SUFFIX);
                }
            }
            return sb.toString();
        }
    }

    public String getShortName() {
        if (name_ != null) {
            return getShortTypeName(name_);
        } else {
            StringBuilder sb = new StringBuilder();
            if (collection_) {
                if (collectionClassName_ != null) {
                    sb.append(ClassUtils.getShortName(collectionClassName_))
                            .append("<");
                }
            }
            sb.append(componentClassDesc_.getShortName());
            if (collection_) {
                if (collectionClassName_ != null) {
                    sb.append(">");
                } else {
                    sb.append(ARRAY_SUFFIX);
                }
            }
            return sb.toString();
        }
    }

    public String getShortClassName() {
        if (name_ != null) {
            return getShortClassName(name_);
        } else if (collection_ && collectionClassName_ != null) {
            return ClassUtils.getShortName(collectionClassName_);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(componentClassDesc_.getShortName());
            if (collection_) {
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
            List<String> list = new ArrayList<String>();
            list.add(componentClassDesc_.getName());
            if (collection_ && collectionClassName_ != null) {
                list.add(collectionClassName_);
            }
            return list.toArray(new String[0]);
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
        if (collection_) {
            return NULL_VALUE;
        } else {
            String name = componentClassDesc_.getName();
            String value = DEFAULT_VALUE_MAP.get(name);
            if (value != null) {
                return value;
            } else {
                return NULL_VALUE;
            }
        }
    }

    public String getInstanceName() {
        if (collection_) {
            return componentClassDesc_.getInstanceName() + "s";
        } else {
            return componentClassDesc_.getInstanceName();
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

    public String getInitialValue() {
        if (collection_) {
            if (collectionClassName_ != null) {
                // 配列でないコレクションの場合（何を生成すればいいかも分からないし）、外からぶら下げてもらうことが多いのでnullにする。
                return null;
            } else {
                // 配列の場合は（何を生成すればいいかも分かるし）空の配列をぶら下げておく。
                return "new " + normalizePackage(componentClassDesc_.getName())
                        + "[0]";
            }
        } else {
            if (componentClassDesc_.getPackageName().startsWith(
                    PACKAGEPREFIX_FREYJA_RENDER_CLASS)
                    || componentClassDesc_.getName().endsWith(SUFFIX_DTO)) {
                try {
                    Class<?> clazz = Class.forName(componentClassDesc_
                            .getName());
                    try {
                        clazz.newInstance();
                        return "new " + getName() + "()";
                    } catch (InstantiationException ignore) {
                    } catch (IllegalAccessException ignore) {
                    }
                } catch (ClassNotFoundException ignore) {
                }
            }
            return null;
        }
    }
}
