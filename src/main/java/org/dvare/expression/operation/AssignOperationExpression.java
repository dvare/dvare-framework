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

import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public abstract class AssignOperationExpression extends AggregationOperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(AssignOperationExpression.class);

    public AssignOperationExpression(OperationType operationType) {
        super(operationType);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        String leftString = tokens[pos - 1];
        String rightString = tokens[pos + 1];


        Expression left = null;
        if (stack.isEmpty()) {

            OperationExpression.TokenType tokenType = OperationExpression.findDataObject(leftString, contexts);
            if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type))
                    != null) {
                TypeBinding typeBinding = contexts.getContext(tokenType.type);
                DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                left = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);
            }


        } else {
            left = stack.pop();
        }


        this.leftOperand = left;


        // right side
        pos = findNextExpression(tokens, pos + 1, stack, contexts);

        if (!stack.isEmpty()) {

            Expression expression = stack.pop();
            while (pos + 1 < tokens.length) {
                ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
                OperationExpression testOp = configurationRegistry.getOperation(tokens[pos + 1]);
                if (testOp instanceof ChainOperationExpression) {
                    stack.push(expression);
                    pos = testOp.parse(tokens, pos + 1, stack, contexts);
                    expression = stack.pop();
                }

                break;
            }
            this.rightOperand = expression;
        }

        stack.push(this);
        return pos;
    }


}