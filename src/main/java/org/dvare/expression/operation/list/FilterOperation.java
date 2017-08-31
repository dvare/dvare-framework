/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.expression.operation.list;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
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
    public LiteralExpression interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        List<?> values = extractValues(expressionBinding, instancesBinding, leftOperand);

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
        return new NullLiteral();
    }


    private List pairFilter(ExpressionBinding expressionBinding, InstancesBinding instancesBinding,
                            Expression includeParam, List values) throws InterpretException {
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


        } else if (includeParam instanceof RelationalOperationExpression || includeParam instanceof ChainOperationExpression) {

            for (Object value : values) {
                if (value instanceof Pair) {
                    Pair valuePair = (Pair) value;
                    Boolean result = buildEqualityOperationExpression(includeParam, expressionBinding,
                            instancesBinding, valuePair.getLeft());
                    if (result) {
                        includedValues.add(valuePair);
                    }
                }
            }
        }
        return includedValues;
    }
}