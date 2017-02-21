package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.*;
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


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {

        List<Object> values = null;
        Class<? extends DataTypeExpression> dataTypeExpression = null;

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
                    LiteralExpression literalExpression = (LiteralExpression) operationExpression.interpret(instancesBinding);
                    if (literalExpression.getType() != null && !literalExpression.getType().equals(NullType.class)) {
                        dataTypeExpression = literalExpression.getType();
                    }
                    values.add(literalExpression.getValue());
                }
                instancesBinding.addInstance(operandType, instance);
            }
        }

        if (values != null) {
            if (!rightOperand.isEmpty()) {
                Expression filterParam = rightOperand.get(0);
                if (filterParam instanceof EqualityOperationExpression) {


                    OperationExpression operationExpression = (OperationExpression) filterParam;


                    Expression leftExpression = operationExpression.getLeftOperand();

                    while (leftExpression instanceof OperationExpression) {
                        leftExpression = ((OperationExpression) leftExpression).getLeftOperand();
                    }


                    if (leftExpression instanceof VariableExpression) {
                        VariableExpression variableExpression = (VariableExpression) leftExpression;
                        String name = variableExpression.getName();
                        String operandType = variableExpression.getOperandType();

                        List filterValues = new ArrayList();

                        for (Object value : values) {


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


                            Object interpret = operationExpression.interpret(instancesBinding);

                            Boolean result = toBoolean(interpret);

                            if (result) {
                                filterValues.add(value);

                            }

                        }

                        return new ListLiteral(filterValues, dataTypeExpression);

                    }


                }


            } else {
                return new ListLiteral(values, dataTypeExpression);
            }
        }
        return new NullLiteral();
    }

}