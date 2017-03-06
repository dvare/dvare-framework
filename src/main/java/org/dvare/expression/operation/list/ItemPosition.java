package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.IntegerLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.*;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.ITEM_POSITION)
public class ItemPosition extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ItemPosition.class);


    public ItemPosition() {
        super(OperationType.ITEM_POSITION);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        List<Object> values = buildValues(leftOperand, expressionBinding, instancesBinding);

        if (values != null && !rightOperand.isEmpty()) {
            Expression expression = rightOperand.get(0);
            if (expression instanceof ArithmeticOperationExpression || expression instanceof AggregationOperationExpression) {

                OperationExpression operationExpression = (OperationExpression) expression;
                Object interpret = operationExpression.interpret(expressionBinding, instancesBinding);
                if (interpret instanceof LiteralExpression) {


                    Object item = ((LiteralExpression) interpret).getValue();
                    if (item != null && values != null) {

                        return LiteralType.getLiteralExpression(values.indexOf(item), IntegerType.class);
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


                        Object interpret = operationExpression.interpret(expressionBinding, instancesBinding);

                        Boolean result = toBoolean(interpret);

                        if (result) {


                            return LiteralType.getLiteralExpression(values.indexOf(value), IntegerType.class);

                        }

                    }

                }

            } else if (expression instanceof IntegerLiteral) {
                Object item = ((LiteralExpression) expression).getValue();
                if (item != null && values != null) {

                    return LiteralType.getLiteralExpression(values.indexOf(item), IntegerType.class);
                }

            }
        }


        return new NullLiteral();
    }

}