/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.expression.operation;

import org.apache.commons.lang3.tuple.Pair;
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
import org.dvare.expression.operation.list.ValuesOperation;
import org.dvare.expression.operation.utility.ExpressionSeparator;
import org.dvare.expression.operation.utility.Function;
import org.dvare.expression.operation.utility.LeftPriority;
import org.dvare.expression.operation.utility.RightPriority;
import org.dvare.expression.veriable.ListVariable;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.InstanceUtils;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class AggregationOperationExpression extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(AggregationOperationExpression.class);
    protected List<Expression> rightOperand = new ArrayList<>();
    protected LiteralExpression leftExpression;

    public AggregationOperationExpression(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack,
                         ContextsBinding contexts) throws ExpressionParseException {


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
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack,
                                      ContextsBinding contexts)
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

                localStack.add(buildExpression(token, contexts));

            }
        }
        return pos;
    }

    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding)
            throws InterpretException {


        Expression valueOperand = this.leftOperand;


        List valuesList = null;
        Class<? extends DataTypeExpression> type = null;


        OperationExpression operationExpression = new ValuesOperation();
        operationExpression.setLeftOperand(valueOperand);

        Object interpret = operationExpression.interpret(instancesBinding);

        if (interpret instanceof ListLiteral) {

            ListLiteral listLiteral = (ListLiteral) interpret;
            type = listLiteral.getType();
            valuesList = listLiteral.getValue();
        }


        if (leftExpression == null) {
            if (type != null) {
                switch (toDataType(type)) {

                    case FloatType: {
                        leftExpression = LiteralType.getLiteralExpression(Float.MIN_VALUE, type);
                        break;
                    }
                    case IntegerType: {
                        leftExpression = LiteralType.getLiteralExpression(Integer.MIN_VALUE, type);
                        break;
                    }

                    default: {
                        leftExpression = new NullLiteral();
                        //throw new IllegalOperationException("Min OperationExpression Not Allowed");
                    }

                }
            } else {
                leftExpression = new NullLiteral();
            }
        }

        if (valuesList != null && type != null) {

            for (Object value : valuesList) {


                LiteralExpression<?> literalExpression = LiteralType.getLiteralExpression(value, type);

                if (literalExpression != null && !(literalExpression instanceof NullLiteral)) {

                    try {
                        Class<? extends DataTypeExpression> dataTypeExpression = literalExpression.getType();
                        leftExpression = new InstanceUtils<DataTypeExpression>().newInstance(dataTypeExpression)
                                .evaluate(this, leftExpression, literalExpression);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("Updating value of  by " + leftExpression.getValue());
                    }
                } else {
                    throw new InterpretException("Literal Expression is null");
                }

            }

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

            values = literalExpressionValues((LiteralExpression) valueOperand);

        } else if (valueOperand instanceof ListLiteralOperationExpression) {

            values = listLiteralValues(instancesBinding, (ListLiteralOperationExpression) valueOperand);

        } else if (valueOperand instanceof VariableExpression) {

            values = variableExpressionValues(instancesBinding, (VariableExpression) valueOperand);

        } else if (valueOperand instanceof ChainOperationExpression) {

            values = chainOperationExpressionValues(instancesBinding, (ChainOperationExpression) valueOperand);

        } else if (valueOperand instanceof Function) {

            values = functionExpressionExpressionValues(instancesBinding, (Function) valueOperand);

        } else if (valueOperand instanceof PairOperation) {

            values = pairValues(instancesBinding, valueOperand);

        } else if (valueOperand instanceof ConditionOperationExpression) {
            logger.error("Condition Operation Expression: " + leftOperand.toString());
        } else {

            logger.error(leftOperand.toString());
        }
        return values;
    }

    private List<?> literalExpressionValues(LiteralExpression literalExpression) throws InterpretException {
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
        LiteralExpression literalExpression = listLiteralOperationExpression.interpret(instancesBinding);

        if (literalExpression instanceof ListLiteral) {
            values = extractValues(instancesBinding, literalExpression);
        } else {
            dataTypeExpression = literalExpression.getType();
        }
        return values;
    }

    private List<?> variableExpressionValues(InstancesBinding instancesBinding, VariableExpression variableExpression)
            throws InterpretException {
        List values = null;
        if (variableExpression instanceof ListVariable) {
            LiteralExpression literalExpression = variableExpression.interpret(instancesBinding);

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
            logger.info(dataSet.toString());
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
                    LiteralExpression literalExpression = (LiteralExpression) interpret;

                    dummyParameters.add(literalExpression);

                    chainOperationExpression.setLeftOperand(leftOperandExpression1);


                } else {
                    dummyParameters.add(LiteralType.getLiteralExpression(object, dataTypeExpression));
                }


            }

            functionValueOperand.setParameters(dummyParameters);


            LiteralExpression literalExpression = (LiteralExpression) function.interpret(instancesBinding);

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
            VariableExpression variableExpression = (VariableExpression) expression;
            dataSet = extractValues(instancesBinding, variableExpression);
            dataSetDataTypeExpression = dataTypeExpression;
        }


        if (dataSet != null && dataSetDataTypeExpression != null) {
            values = new ArrayList<>();
            for (Object object : dataSet) {
                Expression leftOperandExpression1 = chainOperationExpressionTemp.getLeftOperand();
                chainOperationExpressionTemp.setLeftOperand(LiteralType.getLiteralExpression(object, dataSetDataTypeExpression));
                Object interpret = chainOperationExpression.interpret(instancesBinding);
                LiteralExpression literalExpression = (LiteralExpression) interpret;
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

    protected boolean isPairList(List<?> values) {
        return values.stream().allMatch(o -> o instanceof Pair);
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


}