package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TrimString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_DATE, dataTypes = {DataType.StringType})
public class ToDate extends ChainOperationExpression {

    public ToDate() {
        super(OperationType.TO_DATE);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {

            Object value = literalExpression.getValue();
            try {
                LocalDate localDate = null;
                if (value instanceof LocalDate) {
                    localDate = (LocalDate) value;
                } else if (value instanceof LocalDateTime) {
                    localDate = ((LocalDateTime) value).toLocalDate();
                } else if (value instanceof Date) {
                    Date date = (Date) value;
                    localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                } else {
                    String valueString = literalExpression.getValue().toString();
                    valueString = TrimString.trim(valueString);

                    if (valueString.matches(LiteralType.date)) {
                        localDate = LocalDate.parse(valueString, LiteralType.dateFormat);

                    }

                }

                if (localDate != null) {
                    return LiteralType.getLiteralExpression(localDate, DataType.DateType);
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }
        return new NullLiteral<>();
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}