package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.StringType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TrimString;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.SUBSTRING, dataTypes = {DataType.StringType})
public class Substring extends ChainOperationExpression {


    public Substring() {
        super(OperationType.SUBSTRING);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = super.parse(tokens, pos, stack, contexts);

        if (rightOperand.size() != 2) {
            throw new ExpressionParseException(String.format("%s must contain two parameters at %d", getClass().getSimpleName(), pos));
        }


        return pos;
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null
                && rightOperand.size() >= 2) {

            String value = literalExpression.getValue().toString();
            value = TrimString.trim(value);


            LiteralExpression<?> indexExpression = (LiteralExpression<?>) rightOperand.get(0);
            LiteralExpression<?> countExpression = (LiteralExpression) rightOperand.get(1);

            Integer index;
            if (indexExpression.getValue() instanceof Integer) {
                index = (Integer) indexExpression.getValue();
            } else {
                index = Integer.parseInt(indexExpression.getValue().toString());
            }


            Integer count;
            if (countExpression.getValue() instanceof Integer) {
                count = (Integer) countExpression.getValue();
            } else {
                count = Integer.parseInt(countExpression.getValue().toString());
            }

            if (value.length() < count) {
                return new NullLiteral<>();
            }

            Integer start = index - 1;
            Integer end = index - 1 + count;

            if (start < 0 || end > value.length()) {
                return new NullLiteral<>();
            }


            try {
                value = value.substring(start, end);
            } catch (ArrayIndexOutOfBoundsException e) {
                value = value.substring(index, index + count);
            }
            return LiteralType.getLiteralExpression(value, StringType.class);
        }

        return new NullLiteral<>();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}