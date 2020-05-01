package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.SET_YEAR)
public class SetYear extends ChainOperationExpression {


    public SetYear() {
        super(OperationType.SET_YEAR);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null
                && (rightOperand != null && rightOperand.size() == 1)) {

            LiteralExpression<?> yearsExpression = (LiteralExpression<?>) rightOperand.get(0);
            Object yearValue = yearsExpression.getValue();
            Object value = literalExpression.getValue();
            dataTypeExpression = literalExpression.getType();
            switch (toDataType(dataTypeExpression)) {
                case DateType: {
                    if (value instanceof LocalDate && yearValue instanceof Integer) {

                        LocalDate localDate = (LocalDate) value;
                        Integer year = (Integer) yearValue;
                        localDate = localDate.withYear(year);

                        return LiteralType.getLiteralExpression(localDate, dataTypeExpression);

                    }
                    break;
                }

                case DateTimeType: {
                    if (value instanceof LocalDateTime && yearValue instanceof Integer) {

                        LocalDateTime localDateTime = (LocalDateTime) value;
                        Integer year = (Integer) yearValue;
                        localDateTime = localDateTime.withYear(year);
                        return LiteralType.getLiteralExpression(localDateTime, dataTypeExpression);

                    }
                    break;
                }

                case SimpleDateType: {
                    if (value instanceof Date && yearValue instanceof Integer) {

                        Date date = (Date) value;
                        Integer year = (Integer) yearValue;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.set(Calendar.YEAR, year);
                        return LiteralType.getLiteralExpression(calendar.getTime(), dataTypeExpression);
                    }
                    break;
                }


            }
        }

        return new NullLiteral<>();
    }

}