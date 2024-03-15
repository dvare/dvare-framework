package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.relational.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.DateTimeType)
public class DateTimeType extends DataTypeExpression {
    public DateTimeType() {
        super(DataType.DateTimeType);

    }

    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) < 0;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })

    public boolean lessEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) <= 0;
    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) > 0;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        LocalDateTime rightValue = toLocalDateTime(right.getValue());
        return leftValue.compareTo(rightValue) >= 0;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        List<LocalDateTime> rightValues = buildDateTimeList((List<?>) right.getValue());
        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        List<LocalDateTime> rightValues = buildDateTimeList((List<?>) right.getValue());
        return !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDateTime leftValue = toLocalDateTime(left.getValue());
        List<LocalDateTime> values = buildDateTimeList((List<?>) right.getValue());
        LocalDateTime lower = values.get(0);
        LocalDateTime upper = values.get(1);
        return lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0;
    }


    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
