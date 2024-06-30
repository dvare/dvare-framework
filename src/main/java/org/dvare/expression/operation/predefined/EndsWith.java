package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.datatype.DataType;
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
@Operation(type = OperationType.ENDS_WITH, dataTypes = {DataType.StringType})
public class EndsWith extends ChainOperationExpression {
    public EndsWith() {
        super(OperationType.ENDS_WITH);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {


            String value = literalExpression.getValue().toString();
            value = TrimString.trim(value);

            LiteralExpression<?> endswithExpression = super.interpretOperand(rightOperand.get(0), instancesBinding);

            String endswith = endswithExpression.getValue().toString();

            endswith = TrimString.trim(endswith);

            Boolean result = value.endsWith(endswith);
            return LiteralType.getLiteralExpression(result, BooleanType.class);

        }

        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}