package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.DataTypeMapping;
import org.dvare.util.Pair;
import org.dvare.util.Triple;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_KEY)
public class ToKey extends ChainOperationExpression {


    public ToKey() {
        super(OperationType.TO_KEY);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {
            if (literalExpression.getValue() != null) {

                Object tupleValue = literalExpression.getValue();

                if (tupleValue instanceof Pair) {
                    Object key = ((Pair<?, ?>) tupleValue).getKey();
                    if (key != null) {
                        try {
                            return LiteralType.getLiteralExpression(key, DataTypeMapping.getTypeMapping(key.getClass()));
                        } catch (IllegalValueException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                } else if (tupleValue instanceof Triple) {
                    Object value = ((Triple<?, ?, ?>) tupleValue).getLeft();
                    if (value != null) {
                        try {
                            return LiteralType.getLiteralExpression(value, DataTypeMapping.getTypeMapping(value.getClass()));
                        } catch (IllegalValueException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }

        return new NullLiteral<>();
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}
