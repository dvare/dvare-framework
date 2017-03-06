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


package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.validation.LeftPriority;
import org.dvare.expression.operation.validation.RightPriority;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.PUT_EXP)
public class PutExpOperation extends OperationExpression {


    public PutExpOperation() {
        super(OperationType.PUT_EXP);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, expressionBinding, contexts);


        return pos;
    }

    @Override

    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        Stack<Expression> localStack = new Stack<>();
        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                if (op instanceof RightPriority) {
                    List<Expression> expressionList = new ArrayList<>(localStack);

                    if (expressionList.size() == 2) {

                        Expression namedExpression = expressionList.get(0);
                        if (namedExpression instanceof NamedExpression) {
                            Expression expression = expressionList.get(1);

                            if (expressionBinding != null) {
                                expressionBinding.addExpression(((NamedExpression) namedExpression).getName(), expression);
                            } else {
                                throw new ExpressionParseException("Exression Binding is null");
                            }

                        }
                    }

                    return pos;
                } else if (!(op instanceof LeftPriority)) {
                    pos = op.parse(tokens, pos, localStack, expressionBinding, contexts);
                }
            } else {

                TokenType tokenType = findDataObject(token, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                    VariableType.getVariableType(tokenType.token, variableType, tokenType.type);

                    localStack.add(buildExpression(token, contexts));
                } else {
                    localStack.add(new NamedExpression(token));
                }


            }
        }
        return pos;
    }


}


// registerExpression(testVariable, data.test->values()->first())
// getExpression(testVariable)