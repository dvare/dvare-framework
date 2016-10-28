package org.dvare.expression.operation;

import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;

import java.util.List;

public abstract class ArithmeticOperationExpression extends EqualityOperationExpression {


    public ArithmeticOperationExpression(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Object interpret(Object dataRow) throws InterpretException {
        interpretOperand(dataRow, null);
        Expression leftExpression = leftValueOperand;
        if (leftExpression == null)
            return new NullLiteral();
        LiteralExpression<?> rightExpression = rightValueOperand;
        return dataType.evaluate(this, leftExpression, rightExpression);

    }

    @Override
    public Object interpret(Object selfRow, List<Object> dataset) throws InterpretException {
        interpretOperand(selfRow, dataset.get(0));
        Expression leftExpression = leftValueOperand;
        if (leftExpression == null)
            return new NullLiteral();
        LiteralExpression<?> rightExpression = rightValueOperand;
        return dataType.evaluate(this, leftExpression, rightExpression);
    }

    @Override
    public Object interpret(Object selfRow, Object dataRow) throws InterpretException {
        interpretOperand(selfRow, dataRow);
        Expression leftExpression = leftValueOperand;
        if (leftExpression == null)
            return new NullLiteral();
        LiteralExpression<?> rightExpression = rightValueOperand;
        return dataType.evaluate(this, leftExpression, rightExpression);

    }
}