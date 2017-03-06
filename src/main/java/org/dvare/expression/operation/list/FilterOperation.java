package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.FILTER)
public class FilterOperation extends ValuesOperation {

    public FilterOperation() {
        super(OperationType.FILTER);
    }


}