package org.dvare.expression.veriable;


import org.dvare.expression.datatype.DateType;

import java.time.LocalDate;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class DateVariable extends VariableExpression<LocalDate> {


    public DateVariable(String name) {
        this(name, null);

    }

    public DateVariable(String name, LocalDate localDate) {
        super(name, DateType.class, localDate);

    }


}
