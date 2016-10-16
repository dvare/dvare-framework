package org.dvare.expression.operation;

import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;

import java.util.Arrays;
import java.util.List;

public abstract class ArithmeticOperationExpression extends EqualityOperationExpression {


    public ArithmeticOperationExpression(String symbol) {
        this.symbols.add(symbol);
    }

    public ArithmeticOperationExpression(List<String> symbols) {
        this.symbols.addAll(symbols);
    }

    public ArithmeticOperationExpression(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
    }


    @Override
    public Object interpret(Object dataRow) throws InterpretException {
        interpretOperand(dataRow, null);
        Expression leftExpression = leftValueOperand;
        if (leftExpression == null)
            return false;
        LiteralExpression<?> rightExpression = rightValueOperand;
        return dataType.evaluate(this, leftExpression, rightExpression);

    }

    @Override
    public Object interpret(Object selfRow, Object dataRow) throws InterpretException {
        interpretOperand(selfRow, dataRow);
        Expression leftExpression = leftValueOperand;
        if (leftExpression == null)
            return false;
        LiteralExpression<?> rightExpression = rightValueOperand;
        return dataType.evaluate(this, leftExpression, rightExpression);

    }
}