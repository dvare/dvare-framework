package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.DEF)
public class DefOperation extends OperationExpression {


    public DefOperation() {
        super(OperationType.DEF);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, contexts);

        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        String token = tokens[pos];


        String[] parts = token.split(":");
        if (parts.length == 2) {

            String name = parts[0].trim();
            String type = parts[1].trim();
            DataType dataType = DataType.valueOf(type);

            TokenType tokenType = buildTokenType(name);

            if (contexts.getContext(tokenType.type) != null) {
                TypeBinding typeBinding = contexts.getContext(tokenType.type);
                if (typeBinding.getDataType(tokenType.token) == null) {
                    typeBinding.addTypes(tokenType.token, dataType);
                    contexts.addContext(tokenType.type, typeBinding);
                } else {
                    throw new ExpressionParseException(tokenType.type + " context already contains \"" + tokenType.token + "\" variable ");
                }

            } else {
                TypeBinding typeBinding = new TypeBinding();
                typeBinding.addTypes(tokenType.token, dataType);
                contexts.addContext(tokenType.type, typeBinding);
            }


            VariableExpression<?> variableExpression = VariableType.getVariableExpression(tokenType.token, dataType, tokenType.type);
            stack.push(variableExpression);

        } else {
            throw new ExpressionParseException("\"" + token + "\" DataType is missing");
        }


        return pos;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}