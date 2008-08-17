package org.seasar.ymir.extension.creator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassDescBag {

    private Map<ClassType, Map<String, ClassDesc>> map_ = new HashMap<ClassType, Map<String, ClassDesc>>();

    private Map<ClassType, Map<String, ClassDesc>> createdMap_ = new HashMap<ClassType, Map<String, ClassDesc>>();

    private Map<ClassType, Map<String, ClassDesc>> updatedMap_ = new HashMap<ClassType, Map<String, ClassDesc>>();

    private Map<ClassType, Map<String, ClassDesc>> failedMap_ = new HashMap<ClassType, Map<String, ClassDesc>>();

    private ClassDescSet set_ = new ClassDescSet();

    private Map<String, String[]> failedClassDescLackingClassNamesMap_ = new HashMap<String, String[]>();

    public Map<String, ClassDesc> getClassDescMap() {
        return getClassDescMap(map_, null);
    }

    public ClassDesc[] getClassDescs() {
        return getClassDescs(map_, null);
    }

    public Map<String, ClassDesc> getClassDescMap(ClassType type) {
        return getClassDescMap(map_, type);
    }

    public ClassDesc[] getClassDescs(ClassType type) {
        return getClassDescMap(type).values().toArray(new ClassDesc[0]);
    }

    public Iterator<ClassType> getClassDescTypeIterator() {
        return map_.keySet().iterator();
    }

    public Map<String, ClassDesc> getCreatedClassDescMap() {
        return getClassDescMap(createdMap_, null);
    }

    public ClassDesc[] getCreatedClassDescs() {
        return getCreatedClassDescMap().values().toArray(new ClassDesc[0]);
    }

    public Map<String, ClassDesc> getCreatedClassDescMap(ClassType type) {
        return getClassDescMap(createdMap_, type);
    }

    public ClassDesc[] getCreatedClassDescs(ClassType type) {
        return getCreatedClassDescMap(type).values().toArray(new ClassDesc[0]);
    }

    public Map<String, ClassDesc> getUpdatedClassDescMap() {
        return getClassDescMap(updatedMap_, null);
    }

    public ClassDesc[] getUpdatedClassDescs() {
        return getUpdatedClassDescMap().values().toArray(new ClassDesc[0]);
    }

    public Map<String, ClassDesc> getUpdatedClassDescMap(ClassType type) {
        return getClassDescMap(updatedMap_, type);
    }

    public ClassDesc[] getUpdatedClassDescs(ClassType type) {
        return getUpdatedClassDescMap(type).values().toArray(new ClassDesc[0]);
    }

    public Map<String, ClassDesc> getFailedClassDescMap() {
        return getClassDescMap(failedMap_, null);
    }

    public ClassDesc[] getFailedClassDescs() {
        return getFailedClassDescMap().values().toArray(new ClassDesc[0]);
    }

    public Map<String, ClassDesc> getFailedClassDescMap(ClassType type) {
        return getClassDescMap(failedMap_, type);
    }

    public ClassDesc[] getFailedClassDescs(ClassType type) {
        return getFailedClassDescMap(type).values().toArray(new ClassDesc[0]);
    }

    public Map<String, String[]> getFailedClassDescLackingClassNamesMap() {
        return failedClassDescLackingClassNamesMap_;
    }

    public String[] getFailedClassDescLackingClassNames(String className) {
        String[] classNames = failedClassDescLackingClassNamesMap_
                .get(className);
        if (classNames != null) {
            return classNames;
        } else {
            return new String[0];
        }
    }

    Map<String, ClassDesc> getClassDescMap(
            Map<ClassType, Map<String, ClassDesc>> map, ClassType type) {
        Map<String, ClassDesc> got = map.get(type);
        if (got == null) {
            got = new LinkedHashMap<String, ClassDesc>();
            map.put(type, got);
        }
        return got;
    }

    ClassDesc[] getClassDescs(Map<ClassType, Map<String, ClassDesc>> map,
            ClassType type) {
        return getClassDescMap(map, type).values().toArray(new ClassDesc[0]);
    }

    public ClassDescSet getClassDescSet() {
        return set_;
    }

    public void addAsCreated(ClassDesc classDesc) {
        addAsCreated(classDesc, false);
    }

    public void addAsCreated(ClassDesc classDesc, boolean done) {
        if (!done) {
            getClassDescMap().put(classDesc.getName(), classDesc);
            getClassDescMap(classDesc.getType()).put(classDesc.getName(),
                    classDesc);
            set_.add(classDesc);
        }
        getCreatedClassDescMap().put(classDesc.getName(), classDesc);
        getCreatedClassDescMap(classDesc.getType()).put(classDesc.getName(),
                classDesc);
    }

    public void addAsUpdated(ClassDesc classDesc) {
        addAsUpdated(classDesc, false);
    }

    public void addAsUpdated(ClassDesc classDesc, boolean done) {
        if (!done) {
            getClassDescMap().put(classDesc.getName(), classDesc);
            getClassDescMap(classDesc.getType()).put(classDesc.getName(),
                    classDesc);
            set_.add(classDesc);
        }
        getUpdatedClassDescMap().put(classDesc.getName(), classDesc);
        getUpdatedClassDescMap(classDesc.getType()).put(classDesc.getName(),
                classDesc);
    }

    public void addAsFailed(ClassDesc classDesc, String[] lackingClassNames) {
        addAsFailed(classDesc, lackingClassNames, false);
    }

    public void addAsFailed(ClassDesc classDesc, String[] lackingClassNames,
            boolean done) {
        if (!done) {
            getClassDescMap().put(classDesc.getName(), classDesc);
            getClassDescMap(classDesc.getType()).put(classDesc.getName(),
                    classDesc);
        }
        getFailedClassDescMap().put(classDesc.getName(), classDesc);
        getFailedClassDescMap(classDesc.getType()).put(classDesc.getName(),
                classDesc);
        getFailedClassDescLackingClassNamesMap().put(classDesc.getName(),
                lackingClassNames);
    }

    public void remove(String className) {
        remove(map_, className);
        remove(createdMap_, className);
        remove(updatedMap_, className);
        set_.remove(className);
    }

    void remove(Map<ClassType, Map<String, ClassDesc>> map, String className) {
        for (Iterator<Map<String, ClassDesc>> itr = map.values().iterator(); itr
                .hasNext();) {
            Map<String, ClassDesc> typeMap = itr.next();
            typeMap.remove(className);
        }
    }

    public boolean isEmpty() {
        return map_.isEmpty();
    }
}
