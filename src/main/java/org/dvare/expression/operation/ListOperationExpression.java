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

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.List)
public class ListOperationExpression extends OperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(ListOperationExpression.class);

    List<Expression> expressions = new ArrayList<>();

    public ListOperationExpression() {
        super(OperationType.List);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {


        List<String> listPrams = new ArrayList<>();
        while (!tokens[pos].equals("]")) {
            String value = tokens[pos];
            if (!value.equals("[")) {
                listPrams.add(value);
            }
            pos++;
        }


        String values[] = listPrams.toArray(new String[listPrams.size()]);

        Stack<Expression> localStack = new Stack<>();
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = 0; i < values.length; i++) {
            String token = values[i];


            TokenType tokenType = findDataObject(token, contexts);


            Expression expression = null;
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {


                i = op.parse(values, i, localStack, contexts);

                expression = localStack.pop();

            } else if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                TypeBinding typeBinding = contexts.getContext(tokenType.type);
                DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                expression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);
            } else {
                expression = LiteralType.getLiteralExpression(tokenType.token);

            }


            localStack.push(expression);


        }

        expressions.addAll(localStack);


        stack.push(this);

        return pos;
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {
        Class<? extends DataTypeExpression> dataType = null;
        List<Object> values = new ArrayList<>();
        for (Expression expression : expressions) {

            if (expression instanceof OperationExpression) {
                OperationExpression operationExpression = (OperationExpression) expression;

                Object interpret = operationExpression.interpret(instancesBinding);

                if (interpret instanceof LiteralExpression) {
                    LiteralExpression literalExpression = (LiteralExpression) interpret;
                    values.add(literalExpression.getValue());

                    if (dataType == null || toDataType(dataType).equals(DataType.NullType)) {
                        dataType = literalExpression.getType();
                    }
                } else {
                    values.add(interpret);
                }


            } else if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;
                Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
                variableExpression = VariableType.setVariableValue(variableExpression, instance);
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
                values.add(literalExpression.getValue());
                dataType = variableExpression.getType();
            } else if (expression instanceof LiteralExpression) {
                LiteralExpression literalExpression = (LiteralExpression) expression;
                values.add(literalExpression.getValue());
                if (dataType == null || toDataType(dataType).equals(DataType.NullType)) {
                    dataType = literalExpression.getType();
                }
            }


        }


        ListLiteral listLiteral = new ListLiteral(values, dataType);
        if (logger.isDebugEnabled()) {
            logger.debug("List Literal Expression : {} [{}]", toDataType(listLiteral.getType()), listLiteral.getValue());
        }
        return listLiteral;
    }

    public boolean isEmpty() {
        return expressions.isEmpty();
    }

    public Integer getSize() {
        return expressions.size();
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        return pos;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        Iterator<Expression> iterator = expressions.iterator();

        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next().toString());
            if (iterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");

        return stringBuilder.toString();


    }


}
