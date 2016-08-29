package org.dvare.expression.operation.validation;

import org.dvare.annotations.OperationType;
import org.dvare.exceptions.interpreter.InterpretException;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"Implies", "implies", "=>"})
public class Implies extends OperationExpression {
    public Implies() {
        super("Implies", "implies", "=>");
    }

    public Implies copy() {
        return new Implies();
    }


    @Override
    public Object interpret(Object object) throws InterpretException {


        boolean left = (Boolean) leftOperand.interpret(object);
        boolean right = (Boolean) rightOperand.interpret(object);

        return (!left) | right;
    }
}