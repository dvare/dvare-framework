package org.dvare.expression.literal;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.RegexType;

public class RegexLiteral extends LiteralExpression<String> {

    public RegexLiteral(String value) {
        super(value, RegexType.class);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
