package org.dvare.expression.operation.flow;

import org.dvare.annotations.Operation;
import org.dvare.expression.operation.ConditionOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.ELSE)
public class ELSE extends ConditionOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(ELSE.class);

    public ELSE() {
        super(OperationType.ELSE);
    }


}