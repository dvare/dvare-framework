/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.FloatType;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.list.ValuesOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.MODE, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Mode extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Mode.class);


    public Mode() {
        super(OperationType.MODE);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        Expression valueOperand = this.leftOperand;

        List valuesList = null;
        DataType type = null;


        OperationExpression operationExpression = new ValuesOperation();
        operationExpression.setLeftOperand(valueOperand);

        Object interpret = operationExpression.interpret(expressionBinding, instancesBinding);

        if (interpret instanceof ListLiteral) {

            ListLiteral listLiteral = (ListLiteral) interpret;
            type = toDataType(listLiteral.getType());
            valuesList = listLiteral.getValue();
        }


        if (valuesList != null && type != null) {


            switch (type) {

                case FloatType: {

                    List<Float> values = new ArrayList<>();

                    for (Object value : valuesList) {

                        if (value instanceof Float) {
                            values.add((Float) value);
                        }
                    }


                    Float maxValue = 0f;
                    Integer maxCount = 0;

                    for (int i = 0; i < values.size(); ++i) {
                        int count = 0;
                        for (Float value : values) {
                            if (value.compareTo(values.get(i)) == 0) ++count;
                        }
                        if (count > maxCount) {
                            maxCount = count;
                            maxValue = values.get(i);
                        }
                    }

                    leftExpression = LiteralType.getLiteralExpression(maxValue, FloatType.class);

                    break;

                }
                case IntegerType: {
                    List<Integer> values = new ArrayList<>();

                    for (Object value : valuesList) {
                        if (value instanceof Integer) {
                            values.add((Integer) value);
                        }
                    }


                    Integer maxValue = 0, maxCount = 0;

                    for (int i = 0; i < values.size(); ++i) {
                        int count = 0;
                        for (Integer value : values) {
                            if (value.equals(values.get(i))) ++count;
                        }
                        if (count > maxCount) {
                            maxCount = count;
                            maxValue = values.get(i);
                        }
                    }
                    leftExpression = LiteralType.getLiteralExpression(maxValue, IntegerType.class);

                    break;
                }
            }
        }


        return leftExpression;
    }
}