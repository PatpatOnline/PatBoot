package cn.edu.buaa.patpat.boot.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePagination {
    int basePage() default 1;

    int maxPage() default 100;

    int minPageSize() default 1;

    int maxPageSize() default 100;
}
