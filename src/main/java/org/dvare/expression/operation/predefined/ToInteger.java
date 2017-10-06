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
package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TrimString;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_INTEGER, dataTypes = {DataType.StringType})
public class ToInteger extends ChainOperationExpression {


    public ToInteger() {
        super(OperationType.TO_INTEGER);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {

                Object value = literalExpression.getValue();

                switch (toDataType(literalExpression.getType())) {
                    case StringType: {
                        String stringValue = TrimString.trim(value.toString());
                        DataType valueType = LiteralType.computeDataType(stringValue);

                        switch (valueType) {
                            case IntegerType:
                                return LiteralType.getLiteralExpression(Integer.parseInt(stringValue), IntegerType.class);
                            case FloatType:
                                return LiteralType.getLiteralExpression(Math.round(Float.parseFloat(stringValue)), IntegerType.class);
                        }
                    }

                    case IntegerType: {
                        if (value instanceof Integer) {
                            return LiteralType.getLiteralExpression(Integer.class.cast(value), IntegerType.class);
                        } else {
                            DataType valueType = LiteralType.computeDataType(value.toString());
                            if (valueType.equals(DataType.IntegerType)) {
                                return LiteralType.getLiteralExpression(Integer.parseInt(value.toString()), IntegerType.class);
                            }
                        }

                    }
                    case FloatType: {
                        if (value instanceof Float) {
                            return LiteralType.getLiteralExpression(Math.round(Float.class.cast(value)), IntegerType.class);
                        } else if (value != null) {
                            DataType valueType = LiteralType.computeDataType(value.toString());
                            if (valueType.equals(DataType.FloatType)) {
                                return LiteralType.getLiteralExpression(Math.round(Float.parseFloat(value.toString())), IntegerType.class);
                            }

                        }

                    }
                }

            }

        return new NullLiteral<>();
    }
}