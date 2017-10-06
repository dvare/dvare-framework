/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.expression.veriable;


import org.dvare.annotations.Type;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.*;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */

public class ListVariable extends VariableExpression<List> {

    public ListVariable(String name, Class<? extends DataTypeExpression> type) {
        super(name, type);
    }

    public boolean isEmpty() {
        return getSize() < 1;
    }

    public Integer getSize() {
        return value != null ? value.size() : 0;
    }

    public Class<? extends DataTypeExpression> getListType() {

        if (getType() != null) {
            DataType dataType = getType().getAnnotation(Type.class).dataType();
            switch (dataType) {
                case IntegerType:
                    return IntegerListType.class;
                case FloatType:
                    return FloatListType.class;
                case StringType:
                    return StringListType.class;
                case BooleanType:
                    return BooleanListType.class;
                case DateType:
                    return DateListType.class;
                case DateTimeType:
                    return DateTimeListType.class;
                case SimpleDateType:
                    return SimpleDateListType.class;
                case PairType:
                    return PairListType.class;
            }
        }
        return ListType.class;
    }

    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        if (instancesBinding != null) {
            Object instance = instancesBinding.getInstance(operandType);
            VariableType.setVariableValue(this, instance);
        }
        return LiteralType.getLiteralExpression(value, getListType());
    }

}