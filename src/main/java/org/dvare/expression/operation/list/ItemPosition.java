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

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.IntegerType;
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
@Operation(type = OperationType.ITEM_POSITION)
public class ItemPosition extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ItemPosition.class);


    public ItemPosition() {
        super(OperationType.ITEM_POSITION);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        List<?> values = extractValues(instancesBinding, leftOperand);

        if (values != null && !rightOperand.isEmpty()) {
            Expression expression = rightOperand.get(0);
            if (expression instanceof ArithmeticOperationExpression || expression instanceof AggregationOperationExpression) {

                OperationExpression operationExpression = (OperationExpression) expression;
                Object interpret = operationExpression.interpret(instancesBinding);
                if (interpret instanceof LiteralExpression) {

                    Object item = ((LiteralExpression) interpret).getValue();
                    if (item != null) {

                        return LiteralType.getLiteralExpression(values.indexOf(item), IntegerType.class);
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
                        LiteralExpression interpret = operationExpression.interpret(instancesBinding);
                        instancesBinding.removeInstanceItem(operandType, name);

                        Boolean result = toBoolean(interpret);

                        if (result) {


                            return LiteralType.getLiteralExpression(values.indexOf(value), IntegerType.class);

                        }

                    }

                }

            } else if (expression instanceof IntegerLiteral) {
                Object item = ((LiteralExpression) expression).getValue();
                if (item != null) {

                    return LiteralType.getLiteralExpression(values.indexOf(item), IntegerType.class);
                }

            }
        }


        return new NullLiteral();
    }

}