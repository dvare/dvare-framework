package org.dvare.expression.veriable;


import org.dvare.expression.datatype.DateTimeType;

import java.time.LocalDateTime;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class DateTimeVariable extends VariableExpression<LocalDateTime> {

    public DateTimeVariable(String name) {
        this(name, null);
    }

    public DateTimeVariable(String name, LocalDateTime localDateTime) {
        super(name, DateTimeType.class, localDateTime);
    }


}
