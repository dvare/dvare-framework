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
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralType;
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


    protected int parseOperands(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        String leftString = tokens[pos - 1];
        String rightString = tokens[pos + 1];

        DataType variableType = null;

        if (selfTypes.getTypes().containsKey(leftString)) {
            variableType = TypeFinder.findType(leftString, selfTypes);
        }

        Expression left = null;
        if (stack.isEmpty()) {

            if (variableType == null) {
                if (selfTypes.getTypes().containsKey(rightString)) {
                    variableType = TypeFinder.findType(rightString, selfTypes);
                } else {
                    variableType = LiteralType.computeDataType(rightString);
                }
            }

            if (selfTypes.getTypes().containsKey(leftString)) {
                left = VariableType.getVariableType(leftString, variableType);
            } else {
                left = LiteralType.getLiteralExpression(leftString, variableType);
            }

        } else {
            left = stack.pop();
        }


        this.leftOperand = left;


        // right side
        pos = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);

        if (!stack.isEmpty()) {

            Expression expression = stack.pop();
            while (pos + 1 < tokens.length) {
                ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
                OperationExpression testOp = configurationRegistry.getOperation(tokens[pos + 1]);
                if (testOp instanceof ChainOperationExpression) {
                    stack.push(expression);
                    pos = testOp.parse(tokens, pos + 1, stack, selfTypes, dataTypes);
                    expression = stack.pop();
                }

                break;
            }
            this.rightOperand = expression;
        }

        return pos;
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        pos = parseOperands(tokens, pos, stack, selfTypes, dataTypes);
        stack.push(this);
        return pos;
    }


}