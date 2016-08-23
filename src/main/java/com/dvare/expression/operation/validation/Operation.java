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
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public abstract class Operation extends Expression {

    protected List<String> symbols = new ArrayList<>();
    protected Expression leftOperand = null;
    protected Expression rightOperand = null;

    public Operation(String symbol) {
        this.symbols.add(symbol);
    }

    public Operation(List<String> symbols) {
        this.symbols.addAll(symbols);
    }

    public Operation(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
    }

    public List<String> getSymbols() {
        return this.symbols;
    }

    public abstract Operation copy();

    public abstract Node<String> AST();


    public abstract int parse(String[] tokens, int pos, Stack<Expression> stack, Class type) throws ExpressionParseException;

    public abstract Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, Class type) throws ExpressionParseException;


}
