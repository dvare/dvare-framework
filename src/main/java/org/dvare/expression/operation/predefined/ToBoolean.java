package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_Boolean)
public class ToBoolean extends ChainOperationExpression {


    public ToBoolean() {
        super(OperationType.TO_Boolean);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(this.leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {


            Object value = literalExpression.getValue();
            boolean boolValue;

            if (value instanceof Boolean) {
                boolValue = (Boolean) value;
            } else {
                String valueString = value.toString();
                boolValue = Boolean.parseBoolean(valueString);
            }

            return LiteralType.getLiteralExpression(boolValue, BooleanType.class);

        }

        return new NullLiteral<>();
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}