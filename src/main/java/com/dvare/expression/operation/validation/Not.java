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


package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;

import java.util.Stack;

@Operation(type = OperationType.VALIDATION, symbols = {"NOT", "Not", "not", "!"})
public class Not extends OperationExpression {
    public Not() {
        super("NOT", "Not", "not", "!");
    }

    public Not copy() {
        return new Not();
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, Class type) throws ExpressionParseException {
        int i = findNextExpression(tokens, pos + 1, stack, type);
        Expression right = stack.pop();

        this.rightOperand = right;
        stack.push(this);

        return i;
    }

    @Override
    public Object interpret(final Object object) throws InterpretException {
        return !(Boolean) this.rightOperand.interpret(object);
    }
}