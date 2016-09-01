package org.dvare.expression.operation.aggregation;


import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.interpreter.IllegalPropertyValueException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.util.ValueFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public abstract class AggregationOperation extends Expression {

    protected List<String> symbols = new ArrayList<>();
    protected Expression leftOperand = null;
    protected Expression rightOperand = null;

    public AggregationOperation(String symbol) {
        this.symbols.add(symbol);
    }

    public AggregationOperation(List<String> symbols) {
        this.symbols.addAll(symbols);
    }

    public AggregationOperation(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
    }

    public List<String> getSymbols() {
        return this.symbols;
    }

    public abstract AggregationOperation copy();

    protected Object getValue(Object object, String name) throws IllegalPropertyValueException {
        return ValueFinder.findValue(name, object);
    }

    protected Object setValue(Object object, String name, Object value) throws IllegalPropertyValueException {
        return ValueFinder.updateValue(object, name, value);
    }


    public abstract int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException;

    public abstract Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException;
}
