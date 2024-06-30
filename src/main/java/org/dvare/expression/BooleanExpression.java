package org.dvare.expression;


import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.literal.BooleanLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class BooleanExpression extends Expression {
    private static final Logger logger = LoggerFactory.getLogger(BooleanExpression.class);
    private String name;
    private boolean value = true;

    public BooleanExpression(String name) {
        this(name, false);
    }

    public BooleanExpression(String name, boolean value) {
        this.name = name;
        this.value = value;
        if (logger.isDebugEnabled()) {
            logger.debug("Boolean  Expression :  [{} , {}]", name, value);
        }
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        return new BooleanLiteral(value);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }


}
