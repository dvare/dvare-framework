package org.dvare.expression.literal;


import org.dvare.expression.datatype.DateType;

import java.util.Date;

public class DateLiteral extends LiteralExpression<Date> {

    public DateLiteral(Date value) {
        super(value, DateType.class);
    }
}
