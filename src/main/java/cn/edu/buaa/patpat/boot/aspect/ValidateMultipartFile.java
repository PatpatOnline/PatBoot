package cn.edu.buaa.patpat.boot.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateMultipartFile {
    boolean allowNull() default false;

    boolean allowEmpty() default false;

    String[] extensions() default {};

    /**
     * Unit: MB
     */
    long maxSize() default 0;
}