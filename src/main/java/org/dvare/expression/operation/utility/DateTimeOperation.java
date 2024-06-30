package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.DateTimeLiteral;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.DATE_TIME, dataTypes = {DataType.DateTimeType})
public class DateTimeOperation extends OperationExpression {


    public DateTimeOperation() {
        super(OperationType.DATE_TIME);
    }

    public DateTimeOperation(OperationType operationType) {
        super(operationType);
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

            dateFormat = LiteralType.dateTimeFormat;

            NamedExpression namedExpression = (NamedExpression) expression;
            value = namedExpression.getName();

        }


        try {
            if (dateFormat != null && value != null) {
                LocalDateTime localDateTime = LocalDateTime.parse(value, dateFormat);
                DateTimeLiteral literal = new DateTimeLiteral(localDateTime);
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
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {
            String token = tokens[i];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {


                if (op.getClass().equals(RightPriority.class)) {
                    return i;
                }

            } else if (!token.isEmpty() && !token.equals(",")) {
                NamedExpression namedExpression = new NamedExpression(token);
                stack.push(namedExpression);
            }
        }
        return null;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}