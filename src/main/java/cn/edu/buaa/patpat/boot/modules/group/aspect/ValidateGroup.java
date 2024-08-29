/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Should only have one of the properties set to true.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateGroup {
    /**
     * If true, will ignore {@link #requireNotInGroup()}.
     */
    boolean requireInGroup() default false;

    boolean requireNotInGroup() default false;

    boolean requireOwner() default false;
}
