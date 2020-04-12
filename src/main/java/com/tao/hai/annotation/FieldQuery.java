package com.tao.hai.annotation;

import com.tao.hai.constants.SqlParamConstant;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FieldQuery {

    String value() default SqlParamConstant.EQUAL;
}
