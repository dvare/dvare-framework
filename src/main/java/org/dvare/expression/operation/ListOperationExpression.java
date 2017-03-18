/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.expression.operation;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.BooleanExpression;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.BooleanLiteral;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class ListOperationExpression extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ListOperationExpression.class);


    public ListOperationExpression(OperationType operationType) {
        super(operationType);
    }


    protected boolean isPairList(List<Object> values) {
        return values.stream().allMatch(o -> o instanceof Pair);
    }

    protected List<Object> includedFilter(Expression includeParam, ExpressionBinding expressionBinding, InstancesBinding instancesBinding, List<Object> values) throws InterpretException {

        if (includeParam instanceof LogicalOperationExpression) {


            OperationExpression operationExpression = (OperationExpression) includeParam;

            Expression left = operationExpression.getLeftOperand();
            Expression right = operationExpression.getRightOperand();

            List<Object> includedValues = new ArrayList<>();
            for (Object value : values) {
                Boolean result = solveLogical(operationExpression, expressionBinding, instancesBinding, value);
                if (result) {
                    includedValues.add(value);
                }
                operationExpression.setLeftOperand(left);
                operationExpression.setRightOperand(right);

            }


            return includedValues;
        } else if (includeParam instanceof EqualityOperationExpression || includeParam instanceof ChainOperationExpression) {
            List<Object> includedValues = new ArrayList<>();
            for (Object value : values) {


                Boolean result = buildEqualityOperationExpression(includeParam, expressionBinding, instancesBinding, value);
                if (result) {
                    includedValues.add(value);
                }

            }


            return includedValues;

        } else if (includeParam instanceof BooleanExpression) {

            BooleanExpression booleanExpression = (BooleanExpression) includeParam;

            Boolean result = toBoolean(booleanExpression.interpret(expressionBinding, instancesBinding));

            if (!result) {
                return new ArrayList<>();
            }


        }
        return values;
    }


    protected List<Object> excludedFilter(Expression includeParam, Expression exculdeParam, ExpressionBinding expressionBinding, InstancesBinding instancesBinding, List<Object> values) throws InterpretException {

        List<Object> includedValues = includedFilter(includeParam, expressionBinding, instancesBinding, values);


        if (exculdeParam instanceof LogicalOperationExpression) {


            OperationExpression operationExpression = (OperationExpression) includeParam;

            Expression left = operationExpression.getLeftOperand();
            Expression right = operationExpression.getRightOperand();

            List<Object> excludedValues = new ArrayList<>();
            for (Object value : values) {

                Boolean result = solveLogical(operationExpression, expressionBinding, instancesBinding, value);
                if (result) {
                    excludedValues.add(value);
                }

                operationExpression.setLeftOperand(left);
                operationExpression.setRightOperand(right);
            }

            includedValues.removeAll(excludedValues);

            return includedValues;
        } else if (exculdeParam instanceof EqualityOperationExpression || exculdeParam instanceof ChainOperationExpression) {
            List<Object> excludedValues = new ArrayList<>();
            for (Object value : includedValues) {
                Boolean result = buildEqualityOperationExpression(exculdeParam, expressionBinding, instancesBinding, value);
                if (result) {
                    excludedValues.add(value);
                }
            }
            includedValues.removeAll(excludedValues);

        }

        return includedValues;
    }


    protected Boolean buildEqualityOperationExpression(Expression includeParam, ExpressionBinding expressionBinding, InstancesBinding instancesBinding, Object value) throws InterpretException {

        if (includeParam instanceof BooleanLiteral) {
            return toBoolean(includeParam);
        }


        OperationExpression operationExpression = (OperationExpression) includeParam;
        Expression leftExpression = operationExpression.getLeftOperand();

        while (leftExpression instanceof OperationExpression) {
            leftExpression = ((OperationExpression) leftExpression).getLeftOperand();
        }


        if (leftExpression instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) leftExpression;
            String name = variableExpression.getName();
            String operandType = variableExpression.getOperandType();


            Object instance = instancesBinding.getInstance(operandType);
            if (instance == null || !(instance instanceof DataRow)) {
                DataRow dataRow = new DataRow();
                dataRow.addData(name, value);
                instancesBinding.addInstance(operandType, dataRow);
            } else {
                DataRow dataRow = (DataRow) instance;
                dataRow.addData(name, value);
                instancesBinding.addInstance(operandType, dataRow);
            }

            Object interpret = operationExpression.interpret(expressionBinding, instancesBinding);
            return toBoolean(interpret);
        }

        return false;

    }

    protected Boolean solveLogical(OperationExpression operationExpression, ExpressionBinding expressionBinding, InstancesBinding instancesBinding, Object value) throws InterpretException {


        Expression left = operationExpression.getLeftOperand();
        Expression right = operationExpression.getRightOperand();
        if (left != null) {
            if (left instanceof LogicalOperationExpression) {

                Boolean result = solveLogical((OperationExpression) left, expressionBinding, instancesBinding, value);
                operationExpression.setLeftOperand(LiteralType.getLiteralExpression(result, BooleanType.class));
            } else {
                Boolean result = buildEqualityOperationExpression(left, expressionBinding, instancesBinding, value);
                operationExpression.setLeftOperand(LiteralType.getLiteralExpression(result, BooleanType.class));
            }
        }


        if (right != null) {
            if (right instanceof LogicalOperationExpression) {
                Boolean result = solveLogical((OperationExpression) right, expressionBinding, instancesBinding, value);
                operationExpression.setRightOperand(LiteralType.getLiteralExpression(result, BooleanType.class));
            } else {
                Boolean result = buildEqualityOperationExpression(right, expressionBinding, instancesBinding, value);
                operationExpression.setRightOperand(LiteralType.getLiteralExpression(result, BooleanType.class));
            }
        }

        Boolean result = toBoolean(operationExpression.interpret(expressionBinding, instancesBinding));
        operationExpression.setLeftOperand(left);
        operationExpression.setRightOperand(right);
        return result;
    }


}