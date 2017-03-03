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


package org.dvare.expression.literal;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.datatype.RegexType;
import org.dvare.expression.datatype.StringType;
import org.dvare.util.TrimString;

public abstract class LiteralExpression<T> extends Expression {

    protected T value;
    protected Class<? extends DataTypeExpression> type;

    LiteralExpression(T value, Class<? extends DataTypeExpression> type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return this.value;
    }

    public Class<? extends DataTypeExpression> getType() {
        return this.type;
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {
        return this;
    }


    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }

        if (type.equals(StringType.class) || type.equals(RegexType.class)) {
            return "'" + TrimString.trim(value.toString()) + "'";
        } else {
            return value.toString();
        }

    }

}