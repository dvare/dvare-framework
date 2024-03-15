package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.DateLiteral;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.OperationType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.DATE, dataTypes = {DataType.DateType})
public class DateOperation extends DateTimeOperation {


    public DateOperation() {
        super(OperationType.DATE);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {


        pos = findNextExpression(tokens, pos + 1, stack, contexts);


        DateTimeFormatter dateFormat = null;
        String value = null;

        Expression expression = stack.pop();

        if (!stack.isEmpty() && stack.peek() instanceof NamedExpression) {

            if (expression instanceof NamedExpression) {
                NamedExpression namedExpression = (NamedExpression) expression;
                dateFormat = DateTimeFormatter.ofPattern(namedExpression.getName());
            }


            Expression valueExpression = stack.pop();
            if (valueExpression instanceof NamedExpression) {
                NamedExpression namedExpression = (NamedExpression) valueExpression;
                value = namedExpression.getName();
            }


        } else if (expression instanceof NamedExpression) {

            dateFormat = LiteralType.dateFormat;

            NamedExpression namedExpression = (NamedExpression) expression;
            value = namedExpression.getName();


        }


        try {
            if (dateFormat != null && value != null) {
                LocalDate localDate = LocalDate.parse(value, dateFormat);
                DateLiteral literal = new DateLiteral(localDate);
                stack.push(literal);
            }
        } catch (Exception e) {
            String message = String.format(" Unable to Parse literal %s to Date Time", value);
            logger.error(message);
            throw new IllegalValueException(message);
        }

        return pos;
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}