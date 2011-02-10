package org.seasar.ymir.constraint;

import static org.seasar.ymir.RequestProcessor.ATTR_NOTES;
import static org.seasar.ymir.constraint.ConstraintManager.CONSTRAINTBAGS_EMPTY;
import static org.seasar.ymir.constraint.Globals.APPKEY_CORE_CONSTRAINT_PERMISSIONDENIEDMETHOD_ENABLE;
import static org.seasar.ymir.constraint.Globals.APPKEY_CORE_CONSTRAINT_VALIDATIONFAILEDMETHOD_ENABLE;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Request;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.constraint.annotation.SuppressConstraints;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.ContainerUtils;

/**
 * 制約チェックを行なうためのInterceptorです。
 */
public class ConstraintInterceptor extends AbstractYmirProcessInterceptor {
    public static final String ACTION_VALIDATIONFAILED = "_validationFailed";

    public static final String ACTION_PERMISSIONDENIED = "_permissionDenied";

    static final Set<ConstraintType> EMPTY_SUPPRESSTYPESET = EnumSet
            .noneOf(ConstraintType.class);

    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private ApplicationManager applicationManager_;

    private ConstraintManager constraintManager_;

    private Map<Method, Set<ConstraintType>> suppressTypeSetMap_;

    private PageComponentVisitor<Action> visitorForFindingPermissionDeniedMethod_ = new VisitorForFindingPermissionDeniedMethod();

    private PageComponentVisitor<Action> visitorForFindingValidationFailedMethod_ = new VisitorForFindingValidationFailedMethod();

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        suppressTypeSetMap_ = cacheManager.newMap();
    }

    @Binding(bindingType = BindingType.MUST)
    public void setConstraintManager(ConstraintManager constraintManager) {
        constraintManager_ = constraintManager;
    }

    @Override
    public Action actionInvoking(Request request, Action action) {
        PageComponent pageComponent = request.getCurrentDispatch()
                .getPageComponent();

        Action finalAction = null;
        try {
            Notes notes = confirmConstraint(pageComponent, request);
            if (notes != null) {
                request.setAttribute(ATTR_NOTES, notes);

                if (isValidationFailedMethodEnabled()) {
                    // 互換性のため。

                    // バリデーションエラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
                    // メソッドが存在しなければ何もしない（元のアクションメソッドの呼び出しをスキップする）。
                    finalAction = pageComponent.accept(
                            visitorForFindingValidationFailedMethod_, notes);
                    if (finalAction == null) {
                        finalAction = actionManager_
                                .newVoidAction(pageComponent.getPage());
                    }
                } else {
                    throw new WrappingRuntimeException(
                            new ValidationFailedException(notes));
                }
            } else {
                finalAction = action;
            }
        } catch (PermissionDeniedException ex) {
            if (isPermissionDeniedMethodEnabled()) {
                // 互換性のため。

                // 権限エラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
                // メソッドが存在しなければPermissionDeniedExceptionを上に再スローする。
                finalAction = (Action) pageComponent.accept(
                        visitorForFindingPermissionDeniedMethod_, ex);
                if (finalAction == null) {
                    throw new WrappingRuntimeException(ex);
                }
            } else {
                throw new WrappingRuntimeException(ex);
            }
        }

        return finalAction;
    }

    boolean isValidationFailedMethodEnabled() {
        return PropertyUtils.valueOf(applicationManager_
                .findContextApplication().getProperty(
                        APPKEY_CORE_CONSTRAINT_VALIDATIONFAILEDMETHOD_ENABLE),
                true);
    }

    boolean isPermissionDeniedMethodEnabled() {
        return PropertyUtils.valueOf(applicationManager_
                .findContextApplication().getProperty(
                        APPKEY_CORE_CONSTRAINT_PERMISSIONDENIEDMETHOD_ENABLE),
                true);
    }

    Notes confirmConstraint(PageComponent pageComponent, Request request)
            throws PermissionDeniedException {
        Action action = request.getCurrentDispatch().getAction();
        MethodInvoker actionInvoker = null;
        Method actionMethod = null;
        if (action != null) {
            actionInvoker = action.getMethodInvoker();
            if (actionInvoker != null) {
                actionMethod = actionInvoker.getMethod();
            }
        }
        Set<ConstraintType> suppressTypeSet = getSuppressTypeSet(actionMethod);
        Notes notes = new Notes();

        // 共通の制約をチェックする。
        constraintManager_.confirmConstraint(
                getConstraintBagsFromConstraintBundles(), suppressTypeSet,
                pageComponent.getPage(), request, notes);

        // アクションに関連付けられている制約をチェックする。
        constraintManager_.confirmConstraint(constraintManager_
                .getConstraintBags(actionMethod, constraintManager_
                        .getAlwaysDecider()), suppressTypeSet, pageComponent
                .getPage(), request, notes);

        // Page固有の制約をチェックする。
        try {
            VisitorForConfirmingConstraint visitor = new VisitorForConfirmingConstraint(
                    request, suppressTypeSet, notes);
            pageComponent.accept(visitor);

            return visitor.getNotes();
        } catch (WrappingRuntimeException ex) {
            if (ex.getCause() instanceof PermissionDeniedException) {
                throw (PermissionDeniedException) ex.getCause();
            } else {
                throw ex;
            }
        }
    }

    Set<ConstraintType> getSuppressTypeSet(Method actionMethod) {
        if (actionMethod == null) {
            return EnumSet.noneOf(ConstraintType.class);
        }

        Set<ConstraintType> suppressTypeSet = suppressTypeSetMap_
                .get(actionMethod);
        if (suppressTypeSet == null) {
            suppressTypeSet = EnumSet.noneOf(ConstraintType.class);
            ConstraintType[] types = getSuppressConstraintsValue(actionMethod);
            if (types != null) {
                for (int i = 0; i < types.length; i++) {
                    suppressTypeSet.add(types[i]);
                }
            }
            suppressTypeSetMap_.put(actionMethod, suppressTypeSet);
        }
        return suppressTypeSet;
    }

    private ConstraintType[] getSuppressConstraintsValue(Method actionMethod) {
        SuppressConstraints suppress = annotationHandler_.getAnnotation(
                actionMethod, SuppressConstraints.class);
        if (suppress != null) {
            return suppress.value();
        }

        return null;
    }

    ConstraintBag<?>[] getConstraintBagsFromConstraintBundles() {
        Application application = applicationManager_.findContextApplication();
        ConstraintBag<?>[] bags = application
                .getRelatedObject(ConstraintBag[].class);
        if (bags == null) {
            ComponentDef[] bundleCds = ContainerUtils.findAllComponentDefs(
                    application.getS2Container(), ConstraintBundle.class);
            List<ConstraintBag<?>> list = new ArrayList<ConstraintBag<?>>();
            for (int i = 0; i < bundleCds.length; i++) {
                constraintManager_.getConstraintBags(bundleCds[i]
                        .getComponentClass(), (ConstraintBundle) bundleCds[i]
                        .getComponent(), list);
            }
            bags = list.toArray(CONSTRAINTBAGS_EMPTY);
            application.setRelatedObject(ConstraintBag[].class, bags);
        }
        return bags;
    }

    protected class VisitorForFindingValidationFailedMethod extends
            PageComponentVisitor<Action> {
        public Action process(PageComponent pageComponent, Object... parameters) {
            Object page = pageComponent.getPage();
            Class<?> pageClass = pageComponent.getPageClass();
            Method[] methods = ClassUtils.getMethods(pageClass,
                    ACTION_VALIDATIONFAILED);
            if (methods.length == 1) {
                return actionManager_.newAction(page, pageClass, methods[0],
                        new Object[] { parameters[0] });
            } else if (methods.length == 0) {
                return null;
            } else {
                throw new IllegalClientCodeRuntimeException("Method '"
                        + ACTION_VALIDATIONFAILED + "' must be single: class="
                        + pageClass + ", method=" + Arrays.asList(methods));
            }
        }
    }

    protected class VisitorForFindingPermissionDeniedMethod extends
            PageComponentVisitor<Action> {
        public Action process(PageComponent pageComponent, Object... parameters) {
            Object page = pageComponent.getPage();
            Class<?> pageClass = pageComponent.getPageClass();
            Method[] methods = ClassUtils.getMethods(pageClass,
                    ACTION_PERMISSIONDENIED);
            if (methods.length == 1) {
                return actionManager_.newAction(page, pageClass, methods[0],
                        new Object[] { parameters[0] });
            } else if (methods.length == 0) {
                return null;
            } else {
                throw new IllegalClientCodeRuntimeException("Method '"
                        + ACTION_PERMISSIONDENIED + "' must be single: class="
                        + pageClass + ", method=" + Arrays.asList(methods));
            }
        }
    }

    protected class VisitorForConfirmingConstraint extends
            PageComponentVisitor<Object> {
        private Request request_;

        private Set<ConstraintType> suppressTypeSet_;

        private Notes notes_;

        public VisitorForConfirmingConstraint(Request request,
                Set<ConstraintType> suppressTypeSet, Notes notes) {
            request_ = request;
            suppressTypeSet_ = suppressTypeSet;
            notes_ = notes;
        }

        public Object process(PageComponent pageComponent, Object... parameters) {
            Object page = pageComponent.getPage();
            try {
                constraintManager_.confirmConstraint(pageComponent
                        .getPageClass(), suppressTypeSet_, page, request_,
                        notes_);
            } catch (PermissionDeniedException ex) {
                throw new WrappingRuntimeException(ex);
            }

            return null;
        }

        public Notes getNotes() {
            if (!notes_.isEmpty()) {
                return notes_;
            } else {
                return null;
            }
        }
    }
}
