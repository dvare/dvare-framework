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


package org.dvare.expression;


import org.dvare.binding.function.FunctionBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FunctionExpression extends Expression {
    private static Logger logger = LoggerFactory.getLogger(FunctionExpression.class);
    public FunctionBinding binding;
    private String name;
    private List<Expression> parameters = new ArrayList<>();


    public FunctionExpression(String name, FunctionBinding binding) {
        this.name = name;
        this.binding = binding;
        if (logger.isDebugEnabled()) {
            logger.debug("FunctionService Name  Expression :  [{} {}]", name, binding.getReturnType().getDataType());
        }
    }

    public void addParameter(Expression parameter) {
        this.parameters.add(parameter);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Expression> getParameters() {
        return parameters;
    }

    public void setParameters(List<Expression> parameters) {
        this.parameters = parameters;
    }

    public FunctionBinding getBinding() {
        return binding;
    }

    public void setBinding(FunctionBinding binding) {
        this.binding = binding;
    }


}
