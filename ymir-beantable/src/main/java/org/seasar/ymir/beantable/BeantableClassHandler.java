package org.seasar.ymir.beantable;

import java.sql.SQLException;

import org.seasar.cms.beantable.Beantable;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

public class BeantableClassHandler implements ClassHandler {
    private Logger logger_ = Logger.getLogger(getClass());

    private BeantableManager manager_;

    @Binding(bindingType = BindingType.MUST, value = "beantableManager")
    public void setBeantableManager(BeantableManager manager) {
        manager_ = manager;
    }

    public void processClass(String packageName, String shortClassName) {
        Class beanClass;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = getClass().getClassLoader();
        }
        String className = packageName + "." + shortClassName;
        try {
            beanClass = Class.forName(className, true, cl);
        } catch (ClassNotFoundException ex) {
            if (logger_.isDebugEnabled()) {
                logger_.debug("[SKIP] Class not found: " + className);
            }
            return;
        }

        if (!manager_.isManaged(beanClass)) {
            return;
        }

        if (logger_.isInfoEnabled()) {
            logger_.info("UPDATE TABLE FOR class: " + className);
        }

        Beantable beanTable = manager_.newBeantable(beanClass);
        try {
            beanTable.activate();
        } catch (SQLException ex) {
            logger_.error("[SKIP] Can't activate Beantable for: " + className,
                    ex);
            return;
        }
        try {
            beanTable.update(false);
        } catch (SQLException ex) {
            logger_
                    .error("[SKIP] Can't update Beantable for: " + className,
                            ex);
            return;
        }

        if (logger_.isInfoEnabled()) {
            logger_.info("TABLE UPDATED SUCCESSFULLY");
        }
    }
}
