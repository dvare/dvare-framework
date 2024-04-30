package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.SEPARATOR)
public class ExpressionSeparator extends OperationExpression {
    public ExpressionSeparator() {
        super(OperationType.SEPARATOR);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        Expression left = null;
        if (stack.isEmpty()) {
            String leftString = tokens[pos - 1];
            OperationExpression.TokenType tokenType = OperationExpression.findDataObject(leftString, contexts);
            if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type))
                    != null) {
                TypeBinding typeBinding = contexts.getContext(tokenType.type);
                DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                left = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);
            }


        } else {
            left = stack.pop();
        }


        this.leftOperand = left;


        // right side
        pos = findNextExpression(tokens, pos + 1, stack, contexts);

        if (!stack.isEmpty()) {
            this.rightOperand = stack.pop();
        }

        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (; pos < tokens.length; pos++) {
            OperationExpression op = configurationRegistry.getOperation(tokens[pos]);
            if (op != null) {
                pos = op.parse(tokens, pos, stack, contexts);
                var peek = stack.peek();
                if (!(peek instanceof VariableExpression || peek instanceof DefOperation)) {
                    return pos;
                }


            }
        }
        return pos;
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        leftOperand.interpret(instancesBinding);
        return rightOperand.interpret(instancesBinding);

    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}