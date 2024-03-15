package org.dvare.expression.literal;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DateType;

import java.time.LocalDate;

public class DateLiteral extends LiteralExpression<LocalDate> {

    public DateLiteral(LocalDate value) {
        super(value, DateType.class);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
