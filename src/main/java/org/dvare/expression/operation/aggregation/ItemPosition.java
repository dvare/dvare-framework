package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.*;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
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


            Object item = null;
            if (!rightOperand.isEmpty()) {
                Expression expression = rightOperand.get(0);
                if (expression instanceof OperationExpression) {
                    OperationExpression operationExpression = (OperationExpression) expression;
                    Object result = operationExpression.interpret(instancesBinding);

                    if (result instanceof LiteralExpression) {
                        item = ((LiteralExpression) result).getValue();
                    }

                } else if (expression instanceof IntegerLiteral) {
                    item = ((LiteralExpression) expression).getValue();
                }
            }


            if (item != null && values != null) {

                return LiteralType.getLiteralExpression(values.indexOf(instancesBinding), IntegerType.class);
            }


        }


        return new NullLiteral();
    }

}