/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2019 DVARE (Data Validation and Aggregation Rule Engine)
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
package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.SUM, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Sum extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Sum.class);


    public Sum() {
        super(OperationType.SUM);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {


        if (leftOperand instanceof VariableExpression) {
            VariableExpression variableExpression = ((VariableExpression) leftOperand);

            DataType type = toDataType(variableExpression.getType());

            switch (type) {

                case FloatType: {
                    leftExpression = LiteralType.getLiteralExpression(0f, variableExpression.getType());
                    break;
                }
                case IntegerType: {
                    leftExpression = LiteralType.getLiteralExpression(0, variableExpression.getType());
                    break;
                }
                case StringType: {
                    leftExpression = LiteralType.getLiteralExpression("", variableExpression.getType());
                    break;
                }
                default: {
                    leftExpression = new NullLiteral();
                    //throw new IllegalOperationException("Sum OperationExpression Not Allowed");
                }

            }
        }

        return super.interpret(instancesBinding);
    }

}