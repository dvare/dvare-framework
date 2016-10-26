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
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.util.ValueFinder;

import java.util.List;
import java.util.Stack;

public abstract class OperationExpression extends Expression {

    protected final String SELF = "self";
    protected final String DATA = "data";
    protected final String selfPatten = "self\\..{1,}";
    protected final String dataPatten = "data\\..{1,}";
    protected Expression leftOperand = null;
    protected Expression rightOperand = null;
    protected String leftType;
    protected String rightType;

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


    public Node<String> AST() {

        Node<String> root = new Node<String>(this.getClass().getSimpleName());

        root.left = ACTNested(this.leftOperand);

        root.right = ACTNested(this.rightOperand);

        return root;
    }

    private Node<String> ACTNested(Expression expression) {

        Node root;
        if (expression instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) expression;

            root = operation.AST();
        } else if (expression instanceof VariableExpression<?>) {
            VariableExpression variableExpression = (VariableExpression) expression;

            root = new Node<String>(variableExpression.getName());
        } else if (expression instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) expression;
            root = new Node<String>(literalExpression.getValue().toString());
        } else {
            root = new Node<String>("Value");
        }


        return root;
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
