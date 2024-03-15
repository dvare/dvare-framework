package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.operation.IterationOperationExpression;
import org.dvare.expression.operation.OperationType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.END_FOREACH)
public class EndForEach extends IterationOperationExpression {

    public EndForEach() {
        super(OperationType.END_FOREACH);
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}