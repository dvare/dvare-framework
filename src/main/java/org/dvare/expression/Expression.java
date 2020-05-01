package org.dvare.expression;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.literal.BooleanLiteral;
import org.dvare.expression.literal.LiteralExpression;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class Expression {

    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        return new BooleanLiteral(false);
    }

}



