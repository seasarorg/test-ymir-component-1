package org.seasar.ymir.extension.creator.impl;

import static org.seasar.ymir.extension.creator.util.GenericsUtils.getFullyQualifiedTypeName;
import static org.seasar.ymir.extension.creator.util.GenericsUtils.getNormalizedTypeName;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.Desc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.GenericsUtils;
import org.seasar.ymir.extension.creator.util.type.Token;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
import org.seasar.ymir.util.ClassUtils;

public class TypeDescImpl implements TypeDesc {
    private DescPool pool_;

    private ClassDesc componentClassDesc_;

    private String componentTypeName_;

    private boolean collection_;

    private String collectionClassName_;

    private String collectionImplementationClassName_;

    private boolean explicit_;

    private Desc<?> parent_;

    private Set<String> touchedClassNameSet_ = new HashSet<String>();

    public TypeDescImpl(DescPool pool, Type type) {
        this(pool, type, null);
    }

    public TypeDescImpl(DescPool pool, Type type, String qualifier) {
        this(pool, GenericsUtils.toString(type), qualifier);
    }

    public TypeDescImpl(DescPool pool, String typeName) {
        this(pool, typeName, null);
    }

    public TypeDescImpl(DescPool pool, String typeName,
            String componentClassQualifier) {
        pool_ = pool;

        typeName = getFullyQualifiedTypeName(typeName.replace('$', '.'));
        TypeToken token = new TypeToken(typeName);
        boolean collection = token.isArray();

        String componentTypeName;
        String componentClassName;
        if (collection) {
            componentTypeName = GenericsUtils.getComponentName(typeName);
            componentClassName = GenericsUtils
                    .getNonGenericClassName(componentTypeName);
        } else {
            componentTypeName = typeName;
            componentClassName = token.getBaseName();

            Class<?> clazz = pool_.getSourceCreator().getClass(
                    componentClassName);
            if (clazz != null) {
                collection = Collection.class.isAssignableFrom(clazz);
            }
            if (collection) {
                collectionClassName_ = token.getBaseName();
                TypeToken[] types = token.getTypes();
                if (types.length == 0) {
                    componentTypeName = Object.class.getName();
                    componentClassName = Object.class.getName();
                } else {
                    componentTypeName = types[0].getAsString();
                    componentClassName = types[0].getBaseName();
                }
            }
        }

        collection_ = collection;
        componentTypeName_ = componentTypeName;
        componentClassDesc_ = pool_.getClassDesc(componentClassName,
                componentClassQualifier);
        componentClassDesc_.setParent(this);
    }

    public TypeDescImpl(DescPool pool, ClassDesc componentClassDesc) {
        this(pool, componentClassDesc, false, null);
    }

    public TypeDescImpl(DescPool pool, ClassDesc componentClassDesc,
            boolean collection, String collectionClassName) {
        pool_ = pool;
        componentTypeName_ = componentClassDesc.getName();
        componentClassDesc_ = componentClassDesc;
        if (componentClassDesc_.getDescPool() != pool_) {
            throw new IllegalArgumentException(
                    "Can't set ClassDesc born from another DescPool");
        }
        componentClassDesc_.setParent(this);
        collection_ = collection;
        collectionClassName_ = collectionClassName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((collectionClassName_ == null) ? 0 : collectionClassName_
                        .hashCode());
        result = prime
                * result
                + ((collectionImplementationClassName_ == null) ? 0
                        : collectionImplementationClassName_.hashCode());
        result = prime * result + (collection_ ? 1231 : 1237);
        result = prime
                * result
                + ((componentClassDesc_ == null) ? 0 : componentClassDesc_
                        .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TypeDescImpl other = (TypeDescImpl) obj;
        if (collectionClassName_ == null) {
            if (other.collectionClassName_ != null)
                return false;
        } else if (!collectionClassName_.equals(other.collectionClassName_))
            return false;
        if (collectionImplementationClassName_ == null) {
            if (other.collectionImplementationClassName_ != null)
                return false;
        } else if (!collectionImplementationClassName_
                .equals(other.collectionImplementationClassName_))
            return false;
        if (collection_ != other.collection_)
            return false;
        if (componentClassDesc_ == null) {
            if (other.componentClassDesc_ != null)
                return false;
        } else if (!componentClassDesc_.equals(other.componentClassDesc_))
            return false;
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
        componentTypeName_ = classDesc.getName();
        componentClassDesc_ = classDesc;
        componentClassDesc_.setParent(this);
    }

    public void setComponentClassDesc(Class<?> clazz) {
        setComponentClassDesc(pool_.getClassDesc(clazz));
    }

    public String getComponentTypeName() {
        return componentTypeName_;
    }

    public boolean isCollection() {
        return collection_;
    }

    public void setCollection(boolean collection) {
        collection_ = collection;
    }

    public String getCollectionClassName() {
        return collectionClassName_;
    }

    public void setCollectionClassName(String collectionClassName) {
        collectionClassName_ = collectionClassName;
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

    public String getCompleteName() {
        StringBuilder sb = new StringBuilder();
        if (collection_) {
            if (collectionClassName_ != null) {
                sb.append(collectionClassName_).append("<");
            }
        }
        sb.append(componentTypeName_);
        if (collection_) {
            if (collectionClassName_ != null) {
                sb.append(">");
            } else {
                sb.append(ARRAY_SUFFIX);
            }
        }
        return sb.toString();
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        if (collection_) {
            if (collectionClassName_ != null) {
                sb.append(getNormalizedTypeName(collectionClassName_)).append(
                        "<");
            }
        }
        sb.append(getNormalizedTypeName(componentTypeName_));
        if (collection_) {
            if (collectionClassName_ != null) {
                sb.append(">");
            } else {
                sb.append(ARRAY_SUFFIX);
            }
        }
        return sb.toString();
    }

    public String getShortName() {
        StringBuilder sb = new StringBuilder();
        if (collection_) {
            if (collectionClassName_ != null) {
                touchedClassNameSet_.add(collectionClassName_);
                sb.append(ClassUtils.getShortName(collectionClassName_))
                        .append("<");
            }
        }

        TypeToken token = new TypeToken(componentTypeName_);
        token.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor, Object... parameters) {
                touchedClassNameSet_.add(acceptor.getComponentName());
                acceptor.setBaseName(ClassUtils.getShorterName(acceptor
                        .getBaseName()));
                return null;
            }
        });
        sb.append(token.getAsString());

        if (collection_) {
            if (collectionClassName_ != null) {
                sb.append(">");
            } else {
                sb.append(ARRAY_SUFFIX);
            }
        }
        return sb.toString();
    }

    public String getShortClassName() {
        if (collection_ && collectionClassName_ != null) {
            touchedClassNameSet_.add(collectionClassName_);
            return ClassUtils.getShortName(collectionClassName_);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(ClassUtils.getShortName(componentClassDesc_.getName()));
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
            public Object visit(Token acceptor, Object... parameters) {
                acceptor.setBaseName(ClassUtils.getShortName(acceptor
                        .getBaseName()));
                return null;
            }
        });
        return typeToken;
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

    public TypeDesc transcriptTo(TypeDesc desc) {
        DescPool pool = desc.getDescPool();

        if (componentClassDesc_.getDescPool() == pool) {
            desc.setComponentClassDesc(componentClassDesc_);
        } else {
            desc.setComponentClassDesc(componentClassDesc_.transcriptTo(pool
                    .getClassDesc(componentClassDesc_.getName(),
                            componentClassDesc_.getQualifier())));
        }

        desc.setCollection(collection_);
        desc.setCollectionClassName(collectionClassName_);
        desc
                .setCollectionImplementationClassName(collectionImplementationClassName_);
        desc.setExplicit(explicit_);

        return desc;
    }

    @SuppressWarnings("unchecked")
    public <D extends Desc<?>> D getParent() {
        return (D) parent_;
    }

    public void setParent(Desc<?> parent) {
        parent_ = parent;
    }

    public void addDependingClassNamesTo(final Set<String> set) {
        TypeToken token = new TypeToken(componentTypeName_);
        token.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor, Object... parameters) {
                String componentName = acceptor.getComponentName();
                Class<?> componentClass = pool_.getSourceCreator().getClass(
                        componentName);
                if (componentClass != null) {
                    set.add(componentClass.getName());
                } else {
                    set.add(componentName);
                }
                return null;
            }
        });
        if (collection_ && collectionClassName_ != null) {
            set.add(collectionClassName_);
        }
    }

    public String[] getImportClassNames() {
        Set<String> set = new TreeSet<String>();
        addDependingClassNamesTo(set);

        DescUtils.removeStandardClassNames(set);
        return set.toArray(new String[0]);
    }

    public void setTouchedClassNameSet(Set<String> set) {
        if (set == touchedClassNameSet_) {
            return;
        }

        touchedClassNameSet_ = set;

        componentClassDesc_.setTouchedClassNameSet(set);
    }
}
