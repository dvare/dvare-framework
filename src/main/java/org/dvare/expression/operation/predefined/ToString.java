package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.StringType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;

import java.time.LocalDate;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_STRING)
public class ToString extends ChainOperationExpression {


    public ToString() {
        super(OperationType.TO_STRING);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {
            Object value = literalExpression.getValue();
            String valueString;

            if (value instanceof LocalDate) {
                LocalDate localDate = (LocalDate) value;
                valueString = LiteralType.dateFormat.format(localDate);
            } else {
                valueString = value.toString();
            }

            if (valueString != null) {
                return LiteralType.getLiteralExpression(valueString, StringType.class);
            }


        }

        return new NullLiteral<>();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}