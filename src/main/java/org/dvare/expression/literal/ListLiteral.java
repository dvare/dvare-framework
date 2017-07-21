/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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


import org.dvare.annotations.Type;
import org.dvare.expression.datatype.*;

import java.util.List;

public class ListLiteral extends LiteralExpression<List> {


    public ListLiteral(List value, Class<? extends DataTypeExpression> type) {
        super(value, type);
    }

    public boolean isEmpty() {
        return getSize() < 1;
    }

    public Integer getSize() {
        return value != null ? value.size() : 0;
    }


    public Class<? extends DataTypeExpression> getListType() {


        if (getType() != null) {

            Type type = getType().getAnnotation(Type.class);
            switch (type.dataType()) {

                case IntegerType:
                    return IntegerListType.class;
                case FloatListType:
                    return FloatListType.class;
                case StringType:
                    return StringListType.class;
                case BooleanType:
                    return ListType.class;
                case DateTimeType:
                    return ListType.class;
                case DateType:
                    return ListType.class;

            }


        }

        return ListType.class;
    }


}
