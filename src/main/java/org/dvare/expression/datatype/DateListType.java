package org.dvare.expression.datatype;


import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.Equals;
import org.dvare.expression.operation.relational.NotEquals;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.DateListType)
public class DateListType extends ListType {
    public DateListType() {
        super(DataType.DateListType);
    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression left, LiteralExpression right) {
        if (left instanceof ListLiteral && right instanceof ListLiteral) {
            List<LocalDate> leftValues = buildLocalDateList(ListLiteral.class.cast(left).getValue());
            List<LocalDate> rightValues = buildLocalDateList(ListLiteral.class.cast(right).getValue());
            return leftValues.equals(rightValues);

        }
        return false;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression left, LiteralExpression right) {
        return !equal(left, right);
    }
}
