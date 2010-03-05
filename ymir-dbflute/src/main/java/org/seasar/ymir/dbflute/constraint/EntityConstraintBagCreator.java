package org.seasar.ymir.dbflute.constraint;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.seasar.dbflute.Entity;
import org.seasar.ymir.constraint.ConfirmationDecider;
import org.seasar.ymir.constraint.ConstraintBag;
import org.seasar.ymir.constraint.impl.AbstractConstraintBagCreator;
import org.seasar.ymir.dbflute.constraint.annotation.FittedOnDBType;

public class EntityConstraintBagCreator extends
        AbstractConstraintBagCreator<Entity> {
    @Override
    public Class<Entity> getTargetClass() {
        return Entity.class;
    }

    @Override
    protected void createFromClass(Class<Entity> beanClass,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags) {
        List<ConstraintBag<?>> list = new ArrayList<ConstraintBag<?>>();
        super.createFromClass(beanClass, decider, list);

        boolean specified = false;
        for (ConstraintBag<?> bag : list) {
            if (bag.getAnnotation().annotationType() == FittedOnDBType.class) {
                specified = true;
                break;
            }
        }
        if (!specified) {
            bags.add(new ConstraintBag<FittedOnDBType>(
                    (FittedOnDBTypeConstraint) getS2Container().getComponent(
                            FittedOnDBTypeConstraint.class),
                    new FittedOnDBType() {
                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return FittedOnDBType.class;
                        }

                        @Override
                        public String messageKey() {
                            return "";
                        }
                    }, beanClass, decider));
        }
    }
}
