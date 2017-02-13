package org.dvare.expression.literal;


import org.dvare.expression.datatype.DateTimeType;

import java.util.Date;

public class DateTimeLiteral extends LiteralExpression<Date> {


    public DateTimeLiteral(Date value) {
        super(value, DateTimeType.class);
    }
}
