/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.expression.operation;

import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.utility.EndForAll;
import org.dvare.expression.operation.utility.EndForEach;
import org.dvare.expression.operation.utility.LeftPriority;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;

import java.util.ArrayList;
import java.util.Stack;

public class IterationOperationExpression extends OperationExpression {

    protected Expression referenceContext;
    protected NamedExpression derivedContext;

    public IterationOperationExpression(OperationType operationType) {
        super(operationType);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contextsBinding) throws ExpressionParseException {

        String derivedContextsBinding;

        pos++;

        if (pos + 2 < tokens.length && tokens[pos + 2].equals("|")) {

            referenceContext = new NamedExpression(tokens[pos++]);
            derivedContext = new NamedExpression(tokens[pos++]);

        } else if (pos + 1 < tokens.length && tokens[pos + 1].equals("|")) {
            referenceContext = new NamedExpression("self");
            derivedContext = new NamedExpression(tokens[pos]);
        }
        pos++;


        TokenType tokenType = findDataObject(((NamedExpression) referenceContext).getName(), contextsBinding);

        //variableForAll
        if (tokenType.type != null && contextsBinding.getContext(tokenType.type) != null &&
                TypeFinder.findType(tokenType.token, contextsBinding.getContext(tokenType.type)) != null) {


            DataType variableType = TypeFinder.findType(tokenType.token, contextsBinding.getContext(tokenType.type));
            VariableExpression variableExpression = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);

            derivedContextsBinding = variableExpression.getName();
            referenceContext = variableExpression;


            String parts[] = derivedContext.getName().split(":");
            if (parts.length == 2) {

                String name = parts[0].trim();
                String type = parts[1].trim();


                DataType dataType = DataType.valueOf(type);

                final String selfPatten = ".{1,}\\..{1,}";
                if (!name.matches(selfPatten)) {
                    TypeBinding typeBinding = contextsBinding.getContext(name);
                    if (typeBinding == null) {
                        typeBinding = new TypeBinding();
                    }
                    typeBinding.addTypes(name, dataType);
                    contextsBinding.addContext(derivedContextsBinding, typeBinding);
                } else {
                    TokenType derivedTokenType = buildTokenType(name);
                    TypeBinding typeBinding = contextsBinding.getContext(derivedTokenType.type);
                    if (typeBinding == null) {
                        typeBinding = new TypeBinding();
                    }
                    typeBinding.addTypes(derivedTokenType.token, dataType);
                    contextsBinding.addContext(derivedTokenType.type, typeBinding);
                }


                derivedContext = new NamedExpression(name);
            }


        } else {
            //context forALL
            TypeBinding typeBinding = contextsBinding.getContext(((NamedExpression) referenceContext).getName());
            if (contextsBinding.getContext(((NamedExpression) referenceContext).getName()) != null) {

                derivedContextsBinding = derivedContext.getName();

                contextsBinding.addContext(derivedContextsBinding, typeBinding);

            }
        }

        pos = findNextExpression(tokens, pos, stack, contextsBinding);

        this.leftOperand = stack.pop();

        //contextsBinding.removeContext(derivedContextsBinding);


        if (logger.isDebugEnabled()) {
            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());

        }
        stack.push(this);


        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack
            , ContextsBinding contexts) throws ExpressionParseException {

        Stack<Expression> localStack = new Stack<>();
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                if (op.getClass().equals(EndForAll.class) || op.getClass().equals(EndForEach.class)) {
                    stack.add(new CompositeOperationExpression(new ArrayList<>(localStack)));
                    //stack.push(localStack.pop());
                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, localStack, contexts);
                }
            } else {


                localStack.add(buildExpression(token, contexts));

            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }

}
