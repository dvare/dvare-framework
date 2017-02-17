package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
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
            Class itemDataTypeExpress = null;
            if (!rightOperand.isEmpty()) {
                Expression expression = rightOperand.get(0);
                if (expression instanceof OperationExpression) {
                    OperationExpression operationExpression = (OperationExpression) expression;
                    Object result = operationExpression.interpret(instancesBinding);

                    if (result instanceof LiteralExpression) {
                        LiteralExpression literalExpression = (LiteralExpression) result;
                        item = literalExpression.getValue();
                        itemDataTypeExpress = literalExpression.getType();
                    }

                } else if (expression instanceof LiteralExpression) {
                    LiteralExpression literalExpression = (LiteralExpression) expression;
                    item = literalExpression.getValue();
                    itemDataTypeExpress = literalExpression.getType();
                }
            }


            if (item != null && values != null) {
                if (!values.isEmpty() && (dataTypeExpress.equals(itemDataTypeExpress))) {

                    for (Object value : values) {
                        if (value.equals(item)) {
                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                        }
                    }
                }
            }
        }


        return new NullLiteral();
    }

}