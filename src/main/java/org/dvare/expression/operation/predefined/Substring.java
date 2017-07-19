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
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.StringType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TrimString;

import java.util.Stack;

@Operation(type = OperationType.SUBSTRING, dataTypes = {DataType.StringType})
public class Substring extends ChainOperationExpression {


    public Substring() {
        super(OperationType.SUBSTRING);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {
        pos = super.parse(tokens, pos, stack, expressionBinding, contexts);

        if (rightOperand.size() != 2) {
            throw new ExpressionParseException(String.format("%s must contain two parameters at %d", getClass().getSimpleName(), pos));
        }


        return pos;
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        leftValueOperand = super.interpretOperand(this.leftOperand, expressionBinding, instancesBinding);
        LiteralExpression literalExpression = toLiteralExpression(leftValueOperand);
        if ((literalExpression != null && !(literalExpression instanceof NullLiteral) && rightOperand.size() >= 2)) {


            if (literalExpression.getValue() == null) {
                return new NullLiteral<>();
            }

            String value = literalExpression.getValue().toString();
            value = TrimString.trim(value);


            LiteralExpression indexExpression = (LiteralExpression) rightOperand.get(0);
            LiteralExpression countExpression = (LiteralExpression) rightOperand.get(1);

            Integer index;
            if (indexExpression.getValue() instanceof Integer) {
                index = (Integer) indexExpression.getValue();
            } else {
                index = Integer.parseInt(indexExpression.getValue().toString());
            }


            Integer count;
            if (countExpression.getValue() instanceof Integer) {
                count = (Integer) countExpression.getValue();
            } else {
                count = Integer.parseInt(countExpression.getValue().toString());
            }

            if (value.length() < count) {
                return new NullLiteral<>();
            }

            Integer start = index - 1;
            Integer end = index - 1 + count;

            if (start < 0 || end > value.length()) {
                return new NullLiteral<>();
            }


            try {
                value = value.substring(start, end);
            } catch (ArrayIndexOutOfBoundsException e) {
                value = value.substring(index, index + count);
            }
            return LiteralType.getLiteralExpression(value, StringType.class);
        }

        return new NullLiteral<>();
    }


}