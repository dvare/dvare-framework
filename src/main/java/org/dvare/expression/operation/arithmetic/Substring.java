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


package org.dvare.expression.operation.arithmetic;

import org.dvare.annotations.OperationType;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainArithmeticOperationExpression;
import org.dvare.util.TrimString;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"Substring", "substring"}, dataTypes = {DataType.StringType})
public class Substring extends ChainArithmeticOperationExpression {


    public Substring() {
        super("Substring", "substring");
    }

    public Substring copy() {
        return new Substring();
    }


    private Object substring(Object selfRow, Object dataRow) throws InterpretException {
        interpretOperand(selfRow, dataRow);
        LiteralExpression literalExpression = toLiteralExpression(leftValueOperand);
        if (!(literalExpression instanceof NullLiteral)) {

            String value = literalExpression.getValue().toString();
            value = TrimString.trim(value);


            LiteralExpression indexExpression = (LiteralExpression) rightOperand.get(0);
            LiteralExpression countExpression = (LiteralExpression) rightOperand.get(1);

            Integer index = 0;
            if (indexExpression.getValue() instanceof Integer) {
                index = (Integer) indexExpression.getValue();
            } else {
                index = Integer.parseInt(indexExpression.getValue().toString());
            }


            Integer count = 0;
            if (countExpression.getValue() instanceof Integer) {
                count = (Integer) countExpression.getValue();
            } else {
                count = Integer.parseInt(countExpression.getValue().toString());
            }


            value = value.substring(index, index + count);
            LiteralExpression returnExpression = LiteralType.getLiteralExpression(value, dataType);
            return returnExpression;

        }

        return null;
    }

    @Override
    public Object interpret(Object dataRow) throws InterpretException {


        return substring(dataRow, null);

    }

    @Override
    public Object interpret(Object selfRow, Object dataRow) throws InterpretException {

        return substring(selfRow, dataRow);
    }


}