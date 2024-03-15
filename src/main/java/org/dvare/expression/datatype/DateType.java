package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.annotations.Type;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.arithmetic.Subtract;
import org.dvare.expression.operation.relational.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Type(dataType = DataType.DateType)
public class DateType extends DataTypeExpression {
    public DateType() {
        super(DataType.DateType);

    }


    @OperationMapping(operations = {
            Equals.class
    })
    public boolean equal(LiteralExpression<?> left, LiteralExpression<?> right) {

        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) == 0;
    }

    @OperationMapping(operations = {
            NotEquals.class
    })
    public boolean notEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) != 0;
    }

    @OperationMapping(operations = {
            LessThan.class
    })
    public boolean less(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) < 0;
    }

    @OperationMapping(operations = {
            LessEqual.class
    })
    public boolean lessEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) <= 0;
    }

    @OperationMapping(operations = {
            GreaterThan.class
    })
    public boolean greater(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) > 0;
    }

    @OperationMapping(operations = {
            GreaterEqual.class
    })
    public boolean greaterEqual(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        return (leftValue != null && rightValue != null) && leftValue.compareTo(rightValue) >= 0;
    }

    @OperationMapping(operations = {
            In.class
    })
    public boolean in(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        List<LocalDate> rightValues = buildLocalDateList((List<?>) right.getValue());
        return rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            NotIn.class
    })
    public boolean notIn(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        List<LocalDate> rightValues = buildLocalDateList((List<?>) right.getValue());
        return !rightValues.contains(leftValue);
    }

    @OperationMapping(operations = {
            Between.class
    })
    public boolean between(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        List<LocalDate> values = buildLocalDateList((List<?>) right.getValue());
        LocalDate lower = values.get(0);
        LocalDate upper = values.get(1);
        return lower.compareTo(leftValue) <= 0 && leftValue.compareTo(upper) <= 0;
    }


    @OperationMapping(operations = {
            Subtract.class
    })
    public LocalDate sub(LiteralExpression<?> left, LiteralExpression<?> right) {
        LocalDate leftValue = toLocalDate(left.getValue());
        LocalDate rightValue = toLocalDate(right.getValue());
        Period period = Period.between(rightValue, leftValue);

        int year = period.getYears() > 0 ? period.getYears() : -1 * period.getYears();
        int month = period.getMonths() > 0 ? period.getMonths() : -1 * period.getMonths() > 0 ? -1 * period.getMonths() : 1;
        int day = period.getDays() > 0 ? period.getDays() : -1 * period.getDays() > 0 ? -1 * period.getDays() : 1;

        // feb 28 days and leap year
        if (month == 2 && day > 28) {
            if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
                day = day - 29 + 1;
                month = month + 1;
            } else {
                day = day - 28 + 1;
                month = month + 1;
            }
        }


        return LocalDate.of(year, month, day);

    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
