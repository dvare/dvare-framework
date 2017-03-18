package org.dvare.expression.operation.list;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.*;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.FILTER)
public class FilterOperation extends ListOperationExpression {

    public FilterOperation() {
        super(OperationType.FILTER);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        if (leftOperand instanceof PairOperation) {
            OperationExpression valuesOperation = (OperationExpression) leftOperand;

            Object valuesResult = valuesOperation.interpret(expressionBinding, instancesBinding);

            if (valuesResult instanceof ListLiteral) {

                List values = ((ListLiteral) valuesResult).getValue();
                dataTypeExpression = ((ListLiteral) valuesResult).getType();

                if (rightOperand.size() == 1) {

                    Expression includeParam = rightOperand.get(0);

                    List includedValues = pairFilter(expressionBinding, instancesBinding, includeParam, values);
                    return new ListLiteral(includedValues, dataTypeExpression);
                }


            }

        } else {

            List<Object> values = buildValues(leftOperand, expressionBinding, instancesBinding);

            if (values != null) {

                List includedValues = values;
                if (rightOperand.size() == 1) {
                    Expression includeParam = rightOperand.get(0);
                    if (isPairList(values)) {
                        includedValues = pairFilter(expressionBinding, instancesBinding, includeParam, values);
                    } else {
                        includedValues = includedFilter(includeParam, expressionBinding, instancesBinding, values);
                    }


                }
                return new ListLiteral(includedValues, dataTypeExpression);

            }
        }
        return new NullLiteral();
    }


    private List pairFilter(ExpressionBinding expressionBinding, InstancesBinding instancesBinding, Expression includeParam, List values) throws InterpretException {
        List<Pair> includedValues = new ArrayList<>();
        if (includeParam instanceof LogicalOperationExpression) {


            OperationExpression operationExpression = (OperationExpression) includeParam;

            Expression left = operationExpression.getLeftOperand();
            Expression right = operationExpression.getRightOperand();


            for (Object value : values) {
                if (value instanceof Pair) {
                    Pair valuePair = (Pair) value;
                    Boolean result = solveLogical(operationExpression, expressionBinding, instancesBinding, valuePair.getLeft());
                    if (result) {
                        includedValues.add(valuePair);
                    }
                    operationExpression.setLeftOperand(left);
                    operationExpression.setRightOperand(right);
                }

            }


        } else if (includeParam instanceof EqualityOperationExpression || includeParam instanceof ChainOperationExpression) {

            for (Object value : values) {

                if (value instanceof Pair) {
                    Pair valuePair = (Pair) value;
                    Boolean result = buildEqualityOperationExpression(includeParam, expressionBinding, instancesBinding, valuePair.getLeft());
                    if (result) {
                        includedValues.add(valuePair);
                    }
                }
            }


        }
        return includedValues;
    }
}