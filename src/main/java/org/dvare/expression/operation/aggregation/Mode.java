/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.annotations.OperationType;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.FloatType;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.AGGREGATION, symbols = {"Mode", "mode"}, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Mode extends OperationExpression {
    static Logger logger = LoggerFactory.getLogger(Mode.class);


    public Mode() {
        super("Mode", "mode");
    }

    public Mode copy() {
        return new Mode();
    }

    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {

        LiteralExpression leftExpression = null;
        if (leftOperand instanceof LiteralExpression) {
            leftExpression = (LiteralExpression) leftOperand;
        } else {
            leftExpression = new NullLiteral();
        }


        Expression right = this.rightOperand;
        if (right instanceof VariableExpression) {
            VariableExpression variableExpression = ((VariableExpression) right);
            String name = variableExpression.getName();
            DataType type = variableExpression.getType().getDataType();

            switch (type) {

                case FloatType: {

                    List<Float> values = new ArrayList<>();

                    for (Object bindings : dataSet) {
                        Object value = getValue(bindings, name);
                        if (value instanceof Float) {
                            values.add((Float) value);
                        }
                    }


                    Float maxValue = 0f;
                    Integer maxCount = 0;

                    for (int i = 0; i < values.size(); ++i) {
                        int count = 0;
                        for (int j = 0; j < values.size(); ++j) {
                            if (values.get(j).compareTo(values.get(i)) == 0) ++count;
                        }
                        if (count > maxCount) {
                            maxCount = count;
                            maxValue = values.get(i);
                        }
                    }

                    leftExpression = LiteralType.getLiteralExpression(maxValue, new FloatType());

                    break;

                }
                case IntegerType: {
                    List<Integer> values = new ArrayList<>();

                    for (Object bindings : dataSet) {
                        Object value = getValue(bindings, name);
                        if (value instanceof Integer) {
                            values.add((Integer) value);
                        }
                    }


                    Integer maxValue = 0, maxCount = 0;

                    for (int i = 0; i < values.size(); ++i) {
                        int count = 0;
                        for (int j = 0; j < values.size(); ++j) {
                            if (values.get(j) == values.get(i)) ++count;
                        }
                        if (count > maxCount) {
                            maxCount = count;
                            maxValue = values.get(i);
                        }
                    }
                    leftExpression = LiteralType.getLiteralExpression(maxValue, new IntegerType());

                    break;
                }
            }
        }


        return leftExpression;
    }
}