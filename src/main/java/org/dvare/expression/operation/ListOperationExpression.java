package org.dvare.expression.operation;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.BooleanExpression;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.BooleanLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.utility.LetOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class ListOperationExpression extends AggregationOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(ListOperationExpression.class);


    public ListOperationExpression(OperationType operationType) {
        super(operationType);
    }


    protected List<?> includedFilter(Expression includeParam, InstancesBinding instancesBinding, List<?> values) throws InterpretException {

        if (includeParam instanceof LogicalOperationExpression) {

            OperationExpression operationExpression = (OperationExpression) includeParam;

            Expression left = operationExpression.getLeftOperand();
            Expression right = operationExpression.getRightOperand();

            List<Object> includedValues = new ArrayList<>();
            for (Object value : values) {

                if (solveLogical(operationExpression, instancesBinding, value)) {
                    includedValues.add(value);
                }
                operationExpression.setLeftOperand(left);
                operationExpression.setRightOperand(right);

            }

            return includedValues;
        } else if (includeParam instanceof RelationalOperationExpression || includeParam instanceof ChainOperationExpression) {
            List<Object> includedValues = new ArrayList<>();
            for (Object value : values) {

                Boolean result = buildEqualityOperationExpression(includeParam, instancesBinding, value);
                if (result) {
                    includedValues.add(value);
                }

            }


            return includedValues;

        } else if (includeParam instanceof BooleanExpression) {

            BooleanExpression booleanExpression = (BooleanExpression) includeParam;

            boolean result = toBoolean(booleanExpression.interpret(instancesBinding));

            if (!result) {
                return new ArrayList<>();
            }


        }
        return values;
    }


    protected List<?> excludedFilter(Expression includeParam, Expression exculdeParam, InstancesBinding instancesBinding, List<?> values) throws InterpretException {

        List<?> includedValues = includedFilter(includeParam, instancesBinding, values);


        if (exculdeParam instanceof LogicalOperationExpression) {


            OperationExpression operationExpression = (OperationExpression) includeParam;

            Expression left = operationExpression.getLeftOperand();
            Expression right = operationExpression.getRightOperand();

            List<Object> excludedValues = new ArrayList<>();
            for (Object value : values) {

                boolean result = solveLogical(operationExpression, instancesBinding, value);
                if (result) {
                    excludedValues.add(value);
                }

                operationExpression.setLeftOperand(left);
                operationExpression.setRightOperand(right);
            }

            includedValues.removeAll(excludedValues);

            return includedValues;
        } else if (exculdeParam instanceof RelationalOperationExpression || exculdeParam instanceof ChainOperationExpression) {
            List<Object> excludedValues = new ArrayList<>();
            for (Object value : includedValues) {
                Boolean result = buildEqualityOperationExpression(exculdeParam, instancesBinding, value);
                if (result) {
                    excludedValues.add(value);
                }
            }
            includedValues.removeAll(excludedValues);

        }

        return includedValues;
    }


    protected Boolean buildEqualityOperationExpression(Expression includeParam, InstancesBinding instancesBinding, Object value) throws InterpretException {

        if (includeParam instanceof BooleanLiteral) {
            return toBoolean((LiteralExpression<?>) includeParam);
        }


        OperationExpression operationExpression = (OperationExpression) includeParam;
        Expression leftExpression = operationExpression.getLeftOperand();

        while (leftExpression instanceof OperationExpression && !(leftExpression instanceof LetOperation)) {
            leftExpression = ((OperationExpression) leftExpression).getLeftOperand();
        }


        if (leftExpression instanceof LetOperation) {
            leftExpression = ((LetOperation) leftExpression).getVariableExpression();
        }


        if (leftExpression instanceof VariableExpression) {
            VariableExpression<?> variableExpression = (VariableExpression<?>) leftExpression;
            String name = variableExpression.getName();
            String operandType = variableExpression.getOperandType();

            instancesBinding.addInstanceItem(operandType, name, value);

            LiteralExpression<?> interpret = operationExpression.interpret(instancesBinding);

            instancesBinding.removeInstanceItem(operandType, name);

            return toBoolean(interpret);
        }

        return false;

    }

    protected boolean solveLogical(OperationExpression operationExpression, InstancesBinding instancesBinding, Object value) throws InterpretException {


        Expression left = operationExpression.getLeftOperand();
        Expression right = operationExpression.getRightOperand();
        if (left != null) {
            if (left instanceof LogicalOperationExpression) {

                Boolean result = solveLogical((OperationExpression) left, instancesBinding, value);
                operationExpression.setLeftOperand(LiteralType.getLiteralExpression(result, BooleanType.class));
            } else {
                Boolean result = buildEqualityOperationExpression(left, instancesBinding, value);
                operationExpression.setLeftOperand(LiteralType.getLiteralExpression(result, BooleanType.class));
            }
        }


        if (right != null) {
            if (right instanceof LogicalOperationExpression) {
                Boolean result = solveLogical((OperationExpression) right, instancesBinding, value);
                operationExpression.setRightOperand(LiteralType.getLiteralExpression(result, BooleanType.class));
            } else {
                Boolean result = buildEqualityOperationExpression(right, instancesBinding, value);
                operationExpression.setRightOperand(LiteralType.getLiteralExpression(result, BooleanType.class));
            }
        }

        boolean result = toBoolean(operationExpression.interpret(instancesBinding));
        operationExpression.setLeftOperand(left);
        operationExpression.setRightOperand(right);
        return result;
    }


}