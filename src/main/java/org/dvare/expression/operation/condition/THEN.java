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


package org.dvare.expression.operation.condition;

import org.dvare.annotations.Operation;
import org.dvare.annotations.OperationType;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

@Operation(type = OperationType.CONDITION, symbols = {"THEN", "then"})
public class THEN extends ConditionOperation {
    static Logger logger = LoggerFactory.getLogger(THEN.class);


    public THEN() {
        super("THEN", "then");
    }

    public THEN copy() {
        return new THEN();
    }

    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypes, TypeBinding vTypes) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 1, stack, aTypes, vTypes);

        return i;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypes, TypeBinding vTypes) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {

            org.dvare.expression.operation.Operation aggregationOperation = configurationRegistry.getOperation(tokens[i]);
            if (aggregationOperation != null) {
                aggregationOperation = aggregationOperation.copy();
                i = aggregationOperation.parse(tokens, i, stack, aTypes, vTypes);
                return i;
            }

        }
        return null;
    }


}