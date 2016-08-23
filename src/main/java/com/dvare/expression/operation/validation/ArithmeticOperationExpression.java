package com.dvare.expression.operation.validation;

import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.expression.Expression;
import com.dvare.expression.literal.LiteralExpression;

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
    public Object interpret(Object object) throws InterpretException {

        interpretOperand(object);


        Expression leftExpression = leftValueOperand;
        if (leftExpression == null)
            return false;

        LiteralExpression<?> rightExpression = rightValueOperand;


        return dataType.evaluate(this, leftExpression, rightExpression);

    }
}