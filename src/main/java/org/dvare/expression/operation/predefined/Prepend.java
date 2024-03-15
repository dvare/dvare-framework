package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
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
@Operation(type = OperationType.PREPEND, dataTypes = {DataType.StringType})
public class Prepend extends ChainOperationExpression {
    public Prepend() {
        super(OperationType.PREPEND);
    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {

            String value = literalExpression.getValue().toString();
            value = TrimString.trim(value);


            LiteralExpression<?> startExpression = super.interpretOperand(rightOperand.get(0), instancesBinding);

            String prepend = startExpression.getValue().toString();

            prepend = TrimString.trim(prepend);
            if (prepend != null && !prepend.isEmpty()) {
                value = prepend + value;
            }
            return LiteralType.getLiteralExpression(value, StringType.class);

        }

        return new NullLiteral<>();
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}