package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.NamedExpression;
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

    String refrenceValueToken;
    String driveContexttToken;

    public ForEach() {
        super(OperationType.FOREACH);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {


        refrenceValueToken = tokens[pos - 1];
        pos = pos + 1;
        driveContexttToken = tokens[pos];
        pos = pos + 1;


        Expression valueExpression = null;

        if (stack.isEmpty()) {


            TokenType tokenType = findDataObject(refrenceValueToken, contexts);

            if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {

                TypeBinding typeBinding = contexts.getContext(tokenType.type);
                DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                valueExpression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);

            } else {

                TypeBinding typeBinding = contexts.getContext(refrenceValueToken);
                if (contexts.getContext(refrenceValueToken) != null) {
                    contexts.addContext(driveContexttToken, typeBinding);
                    this.leftOperand = new NamedExpression(refrenceValueToken);
                }
            }
        } else {
            valueExpression = stack.pop();
        }

/*
        if (valueExpression instanceof VariableExpression) {

            this.leftOperand = valueExpression;
            // thing not sure at this time
        }*/


        pos = findNextExpression(tokens, pos, stack, contexts);

        this.rightOperand = stack.pop();
        contexts.removeContext(driveContexttToken);


        logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());


        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;


        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                if (op.getClass().equals(EndForEach.class)) {
                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, stack, contexts);
                }
            } else {


                TokenType tokenType = findDataObject(token, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                    VariableExpression variableExpression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);
                    stack.add(variableExpression);

                } else {
                    LiteralExpression literalExpression = LiteralType.getLiteralExpression(token);
                    stack.add(literalExpression);
                }


            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {
        Object object = instancesBinding.getInstance(refrenceValueToken);
        if (object instanceof List) {
            List instances = (List) object;

            List<Boolean> results = new ArrayList<>();

            for (Object instance : instances) {
                instancesBinding.addInstance(driveContexttToken, instance);

                Object result = rightOperand.interpret(instancesBinding);
                results.add(toBoolean(result));


            }

            instancesBinding.removeInstance(refrenceValueToken);


            return results.stream().allMatch(Boolean::booleanValue);

        }

        return false;
    }

}