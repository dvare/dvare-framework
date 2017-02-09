package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.FOREACH)
public class ForEach extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ForEach.class);


    public ForEach() {
        super(OperationType.FOREACH);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        Expression expression = stack.pop();


        this.leftOperand = expression;


        logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());


        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        Stack<Expression> localStack = new Stack<>();

        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

                if (op.getClass().equals(RightPriority.class)) {


                    List<Expression> expressions = new ArrayList<>(localStack);


                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, localStack, contexts);
                }
            } else {


                TokenType tokenType = findDataObject(token, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null && contexts.getContext(tokenType.type).getDataType(tokenType.token) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                    VariableExpression variableExpression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);
                    localStack.add(variableExpression);

                } else {
                    LiteralExpression literalExpression = LiteralType.getLiteralExpression(token);
                    localStack.add(literalExpression);
                }


            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }


}