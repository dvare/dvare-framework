package org.dvare.expression.literal;


import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.*;

import java.util.List;

public class ListLiteral extends LiteralExpression<List<?>> {


    public ListLiteral(List<?> value, Class<? extends DataTypeExpression> type) {
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
                case FloatType:
                    return FloatListType.class;
                case StringType:
                    return StringListType.class;
                case BooleanType:
                    return BooleanListType.class;
                case PairType:
                    return PairListType.class;
                case TripleType:
                    return TripleListType.class;
                case DateType:
                    return DateListType.class;
                case DateTimeType:
                    return DateTimeListType.class;
                case SimpleDateType:
                    return SimpleDateListType.class;

            }
        }
        return ListType.class;
    }


    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
