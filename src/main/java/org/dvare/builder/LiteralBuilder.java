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

    public LiteralBuilder(DataType type, Object value) {
        this.type = type;
        this.value = value;
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
        return LiteralType.getLiteralExpression(value, type);
    }
}
