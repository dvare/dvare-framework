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
import org.dvare.expression.literal.LiteralDataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.validation.RightPriority;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class ChainOperationExpression extends OperationExpression {

    protected List<Expression> rightOperand = new ArrayList<>();

    public ChainOperationExpression(OperationType operationType) {
        super(operationType);
    }


    private int parseOperands(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        String token = tokens[pos - 1];
        pos = pos + 1;


        if (stack.isEmpty()) {
            DataType variableType = null;


            if (token.matches(selfPatten) || token.matches(dataPatten)) {

                if (dataTypes != null && token.matches(dataPatten)) {
                    leftOperandType = DATA_ROW;
                    token = token.substring(5, token.length());
                    variableType = TypeFinder.findType(token, dataTypes);

                }
                if (token.matches(selfPatten)) {
                    leftOperandType = SELF_ROW;
                    token = token.substring(5, token.length());
                    variableType = TypeFinder.findType(token, selfTypes);
                }


                VariableExpression variableExpression = VariableType.getVariableType(token, variableType);
                this.leftOperand = variableExpression;
            } else {

                if (selfTypes.getTypes().containsKey(token)) {
                    leftOperandType = SELF_ROW;
                    variableType = TypeFinder.findType(token, selfTypes);
                    VariableExpression variableExpression = VariableType.getVariableType(token, variableType);
                    this.leftOperand = variableExpression;
                } else {

                    LiteralExpression literalExpression = null;
                    if (token.equals("[")) {
                        List<String> values = new ArrayList<>();
                        while (!tokens[++pos].equals("]")) {
                            String value = tokens[pos];
                            values.add(value);
                        }
                        literalExpression = LiteralType.getLiteralExpression(values.toArray(new String[values.size()]), variableType);
                    } else {
                        literalExpression = LiteralType.getLiteralExpression(token);
                    }

                    this.leftOperand = literalExpression;
                }

            }

        } else {
            this.leftOperand = stack.pop();


        }

        return pos;
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes) throws ExpressionParseException {
        pos = parse(tokens, pos, stack, selfTypes, null);
        return pos;
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        pos = parseOperands(tokens, pos, stack, selfTypes, dataTypes);
        pos = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);


        logger.debug("Operation Expression Call Expression : {}", getClass().getSimpleName());

  /*      if (this.leftOperand instanceof LogicalOperationExpression) {
            LogicalOperationExpression logicalPart = (LogicalOperationExpression) this.leftOperand;
            this.leftOperand = logicalPart.rightOperand;
            logicalPart.rightOperand = this;;
            stack.push(logicalPart);
        } else*/
        {
            stack.push(this);
        }


        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes) throws ExpressionParseException {

        return findNextExpression(tokens, pos, stack, selfTypes, null);
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {
            String token = tokens[i];
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                op = op.copy();
                if (op.getClass().equals(RightPriority.class)) {
                    return i;
                }
            } else {
                DataType type = LiteralDataType.computeDataType(token);
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(token, type);
                rightOperand.add(literalExpression);
            }
        }
        return null;
    }


    public void interpretOperand(final Object selfRow, final Object dataRow) throws InterpretException {


        Object leftDataRow;
        if (leftOperandType != null && leftOperandType.equals(SELF_ROW)) {
            leftDataRow = selfRow;
        } else if (leftOperandType != null && leftOperandType.equals(DATA_ROW)) {
            leftDataRow = dataRow;
        } else {
            leftDataRow = selfRow;
        }

        Expression leftExpression = null;
        Expression left = this.leftOperand;
        if (left instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) left;

            LiteralExpression literalExpression = null;
            if (selfRow != null && dataRow != null) {
                literalExpression = (LiteralExpression) operation.interpret(selfRow, dataRow);
            } else {
                literalExpression = (LiteralExpression) operation.interpret(selfRow);
            }

            if (literalExpression != null) {
                dataTypeExpression = literalExpression.getType();
            }
            leftExpression = literalExpression;

        } else if (left instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) left;
            variableExpression = VariableType.setVariableValue(variableExpression, leftDataRow);
            if (variableExpression != null) {
                dataTypeExpression = variableExpression.getType();
            }
            leftExpression = variableExpression;
        } else if (left instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) left;
            if (literalExpression != null) {
                dataTypeExpression = literalExpression.getType();
            }
            leftExpression = literalExpression;

        }

        this.leftValueOperand = leftExpression;

    }


}