package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.*;
import org.dvare.expression.operation.utility.LetOperation;
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
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {


        List<?> values = extractValues(expressionBinding, instancesBinding, leftOperand);


        if (values != null && !rightOperand.isEmpty()) {

            Expression expression = rightOperand.get(0);

            if (expression instanceof ArithmeticOperationExpression || expression instanceof AggregationOperationExpression) {

                OperationExpression operationExpression = (OperationExpression) expression;
                Object interpret = operationExpression.interpret(expressionBinding, instancesBinding);
                if (interpret instanceof LiteralExpression) {

                    LiteralExpression literalExpression = (LiteralExpression) interpret;
                    Object item = literalExpression.getValue();

                    if (item != null && !values.isEmpty() && (dataTypeExpression.equals(literalExpression.getType()))) {
                        for (Object value : values) {
                            if (value != null && value.equals(item)) {
                                return LiteralType.getLiteralExpression(true, BooleanType.class);
                            }
                        }
                    }

                }


            } else if (expression instanceof RelationalOperationExpression) {
                OperationExpression operationExpression = (OperationExpression) expression;


                Expression leftExpression = operationExpression.getLeftOperand();

                while (leftExpression instanceof OperationExpression && !(leftExpression instanceof LetOperation)) {
                    leftExpression = ((OperationExpression) leftExpression).getLeftOperand();
                }


                if (leftExpression instanceof LetOperation) {
                    leftExpression = LetOperation.class.cast(leftExpression).getVariableExpression();
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
                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                        }
                    }
                }
            } else if (expression instanceof LiteralExpression) {


                LiteralExpression literalExpression = (LiteralExpression) expression;
                Object item = literalExpression.getValue();
                Class itemDataTypeExpress = literalExpression.getType();


                if (item != null) {
                    if (!values.isEmpty() && (dataTypeExpression.equals(itemDataTypeExpress))) {

                        for (Object value : values) {
                            if (value != null && value.equals(item)) {
                                return LiteralType.getLiteralExpression(true, BooleanType.class);
                            }
                        }
                    }
                }
            }

        }


        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }

}