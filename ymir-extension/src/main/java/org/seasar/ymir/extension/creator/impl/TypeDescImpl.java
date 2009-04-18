package org.seasar.ymir.extension.creator.impl;

import static org.seasar.ymir.extension.creator.util.DescUtils.complementPackage;
import static org.seasar.ymir.extension.creator.util.DescUtils.getComponentName;
import static org.seasar.ymir.extension.creator.util.DescUtils.isArray;
import static org.seasar.ymir.extension.creator.util.DescUtils.normalizePackage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.type.Token;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
import org.seasar.ymir.util.ClassUtils;

public class TypeDescImpl implements TypeDesc {
    private DescPool pool_;

    private String name_;

    private ClassDesc componentClassDesc_;

    private boolean collection_;

    private String collectionClassName_;

    private String collectionImplementationClassName_;

    private boolean explicit_;

    public TypeDescImpl(DescPool pool, Type type) {
        this(pool, DescUtils.toString(type));
    }

    public TypeDescImpl(DescPool pool, ClassDesc classDesc) {
        this(pool, classDesc.getName());
    }

    public TypeDescImpl(DescPool pool, String typeName) {
        pool_ = pool;
        setName(typeName);
    }

    public Object clone() {
        TypeDescImpl cloned;
        try {
            cloned = (TypeDescImpl) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }

        return cloned;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        } else if (this == obj) {
            return true;
        }

        TypeDescImpl o = (TypeDescImpl) obj;
        if (name_ == null) {
            if (o.name_ != null) {
                return false;
            }
        } else if (!name_.equals(o.name_)) {
            return false;
        }

        return true;
    }

    public String toString() {
        return getName();
    }

    public DescPool getDescPool() {
        return pool_;
    }

    public ClassDesc getComponentClassDesc() {
        return componentClassDesc_;
    }

    public void setComponentClassDesc(ClassDesc classDesc) {
        componentClassDesc_ = classDesc;
        name_ = null;
    }

    public void setComponentClassDesc(Class<?> clazz) {
        if (pool_ != null) {
            setComponentClassDesc(pool_.getClassDesc(clazz));
        } else {
            setComponentClassDesc(new ClassDescImpl(null, clazz));
        }
    }

    public void setName(String typeName) {
        componentClassDesc_ = null;
        collectionClassName_ = null;
        collection_ = isArray(typeName);
        name_ = normalizePackage(typeName.replace('$', '.'));
        typeName = complementPackage(typeName);

        String componentClassName;
        if (collection_) {
            // 配列である場合はコレクションクラスであっても普通のクラスとして扱う。
            componentClassName = getComponentName(typeName.replace('$', '.'));
        } else {
            // 配列でない場合はコレクションクラスである場合についての処理を行なう。
            TypeToken token = new TypeToken(typeName);
            Class<?> clazz;
            if (pool_ != null) {
                clazz = pool_.getSourceCreator().getClass(token.getBaseName());
            } else {
                clazz = DescUtils.findClass(token.getBaseName());
            }
            if (clazz != null) {
                collection_ = Collection.class.isAssignableFrom(clazz);
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
        if (pool_ != null) {
            componentClassDesc_ = pool_.getClassDesc(className);
        } else {
            componentClassDesc_ = new ClassDescImpl(null, className);
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

    public void setCollectionClass(Class<?> collectionClass) {
        setCollectionClassName(collectionClass != null ? collectionClass
                .getName() : null);
    }

    public String getCollectionImplementationClassName() {
        return collectionImplementationClassName_;
    }

    public void setCollectionImplementationClassName(
            String collectionImplementationClassName) {
        collectionImplementationClassName_ = collectionImplementationClassName;
    }

    public void setCollectionImplementationClass(
            Class<?> collectionImplementationClass) {
        setCollectionImplementationClassName(collectionImplementationClass != null ? collectionImplementationClass
                .getName()
                : null);
    }

    public boolean isExplicit() {
        return explicit_;
    }

    public void setExplicit(boolean explicit) {
        explicit_ = explicit;
    }

    public void transcript(TypeDesc typeDesc) {
        boolean transcriptCollectionImplementationClassName = true;
        if (collectionClassName_ != null
                && collectionClassName_.equals(typeDesc
                        .getCollectionClassName())
                && typeDesc.getCollectionImplementationClassName() == null) {
            // このTypeDescのcollectionClassNameがtranscript元と同じでかつ
            // transcript元のcollectionImplementationClassNameがnullの場合は、
            // このTypeDescのcollectionImplementationClassName情報をnullにしてしまわないようにする。
            transcriptCollectionImplementationClassName = false;
        }

        setComponentClassDesc(typeDesc.getComponentClassDesc());
        collection_ = typeDesc.isCollection();
        collectionClassName_ = typeDesc.getCollectionClassName();
        if (transcriptCollectionImplementationClassName) {
            collectionImplementationClassName_ = typeDesc
                    .getCollectionImplementationClassName();
        }
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
            return String.valueOf((Object) null);
        } else {
            Object value = ClassUtils.getDefaultValue(componentClassDesc_
                    .getName());
            if (value instanceof Character) {
                value = Integer.valueOf(((Character) value).charValue());
            } else if (value instanceof Float) {
                value = Integer.valueOf(((Float) value).intValue());
            }
            return String.valueOf(value);
        }
    }

    public String getInstanceName() {
        if (collection_) {
            return componentClassDesc_.getInstanceName() + "s";
        } else {
            return componentClassDesc_.getInstanceName();
        }
    }
}
