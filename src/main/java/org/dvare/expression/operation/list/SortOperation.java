/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
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
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.utility.LetOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.util.DataTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Operation(type = OperationType.SORT)
public class SortOperation extends ListOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(SortOperation.class);

    public SortOperation() {
        super(OperationType.SORT);
    }

    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {

        Comparator<Object> pairComparator = (o1, o2) -> {

            if (o1 instanceof Pair && o2 instanceof Pair) {

                Object c1 = ((Pair) o1).getLeft();
                Object c2 = ((Pair) o2).getLeft();
                if (c1 != null && c2 != null) {
                    DataType type = DataTypeMapping.getTypeMapping(c1.getClass());
                    return compare(c1, c2, type);
                }

            }
            return -1;
        };


        List<?> values = extractValues(instancesBinding, leftOperand);
        if (values != null) {

            List<Object> notNullList = new ArrayList<>();
            for (Object value : values) {
                if (value != null) {
                    notNullList.add(value);
                }
            }
            values = notNullList;

            if (rightOperand.isEmpty()) {

                if (isPairList(values)) {
                    values.sort(pairComparator);
                } else {
                    values.sort(null);
                }

                return new ListLiteral(values, dataTypeExpression);

            } else if (rightOperand.get(0) instanceof ChainOperationExpression) {

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


                    values.sort((o1, o2) -> {

                        try {
                            ValueType c1 = buildCompareValue(instancesBinding, chainOperationExpression, variableExpression, o1);

                            ValueType c2 = buildCompareValue(instancesBinding, chainOperationExpression, variableExpression, o2);

                            if (c1 != null && c1.value != null && c2 != null && c2.value != null) {

                                return compare(c1.value, c2.value, toDataType(c1.type));
                            }


                        } catch (Exception e) {
                            return -1;
                        }
                        return -1;
                    });

                    return new ListLiteral(values, dataTypeExpression);

                }

            }
        }

        return new NullLiteral();
    }


    private ValueType buildCompareValue(InstancesBinding instancesBinding,
                                        ChainOperationExpression chainOperationExpression, VariableExpression variableExpression,
                                        Object value) throws InterpretException {
        String name = variableExpression.getName();
        String operandType = variableExpression.getOperandType();


        instancesBinding.addInstanceItem(operandType, name, value);
        Object chainOperationInterpret = chainOperationExpression.interpret(instancesBinding);
        instancesBinding.removeInstanceItem(operandType, name);

        if (chainOperationInterpret instanceof LiteralExpression) {

            LiteralExpression literalExpression = (LiteralExpression) chainOperationInterpret;


            ValueType paramValue = new ValueType();
            paramValue.value = literalExpression.getValue();
            paramValue.type = literalExpression.getType();

            return paramValue;
        }
        return null;
    }

    private int compare(Object o1, Object o2, DataType type) {
        switch (type) {

            case StringType: {

                String i1 = (String) o1;

                String i2 = (String) o2;

                return i1.compareTo(i2);
            }

            case IntegerType: {

                Integer i1 = (Integer) o1;

                Integer i2 = (Integer) o2;

                return i1.compareTo(i2);
            }


            case FloatType: {

                Float i1 = (Float) o1;

                Float i2 = (Float) o2;

                return i1.compareTo(i2);
            }

            case DateType: {

                LocalDate i1 = (LocalDate) o1;

                LocalDate i2 = (LocalDate) o2;

                return i1.compareTo(i2);
            }

            case DateTimeType: {

                LocalDateTime i1 = (LocalDateTime) o1;

                LocalDateTime i2 = (LocalDateTime) o2;

                return i1.compareTo(i2);
            }


            case SimpleDateType: {

                Date i1 = (Date) o1;

                Date i2 = (Date) o2;

                return i1.compareTo(i2);
            }
        }

        return -1;
    }

    private class ValueType {
        Class<? extends DataTypeExpression> type;
        Object value;
    }


}