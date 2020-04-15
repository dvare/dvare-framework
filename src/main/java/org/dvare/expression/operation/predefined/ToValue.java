package org.dvare.expression.operation.predefined;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.DataTypeMapping;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_VALUE)
public class ToValue extends ChainOperationExpression {


    public ToValue() {
        super(OperationType.TO_VALUE);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression literalExpression = super.interpretOperand(this.leftOperand, instancesBinding);
        if (literalExpression != null && literalExpression.getValue() != null) {

            Object pairValue = literalExpression.getValue();

            if (pairValue instanceof Pair) {
                Object value = Pair.class.cast(pairValue).getValue();
                if (value != null) {
                    try {
                        return LiteralType.getLiteralExpression(value, DataTypeMapping.getTypeMapping(value.getClass()));
                    } catch (IllegalValueException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }

        }

        return new NullLiteral<>();
    }

}