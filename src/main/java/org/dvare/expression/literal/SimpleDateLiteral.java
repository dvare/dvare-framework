package org.dvare.expression.literal;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.SimpleDateType;

import java.util.Date;

public class SimpleDateLiteral extends LiteralExpression<Date> {

    public SimpleDateLiteral(Date value) {
        super(value, SimpleDateType.class);
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
