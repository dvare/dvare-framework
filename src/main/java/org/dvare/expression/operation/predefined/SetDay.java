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
@Operation(type = OperationType.SET_DAY)
public class SetDay extends ChainOperationExpression {


    public SetDay() {
        super(OperationType.SET_DAY);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null
                && rightOperand != null && !rightOperand.isEmpty()) {

            LiteralExpression<?> daysExpression = (LiteralExpression<?>) rightOperand.get(0);

            Object daysValue = daysExpression.getValue();
            Object value = literalExpression.getValue();
            dataTypeExpression = literalExpression.getType();
            switch (toDataType(dataTypeExpression)) {
                case DateType: {
                    if (value instanceof LocalDate && daysValue instanceof Integer) {

                        LocalDate localDate = (LocalDate) value;
                        Integer days = (Integer) daysValue;
                        localDate = localDate.withDayOfMonth(days);

                        return LiteralType.getLiteralExpression(localDate, dataTypeExpression);

                    }
                    break;
                }

                case DateTimeType: {
                    if (value instanceof LocalDateTime && daysValue instanceof Integer) {

                        LocalDateTime localDateTime = (LocalDateTime) value;
                        Integer days = (Integer) daysValue;
                        localDateTime = localDateTime.withDayOfMonth(days);
                        return LiteralType.getLiteralExpression(localDateTime, dataTypeExpression);

                    }
                    break;
                }

                case SimpleDateType: {
                    if (value instanceof Date && daysValue instanceof Integer) {

                        Date date = (Date) value;
                        Integer days = (Integer) daysValue;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.set(Calendar.DATE, days);
                        return LiteralType.getLiteralExpression(calendar.getTime(), dataTypeExpression);
                    }
                    break;
                }


            }

        }

        return new NullLiteral<>();
    }

}