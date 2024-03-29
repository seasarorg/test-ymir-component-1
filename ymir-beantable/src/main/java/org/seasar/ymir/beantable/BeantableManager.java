package org.seasar.ymir.beantable;

import static org.seasar.ymir.beantable.Globals.APPKEY_BEANTABLE_ENABLE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.beantable.Beantable;
import org.seasar.cms.pluggable.ClassTraverser;
import org.seasar.cms.pluggable.hotdeploy.DistributedHotdeployBehavior;
import org.seasar.cms.pluggable.hotdeploy.HotdeployListener;
import org.seasar.cms.pluggable.util.PluggableUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.beantable.annotation.Managed;

public class BeantableManager implements LifecycleListener, HotdeployListener {
    private S2Container container_;

    private ApplicationManager applicationManager_;

    private AnnotationHandler annotationHandler_;

    private ClassHandler beantableClassHandler_;

    private ClassTraverserBag[] traverserBags_;

    private Log log_ = LogFactory.getLog(BeantableManager.class);

    @Binding(bindingType = BindingType.MUST)
    public void setContainer(S2Container container) {
        container_ = container;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST, value = "beantableClassHandler")
    public void setBeantableClassHandler(
            BeantableClassHandler beantableClassHandler) {
        beantableClassHandler_ = beantableClassHandler;
    }

    /*
     * LifecycleListener
     */

    public void init() {
        List<ClassTraverserBag> bagList = new ArrayList<ClassTraverserBag>();

        Application[] applications = applicationManager_.getApplications();
        for (int i = 0; i < applications.length; i++) {
            if (isEnabled(applications[i])) {
                ClassTraverserBag bag = newClassTraverserBag(applications[i]);
                bagList.add(bag);
            }
        }
        traverserBags_ = bagList.toArray(new ClassTraverserBag[0]);

        traserse();
    }

    void traserse() {
        DistributedHotdeployBehavior behavior = (DistributedHotdeployBehavior) S2ContainerBehavior
                .getProvider();
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        behavior.start();
        try {
            Thread.currentThread().setContextClassLoader(
                    PluggableUtils.adjustClassLoader(behavior, classLoader));

            for (int i = 0; i < traverserBags_.length; i++) {
                traverserBags_[i].getClassTraverser().traverse();
            }
        } finally {
            Thread.currentThread().setContextClassLoader(classLoader);
            behavior.stop();
        }
    }

    ClassTraverserBag newClassTraverserBag(Application application) {
        ClassTraverser traverser = new ClassTraverser();
        traverser.setClassHandler(beantableClassHandler_);
        String targetPackageName = application.getFirstRootPackageName()
                + ".dao";
        traverser.addClassPattern(targetPackageName, ".*");
        traverser.addIgnoreClassPattern(targetPackageName,
                "(Abstract)?.*(Base|Dao)");
        for (Class<?> referenceClass : application.getReferenceClasses()) {
            traverser.addReferenceClass(referenceClass);
        }
        return new ClassTraverserBag(traverser, application);
    }

    boolean isEnabled(Application application) {
        return ("true".equals(application.getProperty(APPKEY_BEANTABLE_ENABLE)) && application
                .getReferenceClasses().length > 0);
    }

    public void enableBeantable(Class<?> clazz, boolean correctTableSchema) {
        ClassTraverserBag bag = getClassTraverserBag(clazz);
        if (bag == null) {
            return;
        }

        if (!isEnabled(bag.getApplication())) {
            return;
        }
        if (!isBeanClass(clazz, bag.getClassTraverser())) {
            return;
        }
        if (!isManaged(clazz)) {
            return;
        }

        Beantable<?> beantable = newBeantable(clazz);
        try {
            beantable.activate();
        } catch (SQLException ex) {
            log_.error("Can't activate table: " + clazz.getName(), ex);
        }
        try {
            beantable.update(correctTableSchema);
        } catch (SQLException ex) {
            log_.error("Can't update table: " + clazz.getName(), ex);
        }
    }

    public void destroy() {
    }

    /*
     * HotdeployListener
     */

    @SuppressWarnings("unchecked")
    public void definedClass(Class clazz) {
        enableBeantable(clazz, true);
    }

    ClassTraverserBag getClassTraverserBag(Class<?> clazz) {
        if (traverserBags_ != null) {
            for (int i = 0; i < traverserBags_.length; i++) {
                if (traverserBags_[i].getApplication().isCapable(clazz)) {
                    return traverserBags_[i];
                }
            }
        }
        return null;
    }

    boolean isBeanClass(Class<?> clazz, ClassTraverser traverser) {
        String packageName;
        String shortClassName;
        String className = clazz.getName();
        int dot = className.lastIndexOf('.');
        if (dot < 0) {
            packageName = "";
            shortClassName = className;
        } else {
            packageName = className.substring(0, dot);
            shortClassName = className.substring(dot + 1);
        }
        return traverser.isMatched(packageName, shortClassName);
    }

    @SuppressWarnings("unchecked")
    Beantable newBeantable(Class beanClass) {
        Beantable beanTable = (Beantable) container_
                .getComponent(Beantable.class);
        beanTable.setBeanClass(beanClass);
        return beanTable;
    }

    boolean isManaged(Class<?> beanClass) {
        Managed managed = annotationHandler_.getAnnotation(beanClass,
                Managed.class);
        return (managed != null && managed.value());
    }
}
