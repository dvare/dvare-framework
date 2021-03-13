package org.dvare.expression.operation;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.list.PairOperation;
import org.dvare.expression.operation.list.TripleOperation;
import org.dvare.expression.operation.utility.ExpressionSeparator;
import org.dvare.expression.operation.utility.Function;
import org.dvare.expression.operation.utility.LeftPriority;
import org.dvare.expression.operation.utility.RightPriority;
import org.dvare.expression.veriable.ListVariable;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class AggregationOperationExpression extends OperationExpression {
    protected static final Logger logger = LoggerFactory.getLogger(AggregationOperationExpression.class);
    protected List<Expression> rightOperand = new ArrayList<>();
    protected LiteralExpression<?> leftExpression;

    public AggregationOperationExpression(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {


        String token = tokens[pos - 1];
        //pos = pos + 1;

        if (stack.isEmpty() || stack.peek() instanceof AssignOperationExpression) {
            ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
            OperationExpression operationExpression = configurationRegistry.getOperation(token);

            if (operationExpression != null) {
                if (operationExpression instanceof ListLiteralOperationExpression) {
                    pos = operationExpression.parse(tokens, pos, stack, contexts);
                    this.leftOperand = stack.pop();
                }
            } else {
                TokenType tokenType = findDataObject(token, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                    this.leftOperand = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);
                } else {
                    throw new ExpressionParseException("Left Operand of Aggregation Operation must be List or Variable ");
                }
            }

        } else {
            this.leftOperand = stack.pop();
        }


        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        if (logger.isDebugEnabled()) {
            logger.debug("Operation Expression Call Expression : {}", getClass().getSimpleName());
        }
        stack.push(this);
        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts)
            throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        Stack<Expression> localStack = new Stack<>();
        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

                if (op.getClass().equals(RightPriority.class)) {
                    this.rightOperand = new ArrayList<>(localStack);
                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {

                    pos = op.parse(tokens, pos, localStack, contexts);
                }


            } else {

                localStack.add(buildExpression(token, contexts, pos, tokens));

            }
        }
        return pos;
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding)
            throws InterpretException {


        List<?> valuesList = extractValues(instancesBinding, leftOperand);


        for (Object value : valuesList) {
            LiteralExpression<?> literalExpression = LiteralType.getLiteralExpression(value, dataTypeExpression);

            if (!(literalExpression instanceof NullLiteral)) {

                try {
                    leftExpression = literalExpression.getType().newInstance().evaluate(this, leftExpression, literalExpression);
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Updating value of  by " + leftExpression.getValue());
                }

            } /*else {
                throw new InterpretException("Literal Expression is null");
            }*/

        }

        return leftExpression;
    }


    protected List<?> extractValues(InstancesBinding instancesBinding, Expression valueOperand)
            throws InterpretException {
        List values = null;

        if (valueOperand instanceof ListOperationExpression || valueOperand instanceof ExpressionSeparator) {
            OperationExpression valuesOperation = (OperationExpression) valueOperand;
            Object valuesResult = valuesOperation.interpret(instancesBinding);
            if (valuesResult instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) valuesResult;
                dataTypeExpression = listLiteral.getType();

                values = listLiteral.getValue();

            }

        } else if (valueOperand instanceof LiteralExpression) {

            values = literalExpressionValues((LiteralExpression<?>) valueOperand);

        } else if (valueOperand instanceof ListLiteralOperationExpression) {

            values = listLiteralValues(instancesBinding, (ListLiteralOperationExpression) valueOperand);

        } else if (valueOperand instanceof VariableExpression) {

            values = variableExpressionValues(instancesBinding, (VariableExpression<?>) valueOperand);

        } else if (valueOperand instanceof ChainOperationExpression) {

            values = chainOperationExpressionValues(instancesBinding, (ChainOperationExpression) valueOperand);

        } else if (valueOperand instanceof Function) {

            values = functionExpressionExpressionValues(instancesBinding, (Function) valueOperand);

        } else if (valueOperand instanceof PairOperation) {

            values = pairValues(instancesBinding, valueOperand);

        } else if (valueOperand instanceof TripleOperation) {

            values = tripleValues(instancesBinding, valueOperand);

        } else if (valueOperand instanceof ConditionOperationExpression) {
            logger.error("Condition Operation Expression: " + leftOperand.toString());
        } else {

            logger.error(leftOperand.toString());
        }
        return values;
    }

    private List<?> literalExpressionValues(LiteralExpression<?> literalExpression) throws InterpretException {
        List values;

        if (literalExpression instanceof ListLiteral) {
            ListLiteral listLiteral = ListLiteral.class.cast(literalExpression);
            values = listLiteral.getValue();
            dataTypeExpression = listLiteral.getType();
        } else {
            values = new ArrayList<>();
            values.add(literalExpression.getValue());
            dataTypeExpression = literalExpression.getType();
        }

        return values;
    }

    private List<?> listLiteralValues(
            InstancesBinding instancesBinding, ListLiteralOperationExpression listLiteralOperationExpression)
            throws InterpretException {
        List values = null;
        LiteralExpression<?> literalExpression = listLiteralOperationExpression.interpret(instancesBinding);

        if (literalExpression instanceof ListLiteral) {
            values = extractValues(instancesBinding, literalExpression);
        } else {
            dataTypeExpression = literalExpression.getType();
        }
        return values;
    }

    private List<?> variableExpressionValues(InstancesBinding instancesBinding, VariableExpression<?> variableExpression)
            throws InterpretException {
        List values = null;
        if (variableExpression instanceof ListVariable) {
            LiteralExpression<?> literalExpression = variableExpression.interpret(instancesBinding);

            if (literalExpression instanceof ListLiteral) {
                values = extractValues(instancesBinding, literalExpression);
            } else {
                dataTypeExpression = literalExpression.getType();
            }

        } else {

            dataTypeExpression = variableExpression.getType();
            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());

            List<?> dataSet;
            if (instance instanceof List) {
                dataSet = (List<?>) instance;
            } else {
                dataSet = Collections.singletonList(instance);
            }


            values = new ArrayList<>();
            for (Object object : dataSet) {
                variableExpression = VariableType.setVariableValue(variableExpression, object);
                Object value = variableExpression.getValue();
                values.add(value);
            }

        }
        return values;
    }

    private List<?> functionExpressionExpressionValues(InstancesBinding instancesBinding, Function function)
            throws InterpretException {
        List values;

        FunctionExpression functionValueOperand = (FunctionExpression) function.getLeftOperand();


        List<List> dataSet = new ArrayList<>();
        for (Expression parameter : functionValueOperand.getParameters()) {

            List dataSetList = extractValues(instancesBinding, parameter);
            dataSet.add(dataSetList);

        }


        if (!dataSet.isEmpty()) {

            List<List> d1 = new ArrayList<>();
            for (int i = 0; i < dataSet.get(0).size(); i++) {

                List<Object> d2 = new ArrayList<>();
                for (List aDataSet : dataSet) {
                    d2.add(aDataSet.get(i));
                }
                d1.add(d2);
            }

            dataSet = d1;
        }


        List<Expression> orignalParameters = functionValueOperand.getParameters();

        values = new ArrayList<>();
        for (List<?> paramsvalues : dataSet) {


            List<Expression> dummyParameters = new ArrayList<>();


            for (int i = 0; i < paramsvalues.size(); i++) {


                Object object = paramsvalues.get(i);


                Expression parameter = orignalParameters.get(i);


                if (parameter instanceof ChainOperationExpression) {

                    ChainOperationExpression chainOperationExpression = (ChainOperationExpression) parameter;

                    Expression leftOperandExpression1 = chainOperationExpression.getLeftOperand();
                    chainOperationExpression.setLeftOperand(LiteralType.getLiteralExpression(object, dataTypeExpression));
                    Object interpret = chainOperationExpression.interpret(instancesBinding);
                    LiteralExpression<?> literalExpression = (LiteralExpression<?>) interpret;

                    dummyParameters.add(literalExpression);

                    chainOperationExpression.setLeftOperand(leftOperandExpression1);


                } else {
                    dummyParameters.add(LiteralType.getLiteralExpression(object, dataTypeExpression));
                }


            }

            functionValueOperand.setParameters(dummyParameters);


            LiteralExpression<?> literalExpression = (LiteralExpression<?>) function.interpret(instancesBinding);

            if (literalExpression.getType() != null && !literalExpression.getType().equals(NullType.class)) {
                dataTypeExpression = literalExpression.getType();
            }
            values.add(literalExpression.getValue());


        }

        functionValueOperand.setParameters(orignalParameters);

        return values;
    }


    private List<?> chainOperationExpressionValues(InstancesBinding instancesBinding,
                                                   ChainOperationExpression chainOperationExpression)
            throws InterpretException {
        List values = null;

        Expression expression = chainOperationExpression.getLeftOperand();
        ChainOperationExpression chainOperationExpressionTemp = chainOperationExpression;
        while (expression instanceof ChainOperationExpression) {
            chainOperationExpressionTemp = (ChainOperationExpression) expression;
            expression = ((ChainOperationExpression) expression).getLeftOperand();
        }

        List dataSet = null;
        Class<? extends DataTypeExpression> dataSetDataTypeExpression = null;
        if (expression instanceof VariableExpression) {
            VariableExpression<?> variableExpression = (VariableExpression<?>) expression;
            dataSet = extractValues(instancesBinding, variableExpression);
            dataSetDataTypeExpression = dataTypeExpression;
        }


        if (dataSet != null && dataSetDataTypeExpression != null) {
            values = new ArrayList<>();
            for (Object object : dataSet) {
                Expression leftOperandExpression1 = chainOperationExpressionTemp.getLeftOperand();
                chainOperationExpressionTemp.setLeftOperand(LiteralType.getLiteralExpression(object, dataSetDataTypeExpression));
                Object interpret = chainOperationExpression.interpret(instancesBinding);
                LiteralExpression<?> literalExpression = (LiteralExpression<?>) interpret;
                if (literalExpression.getType() != null && !(literalExpression.getType().equals(NullType.class))) {
                    dataTypeExpression = literalExpression.getType();
                }
                values.add(literalExpression.getValue());

                chainOperationExpressionTemp.setLeftOperand(leftOperandExpression1);
            }

        }


        return values;
    }

    private List<?> pairValues(InstancesBinding instancesBinding, Expression expression)
            throws InterpretException {
        if (expression instanceof PairOperation || expression instanceof ListOperationExpression) {
            OperationExpression pairOperation = (OperationExpression) expression;

            Object pairResultList = pairOperation.interpret(instancesBinding);

            if (pairResultList instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) pairResultList;
                return listLiteral.getValue();

            }
        }
        return null;
    }

    private List<?> tripleValues(InstancesBinding instancesBinding, Expression expression)
            throws InterpretException {
        if (expression instanceof TripleOperation || expression instanceof ListOperationExpression) {
            OperationExpression tripleOperation = (OperationExpression) expression;

            Object tripleResultList = tripleOperation.interpret(instancesBinding);

            if (tripleResultList instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) tripleResultList;
                return listLiteral.getValue();

            }
        }
        return null;
    }


    protected boolean isPairList(List<?> values) {
        return values.stream().allMatch(o -> o instanceof Pair);
    }

    protected boolean isTripleList(List<?> values) {
        return values.stream().allMatch(o -> o instanceof Triple);
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" -> ");
        }

        toStringBuilder.append(operationType.getSymbols().get(0));


        if (rightOperand != null) {
            toStringBuilder.append("(");
            Iterator<Expression> expressionIterator = rightOperand.iterator();
            while (expressionIterator.hasNext()) {
                Expression expression = expressionIterator.next();
                toStringBuilder.append(expression.toString());
                if (expressionIterator.hasNext()) {
                    toStringBuilder.append(", ");
                }
            }
            toStringBuilder.append(")");
            toStringBuilder.append(" ");


        } else {
            toStringBuilder.append(" ");
        }

        return toStringBuilder.toString();
    }


    public List<Expression> getRightListOperand() {
        return rightOperand;
    }
}