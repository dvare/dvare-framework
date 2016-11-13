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

package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;

import java.util.Stack;

@Operation(type = OperationType.LEFT_PRIORITY)
public class LeftPriority extends OperationExpression {
    public LeftPriority() {
        super(OperationType.LEFT_PRIORITY);
    }

    public LeftPriority copy() {
        return new LeftPriority();
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {

        pos = parse(tokens, pos, stack, typeBinding, null);


        return pos;
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);

        while (!stack.peek().getClass().equals(RightPriority.class)) {
            pos = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);
        }

        if (stack.peek().getClass().equals(RightPriority.class)) {
            stack.pop();
        }

        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos, stack, typeBinding, null);
        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {

            OperationExpression op = configurationRegistry.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();

                if (op.getClass().equals(RightPriority.class)) {
                    stack.push(op);
                    return i;

                } else {
                    if (dataTypes == null) {
                        i = op.parse(tokens, i, stack, selfTypes);
                    } else {
                        i = op.parse(tokens, i, stack, selfTypes, dataTypes);
                    }
                }


            }
        }
        return null;
    }
}