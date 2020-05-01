package org.dvare.expression.operation.list;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.FILTER)
public class FilterOperation extends ListOperationExpression {

    public FilterOperation() {
        super(OperationType.FILTER);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        List<?> values = extractValues(instancesBinding, leftOperand);

        if (values != null) {

            List includedValues = values;
            if (rightOperand.size() == 1) {
                Expression includeParam = rightOperand.get(0);
                if (isPairList(values)) {
                    includedValues = pairFilter(instancesBinding, includeParam, values);
                } else {
                    includedValues = includedFilter(includeParam, instancesBinding, values);
                }


            }
            return new ListLiteral(includedValues, dataTypeExpression);

        }
        return new NullLiteral<>();
    }


    private List<?> pairFilter(InstancesBinding instancesBinding,
                               Expression includeParam, List values) throws InterpretException {
        List<Pair> includedValues = new ArrayList<>();
        if (includeParam instanceof LogicalOperationExpression) {


            OperationExpression operationExpression = (OperationExpression) includeParam;

            Expression left = operationExpression.getLeftOperand();
            Expression right = operationExpression.getRightOperand();


            for (Object value : values) {
                if (value instanceof Pair) {
                    Pair valuePair = (Pair) value;
                    Boolean result = solveLogical(operationExpression, instancesBinding, valuePair);
                    if (result) {
                        includedValues.add(valuePair);
                    }
                    operationExpression.setLeftOperand(left);
                    operationExpression.setRightOperand(right);
                }

            }


        } else if (includeParam instanceof RelationalOperationExpression || includeParam instanceof ChainOperationExpression) {

            for (Object value : values) {
                if (value instanceof Pair) {
                    Pair valuePair = (Pair) value;
                    Boolean result = buildEqualityOperationExpression(includeParam,
                            instancesBinding, valuePair);
                    if (result) {
                        includedValues.add(valuePair);
                    }
                }
            }
        }
        return includedValues;
    }
}