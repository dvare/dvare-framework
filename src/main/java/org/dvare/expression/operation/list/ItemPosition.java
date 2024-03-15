package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.*;
import org.dvare.expression.operation.utility.LetOperation;
import org.dvare.expression.veriable.VariableExpression;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.ITEM_POSITION)
public class ItemPosition extends AggregationOperationExpression {

    public ItemPosition() {
        super(OperationType.ITEM_POSITION);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        List<?> values = extractValues(instancesBinding, leftOperand);

        if (values != null && !rightOperand.isEmpty()) {
            Expression expression = rightOperand.get(0);
            if (expression instanceof ArithmeticOperationExpression || expression instanceof AggregationOperationExpression) {

                OperationExpression operationExpression = (OperationExpression) expression;
                LiteralExpression<?> interpret = operationExpression.interpret(instancesBinding);

                Object item = interpret.getValue();
                if (item != null) {
                    return LiteralType.getLiteralExpression(values.indexOf(item) + 1, IntegerType.class);
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

                        boolean result = toBoolean(interpret);

                        if (result) {


                            return LiteralType.getLiteralExpression(values.indexOf(value) + 1, IntegerType.class);

                        }

                    }

                }

            } else if (expression instanceof LiteralExpression) {
                Object item = ((LiteralExpression<?>) expression).getValue();
                if (item != null) {

                    return LiteralType.getLiteralExpression(values.indexOf(item) + 1, IntegerType.class);
                }

            }
        }


        return new NullLiteral<>();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}