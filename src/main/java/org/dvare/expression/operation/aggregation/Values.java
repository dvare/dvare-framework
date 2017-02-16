package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.VALUE)
public class Values extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(Values.class);


    public Values() {
        super(OperationType.VALUE);
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {

        Object aggregation = instancesBinding.getInstance("self");
        List<Object> dataSet = (List) instancesBinding.getInstance("data");


        Expression right = this.leftOperand;
        if (right instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) right;

            List<Object> values = new ArrayList<>();
            for (Object object : dataSet) {
                Object value = getValue(object, variableExpression.getName());
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(value, variableExpression.getType());
                values.add(literalExpression.getValue());
            }


            return new ListLiteral(values, variableExpression.getType());


        }

        return new NullLiteral();
    }

}