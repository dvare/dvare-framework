package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.exceptions.interpreter.InterpretException;

@Operation(type = OperationType.VALIDATION, symbols = {"AND", "And", "and", "&&"})
public class And extends OperationExpression {
    public And() {
        super("AND", "And", "and", "&&");
    }

    public And copy() {
        return new And();
    }


    @Override
    public Object interpret(Object object) throws InterpretException {
        Boolean left = (Boolean) leftOperand.interpret(object);
        Boolean right = (Boolean) rightOperand.interpret(object);

        return left & right;
    }
}