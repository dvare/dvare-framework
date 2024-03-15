package org.dvare.expression.literal;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DateTimeType;

import java.time.LocalDateTime;

public class DateTimeLiteral extends LiteralExpression<LocalDateTime> {


    public DateTimeLiteral(LocalDateTime value) {
        super(value, DateTimeType.class);
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
