package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.*;
import org.dvare.expression.operation.utility.LetOperation;
import org.dvare.expression.veriable.VariableExpression;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.HAS_ITEM)
public class HasItem extends AggregationOperationExpression {

    public HasItem() {
        super(OperationType.HAS_ITEM);
    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        List<?> values = extractValues(instancesBinding, leftOperand);

        if (values != null && !rightOperand.isEmpty()) {

            Expression expression = rightOperand.get(0);

            if (expression instanceof ArithmeticOperationExpression || expression instanceof AggregationOperationExpression) {

                OperationExpression operationExpression = (OperationExpression) expression;
                LiteralExpression<?> literalExpression = operationExpression.interpret(instancesBinding);

                Object item = literalExpression.getValue();

                if (item != null && !values.isEmpty() && (dataTypeExpression.equals(literalExpression.getType()))) {
                    for (Object value : values) {
                        if (value != null && value.equals(item)) {
                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                        }
                    }
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
                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                        }
                    }
                }
            } else if (expression instanceof LiteralExpression) {


                LiteralExpression<?> literalExpression = (LiteralExpression<?>) expression;
                Object item = literalExpression.getValue();
                Class<?> itemDataTypeExpress = literalExpression.getType();

                if (item != null && !values.isEmpty() && (dataTypeExpression.equals(itemDataTypeExpress))) {

                    for (Object value : values) {
                        if (value != null && value.equals(item)) {
                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                        }
                    }
                }

            }

        }


        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}