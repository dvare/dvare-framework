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
@Operation(type = OperationType.SUB_MONTHS)
public class SubMonths extends ChainOperationExpression {


    public SubMonths() {
        super(OperationType.SUB_MONTHS);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);

        if ((!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) && (rightOperand != null && rightOperand.size() == 1)) {

            LiteralExpression<?> monthsExpression = (LiteralExpression<?>) rightOperand.get(0);
            Object monthsValue = monthsExpression.getValue();
            Object value = literalExpression.getValue();
            dataTypeExpression = literalExpression.getType();
            switch (toDataType(dataTypeExpression)) {
                case DateType: {
                    if (value instanceof LocalDate && monthsValue instanceof Integer) {

                        LocalDate localDate = (LocalDate) value;
                        Integer months = (Integer) monthsValue;
                        localDate = localDate.minusMonths(months);

                        return LiteralType.getLiteralExpression(localDate, dataTypeExpression);

                    }
                    break;
                }

                case DateTimeType: {
                    if (value instanceof LocalDateTime && monthsValue instanceof Integer) {

                        LocalDateTime localDateTime = (LocalDateTime) value;
                        Integer months = (Integer) monthsValue;
                        localDateTime = localDateTime.minusMonths(months);
                        return LiteralType.getLiteralExpression(localDateTime, dataTypeExpression);

                    }
                    break;
                }

                case SimpleDateType: {
                    if (value instanceof Date && monthsValue instanceof Integer) {

                        Date date = (Date) value;
                        Integer months = (Integer) monthsValue;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.MONTH, -months);
                        return LiteralType.getLiteralExpression(calendar.getTime(), dataTypeExpression);
                    }
                    break;
                }


            }
        }


        return new NullLiteral<>();
    }

}