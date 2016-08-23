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


package com.dvare.expression.literal;

import com.dvare.expression.Expression;
import com.dvare.expression.datatype.DataTypeExpression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiteralExpression<T> extends Expression {


    protected final Map<Class, String> legalOperations = new HashMap<Class, String>();
    protected T value;
    protected DataTypeExpression type;

    LiteralExpression(T value, DataTypeExpression type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return this.value;
    }

    public DataTypeExpression getType() {
        return this.type;
    }

    public List<?> legalOperations() {

        return Arrays.asList(legalOperations.keySet().toArray());
    }

    public boolean isLegalOperations(Class operation) {
        if (operation == null) {
            return false;
        }
        return legalOperations.containsKey(operation);
    }


}