package org.dvare.expression.operation.relational;

import org.dvare.annotations.Operation;
import org.dvare.expression.operation.OperationType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.NOT_IN)
public class NotIn extends In {
    public NotIn() {
        super(OperationType.NOT_IN);
    }
}