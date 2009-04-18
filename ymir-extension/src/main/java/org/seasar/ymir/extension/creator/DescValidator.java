package org.seasar.ymir.extension.creator;

import java.util.Set;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.util.ClassUtils;

public class DescValidator {
    private DescValidator() {
    }

    public static Result validate(ClassDesc classDesc, ClassDescSet classDescSet) {
        return validate(classDesc, classDescSet, new Result());
    }

    static Result validate(ClassDesc classDesc, ClassDescSet classDescSet,
            Result result) {
        PropertyDesc[] pds = classDesc.getPropertyDescs();
        for (int i = 0; i < pds.length; i++) {
            validate(pds[i], classDescSet, result);
        }
        MethodDesc[] mds = classDesc.getMethodDescs();
        for (int i = 0; i < mds.length; i++) {
            validate(mds[i], classDescSet, result);
        }
        return result;
    }

    static Result validate(PropertyDesc propertyDesc,
            ClassDescSet classDescSet, Result result) {
        return validate(propertyDesc.getTypeDesc(), classDescSet, result);
    }

    static Result validate(MethodDesc methodDesc, ClassDescSet classDescSet,
            Result result) {
        validate(methodDesc.getReturnTypeDesc(), classDescSet, result);

        ParameterDesc[] parameterDescs = methodDesc.getParameterDescs();
        for (int i = 0; i < parameterDescs.length; i++) {
            validate(parameterDescs[i], classDescSet, result);
        }
        return result;
    }

    static Result validate(ParameterDesc parameterDesc,
            ClassDescSet classDescSet, Result result) {
        return validate(parameterDesc.getTypeDesc(), classDescSet, result);
    }

    public static Result validate(TypeDesc typeDesc, ClassDescSet classDescSet) {
        return validate(typeDesc, classDescSet, new Result());
    }

    static Result validate(TypeDesc typeDesc, ClassDescSet classDescSet,
            Result result) {
        String className = typeDesc.getComponentClassDesc().getName();
        if (!isValidClassName(className, classDescSet)) {
            result.addClassName(className);
        }
        return result;
    }

    public static boolean isValidClassName(String className,
            ClassDescSet classDescSet) {
        if (Void.TYPE.getName().equals(className)) {
            return true;
        } else if (ClassUtils.isPrimitive(className)) {
            return true;
        } else if (classDescSet != null && classDescSet.contains(className)) {
            return true;
        } else if (DescUtils.getClass(className) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static class Result {
        private Set<String> classNameSet_ = new TreeSet<String>();

        public void addClassName(String className) {
            classNameSet_.add(className);
        }

        public String[] getClassNames() {
            return classNameSet_.toArray(new String[0]);
        }

        public boolean isValid() {
            return classNameSet_.isEmpty();
        }
    }
}
