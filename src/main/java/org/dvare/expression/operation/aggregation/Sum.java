package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.SUM, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Sum extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Sum.class);


    public Sum() {
        super(OperationType.SUM);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {


        if (leftOperand instanceof VariableExpression) {
            VariableExpression variableExpression = ((VariableExpression) leftOperand);

            DataType type = toDataType(variableExpression.getType());

            switch (type) {

                case FloatType: {
                    leftExpression = LiteralType.getLiteralExpression(0f, variableExpression.getType());
                    break;
                }
                case IntegerType: {
                    leftExpression = LiteralType.getLiteralExpression(0, variableExpression.getType());
                    break;
                }
                case StringType: {
                    leftExpression = LiteralType.getLiteralExpression("", variableExpression.getType());
                    break;
                }
                default: {
                    leftExpression = new NullLiteral<>();
                    //throw new IllegalOperationException("Sum OperationExpression Not Allowed");
                    break;
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