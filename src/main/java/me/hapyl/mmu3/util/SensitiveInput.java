package me.hapyl.mmu3.util;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that this class or method has sensitive input, and will throw exceptions if it isn't valid.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface SensitiveInput {

    /**
     * @return the rule input should match.
     */
    @Nonnull
    String rule();

    /**
     * @return the executions that will be thrown if input doesn't match the rule.
     */
    @Nonnull
    Class<? extends Exception>[] exception() default RuntimeException.class;

}
