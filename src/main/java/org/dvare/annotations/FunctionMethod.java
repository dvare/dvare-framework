package org.dvare.annotations;

import org.dvare.expression.datatype.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface FunctionMethod {

    DataType[] parameters();

    DataType returnType();

    boolean list() default false;

}
