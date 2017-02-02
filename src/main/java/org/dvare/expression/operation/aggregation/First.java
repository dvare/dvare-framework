package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.FIRST)
public class First extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(First.class);


    public First() {
        super(OperationType.FIRST);
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {

        Object aggregation = instancesBinding.getInstance("self");
        List<Object> dataSet = (List) instancesBinding.getInstance("data");


        Expression right = this.rightOperand;
        if (right instanceof VariableExpression && !dataSet.isEmpty()) {
            VariableExpression variableExpression = (VariableExpression) right;


            Object row = dataSet.get(0);
            Object value = getValue(row, variableExpression.getName());

            LiteralExpression literalExpression = LiteralType.getLiteralExpression(value, variableExpression.getType());

            return literalExpression;

        }

        return null;
    }

}