package org.dvare.expression.literal;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.datatype.RegexType;
import org.dvare.expression.datatype.StringType;
import org.dvare.util.TrimString;

public abstract class LiteralExpression<T> extends Expression {

    protected T value;
    protected Class<? extends DataTypeExpression> type;

    LiteralExpression(T value, Class<? extends DataTypeExpression> type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return this.value;
    }

    public Class<? extends DataTypeExpression> getType() {
        return this.type;
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        return this;
    }


    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }

        if (type.equals(StringType.class) || type.equals(RegexType.class)) {
            return "'" + TrimString.trim(value.toString()) + "'";
        } else {
            return value.toString();
        }

    }

}