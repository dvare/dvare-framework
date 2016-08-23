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


package com.dvare.binding.rule;


import com.dvare.action.ActionDispatcher;
import com.dvare.action.NullActionDispatcher;
import com.dvare.expression.Expression;

public class RuleBinding {
    private String name;
    private String rawExpression;
    private Expression expression;
    private ActionDispatcher dispatcher = new NullActionDispatcher();

    public RuleBinding(Expression expression) {
        this.expression = expression;
    }


    public RuleBinding(Expression expression, ActionDispatcher dispatcher) {
        this.expression = expression;
        this.dispatcher = dispatcher;

    }

        /* Getter and Setter */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public ActionDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(ActionDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getRawExpression() {
        return rawExpression;
    }

    public void setRawExpression(String rawExpression) {
        this.rawExpression = rawExpression;
    }


}
