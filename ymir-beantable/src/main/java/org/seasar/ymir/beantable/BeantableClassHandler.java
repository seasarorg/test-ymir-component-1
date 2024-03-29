package org.seasar.ymir.beantable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

public class BeantableClassHandler implements ClassHandler {
    private Log log_ = LogFactory.getLog(BeantableClassHandler.class);

    private BeantableManager manager_;

    @Binding(bindingType = BindingType.MUST, value = "beantableManager")
    public void setBeantableManager(BeantableManager manager) {
        manager_ = manager;
    }

    public void processClass(String packageName, String shortClassName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = getClass().getClassLoader();
        }
        String className = packageName + "." + shortClassName;
        try {
            manager_.enableBeantable(Class.forName(className, true, cl), false);
        } catch (ClassNotFoundException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("[SKIP] Class not found: " + className);
            }
            return;
        }
        //
        //        if (!manager_.isManaged(beanClass)) {
        //            return;
        //        }
        //
        //        if (logger_.isInfoEnabled()) {
        //            logger_.info("UPDATE TABLE FOR class: " + className);
        //        }
        //
        //        Beantable beanTable = manager_.newBeantable(beanClass);
        //        try {
        //            beanTable.activate();
        //        } catch (SQLException ex) {
        //            logger_.error("[SKIP] Can't activate Beantable for: " + className,
        //                    ex);
        //            return;
        //        }
        //        try {
        //            beanTable.update(false);
        //        } catch (SQLException ex) {
        //            logger_
        //                    .error("[SKIP] Can't update Beantable for: " + className,
        //                            ex);
        //            return;
        //        }
        //
        //        if (logger_.isInfoEnabled()) {
        //            logger_.info("TABLE UPDATED SUCCESSFULLY");
        //        }
    }
}
