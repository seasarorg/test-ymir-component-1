package org.seasar.ymir.conversation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Conversation {
    String name();

    String phase() default "";

    boolean alwaysCreateConversation() default true;

    String[] followAfter() default {};
}
