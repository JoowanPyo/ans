package com.gemiso.zodiac.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SoftDelete {
    String deletedAtField() default "deleted_at";
}
