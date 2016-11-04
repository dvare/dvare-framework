package org.dvare.builder;


import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;

public class LiteralBuilder {

    private Object value;

    private DataType type;

    public LiteralBuilder() {
    }

    public LiteralBuilder(Object value, DataType type) {
        this.value = value;
        this.type = type;
    }


    public LiteralBuilder literalValue(Object value) {
        this.value = value;
        return this;
    }

    public LiteralBuilder literalType(DataType type) {
        this.type = type;
        return this;
    }

    public LiteralExpression build() throws IllegalPropertyException, IllegalValueException {
        LiteralExpression literalExpression = LiteralType.getLiteralExpression(value.toString(), type);
        return literalExpression;
    }
}
