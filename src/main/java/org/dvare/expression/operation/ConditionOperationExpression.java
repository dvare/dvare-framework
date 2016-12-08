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


package org.dvare.expression.operation;


import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;

import java.util.Stack;

public abstract class ConditionOperationExpression extends OperationExpression {


    protected Expression condition = null;
    protected Expression thenOperand = null;
    protected Expression elseOperand = null;

    public ConditionOperationExpression(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypes) throws ExpressionParseException {
        pos = parse(tokens, pos, stack, aTypes, null);
        return pos;
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypes, TypeBinding vTypes) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, aTypes, vTypes);
        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypes) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, aTypes, null);
        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypes, TypeBinding vTypes) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (int i = pos; i < tokens.length; i++) {
            OperationExpression op = configurationRegistry.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();
                i = op.parse(tokens, i, stack, aTypes, vTypes);
                return i;
            }

        }
        return null;
    }

    protected Boolean toBoolean(Object interpret) {
        Boolean result;
        if (interpret instanceof LiteralExpression) {
            result = (Boolean) ((LiteralExpression) interpret).getValue();
        } else {
            result = (Boolean) interpret;
        }
        return result;
    }

}
