package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.IntegerLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.*;
import org.dvare.expression.operation.utility.LetOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.GET_ITEM)
public class GetItem extends AggregationOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(GetItem.class);


    public GetItem() {
        super(OperationType.GET_ITEM);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        List<?> values = extractValues(instancesBinding, leftOperand);

        if (values != null && !rightOperand.isEmpty()) {
            Expression expression = rightOperand.get(0);

            if (expression instanceof ArithmeticOperationExpression || expression instanceof AggregationOperationExpression) {

                OperationExpression operationExpression = (OperationExpression) expression;
                Object interpret = operationExpression.interpret(instancesBinding);
                if (interpret instanceof IntegerLiteral) {
                    return buildItem(values, (IntegerLiteral) interpret, dataTypeExpression);
                }


            } else if (expression instanceof RelationalOperationExpression || expression instanceof ChainOperationExpression) {


                OperationExpression operationExpression = (OperationExpression) expression;


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

                    for (Object value : values) {

                        instancesBinding.addInstanceItem(operandType, name, value);
                        LiteralExpression<?> interpret = operationExpression.interpret(instancesBinding);
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


        return new NullLiteral<>();
    }

    private LiteralExpression<?> buildItem(List<?> values, IntegerLiteral integerLiteral, Class<?> dataTypeExpress) throws InterpretException {
        Integer index = integerLiteral.getValue();
        if (index != null && values != null) {
            index--; // start index from 1
            if (index < values.size()) {

                Object value = values.get(index);
                return LiteralType.getLiteralExpression(value, dataTypeExpress);


            }
        }
        return new NullLiteral<>();
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}