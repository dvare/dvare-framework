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

import org.dvare.annotations.OperationType;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.ConditionOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;

@org.dvare.annotations.Operation(type = OperationType.CONDITION, symbols = {"IF", "if", "ELSEIF", "elseif"})
public class IF extends ConditionOperationExpression {
    static Logger logger = LoggerFactory.getLogger(IF.class);


    public IF() {
        super("IF", "if", "ELSEIF", "elseif");
    }

    public IF copy() {
        return new IF();
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        int i = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);
        stack.push(this);
        return i;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {

            OperationExpression operation = configurationRegistry.getOperation(tokens[i]);
            if (operation != null) {
                operation = operation.copy();

                if (condition != null) {
                    stack.push(condition);
                }
                i = operation.parse(tokens, i, stack, selfTypes, dataTypes);
                this.condition = stack.pop();
                continue;
            }

            ConditionOperationExpression conditionOperation = configurationRegistry.getConditionOperation(tokens[i]);

            if (conditionOperation != null) {
                conditionOperation = conditionOperation.copy();
                if (conditionOperation instanceof THEN) {
                    i = conditionOperation.parse(tokens, i, stack, selfTypes, dataTypes);
                    this.thenOperand = stack.pop();
                    continue;
                }

                if (conditionOperation instanceof ELSE) {
                    i = conditionOperation.parse(tokens, i, stack, selfTypes, dataTypes);
                    this.elseOperand = stack.pop();
                    continue;
                }

                if (conditionOperation instanceof IF) {
                    i = conditionOperation.parse(tokens, i, stack, selfTypes, dataTypes);
                    this.elseOperand = stack.pop();
                    return i;
                }

                if (conditionOperation instanceof ENDIF) {
                    return i;
                }

            }
        }
        return null;
    }


    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {

        List<Object> newDataSet = new java.util.ArrayList<>();

        for (Object dataRow : dataSet) {
            Boolean result = (Boolean) condition.interpret(aggregation, dataRow);
            if (result) {
                newDataSet.add(dataRow);
            }
        }

        if (!newDataSet.isEmpty()) {
            return thenOperand.interpret(aggregation, dataSet);
        }
        if (elseOperand != null) {
            return elseOperand.interpret(aggregation, dataSet);
        } else {
            return aggregation;
        }
    }

}