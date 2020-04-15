package org.dvare.annotations;

import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Operation {

    OperationType type();

    DataType[] dataTypes() default {DataType.IntegerType, DataType.FloatType, DataType.StringType, DataType.BooleanType,
            DataType.DateTimeType, DataType.DateType, DataType.SimpleDateType,
            DataType.RegexType, DataType.NullType, DataType.ListType, DataType.PairType};
}
