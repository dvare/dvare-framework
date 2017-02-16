package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.IntegerLiteral;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.AT)
public class At extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(At.class);


    public At() {
        super(OperationType.AT);
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {

        Object aggregation = instancesBinding.getInstance("self");
        List<Object> dataSet = (List) instancesBinding.getInstance("data");


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


            Integer index = null;
            if (!rightOperand.isEmpty()) {
                Expression expression = rightOperand.get(0);
                if (expression instanceof OperationExpression) {
                    OperationExpression operationExpression = (OperationExpression) expression;
                    Object result = operationExpression.interpret(instancesBinding);

                    if (result instanceof IntegerLiteral) {
                        index = ((IntegerLiteral) result).getValue();
                    }

                } else if (expression instanceof IntegerLiteral) {
                    index = ((IntegerLiteral) expression).getValue();
                }
            }


            if (index != null && values != null) {
                if (values.size() < index) {

                    Object value = values.get(index);
                    return LiteralType.getLiteralExpression(value, dataTypeExpress);


                }
            }


        }


        return new NullLiteral();
    }

}