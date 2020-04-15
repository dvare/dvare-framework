package org.dvare.expression.operation.relational;

import org.dvare.annotations.Operation;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.RelationalOperationExpression;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.NOT_EQUAL)
public class NotEquals extends RelationalOperationExpression {
    public NotEquals() {
        super(OperationType.NOT_EQUAL);
    }


}