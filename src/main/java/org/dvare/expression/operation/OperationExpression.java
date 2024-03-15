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
import org.dvare.expression.ExpressionVisitor;
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

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class OperationExpression extends Expression {
    protected static Logger logger = LoggerFactory.getLogger(OperationExpression.class);
    protected OperationType operationType;
    protected Expression leftOperand = null;
    protected Expression rightOperand = null;
    protected Class<? extends DataTypeExpression> dataTypeExpression;


    public OperationExpression(OperationType operationType) {
        this.operationType = operationType;

    }


    public OperationExpression(OperationType operationType, Expression leftOperand) {
        this.operationType = operationType;
        this.leftOperand = leftOperand;
    }


    public OperationExpression(OperationType operationType, Expression leftOperand, Expression rightOperand) {
        this.operationType = operationType;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    protected static TokenType buildTokenType(String token) {
        TokenType tokenType = new TokenType();
        final String selfPatten = ".{1,}\\..{1,}";
        if (!token.matches(selfPatten)) {
            tokenType.token = token;
            tokenType.type = "self";
        } else {
            tokenType.type = token.substring(0, token.indexOf("."));
            tokenType.token = token.substring(token.indexOf(".") + 1, token.length());
        }
        return tokenType;
    }

    public static TokenType findDataObject(String token, ContextsBinding contexts) {

        TokenType tokenType = buildTokenType(token);
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


    protected Expression buildExpression(String token, ContextsBinding contextsBinding, int pos, String[] tokens) throws IllegalPropertyException, IllegalValueException {
        TokenType tokenType = findDataObject(token, contextsBinding);
        if (tokenType.type != null && contextsBinding.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contextsBinding.getContext(tokenType.type)) != null) {
            TypeBinding typeBinding = contextsBinding.getContext(tokenType.type);
            DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
            return VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);


        } else {
            return LiteralType.getLiteralExpression(token, pos, tokens);
        }
    }


    protected Object getValue(Object object, String name) throws IllegalPropertyValueException {
        return ValueFinder.findValue(name, object);
    }

    protected Object setValue(Object object, String name, Object value) throws IllegalPropertyValueException {
        return ValueFinder.updateValue(object, name, value);
    }


    protected DataType toDataType(Class<?> dataTypeExpression) {
        if (dataTypeExpression.isAnnotationPresent(Type.class)) {
            Type type = dataTypeExpression.getAnnotation(Type.class);
            return type.dataType();
        }
        return null;
    }


    public LiteralExpression<?> interpretOperand(Expression expression, InstancesBinding instancesBinding) throws InterpretException {

        LiteralExpression<?> literalExpression;
        if (expression instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) expression;
            literalExpression = operation.interpret(instancesBinding);
        } else if (expression instanceof VariableExpression) {
            VariableExpression<?> variableExpression = (VariableExpression<?>) expression;
            literalExpression = variableExpression.interpret(instancesBinding);
        } else if (expression instanceof LiteralExpression) {
            literalExpression = (LiteralExpression<?>) expression;
        } else {
            literalExpression = new NullLiteral<>();
        }

        if (!(literalExpression instanceof NullLiteral)) {
            dataTypeExpression = literalExpression.getType();
        }


        return literalExpression;

    }

    protected boolean toBoolean(LiteralExpression<?> interpret) {
        boolean result = false;
        if (interpret != null) {
            if (!(interpret instanceof NullLiteral) && interpret.getValue() != null) {
                Object value = interpret.getValue();
                if (value instanceof Boolean) {
                    result = (Boolean) value;
                }
            }

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

        toStringBuilder.append(operationType.getTokens().get(0));
        toStringBuilder.append(" ");

        if (rightOperand != null) {
            toStringBuilder.append(rightOperand.toString());
            toStringBuilder.append(" ");
        }

        return toStringBuilder.toString();
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(Expression leftOperand) {
        this.leftOperand = leftOperand;
    }

    /*Getter and Setters*/

    public Expression getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(Expression rightOperand) {
        this.rightOperand = rightOperand;
    }

    public List<String> getSymbols() {
        return this.operationType.getTokens();
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        leftOperand.accept(v);
        rightOperand.accept(v);
        v.visit(this);
    }

    protected enum Side {
        Left, Right;
    }

    public static class TokenType {
        public String type;
        public String token;
    }

}