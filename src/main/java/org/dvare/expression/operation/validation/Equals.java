package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.expression.operation.EqualityOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.EQUAL)
public class Equals extends EqualityOperationExpression {
    public Equals() {
        super(OperationType.EQUAL);
    }


}