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


package com.dvare.expression.operation.validation;

import com.dvare.ast.Node;
import com.dvare.config.ConfigurationRegistry;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;
import com.dvare.expression.datatype.DataType;
import com.dvare.expression.literal.LiteralExpression;
import com.dvare.expression.veriable.VariableExpression;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public abstract class OperationExpression extends Operation {
    static Logger logger = LoggerFactory.getLogger(OperationExpression.class);

    public OperationExpression(String symbol) {
        super(symbol);
    }

    public OperationExpression(List<String> symbols) {
        super(symbols);
    }

    public OperationExpression(String... symbols) {
        super(symbols);
    }


    private DataType typeMapping(Class type) {

        String simpleName = type.getSimpleName();


        if (simpleName.equals("int")) {
            return DataType.IntegerType;
        }

        simpleName = simpleName.substring(0, 1).toUpperCase() +
                simpleName.substring(1).toLowerCase();
        simpleName = simpleName + "Type";

        DataType dataType = DataType.valueOf(simpleName);

        return dataType;
    }


    protected DataType findType(String name, Class type) {
        DataType variableType = null;
        if (name.contains(".")) {

            String fields[] = name.split(".");

            Iterator<String> iterator = Arrays.asList(fields).iterator();

            Class childType = type;
            while (iterator.hasNext()) {
                String field = iterator.next();

                if (iterator.hasNext()) {

                    Field newType = FieldUtils.getDeclaredField(childType, field, true);
                    childType = newType.getType();

                } else {
                    Field newType = FieldUtils.getDeclaredField(childType, field, true);
                    if (newType != null) {
                        variableType = typeMapping(newType.getType());
                    }
                }

            }


        } else {
            String field = name;
            Field newType = FieldUtils.getDeclaredField(type, field, true);
            if (newType != null) {
                variableType = typeMapping(newType.getType());

            }
        }
        return variableType;
    }

    public int parse(String[] tokens, int pos, Stack<Expression> stack, Class type) throws ExpressionParseException {
        Expression left = stack.pop();
        int i = findNextExpression(tokens, pos + 1, stack, type);
        Expression right = stack.pop();

        this.leftOperand = left;
        this.rightOperand = right;

        logger.debug("Operation Call Expression : {}", getClass().getSimpleName());

        stack.push(this);

        return i;
    }


    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, Class type) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {

            Operation op = configurationRegistry.getValidationOperation(tokens[i]);
            if (op != null) {
                op = op.copy();


                // we found an operation
                i = op.parse(tokens, i, stack, type);


                return i;
            }
        }
        return null;
    }


    public Node<String> AST() {

        Node<String> root = new Node<String>(this.getClass().getSimpleName());

        root.left = ASTNusted(this.leftOperand);

        root.right = ASTNusted(this.rightOperand);

        return root;
    }

    private Node<String> ASTNusted(Expression expression) {

        Node root;
        if (expression instanceof Operation) {
            Operation operation = (Operation) expression;

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
}