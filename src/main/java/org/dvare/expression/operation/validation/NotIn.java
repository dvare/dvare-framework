package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.NOT_IN)
public class NotIn extends In {
    public NotIn() {
        super(OperationType.NOT_IN);
    }

    public NotIn copy() {
        return new NotIn();
    }
}