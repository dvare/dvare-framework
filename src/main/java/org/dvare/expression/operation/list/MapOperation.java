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
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.utility.LetOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.MAP)
public class MapOperation extends ListOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(MapOperation.class);


    public MapOperation() {
        super(OperationType.MAP);
    }


    @Override
    public LiteralExpression interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        if (leftOperand instanceof PairOperation) {
            List<?> values = extractValues(expressionBinding, instancesBinding, leftOperand);
            return new ListLiteral(values, dataTypeExpression);
        } else {
            List<?> values = extractValues(expressionBinding, instancesBinding, leftOperand);
            if (values != null) {
                if (rightOperand.isEmpty() || !(rightOperand.get(0) instanceof ChainOperationExpression)) {
                    return new ListLiteral(values, dataTypeExpression);
                } else {
                    ChainOperationExpression chainOperationExpression = (ChainOperationExpression) rightOperand.get(0);


                    Expression leftExpression = chainOperationExpression.getLeftOperand();

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


                        List<Object> mappedValues = new ArrayList<>();

                        for (Object value : values) {

                            instancesBinding.addInstanceItem(operandType, name, value);

                            Object chainOperationInterpret = chainOperationExpression.interpret(expressionBinding, instancesBinding);
                            instancesBinding.removeInstanceItem(operandType, name);


                            if (chainOperationInterpret instanceof LiteralExpression) {

                                LiteralExpression literalExpression = (LiteralExpression) chainOperationInterpret;
                                if (!(literalExpression instanceof NullLiteral) && literalExpression.getType() != null && !literalExpression.getType().equals(dataTypeExpression)) {
                                    dataTypeExpression = literalExpression.getType();

                                }

                                Object mappedValue = literalExpression.getValue();
                                mappedValues.add(mappedValue);


                            }


                        }


                        return new ListLiteral(mappedValues, dataTypeExpression);


                    }

                }

            }


        }
        return new NullLiteral();
    }


}