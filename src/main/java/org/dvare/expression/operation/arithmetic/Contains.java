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

import org.dvare.annotations.Operation;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TrimString;

@Operation(type = OperationType.CONTAINS, dataTypes = {DataType.StringType})
public class Contains extends ChainOperationExpression {
    public Contains() {
        super(OperationType.CONTAINS);
    }

    public Contains copy() {
        return new Contains();
    }

    private Object contains(Object selfRow, Object dataRow) throws InterpretException {
        interpretOperand(selfRow, dataRow);
        LiteralExpression literalExpression = toLiteralExpression(leftValueOperand);
        if (!(literalExpression instanceof NullLiteral)) {

            String value = literalExpression.getValue().toString();
            value = TrimString.trim(value);


            LiteralExpression startExpression = (LiteralExpression) rightOperand.get(0);

            String start = null;
            if (startExpression.getValue() instanceof Integer) {
                start = (String) startExpression.getValue();
            } else {
                start = startExpression.getValue().toString();
            }

            start = TrimString.trim(start);

            Boolean result = value.contains(start);

            try {
                LiteralExpression returnExpression = LiteralType.getLiteralExpression(result.toString(), DataType.BooleanType);
                return returnExpression;
            } catch (IllegalValueException e) {
            }

        }

        return null;
    }

    @Override
    public Object interpret(Object dataRow) throws InterpretException {


        return contains(dataRow, null);

    }

    @Override
    public Object interpret(Object selfRow, Object dataRow) throws InterpretException {

        return contains(selfRow, dataRow);
    }

}