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


import org.dvare.ast.Node;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.interpreter.IllegalPropertyValueException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.util.ValueFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;

public abstract class OperationExpression extends Expression {
    public static final String SELF_ROW = "SELF_ROW";
    public static final String DATA_ROW = "DATA_ROW";
    public static final String selfPatten = "self\\..{1,}";
    public static final String dataPatten = "data\\..{1,}";
    protected static Logger logger = LoggerFactory.getLogger(OperationExpression.class);
    protected Expression leftOperand = null;
    protected Expression rightOperand = null;
    protected DataTypeExpression dataTypeExpression;
    protected Expression leftValueOperand;
    protected Expression rightValueOperand;
    protected String leftOperandType;
    protected String rightOperandType;

    protected OperationType operationType;

    public OperationExpression(OperationType operationType) {
        this.operationType = operationType;
    }

    public List<String> getSymbols() {
        return this.operationType.getSymbols();
    }

    public abstract OperationExpression copy();


    public abstract Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException;

    public abstract Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException;

    public abstract Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException;

    public abstract Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException;


    protected Object getValue(Object object, String name) throws IllegalPropertyValueException {
        return ValueFinder.findValue(name, object);
    }

    protected Object setValue(Object object, String name, Object value) throws IllegalPropertyValueException {
        return ValueFinder.updateValue(object, name, value);
    }


    protected LiteralExpression toLiteralExpression(Expression expression) {

        LiteralExpression leftExpression = null;
        if (expression instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) expression;
            leftExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
        } else if (expression instanceof LiteralExpression) {
            leftExpression = (LiteralExpression) expression;
        }
        return leftExpression;
    }


    public Node<String> AST() {

        Node<String> root = new Node<String>(this.toString());

        root.left = new Node<String>(this.leftOperand.toString());

        root.right = new Node<String>(this.rightOperand.toString());

        return root;
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
}
