package org.dvare.expression.operation.list;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.*;
import org.dvare.expression.operation.utility.Function;
import org.dvare.expression.veriable.ListVariable;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
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
        List<?> values = extractValues(expressionBinding, instancesBinding, this.leftOperand);

        if (values != null) {
            List<?> includedValues = values;
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

    private List<?> extractValues(ExpressionBinding expressionBinding, InstancesBinding instancesBinding, Expression valueOperand) throws InterpretException {
        List values;


        if (valueOperand instanceof LiteralExpression) {

            values = literalExpressionValues((LiteralExpression) valueOperand);

        } else if (valueOperand instanceof ListLiteralOperationExpression) {

            values = listLiteralValues(expressionBinding, instancesBinding, (ListLiteralOperationExpression) valueOperand);

        } else if (valueOperand instanceof VariableExpression) {

            values = variableExpressionValues(instancesBinding, (VariableExpression) valueOperand);

        } else if (valueOperand instanceof ChainOperationExpression) {

            values = chainOperationExpressionValues(expressionBinding, instancesBinding, (ChainOperationExpression) valueOperand);

        } else if (valueOperand instanceof Function) {

            values = functionExpressionExpressionValues(expressionBinding, instancesBinding, (Function) valueOperand);

        } else if (valueOperand instanceof PairOperation) {

            values = pairValues(expressionBinding, instancesBinding, valueOperand);

        } else {

            values = buildValues(valueOperand, expressionBinding, instancesBinding);
            if (isPairList(values)) {
                values = extractPairValues(values);
            }

        }
        return values;
    }

    private List<?> literalExpressionValues(LiteralExpression literalExpression) throws InterpretException {
        List values;

        if (literalExpression instanceof ListLiteral) {
            values = ((ListLiteral) literalExpression).getValue();
            dataTypeExpression = ((ListLiteral) literalExpression).getType();
        } else {
            values = new ArrayList<>();

            values.add(literalExpression.getValue());

            dataTypeExpression = literalExpression.getType();
        }

        return values;
    }


    private List<?> listLiteralValues(ExpressionBinding expressionBinding, InstancesBinding instancesBinding, ListLiteralOperationExpression listLiteralOperationExpression) throws InterpretException {
        List values = null;

        Object interpret = listLiteralOperationExpression.interpret(expressionBinding, instancesBinding);
        if (interpret instanceof ListLiteral) {
            values = ((ListLiteral) interpret).getValue();
            dataTypeExpression = ((ListLiteral) interpret).getType();
        }

        return values;
    }

    private List<?> variableExpressionValues(InstancesBinding instancesBinding, VariableExpression variableExpression) throws InterpretException {
        List values = null;
        if (variableExpression instanceof ListVariable) {
            dataTypeExpression = variableExpression.getType();
            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
            variableExpression = VariableType.setVariableValue(variableExpression, instance);
            values = ((ListVariable) variableExpression).getValue();

        } else {

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

        }
        return values;
    }

    private List<?> functionExpressionExpressionValues(ExpressionBinding expressionBinding, InstancesBinding instancesBinding, Function function) throws InterpretException {
        List values = null;


        Expression functionValueOperand = function.getLeftOperand();

        List dataSet = extractValues(expressionBinding, instancesBinding, functionValueOperand);


        values = new ArrayList<>();
        for (Object object : dataSet) {

            function.setLeftOperand(LiteralType.getLiteralExpression(object, dataTypeExpression));

            LiteralExpression literalExpression = (LiteralExpression) function.interpret(expressionBinding, instancesBinding);

            if (literalExpression.getType() != null && !literalExpression.getType().equals(NullType.class)) {
                dataTypeExpression = literalExpression.getType();
            }
            values.add(literalExpression.getValue());

        }


        return values;
    }

    private List<?> chainOperationExpressionValues(ExpressionBinding expressionBinding, InstancesBinding instancesBinding, ChainOperationExpression operationExpression) throws InterpretException {
        List values = null;

        Expression expression = operationExpression.getLeftOperand();
        ChainOperationExpression chainOperationExpression = operationExpression;
        while (expression instanceof ChainOperationExpression) {
            chainOperationExpression = (ChainOperationExpression) expression;
            expression = ((ChainOperationExpression) expression).getLeftOperand();
        }

        List dataSet = null;
        Class<? extends DataTypeExpression> dataSetDataTypeExpression = null;
        if (expression instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) expression;
            dataSet = extractValues(expressionBinding, instancesBinding, variableExpression);
            dataSetDataTypeExpression = dataTypeExpression;
        }


        if (dataSet != null && dataSetDataTypeExpression != null) {
            values = new ArrayList<>();
            for (Object object : dataSet) {
                Expression leftOperandExpression1 = chainOperationExpression.getLeftOperand();
                chainOperationExpression.setLeftOperand(LiteralType.getLiteralExpression(object, dataSetDataTypeExpression));
                LiteralExpression literalExpression = (LiteralExpression) operationExpression.interpret(expressionBinding, instancesBinding);
                if (literalExpression.getType() != null && !(literalExpression.getType().equals(NullType.class))) {
                    dataTypeExpression = literalExpression.getType();
                }
                values.add(literalExpression.getValue());

                chainOperationExpression.setLeftOperand(leftOperandExpression1);
            }

        }

        return values;
    }

    private List<?> pairValues(ExpressionBinding expressionBinding, InstancesBinding instancesBinding, Expression expression) throws InterpretException {
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

    private List<?> extractPairValues(List pairList) {
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