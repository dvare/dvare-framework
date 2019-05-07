/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2019 DVARE (Data Validation and Aggregation Rule Engine)
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
package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.expression.Expression;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.LET)
public class LetOperation extends OperationExpression {


    public LetOperation() {
        super(OperationType.LET);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, contexts);


        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        String token = tokens[pos];


        String parts[] = token.split(":");
        if (parts.length == 2) {

            String name = parts[0].trim();
            String type = parts[1].trim();
            TokenType tokenType = buildTokenType(name);

            DataType dataType = DataType.valueOf(type);


            if (tokenType.type.equals("self")) {
                tokenType.type = "tmp";
            }

            TypeBinding typeBinding = contexts.getContext(tokenType.type);
            if (typeBinding == null) {
                typeBinding = new TypeBinding();
            }

            if (typeBinding.getDataType(tokenType.token) == null) {
                typeBinding.addTypes(tokenType.token, dataType);
                contexts.addContext(tokenType.type, typeBinding);
            }/*else {
                    throw new ExpressionParseException(tokenType.type + "context already contains " + tokenType.token + " variable ");
                }*/


            leftOperand = new NamedExpression(token);

                /*VariableExpression variableExpression = VariableType.getVariableExpression(name, dataType, "temp");
                stack.push(variableExpression);*/

            stack.push(this);

        } else {
            throw new ExpressionParseException("\"" + token + "\" DataType is missing");
        }


        return pos;
    }


    public VariableExpression getVariableExpression() throws InterpretException {


        if (NamedExpression.class.cast(leftOperand).getName().contains(":")) {
            String parts[] = NamedExpression.class.cast(leftOperand).getName().split(":");
            String name = parts[0].trim();
            String type = parts[1].trim();
            TokenType tokenType = buildTokenType(name);
            DataType dataType = DataType.valueOf(type);
            try {
                if (tokenType.type.equals("self")) {
                    tokenType.type = "tmp";
                }
                return VariableType.getVariableExpression(tokenType.token, dataType, tokenType.type);
            } catch (IllegalPropertyException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {

        VariableExpression variableExpression = getVariableExpression();

        if (variableExpression != null) {
            return variableExpression.interpret(instancesBinding);
        }
        return new NullLiteral();
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        toStringBuilder.append(operationType.getSymbols().get(0));
        toStringBuilder.append(" ");

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" ");
        }


        return toStringBuilder.toString();
    }


}