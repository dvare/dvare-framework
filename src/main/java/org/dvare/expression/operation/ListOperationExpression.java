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
import java.util.List;
import java.util.Stack;

public class ListOperationExpression extends OperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(ListOperationExpression.class);

    List<Expression> expressions = new ArrayList<>();

    public ListOperationExpression() {
        super(null);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {


        List<String> listPrams = new ArrayList<>();
        while (!tokens[++pos].equals("]")) {
            String value = tokens[pos];
            listPrams.add(value);
        }


        String values[] = listPrams.toArray(new String[listPrams.size()]);

        Stack<Expression> localStack = new Stack<>();
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = 0; i < values.length; i++) {
            String token = values[i];


            String typeString = findDataObject(token, selfTypes, dataTypes);
            String typeStringTokens[] = typeString.split(":");
            String operandType = null;
            if (typeStringTokens.length == 2) {
                token = typeStringTokens[0];
                operandType = typeStringTokens[1];
            }

            Expression expression = null;
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                op = op.copy();

                if (dataTypes != null) {
                    i = op.parse(values, i, localStack, selfTypes, dataTypes);
                } else {
                    i = op.parse(values, i, localStack, selfTypes);
                }

                expression = localStack.pop();

            } else if (operandType != null && operandType.equals(SELF_ROW) && selfTypes.getTypes().containsKey(token)) {
                DataType variableType = TypeFinder.findType(token, selfTypes);
                expression = VariableType.getVariableType(token, variableType, operandType);
            } else if (operandType != null && operandType.equals(DATA_ROW) && dataTypes.getTypes().containsKey(token)) {
                DataType variableType = TypeFinder.findType(token, dataTypes);
                expression = VariableType.getVariableType(token, variableType, operandType);
            } else {
                if (token.equals("[")) {

                    i = new ListOperationExpression().parse(values, i, localStack, selfTypes, dataTypes);
                    expression = localStack.pop();


                } else {
                    expression = LiteralType.getLiteralExpression(token);
                }
            }


            localStack.push(expression);


        }

        expressions.addAll(localStack);



        stack.push(this);

        return pos;
    }


    public Object interpretListExpression(final Object selfRow, final Object dataRow) throws InterpretException {

        DataTypeExpression dataType = null;
        List<Object> values = new ArrayList<>();
        for (Expression expression : expressions) {

            if (expression instanceof OperationExpression) {
                OperationExpression operationExpression = (OperationExpression) expression;

                Object interpret = null;
                if (dataRow == null) {
                    interpret = operationExpression.interpret(selfRow);

                } else {
                    interpret = operationExpression.interpret(selfRow, dataRow);
                }

                if (interpret instanceof LiteralExpression) {
                    LiteralExpression literalExpression = (LiteralExpression) interpret;
                    values.add(literalExpression.getValue());

                    if (dataType == null) {
                        dataType = literalExpression.getType();
                    }
                } else {
                    values.add(interpret);
                }


            } else if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;

                if (dataRow != null && variableExpression.getOperandType().equals(DATA_ROW)) {
                    variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
                    LiteralExpression literalExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
                    values.add(literalExpression.getValue());
                    dataType = variableExpression.getType();
                } else {
                    variableExpression = VariableType.setVariableValue(variableExpression, selfRow);
                    LiteralExpression literalExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
                    values.add(literalExpression.getValue());
                    dataType = variableExpression.getType();
                }


            } else if (expression instanceof LiteralExpression) {
                LiteralExpression literalExpression = (LiteralExpression) expression;
                values.add(literalExpression.getValue());
                if (dataType == null) {
                    dataType = literalExpression.getType();
                }
            }


        }


        ListLiteral listLiteral = new ListLiteral<List>(values, dataType, values.size());

        logger.debug("List Literal Expression : {} [{}]", listLiteral.getType().getDataType(), listLiteral.getValue());

        return listLiteral;
    }


    @Override
    public Object interpret(final Object selfRow) throws InterpretException {
        return interpretListExpression(selfRow, null);
    }

    @Override
    public Object interpret(final Object selfRow, final Object dataRow) throws InterpretException {
        return interpretListExpression(selfRow, dataRow);
    }

    public boolean isEmpty() {
        return expressions.isEmpty();
    }

    public Integer getSize() {
        return expressions.size();
    }


    @Override
    public OperationExpression copy() {
        return null;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {
        return null;
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes) throws ExpressionParseException {
        return null;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        return null;
    }


}
