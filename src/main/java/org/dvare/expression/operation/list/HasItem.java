package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.*;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.HAS_ITEM)
public class HasItem extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(HasItem.class);


    public HasItem() {
        super(OperationType.HAS_ITEM);
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {


        Expression right = leftOperand;
        if (right instanceof ValuesOperation) {
            OperationExpression valuesOperation = (OperationExpression) right;

            Object valuesResult = valuesOperation.interpret(instancesBinding);

            List<Object> values = null;
            Class dataTypeExpress = null;
            if (valuesResult instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) valuesResult;
                values = listLiteral.getValue();
                dataTypeExpress = listLiteral.getType();
            }

            if (values != null) {
                if (!rightOperand.isEmpty()) {
                    Expression expression = rightOperand.get(0);

                    if (expression instanceof ArithmeticOperationExpression || expression instanceof AggregationOperationExpression) {

                        OperationExpression operationExpression = (OperationExpression) expression;
                        Object interpret = operationExpression.interpret(instancesBinding);
                        if (interpret instanceof LiteralExpression) {

                            LiteralExpression literalExpression = (LiteralExpression) interpret;
                            Object item = literalExpression.getValue();
                            if (item != null) {
                                if (!values.isEmpty() && (dataTypeExpress.equals(literalExpression.getType()))) {

                                    for (Object value : values) {
                                        if (value.equals(item)) {
                                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                                        }
                                    }
                                }


                            }

                        }


                    } else if (expression instanceof EqualityOperationExpression) {
                        OperationExpression operationExpression = (OperationExpression) expression;


                        Expression leftExpression = operationExpression.getLeftOperand();

                        while (leftExpression instanceof OperationExpression) {
                            leftExpression = ((OperationExpression) leftExpression).getLeftOperand();
                        }


                        if (leftExpression instanceof VariableExpression) {
                            VariableExpression variableExpression = (VariableExpression) leftExpression;
                            String name = variableExpression.getName();
                            String operandType = variableExpression.getOperandType();


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


                                    return LiteralType.getLiteralExpression(true, BooleanType.class);

                                }

                            }

                        }


                    } else if (expression instanceof LiteralExpression) {


                        LiteralExpression literalExpression = (LiteralExpression) expression;
                        Object item = literalExpression.getValue();
                        Class itemDataTypeExpress = literalExpression.getType();


                        if (item != null) {
                            if (!values.isEmpty() && (dataTypeExpress.equals(itemDataTypeExpress))) {

                                for (Object value : values) {
                                    if (value.equals(item)) {
                                        return LiteralType.getLiteralExpression(true, BooleanType.class);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }


        return new NullLiteral<>();
    }

}