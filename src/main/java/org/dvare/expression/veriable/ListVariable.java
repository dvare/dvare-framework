package org.dvare.expression.veriable;


import org.dvare.annotations.Type;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.*;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */

public class ListVariable extends VariableExpression<List<?>> {

    public ListVariable(String name, Class<? extends DataTypeExpression> type) {
        super(name, type);
    }

    public boolean isEmpty() {
        return getSize() < 1;
    }

    public Integer getSize() {
        return value != null ? value.size() : 0;
    }

    public Class<? extends DataTypeExpression> getListType() {

        if (getType() != null) {
            DataType dataType = getType().getAnnotation(Type.class).dataType();
            switch (dataType) {
                case IntegerType:
                    return IntegerListType.class;
                case FloatType:
                    return FloatListType.class;
                case StringType:
                    return StringListType.class;
                case BooleanType:
                    return BooleanListType.class;
                case DateType:
                    return DateListType.class;
                case DateTimeType:
                    return DateTimeListType.class;
                case SimpleDateType:
                    return SimpleDateListType.class;
                case PairType:
                    return PairListType.class;
                case TripleType:
                    return TripleListType.class;
            }
        }
        return ListType.class;
    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        if (instancesBinding != null) {
            Object instance = instancesBinding.getInstance(operandType);
            VariableType.setVariableValue(this, instance);
        }
        return LiteralType.getLiteralExpression(value, getListType());
    }

}
