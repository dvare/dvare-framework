package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.FloatLiteral;
import org.dvare.expression.literal.IntegerLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.MAXIMUM, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Maximum extends AggregationOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(Maximum.class);

    public Maximum() {
        super(OperationType.MAXIMUM);
    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        extractValues(instancesBinding, leftOperand);
        DataType type = toDataType(dataTypeExpression);
        if (type != null) {
            switch (type) {

                case FloatType: {
                    leftExpression = new FloatLiteral(Float.MIN_VALUE);
                    break;
                }
                case IntegerType: {
                    leftExpression = new IntegerLiteral(Integer.MIN_VALUE);
                    break;
                }

                default: {
                    leftExpression = new NullLiteral<>();
                    //throw new IllegalOperationException("Min OperationExpression Not Allowed");
                }
            }
        }
        return super.interpret(instancesBinding);

    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}