package cn.edu.buaa.patpat.boot.annotations;

import cn.edu.buaa.patpat.boot.modules.account.dto.AuthLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestValidation {
    boolean checkParams() default true;

    AuthLevel authLevel() default AuthLevel.NONE;
}
