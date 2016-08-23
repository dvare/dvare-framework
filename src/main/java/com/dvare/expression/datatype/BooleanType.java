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


package com.dvare.expression.datatype;

import com.dvare.annotations.Type;
import com.dvare.annotations.TypeOperation;
import com.dvare.expression.literal.LiteralExpression;

import java.util.List;

@Type(dataType = DataType.BooleanType)
public class BooleanType extends DataTypeExpression {
    public BooleanType() {
        super(DataType.BooleanType);
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        Boolean leftValue = (Boolean) left.getValue();
        Boolean rightValue = (Boolean) right.getValue();
        return leftValue == rightValue;
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        Boolean leftValue = (Boolean) left.getValue();
        Boolean rightValue = (Boolean) right.getValue();
        return leftValue != rightValue;
    }

    @TypeOperation(operations = {
            com.dvare.expression.operation.validation.In.class
    })
    public boolean in(LiteralExpression left, LiteralExpression right) {
        Boolean leftValue = (Boolean) left.getValue();
        List<Boolean> rightValues = (List<Boolean>) right.getValue();
        for (Boolean rightValue : rightValues) {
            if (leftValue == rightValue) {
                return true;
            }
        }
        return false;
    }

}