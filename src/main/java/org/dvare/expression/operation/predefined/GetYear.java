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
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Operation(type = OperationType.GET_YEARS)
public class GetYear extends ChainOperationExpression {


    public GetYear() {
        super(OperationType.GET_YEARS);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        Expression leftValueOperand = super.interpretOperand(this.leftOperand, instancesBinding);
        LiteralExpression literalExpression = toLiteralExpression(leftValueOperand);
        if ((literalExpression != null && !(literalExpression instanceof NullLiteral))) {


            if (literalExpression.getValue() == null) {
                return new NullLiteral();
            }


            Object value = literalExpression.getValue();

            switch (toDataType(literalExpression.getType())) {
                case DateType: {
                    if (value instanceof LocalDate) {
                        LocalDate localDate = (LocalDate) value;
                        return LiteralType.getLiteralExpression(localDate.getYear(), IntegerType.class);

                    }
                    break;
                }

                case DateTimeType: {
                    if (value instanceof LocalDateTime) {

                        LocalDateTime localDateTime = (LocalDateTime) value;
                        return LiteralType.getLiteralExpression(localDateTime.getYear(), IntegerType.class);

                    }
                    break;
                }

                case SimpleDateType: {
                    if (value instanceof Date) {

                        Date date = (Date) value;

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        return LiteralType.getLiteralExpression(calendar.get(Calendar.YEAR), IntegerType.class);
                    }
                    break;
                }


            }


        }

        return new NullLiteral<>();
    }

}