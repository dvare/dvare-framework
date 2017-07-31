package org.dvare.expression.operation.relational;

import org.dvare.annotations.Operation;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.RelationalOperationExpression;

@Operation(type = OperationType.EQUAL)
public class Equals extends RelationalOperationExpression {
    public Equals() {
        super(OperationType.EQUAL);
    }


}