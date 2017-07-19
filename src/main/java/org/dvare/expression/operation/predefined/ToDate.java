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


package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.DateLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TrimString;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Operation(type = OperationType.TO_DATE, dataTypes = {DataType.StringType})
public class ToDate extends ChainOperationExpression {

    public ToDate() {
        super(OperationType.TO_DATE);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        leftValueOperand = super.interpretOperand(this.leftOperand, expressionBinding, instancesBinding);
        LiteralExpression literalExpression = toLiteralExpression(leftValueOperand);
        if (literalExpression != null && !(literalExpression instanceof NullLiteral)) {


            if (literalExpression.getValue() == null) {
                return new NullLiteral();
            }


            if (literalExpression instanceof DateLiteral) {
                return literalExpression;
            }


            Object value = literalExpression.getValue();
            try {
                LocalDate localDate = null;
                if (value instanceof LocalDate) {
                    localDate = (LocalDate) value;
                } else if (value instanceof Date) {
                    Date date = (Date) value;
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                } else {
                    String valueString = literalExpression.getValue().toString();
                    valueString = TrimString.trim(valueString);

                    if (valueString.matches(LiteralType.date)) {
                        localDate = LocalDate.parse(valueString, LiteralType.dateFormat);

                    }

                }

                if (localDate != null) {
                    return LiteralType.getLiteralExpression(localDate, DataType.DateType);
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }


        }

        return new NullLiteral<>();
    }

}