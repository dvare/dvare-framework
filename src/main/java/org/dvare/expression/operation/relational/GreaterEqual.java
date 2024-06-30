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
@Operation(type = OperationType.GREATER_EQUAL, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType, DataType.SimpleDateType})
public class GreaterEqual extends RelationalOperationExpression {
    public GreaterEqual() {
        super(OperationType.GREATER_EQUAL);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}