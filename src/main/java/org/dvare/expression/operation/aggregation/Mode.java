package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.FloatType;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.list.ValuesOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.MODE, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Mode extends AggregationOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(Mode.class);


    public Mode() {
        super(OperationType.MODE);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        Expression valueOperand = this.leftOperand;

        List<?> valuesList = null;
        DataType type = null;


        OperationExpression operationExpression = new ValuesOperation();
        operationExpression.setLeftOperand(valueOperand);

        Object interpret = operationExpression.interpret(instancesBinding);

        if (interpret instanceof ListLiteral) {

            ListLiteral listLiteral = (ListLiteral) interpret;
            type = toDataType(listLiteral.getType());
            valuesList = listLiteral.getValue();
        }


        if (valuesList != null && type != null) {


            switch (type) {

                case FloatType: {

                    List<Float> values = new ArrayList<>();

                    for (Object value : valuesList) {

                        if (value instanceof Float) {
                            values.add((Float) value);
                        }
                    }


                    Float maxValue = 0f;
                    int maxCount = 0;

                    for (int i = 0; i < values.size(); ++i) {
                        int count = 0;
                        for (Float value : values) {
                            if (value.compareTo(values.get(i)) == 0) ++count;
                        }
                        if (count > maxCount) {
                            maxCount = count;
                            maxValue = values.get(i);
                        }
                    }

                    leftExpression = LiteralType.getLiteralExpression(maxValue, FloatType.class);

                    break;

                }
                case IntegerType: {
                    List<Integer> values = new ArrayList<>();

                    for (Object value : valuesList) {
                        if (value instanceof Integer) {
                            values.add((Integer) value);
                        }
                    }


                    int maxValue = 0, maxCount = 0;

                    for (int i = 0; i < values.size(); ++i) {
                        int count = 0;
                        for (Integer value : values) {
                            if (value.equals(values.get(i))) ++count;
                        }
                        if (count > maxCount) {
                            maxCount = count;
                            maxValue = values.get(i);
                        }
                    }
                    leftExpression = LiteralType.getLiteralExpression(maxValue, IntegerType.class);

                    break;
                }
            }
        }


        return leftExpression;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}