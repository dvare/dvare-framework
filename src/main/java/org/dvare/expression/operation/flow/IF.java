/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.dvare.expression.operation.flow;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.ConditionOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

@Operation(type = OperationType.IF)
public class IF extends ConditionOperationExpression {
    static Logger logger = LoggerFactory.getLogger(IF.class);


    public IF() {
        super(OperationType.IF);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, expressionBinding, contexts);
        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (; pos < tokens.length; pos++) {

            OperationExpression operation = configurationRegistry.getOperation(tokens[pos]);
            if (operation != null) {


                if (operation instanceof IF || operation instanceof ELSE || operation instanceof THEN || operation instanceof ENDIF) {

                    if (operation instanceof THEN) {
                        pos = operation.parse(tokens, pos, stack, expressionBinding, contexts);
                        this.thenOperand = stack.pop();
                        continue;
                    }

                    if (operation instanceof ELSE) {
                        pos = operation.parse(tokens, pos, stack, expressionBinding, contexts);
                        this.elseOperand = stack.pop();
                        return pos;
                    }

                    /*if (operation instanceof IF) {
                        pos = operation.parse(tokens, pos, stack, contexts);
                        this.elseOperand = stack.pop();
                        return pos;
                    }*/

                    if (operation instanceof ENDIF) {
                        return pos;
                    }

                } else {


                    if (condition != null) {
                        stack.push(condition);
                    }
                    pos = operation.parse(tokens, pos, stack, expressionBinding, contexts);
                    this.condition = stack.pop();
                }
            }
        }
        return pos;
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        Boolean result = toBoolean(condition.interpret(expressionBinding, instancesBinding));
        if (result) {
            return thenOperand.interpret(expressionBinding, instancesBinding);
        } else if (elseOperand != null) {
            return elseOperand.interpret(expressionBinding, instancesBinding);
        }
        return result;
    }


}