package org.seasar.ymir.conversation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeginSubConversation {
    // TODO 互換性のため残しているが、いつかなくす。
    String name() default "";

    String reenter();
}
