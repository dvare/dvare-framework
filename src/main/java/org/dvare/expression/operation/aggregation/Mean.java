package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
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
import java.util.Collections;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.MEAN, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Mean extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Mean.class);


    public Mean() {
        super(OperationType.MEAN);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {


        Expression valueOperand = this.leftOperand;


        List valuesList = null;
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

                    Collections.sort(values);
                    Float sum = 0f;
                    for (Float i : values) {
                        sum = sum + i;
                    }

                    Float result = sum / values.size();
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

                    Collections.sort(values);
                    Integer sum = 0;
                    for (Integer i : values) {
                        sum = sum + i;
                    }

                    Integer result = sum / values.size();

                    leftExpression = LiteralType.getLiteralExpression(result, IntegerType.class);


                    break;
                }
            }
        }


        return leftExpression;
    }


}