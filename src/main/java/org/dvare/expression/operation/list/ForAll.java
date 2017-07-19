package org.dvare.expression.operation.list;

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
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.utility.LeftPriority;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.FORALL)
public class ForAll extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ForAll.class);

    private String refrenceValueToken;
    private String driveContexttToken;

    public ForAll() {
        super(OperationType.FORALL);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {


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

        if (valueExpression instanceof ListOperationExpression) {

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
        }


        pos = findNextExpression(tokens, pos, stack, expressionBinding, contexts);

        this.rightOperand = stack.pop();
        contexts.removeContext(driveContexttToken);

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
                if (op.getClass().equals(EndForAllEach.class)) {
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

        if (leftOperand instanceof NamedExpression) {
            Object object = instancesBinding.getInstance(refrenceValueToken);
            if (object instanceof List) {
                List instances = (List) object;

                List<Boolean> results = new ArrayList<>();

                for (Object instance : instances) {
                    instancesBinding.addInstance(driveContexttToken, instance);

                    Object result = rightOperand.interpret(expressionBinding, instancesBinding);
                    results.add(toBoolean(result));


                }

                instancesBinding.removeInstance(refrenceValueToken);
                Boolean result = results.stream().allMatch(Boolean::booleanValue);
                return LiteralType.getLiteralExpression(result, BooleanType.class);

            }
        } else if (leftOperand instanceof ListOperationExpression) {

            OperationExpression valuesOperation = (OperationExpression) leftOperand;
            Object valuesResult = valuesOperation.interpret(expressionBinding, instancesBinding);
            if (valuesResult instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) valuesResult;
                dataTypeExpression = listLiteral.getType();

                List values = listLiteral.getValue();

                for (Object value : values) {


                }


            }

        }
        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }

}