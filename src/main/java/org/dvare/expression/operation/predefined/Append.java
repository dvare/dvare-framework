package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.StringType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TrimString;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.APPEND, dataTypes = {DataType.StringType})
public class Append extends ChainOperationExpression {
    public Append() {
        super(OperationType.APPEND);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {

            String value = literalExpression.getValue().toString();
            value = TrimString.trim(value);

            LiteralExpression<?> appendExpression = super.interpretOperand(rightOperand.get(0), instancesBinding);

            String append = appendExpression.getValue().toString();

            append = TrimString.trim(append);

            value = value.concat(append);

            return LiteralType.getLiteralExpression(value, StringType.class);

        }

        return new NullLiteral<>();
    }


}