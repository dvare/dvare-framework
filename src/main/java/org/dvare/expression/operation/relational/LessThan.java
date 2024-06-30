package org.dvare.expression.operation.relational;

import org.dvare.annotations.Operation;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.RelationalOperationExpression;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.LESS, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType, DataType.SimpleDateType})
public class LessThan extends RelationalOperationExpression {
    public LessThan() {
        super(OperationType.LESS);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}