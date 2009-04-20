package org.seasar.ymir.extension.creator.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AbstractAnnotatedDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.MethodDescKey;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.util.ClassUtils;

public class ClassDescImpl extends AbstractAnnotatedDesc implements ClassDesc {
    private DescPool pool_;

    private String name_;

    private ClassType type_;

    private String superclassName_;

    private boolean baseClassAbstract_;

    private TypeDesc[] interfaceTypeDescs_;

    private Map<String, PropertyDesc> propertyDescMap_ = new LinkedHashMap<String, PropertyDesc>();

    private Map<MethodDescKey, MethodDesc> methodDescMap_ = new LinkedHashMap<MethodDescKey, MethodDesc>();

    private Map<String, Object> parameter_;

    private String bornOf_;

    public ClassDescImpl(DescPool pool, String name) {
        pool_ = pool;
        name_ = name;
        type_ = ClassType.typeOfClass(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
        result = prime * result + ((pool_ == null) ? 0 : pool_.hashCode());
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
        final ClassDescImpl other = (ClassDescImpl) obj;
        if (name_ == null) {
            if (other.name_ != null)
                return false;
        } else if (!name_.equals(other.name_))
            return false;
        if (pool_ == null) {
            if (other.pool_ != null)
                return false;
        } else if (!pool_.equals(other.pool_))
            return false;
        return true;
    }

    public String toString() {
        return name_;
    }

    public Object clone() {
        ClassDescImpl cloned = (ClassDescImpl) super.clone();

        cloned.pool_ = null;

        if (interfaceTypeDescs_ != null) {
            cloned.interfaceTypeDescs_ = new TypeDesc[interfaceTypeDescs_.length];
            System.arraycopy(interfaceTypeDescs_, 0,
                    cloned.interfaceTypeDescs_, 0, interfaceTypeDescs_.length);
        }

        cloned.propertyDescMap_ = new LinkedHashMap<String, PropertyDesc>();
        for (Iterator<Map.Entry<String, PropertyDesc>> itr = propertyDescMap_
                .entrySet().iterator(); itr.hasNext();) {
            Map.Entry<String, PropertyDesc> entry = itr.next();
            cloned.propertyDescMap_.put(entry.getKey(), (PropertyDesc) entry
                    .getValue().clone());
        }

        cloned.methodDescMap_ = new LinkedHashMap<MethodDescKey, MethodDesc>();
        for (Iterator<Map.Entry<MethodDescKey, MethodDesc>> itr = methodDescMap_
                .entrySet().iterator(); itr.hasNext();) {
            Map.Entry<MethodDescKey, MethodDesc> entry = itr.next();
            cloned.methodDescMap_.put(entry.getKey(), (MethodDesc) entry
                    .getValue().clone());
        }

        if (parameter_ != null) {
            cloned.parameter_ = new HashMap<String, Object>(parameter_);
        }

        return cloned;
    }

    public DescPool getDescPool() {
        return pool_;
    }

    public String getName() {
        return name_;
    }

    public ClassType getType() {
        return type_;
    }

    public boolean isTypeOf(ClassType type) {
        return type == getType();
    }

    public TypeDesc[] getInterfaceTypeDescs() {
        return interfaceTypeDescs_;
    }

    public void setInterfaceTypeDescs(TypeDesc[] interfaceTypeDescs) {
        interfaceTypeDescs_ = interfaceTypeDescs;
    }

    public PropertyDesc addProperty(String name, int mode) {
        PropertyDesc propertyDesc = getPropertyDesc(name);
        if (propertyDesc == null) {
            propertyDesc = new PropertyDescImpl(pool_, name);
            propertyDescMap_.put(name, propertyDesc);
        }
        propertyDesc.addMode(mode);
        return propertyDesc;
    }

    public PropertyDesc getPropertyDesc(String name) {
        return propertyDescMap_.get(name);
    }

    public void setPropertyDesc(PropertyDesc propertyDesc) {
        propertyDescMap_.put(propertyDesc.getName(), propertyDesc);
    }

    public void removePropertyDesc(String name) {
        propertyDescMap_.remove(name);
    }

    public PropertyDesc[] getPropertyDescs() {
        return propertyDescMap_.values().toArray(new PropertyDesc[0]);
    }

    public PropertyDesc[] getPropertyDescsOrderByName() {
        PropertyDesc[] pds = getPropertyDescs();
        Arrays.sort(pds, new Comparator<PropertyDesc>() {
            public int compare(PropertyDesc o1, PropertyDesc o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return pds;
    }

    public MethodDesc getMethodDesc(MethodDesc methodDesc) {
        return methodDescMap_.get(new MethodDescKey(methodDesc));
    }

    public void setMethodDesc(MethodDesc methodDesc) {
        methodDescMap_.put(new MethodDescKey(methodDesc), methodDesc);
    }

    public MethodDesc[] getMethodDescs() {
        return methodDescMap_.values().toArray(new MethodDesc[0]);
    }

    public MethodDesc[] getMethodDescsOrderByName() {
        MethodDesc[] mds = getMethodDescs();
        Arrays.sort(mds, new Comparator<MethodDesc>() {
            public int compare(MethodDesc o1, MethodDesc o2) {
                return new MethodDescKey(o1).compareTo(new MethodDescKey(o2));
            }
        });
        return mds;
    }

    public void removeMethodDesc(MethodDesc methodDesc) {
        methodDescMap_.remove(new MethodDescKey(methodDesc));
    }

    public String getSuperclassName() {
        return superclassName_;
    }

    public void setSuperclassName(String superclassName) {
        superclassName_ = superclassName;
    }

    public boolean isBaseClassAbstract() {

        return baseClassAbstract_;
    }

    public void setBaseClassAbstract(boolean baseClassAbstract) {
        baseClassAbstract_ = baseClassAbstract;
    }

    public void merge(ClassDesc classDesc, boolean force) {
        if (classDesc == null) {
            return;
        }

        if (classDesc.getSuperclassName() != null
                && !classDesc.getSuperclassName()
                        .equals(Object.class.getName())
                && (force || superclassName_ == null)) {
            setSuperclassName(classDesc.getSuperclassName());
        }

        for (PropertyDesc propertyDesc : classDesc.getPropertyDescs()) {
            PropertyDesc pd = getPropertyDesc(propertyDesc.getName());
            if (pd == null) {
                setPropertyDesc((PropertyDesc) propertyDesc.clone());
            } else {
                DescUtils.merge(pd, propertyDesc, force);
            }
        }

        for (MethodDesc methodDesc : classDesc.getMethodDescs()) {
            MethodDesc md = getMethodDesc(methodDesc);
            if (md == null) {
                setMethodDesc((MethodDesc) methodDesc.clone());
            } else {
                DescUtils.merge(md, methodDesc, force);
            }
        }

        setAnnotationDescs(DescUtils.merge(getAnnotationDescs(), classDesc
                .getAnnotationDescs(), force));
    }

    public void applyBornOfToAllMembers() {
        String bornOf = getBornOf();
        if (bornOf == null) {
            return;
        }

        for (PropertyDesc propertyDesc : getPropertyDescs()) {
            applyBornOfTo(propertyDesc, bornOf);
        }

        for (MethodDesc methodDesc : getMethodDescs()) {
            applyBornOfTo(methodDesc, bornOf);
        }
    }

    void applyBornOfTo(MethodDesc methodDesc, String bornOf) {
        methodDesc.setAnnotationDesc(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { bornOf }));
    }

    void applyBornOfTo(PropertyDesc propertyDesc, String bornOf) {
        // Getter。
        propertyDesc.setAnnotationDescOnGetter(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { bornOf }));

        // Setter。
        propertyDesc.setAnnotationDescOnSetter(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { bornOf }));

        // フィールド。
        propertyDesc.setAnnotationDesc(new MetaAnnotationDescImpl(
                Globals.META_NAME_BORNOF, new String[] { bornOf }));
    }

    public void removeBornOfFromAllMembers(String bornOf) {
        if (bornOf == null) {
            return;
        }

        for (PropertyDesc propertyDesc : getPropertyDescs()) {
            removeBornOfFrom(propertyDesc, bornOf);
        }

        for (MethodDesc methodDesc : getMethodDescs()) {
            removeBornOfFrom(methodDesc, bornOf);
        }
    }

    void removeBornOfFrom(MethodDesc methodDesc, String bornOf) {
        String[] values = methodDesc.getMetaValue(Globals.META_NAME_BORNOF);
        if (values != null) {
            methodDesc.removeMetaAnnotationDesc(Globals.META_NAME_BORNOF);

            List<String> valueList = new ArrayList<String>();
            for (String value : values) {
                if (!value.equals(bornOf)) {
                    valueList.add(value);
                }
            }
            values = valueList.toArray(new String[0]);
            if (values.length == 0) {
                removeMethodDesc(methodDesc);
            } else {
                methodDesc.setAnnotationDesc(new MetaAnnotationDescImpl(
                        Globals.META_NAME_BORNOF, values));
            }
        }
    }

    void removeBornOfFrom(PropertyDesc propertyDesc, String bornOf) {
        int mode = propertyDesc.getMode();
        boolean mayFieldBeRemoved = false;

        // Getter。

        String[] values = propertyDesc
                .getMetaValueOnGetter(Globals.META_NAME_BORNOF);
        if (values != null) {
            propertyDesc
                    .removeMetaAnnotationDescOnGetter(Globals.META_NAME_BORNOF);

            List<String> valueList = new ArrayList<String>();
            for (String value : values) {
                if (!value.equals(bornOf)) {
                    valueList.add(value);
                }
            }
            values = valueList.toArray(new String[0]);
            if (values.length == 0) {
                mode &= ~PropertyDesc.READ;
                propertyDesc.setMode(mode);
            } else {
                propertyDesc
                        .setAnnotationDescOnGetter(new MetaAnnotationDescImpl(
                                Globals.META_NAME_BORNOF, values));
            }
        }

        // Setter。

        values = propertyDesc.getMetaValueOnSetter(Globals.META_NAME_BORNOF);
        if (values != null) {
            propertyDesc
                    .removeMetaAnnotationDescOnSetter(Globals.META_NAME_BORNOF);

            List<String> valueList = new ArrayList<String>();
            for (String value : values) {
                if (!value.equals(bornOf)) {
                    valueList.add(value);
                }
            }
            values = valueList.toArray(new String[0]);
            if (values.length == 0) {
                mode &= ~PropertyDesc.WRITE;
                propertyDesc.setMode(mode);
            } else {
                propertyDesc
                        .setAnnotationDescOnSetter(new MetaAnnotationDescImpl(
                                Globals.META_NAME_BORNOF, values));
            }
        }

        // フィールド。

        values = propertyDesc.getMetaValue(Globals.META_NAME_BORNOF);
        if (values != null) {
            propertyDesc.removeMetaAnnotationDesc(Globals.META_NAME_BORNOF);

            List<String> valueList = new ArrayList<String>();
            for (String value : values) {
                if (!value.equals(bornOf)) {
                    valueList.add(value);
                }
            }
            values = valueList.toArray(new String[0]);
            if (values.length == 0) {
                mayFieldBeRemoved = true;
            } else {
                propertyDesc.setAnnotationDesc(new MetaAnnotationDescImpl(
                        Globals.META_NAME_BORNOF, values));
            }
        }

        if (mode == PropertyDesc.NONE && mayFieldBeRemoved) {
            removePropertyDesc(propertyDesc.getName());
        }
    }

    public boolean isEmpty() {
        return (propertyDescMap_.isEmpty() && methodDescMap_.isEmpty());
    }

    public void clear() {
        super.clear();
        parameter_ = null;
        propertyDescMap_.clear();
        methodDescMap_.clear();
    }

    public void setPropertyDescs(PropertyDesc[] propertyDescs) {
        propertyDescMap_.clear();
        for (PropertyDesc propertyDesc : propertyDescs) {
            setPropertyDesc(propertyDesc);
        }
    }

    public void setMethodDescs(MethodDesc[] methodDescs) {
        methodDescMap_.clear();
        for (MethodDesc methodDesc : methodDescs) {
            setMethodDesc(methodDesc);
        }
    }

    public String getPathOfClass() {
        return YmirContext.getYmir().getPathOfPageClass(getName());
    }

    public String getShortName() {
        return ClassUtils.getShortName(getName());
    }

    public String getNameBase() {
        return getNameBase(getName());
    }

    String getNameBase(String name) {
        name = ClassUtils.getShortName(name);
        return name
                .substring(0, name.length() - getType().getSuffix().length());
    }

    public String getPackageName() {
        return getPackageName(getName());
    }

    String getPackageName(String name) {
        int dot = name.lastIndexOf('.');
        if (dot < 0) {
            return "";
        } else {
            return name.substring(0, dot);
        }
    }

    public String getInstanceName() {
        return getInstanceName(getName());
    }

    String getInstanceName(String name) {
        String instanceName = uncapFirst(ClassUtils.getShorterName(name));
        if (instanceName.equals(name)) {
            instanceName += "Value";
        }
        return instanceName;
    }

    String uncapFirst(String string) {
        if (string == null || string.length() == 0) {
            return string;
        } else {
            return Character.toLowerCase(string.charAt(0))
                    + string.substring(1);
        }
    }

    public Map<String, Object> getOptionalSourceGeneratorParameter() {
        return parameter_;
    }

    public void setOptionalSourceGeneratorParameter(
            Map<String, Object> parameter) {
        parameter_ = parameter;
    }

    public String getBornOf() {
        return bornOf_;
    }

    public void setBornOf(String bornOf) {
        bornOf_ = bornOf;
    }
}
