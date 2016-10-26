package org.dvare.expression.operation;

import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralDataType;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public abstract class AssignOperationExpression extends AggregationOperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(AssignOperationExpression.class);

    public AssignOperationExpression(OperationType operationType) {
        super(operationType);
    }


    protected int parseOperands(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {

        String leftString = tokens[pos - 1];
        String rightString = tokens[pos + 1];

        DataType variableType = null;

        if (aTypeBinding.getTypes().containsKey(leftString)) {
            variableType = TypeFinder.findType(leftString, aTypeBinding);
        }

        Expression left = null;
        if (stack.isEmpty()) {

            if (variableType == null) {
                if (aTypeBinding.getTypes().containsKey(rightString)) {
                    variableType = TypeFinder.findType(rightString, aTypeBinding);
                } else {
                    variableType = LiteralDataType.computeDataType(rightString);
                }
            }

            if (aTypeBinding.getTypes().containsKey(leftString)) {
                left = VariableType.getVariableType(leftString, variableType);
            } else {
                left = LiteralType.getLiteralExpression(leftString, variableType);
            }

        } else {
            left = stack.pop();
        }


        this.leftOperand = left;

        pos = findNextExpression(tokens, pos + 1, stack, aTypeBinding, vTypeBinding);

        if (!stack.isEmpty()) {
            Expression right = stack.pop();
            this.rightOperand = right;
        }

        return pos;
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {
        pos = parseOperands(tokens, pos, stack, aTypeBinding, vTypeBinding);
        stack.push(this);
        return pos;
    }


}