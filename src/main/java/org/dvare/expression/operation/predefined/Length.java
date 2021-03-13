package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.LENGTH)
public class Length extends ChainOperationExpression {

    public Length() {
        super(OperationType.LENGTH);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {
            if (toDataType(literalExpression.getType()) == DataType.StringType) {
                Object value = literalExpression.getValue();
                if (value != null) {
                    return LiteralType.getLiteralExpression(value.toString().length(), IntegerType.class);
                }
            }

        }

        return new NullLiteral<>();
    }


}