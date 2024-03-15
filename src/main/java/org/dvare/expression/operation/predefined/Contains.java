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
@Operation(type = OperationType.CONTAINS, dataTypes = {DataType.StringType})
public class Contains extends ChainOperationExpression {
    public Contains() {
        super(OperationType.CONTAINS);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {

            String value = literalExpression.getValue().toString();
            value = TrimString.trim(value);


            LiteralExpression<?> containsExpression = super.interpretOperand(rightOperand.get(0), instancesBinding);

            String contains = containsExpression.getValue().toString();

            contains = TrimString.trim(contains);

            Boolean result = value.contains(contains);
            return LiteralType.getLiteralExpression(result, BooleanType.class);


        }

        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}