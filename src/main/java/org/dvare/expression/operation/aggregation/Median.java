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
@Operation(type = OperationType.MEDIAN, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Median extends AggregationOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(Median.class);


    public Median() {
        super(OperationType.MEDIAN);
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
            int length = valuesList.size();
            int middle = length / 2;

            switch (type) {

                case FloatType: {

                    List<Float> values = new ArrayList<>();

                    for (Object value : valuesList) {
                        if (value instanceof Float) {
                            values.add((Float) value);
                        }
                    }


                    Float result;
                    if (length % 2 == 1) {
                        result = values.get(middle);

                    } else {
                        Float value1 = values.get(middle - 1);
                        Float value2 = values.get(middle - 2);
                        result = (value1 + value2) / 2.f;
                    }


                    leftExpression = LiteralType.getLiteralExpression(result, FloatType.class);

                    break;
                }
                case IntegerType: {
                    List<Integer> values = new ArrayList<>();

                    for (Object value : valuesList) {

                        if (value instanceof Integer) {
                            values.add((Integer) value);
                        }
                    }


                    Integer result;
                    if (length % 2 == 1) {
                        result = values.get(middle);

                    } else {
                        Integer value1 = values.get(middle);
                        Integer value2 = values.get(middle - 1);
                        result = (value1 + value2) / 2;
                    }


                    leftExpression = LiteralType.getLiteralExpression(result, IntegerType.class);
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