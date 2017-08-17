/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.dvare.binding.expression;

import org.dvare.expression.Expression;

import java.util.HashMap;


public class ExpressionBinding {
    private HashMap<String, Expression> expressions = new HashMap<>();

    public ExpressionBinding() {

    }

    public ExpressionBinding(HashMap<String, Expression> expressions) {
        if (expressions != null) {
            this.expressions = expressions;
        }
    }

    public void addExpression(String name, Expression expression) {
        if (name != null) {
            expressions.put(name, expression);
        }

    }

    public Expression getExpression(String name) {
        if (expressions.containsKey(name)) {
            return expressions.get(name);
        }
        return null;
    }

    public void removeExpression(String name) {
        if (expressions.containsKey(name)) {
            expressions.remove(name);
        }
    }


    public HashMap<String, Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(HashMap<String, Expression> expressions) {
        this.expressions = expressions;
    }
}
