package org.dvare.expression.literal;


import org.dvare.expression.datatype.StringType;

public class StringLiteral extends LiteralExpression<String> {


    public StringLiteral(String value) {
        super(value, StringType.class);
    }
}
