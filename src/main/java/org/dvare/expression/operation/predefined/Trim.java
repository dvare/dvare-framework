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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muhammad Hammad
 * @since 2019-05-08
 */
@Operation(type = OperationType.TRIM)
public class Trim extends ChainOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Length.class);

    public Trim() {
        super(OperationType.TRIM);
    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {
            if (toDataType(literalExpression.getType()) == DataType.StringType) {
                Object value = literalExpression.getValue();
                if (value != null) {
                    return LiteralType.getLiteralExpression(value.toString().trim(), StringType.class);
                }
            }
        }
        return new NullLiteral<>();
    }


}