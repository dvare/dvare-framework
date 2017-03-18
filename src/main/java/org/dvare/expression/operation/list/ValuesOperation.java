package org.dvare.expression.operation.list;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.util.DataTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.VALUES)
public class ValuesOperation extends ListOperationExpression {
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

        if (left instanceof VariableExpression) {
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
        } else if (left instanceof PairOperation) {

            values = pairValues(left, expressionBinding, instancesBinding);

        } else {

           /* if (left instanceof ListOperationExpression) {
                ListOperationExpression listOperationExpression = (ListOperationExpression) left;
                Expression expression = listOperationExpression.getLeftOperand();
                while (expression instanceof OperationExpression) {

                    if (expression instanceof PairOperation) {
                        values = pairValues(left, expressionBinding, instancesBinding);
                        break;
                    } else {
                        expression = ((OperationExpression) expression).getLeftOperand();
                    }


                }

            }*/

            values = buildValues(left, expressionBinding, instancesBinding);
            if (isPairList(values)) {
                values = extractPairValues(values);
            }

        }
        return values;
    }


    private List<Object> pairValues(Expression expression, ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        if (expression instanceof PairOperation || expression instanceof ListOperationExpression) {
            OperationExpression pairOperation = (OperationExpression) expression;

            Object pairResultList = pairOperation.interpret(expressionBinding, instancesBinding);

            if (pairResultList instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) pairResultList;
                List pairList = listLiteral.getValue();
                return extractPairValues(pairList);

            }
        }
        return null;
    }

    private List<Object> extractPairValues(List pairList) {
        List<Object> pairValues = new ArrayList<>();

        for (Object pairObject : pairList) {
            if (pairObject instanceof Pair) {
                Pair pair = (Pair) pairObject;
                if (dataTypeExpression == null) {
                    DataType dataType = DataTypeMapping.getTypeMapping(pair.getValue().getClass());
                    dataTypeExpression = DataTypeMapping.getDataTypeClass(dataType);
                }
                pairValues.add(pair.getValue());
            }
        }
        return pairValues;
    }
}