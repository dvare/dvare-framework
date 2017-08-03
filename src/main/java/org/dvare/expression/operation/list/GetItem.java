package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.IntegerLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.*;
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
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        List<?> values = extractValues(expressionBinding, instancesBinding, leftOperand);

        if (values != null && !rightOperand.isEmpty()) {
            Expression expression = rightOperand.get(0);

            if (expression instanceof ArithmeticOperationExpression || expression instanceof AggregationOperationExpression) {

                OperationExpression operationExpression = (OperationExpression) expression;
                Object interpret = operationExpression.interpret(expressionBinding, instancesBinding);
                if (interpret instanceof IntegerLiteral) {
                    return buildItem(values, (IntegerLiteral) interpret, dataTypeExpression);
                }


            } else if (expression instanceof RelationalOperationExpression || expression instanceof ChainOperationExpression) {


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

                        instancesBinding.addInstanceItem(operandType, name, value);
                        Object interpret = operationExpression.interpret(expressionBinding, instancesBinding);
                        instancesBinding.removeInstanceItem(operandType, name);

                        if (toBoolean(interpret)) {
                            return LiteralType.getLiteralExpression(value, dataTypeExpression);

                        }

                    }

                }


            } else if (expression instanceof IntegerLiteral) {

                return buildItem(values, (IntegerLiteral) expression, dataTypeExpression);

            }
        }


        return new NullLiteral();
    }

    private LiteralExpression buildItem(List<?> values, IntegerLiteral integerLiteral, Class dataTypeExpress) throws InterpretException {
        Integer index = integerLiteral.getValue();
        if (index != null && values != null) {
            index--; // start index from 1
            if (index < values.size()) {

                Object value = values.get(index);
                return LiteralType.getLiteralExpression(value, dataTypeExpress);


            }
        }
        return new NullLiteral();
    }


}