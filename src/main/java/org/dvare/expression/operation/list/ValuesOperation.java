package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.BooleanExpression;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.*;
import org.dvare.expression.operation.*;
import org.dvare.expression.operation.utility.GetExpOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.VALUES)
public class ValuesOperation extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ValuesOperation.class);


    public ValuesOperation() {
        super(OperationType.VALUES);
    }

    public ValuesOperation(OperationType operationType) {
        super(operationType);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        List<Object> values = extractValues(expressionBinding, instancesBinding);

        if (values != null) {
            List<Object> includedValues = values;
            if (!rightOperand.isEmpty()) {
                if (rightOperand.size() == 1) {
                    Expression includeParam = rightOperand.get(0);
                    includedValues = includedFilter(includeParam, expressionBinding, instancesBinding, values);

                } else if (rightOperand.size() == 2) {
                    Expression includeParam = rightOperand.get(0);
                    Expression exculdeParam = rightOperand.get(1);
                    includedValues = excludedFilter(includeParam, exculdeParam, expressionBinding, instancesBinding, values);


                }


            }

            return new ListLiteral(includedValues, dataTypeExpression);

        }
        return new NullLiteral();
    }


    private List<Object> extractValues(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        List<Object> values = null;
        Expression left = this.leftOperand;

        if (left instanceof ValuesOperation || left instanceof MapOperation || left instanceof GetExpOperation) {
            values = buildValues(left, expressionBinding, instancesBinding);
        } else if (left instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) left;
            dataTypeExpression = variableExpression.getType();
            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
            List dataSet;
            if (instance instanceof List) {
                dataSet = (List) instance;
            } else {
                dataSet = new ArrayList<>();
                dataSet.add(instance);
            }
            values = new ArrayList<>();
            for (Object object : dataSet) {
                Object value = getValue(object, variableExpression.getName());
                values.add(value);
            }

        } else if (left instanceof ChainOperationExpression) {
            ChainOperationExpression operationExpression = (ChainOperationExpression) left;
            Expression expression = operationExpression.getLeftOperand();
            while (expression instanceof ChainOperationExpression) {
                expression = ((ChainOperationExpression) expression).getLeftOperand();
            }
            if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;
                String operandType = variableExpression.getOperandType();
                dataTypeExpression = variableExpression.getType();
                Object instance = instancesBinding.getInstance(operandType);
                List dataSet;
                if (instance instanceof List) {
                    dataSet = (List) instance;
                } else {
                    dataSet = new ArrayList<>();
                    dataSet.add(instance);
                }
                values = new ArrayList<>();
                for (Object object : dataSet) {
                    instancesBinding.addInstance(operandType, object);
                    LiteralExpression literalExpression = (LiteralExpression) operationExpression.interpret(expressionBinding, instancesBinding);
                    if (literalExpression.getType() != null && !literalExpression.getType().equals(NullType.class)) {
                        dataTypeExpression = literalExpression.getType();
                    }
                    values.add(literalExpression.getValue());
                }
                instancesBinding.addInstance(operandType, instance);
            }
        }
        return values;
    }


    private List<Object> includedFilter(Expression includeParam, ExpressionBinding expressionBinding, InstancesBinding instancesBinding, List<Object> values) throws InterpretException {

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

    private List<Object> excludedFilter(Expression includeParam, Expression exculdeParam, ExpressionBinding expressionBinding, InstancesBinding instancesBinding, List<Object> values) throws InterpretException {

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


    private Boolean buildEqualityOperationExpression(Expression includeParam, ExpressionBinding expressionBinding, InstancesBinding instancesBinding, Object value) throws InterpretException {

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


    private Boolean solveLogical(OperationExpression operationExpression, ExpressionBinding expressionBinding, InstancesBinding instancesBinding, Object value) throws InterpretException {


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