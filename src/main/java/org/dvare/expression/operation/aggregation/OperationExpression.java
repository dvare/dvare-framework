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


package org.dvare.expression.operation.aggregation;

import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;

public abstract class OperationExpression extends AggregationOperation {
    static Logger logger = LoggerFactory.getLogger(OperationExpression.class);

    public OperationExpression(String symbol) {
        super(symbol);
    }

    public OperationExpression(List<String> symbols) {
        super(symbols);
    }

    public OperationExpression(String... symbols) {
        super(symbols);
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, aTypeBinding, vTypeBinding);
        if (!stack.isEmpty()) {
            Expression right = stack.pop();
            this.rightOperand = right;
        }
        stack.push(this);
        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (int i = pos; i < tokens.length; i++) {
            AggregationOperation op = configurationRegistry.getAggregationOperation(tokens[i]);
            if (op != null) {
                op = op.copy();
                i = op.parse(tokens, i, stack, aTypeBinding, vTypeBinding);
                return i;
            }
        }
        return null;
    }

    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {

        LiteralExpression leftExpression = null;
        if (leftOperand instanceof LiteralExpression) {
            leftExpression = (LiteralExpression) leftOperand;
        } else {
            leftExpression = new NullLiteral();
        }

        for (Object bindings : dataSet) {


            Expression right = this.rightOperand;
            LiteralExpression<?> literalExpression = null;
            if (right instanceof AggregationOperation) {
                AggregationOperation operation = (AggregationOperation) right;
                literalExpression = (LiteralExpression) operation.interpret(aggregation, dataSet);
            } else if (right instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) right;
                variableExpression = VariableType.setVariableValue(variableExpression, bindings);
                literalExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
            } else if (right instanceof LiteralExpression) {
                literalExpression = (LiteralExpression<?>) right;
            }

            if (literalExpression != null && !(literalExpression instanceof NullLiteral)) {


                leftExpression = literalExpression.getType().evaluate(this, leftExpression, literalExpression);
                logger.debug("Updating value of  by " + leftExpression.getValue());

            } else {
                throw new InterpretException("Literal Expression is null");
            }

        }

        return leftExpression;
    }


}