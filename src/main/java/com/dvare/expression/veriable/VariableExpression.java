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


package com.dvare.expression.veriable;

import com.dvare.expression.Expression;
import com.dvare.expression.datatype.DataTypeExpression;

public abstract class VariableExpression<T> extends Expression {

    protected String name;
    protected DataTypeExpression type;
    protected T value;
    protected boolean list;
    protected Integer listSize;

    VariableExpression(String name, DataTypeExpression type) {
        this(name, type, null);
    }

    VariableExpression(String name, DataTypeExpression type, T value) {
        this(name, type, value, false, 0);
    }

    VariableExpression(String name, DataTypeExpression type, T value, boolean list, Integer listSize) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.list = list;
        this.listSize = listSize;
    }


    public String getName() {
        return this.name;
    }

    public DataTypeExpression getType() {
        return type;
    }

    public Integer getListSize() {
        return listSize;
    }

    public void setListSize(Integer listSize) {
        this.listSize = listSize;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public abstract T getValue();

    public abstract void setValue(T value);


}
