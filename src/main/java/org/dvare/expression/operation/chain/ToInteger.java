/*The MIT License (MIT)

Copyright (c) 2016-2017 Muhammad Hammad

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


package org.dvare.expression.operation.chain;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TrimString;

@Operation(type = OperationType.TO_INTEGER, dataTypes = {DataType.StringType})
public class ToInteger extends ChainOperationExpression {


    public ToInteger() {
        super(OperationType.TO_INTEGER);
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {
        leftValueOperand = super.interpretOperand(this.leftOperand, instancesBinding);
        LiteralExpression literalExpression = toLiteralExpression(leftValueOperand);
        if (literalExpression != null && !(literalExpression instanceof NullLiteral)) {

            if (literalExpression.getValue() == null) {
                return new NullLiteral();
            }

            String value = literalExpression.getValue().toString();
            value = TrimString.trim(value);

            try {
                LiteralExpression returnExpression = LiteralType.getLiteralExpression(Integer.parseInt(value), DataType.IntegerType);
                return returnExpression;
            } catch (IllegalValueException e) {
                logger.error(e.getMessage(), e);
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return new NullLiteral<>();
    }

}