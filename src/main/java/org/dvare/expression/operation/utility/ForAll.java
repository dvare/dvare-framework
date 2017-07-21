package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.FORALL)
public class ForAll extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ForAll.class);

    protected NamedExpression referenceContext;
    protected NamedExpression derivedContext;


    public ForAll() {
        super(OperationType.FORALL);
    }

    public ForAll(OperationType operationType) {
        super(operationType);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contextsBinding) throws ExpressionParseException {


        pos++;


        if (pos + 2 < tokens.length && tokens[pos + 2].equals("|")) {

            referenceContext = new NamedExpression(tokens[pos++]);
            derivedContext = new NamedExpression(tokens[pos++]);

        } else if (pos + 1 < tokens.length && tokens[pos + 1].equals("|")) {
            referenceContext = new NamedExpression("self");
            derivedContext = new NamedExpression(tokens[pos]);

        }
        pos++;


        Expression valueExpression = null;

        if (stack.isEmpty()) {


            TokenType tokenType = findDataObject(referenceContext.getName(), contextsBinding);

            if (tokenType.type != null && contextsBinding.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contextsBinding.getContext(tokenType.type)) != null) {
/*
                TypeBinding typeBinding = contexts.getContext(tokenType.type);
                DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                valueExpression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);*/

            } else {

                TypeBinding typeBinding = contextsBinding.getContext(referenceContext.getName());
                if (contextsBinding.getContext(referenceContext.getName()) != null) {
                    contextsBinding.addContext(derivedContext.getName(), typeBinding);
                    this.leftOperand = new NamedExpression(referenceContext.getName());
                }
            }
        } else {
            //valueExpression = stack.pop();
        }

        /*if (valueExpression instanceof ListOperationExpression) {

            ListOperationExpression listOperationExpression = (ListOperationExpression) valueExpression;

            Expression leftExpression = listOperationExpression.getLeftOperand();

            while (leftExpression instanceof OperationExpression) {
                leftExpression = ((OperationExpression) leftExpression).getLeftOperand();
            }

            if (leftExpression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) leftExpression;
                this.leftOperand = valueExpression;
                DataType dataType = toDataType(variableExpression.getType());

                TokenType tokenType = buildTokenType(driveContexttToken);

                if (contexts.getContext(tokenType.type) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    typeBinding.addTypes(tokenType.token, dataType);
                    contexts.addContext(tokenType.type, typeBinding);
                } else {
                    TypeBinding typeBinding = new TypeBinding();
                    typeBinding.addTypes(tokenType.token, dataType);
                    contexts.addContext(tokenType.type, typeBinding);
                }
            }
        }*/


        pos = findNextExpression(tokens, pos, stack, expressionBinding, contextsBinding);

        this.leftOperand = stack.pop();
        contextsBinding.removeContext(derivedContext.getName());

        if (logger.isDebugEnabled()) {
            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());

        }
        stack.push(this);


        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;


        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                if (op.getClass().equals(EndForAll.class) || op.getClass().equals(EndForEach.class)) {
                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, stack, expressionBinding, contexts);
                }
            } else {


                stack.add(buildExpression(token, contexts));

            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {


        Object object = instancesBinding.getInstance(referenceContext.getName());
        if (object instanceof List) {
            List instances = (List) object;

            List<Boolean> results = new ArrayList<>();

            for (Object instance : instances) {
                instancesBinding.addInstance(derivedContext.getName(), instance);

                Object result = leftOperand.interpret(expressionBinding, instancesBinding);
                results.add(toBoolean(result));

            }

            instancesBinding.removeInstance(derivedContext.getName());
            Boolean result = results.stream().allMatch(Boolean::booleanValue);
            return LiteralType.getLiteralExpression(result, BooleanType.class);

        }
         /*else if (leftOperand instanceof ListOperationExpression) {

            OperationExpression valuesOperation = (OperationExpression) leftOperand;
            Object valuesResult = valuesOperation.interpret(expressionBinding, instancesBinding);
            if (valuesResult instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) valuesResult;
                dataTypeExpression = listLiteral.getType();

                List values = listLiteral.getValue();

                for (Object value : values) {


                }


            }

        }*/
        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }


    @Override
    public String toString() {

        StringBuilder toStringBuilder = new StringBuilder();

        toStringBuilder.append(this.getSymbols().get(0));


        toStringBuilder.append(" ");
        toStringBuilder.append(referenceContext.toString());

        toStringBuilder.append(" ");
        toStringBuilder.append(derivedContext.toString());


        toStringBuilder.append(" ");
        toStringBuilder.append("|");
        toStringBuilder.append(" ");

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" ");
        }

        return toStringBuilder.toString();


    }
}