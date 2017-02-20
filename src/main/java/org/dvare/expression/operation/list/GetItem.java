package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.IntegerLiteral;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.EqualityOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.GET_ITEM)
public class GetItem extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(GetItem.class);


    public GetItem() {
        super(OperationType.GET_ITEM);
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {

        Expression right = leftOperand;
        if (right instanceof Values) {
            OperationExpression valuesOperation = (OperationExpression) right;

            Object valuesResult = valuesOperation.interpret(instancesBinding);

            List<Object> values = null;
            Class dataTypeExpress = null;
            if (valuesResult instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) valuesResult;
                values = listLiteral.getValue();
                dataTypeExpress = listLiteral.getType();
            }


            if (!rightOperand.isEmpty()) {
                Expression expression = rightOperand.get(0);
                if (expression instanceof EqualityOperationExpression) {


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


                                return LiteralType.getLiteralExpression(value, dataTypeExpress);

                            }

                        }

                    }


                } else if (expression instanceof IntegerLiteral) {

                    Integer index = ((IntegerLiteral) expression).getValue();
                    if (index != null && values != null) {
                        index--; // start index from 1
                        if (index < values.size()) {

                            Object value = values.get(index);
                            return LiteralType.getLiteralExpression(value, dataTypeExpress);


                        }
                    }
                }
            }


        }


        return new NullLiteral();
    }

}