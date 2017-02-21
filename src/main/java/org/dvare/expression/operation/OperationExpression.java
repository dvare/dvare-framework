/*The MIT License (MIT)

Copyright (c) 2016-2017 Muhammad Hammad

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


import org.dvare.annotations.Type;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.interpreter.IllegalPropertyValueException;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.dvare.util.ValueFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;

public abstract class OperationExpression extends Expression {
    protected static Logger logger = LoggerFactory.getLogger(OperationExpression.class);
    protected OperationType operationType;
    protected Expression leftOperand = null;
    protected Expression rightOperand = null;
    protected Class<? extends DataTypeExpression> dataTypeExpression;
    protected Expression leftValueOperand;
    protected Expression rightValueOperand;


    public OperationExpression(OperationType operationType) {
        this.operationType = operationType;

    }

    public static TokenType findDataObject(String token, ContextsBinding contexts) {
        TokenType tokenType = new TokenType();
        final String selfPatten = ".{1,}\\..{1,}";
        if (!token.matches(selfPatten)) {
            tokenType.token = token;
            tokenType.type = "self";
        } else {
            tokenType.type = token.substring(0, token.indexOf("."));
            tokenType.token = token.substring(token.indexOf(".") + 1, token.length());
        }


        if (contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
            return tokenType;
        } else {

            for (String key : contexts.getContextNames()) {

                if (!key.equals("self")) {
                    TypeBinding typeBinding = contexts.getContext(key);
                    if (typeBinding != null && typeBinding.getDataType(token) != null) {


                        tokenType.type = key;
                        tokenType.token = token;

                        return tokenType;

                    }

                }

            }


        }

        tokenType.type = null;
        tokenType.token = token;

        return tokenType;
    }

    public abstract Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException;

    public abstract Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException;

    protected Expression buildExpression(String token, ContextsBinding contextsBinding) throws IllegalPropertyException, IllegalValueException {
        TokenType tokenType = findDataObject(token, contextsBinding);
        if (tokenType.type != null && contextsBinding.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contextsBinding.getContext(tokenType.type)) != null) {
            TypeBinding typeBinding = contextsBinding.getContext(tokenType.type);
            DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
            return VariableType.getVariableType(tokenType.token, variableType, tokenType.type);


        } else {
            return LiteralType.getLiteralExpression(token);
        }
    }


    protected Object getValue(Object object, String name) throws IllegalPropertyValueException {
        return ValueFinder.findValue(name, object);
    }

    protected Object setValue(Object object, String name, Object value) throws IllegalPropertyValueException {
        return ValueFinder.updateValue(object, name, value);
    }


    protected DataType toDataType(Class<? extends DataTypeExpression> dataTypeExpression) {
        if (dataTypeExpression.isAnnotationPresent(Type.class)) {
            Type type = dataTypeExpression.getAnnotation(Type.class);
            return type.dataType();
        }
        return null;
    }


    public Expression interpretOperand(Expression expression, InstancesBinding instancesBinding) throws InterpretException {


        Expression leftExpression = null;

        if (expression instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) expression;

            LiteralExpression literalExpression;
            literalExpression = (LiteralExpression) operation.interpret(instancesBinding);

            if (literalExpression != null) {
                dataTypeExpression = literalExpression.getType();
            }
            leftExpression = literalExpression;

        } else if (expression instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) expression;
            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
            if (instance instanceof List) {
                instance = ((List) instance).isEmpty() ? null : ((List) instance).get(0);
            }
            variableExpression = VariableType.setVariableValue(variableExpression, instance);
            if (variableExpression != null) {
                dataTypeExpression = variableExpression.getType();
            }
            leftExpression = variableExpression;
        } else if (expression instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) expression;
            dataTypeExpression = literalExpression.getType();
            leftExpression = literalExpression;

        }

        return leftExpression;

    }

    protected LiteralExpression toLiteralExpression(Expression expression) throws InterpretException {


        LiteralExpression leftExpression = null;
        if (expression instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) expression;
            leftExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
        } else if (expression instanceof LiteralExpression) {
            leftExpression = (LiteralExpression) expression;
        }
        return leftExpression;
    }

    protected Boolean toBoolean(Object interpret) {
        Boolean result = false;
        if (interpret instanceof LiteralExpression) {


            if (!(interpret instanceof NullLiteral) && ((LiteralExpression) interpret).getValue() != null) {
                result = (Boolean) ((LiteralExpression) interpret).getValue();
            }

        } else if (interpret != null && interpret instanceof Boolean) {
            result = (Boolean) interpret;
        }
        return result;
    }


    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" ");
        }

        toStringBuilder.append(operationType.getSymbols().get(0));
        toStringBuilder.append(" ");

        if (rightOperand != null) {
            toStringBuilder.append(rightOperand.toString());
            toStringBuilder.append(" ");
        }

        return toStringBuilder.toString();
    }




    /*Getter and Setters*/

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(Expression leftOperand) {
        this.leftOperand = leftOperand;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(Expression rightOperand) {
        this.rightOperand = rightOperand;
    }

    public List<String> getSymbols() {
        return this.operationType.getSymbols();
    }

    public static class TokenType {
        public String type;
        public String token;
    }

}
