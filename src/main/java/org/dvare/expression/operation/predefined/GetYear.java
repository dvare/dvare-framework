package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.IntegerType;
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
@Operation(type = OperationType.GET_YEAR)
public class GetYear extends ChainOperationExpression {


    public GetYear() {
        super(OperationType.GET_YEAR);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {


            Object value = literalExpression.getValue();

            switch (toDataType(literalExpression.getType())) {
                case DateType: {
                    if (value instanceof LocalDate) {
                        LocalDate localDate = (LocalDate) value;
                        return LiteralType.getLiteralExpression(localDate.getYear(), IntegerType.class);

                    }
                    break;
                }

                case DateTimeType: {
                    if (value instanceof LocalDateTime) {

                        LocalDateTime localDateTime = (LocalDateTime) value;
                        return LiteralType.getLiteralExpression(localDateTime.getYear(), IntegerType.class);

                    }
                    break;
                }

                case SimpleDateType: {
                    if (value instanceof Date) {

                        Date date = (Date) value;

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        return LiteralType.getLiteralExpression(calendar.get(Calendar.YEAR), IntegerType.class);
                    }
                    break;
                }


            }
        }


        return new NullLiteral<>();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}