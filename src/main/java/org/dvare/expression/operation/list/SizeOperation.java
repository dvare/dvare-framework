package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.SIZE)
public class SizeOperation extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(SizeOperation.class);


    public SizeOperation() {
        super(OperationType.SIZE);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        Expression right = leftOperand;
        if (right instanceof OperationExpression) {

            List<?> values = buildValues(leftOperand, expressionBinding, instancesBinding);
            if (values != null) {
                return LiteralType.getLiteralExpression(values.size(), IntegerType.class);
            }

        } else if (right instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) right;

            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
            List dataSet;
            if (instance instanceof List) {
                dataSet = (List) instance;
            } else {
                dataSet = new ArrayList<>();
                dataSet.add(instance);
            }

            List<Object> values = new ArrayList<>();
            for (Object object : dataSet) {
                Object value = getValue(object, variableExpression.getName());
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(value, variableExpression.getType());
                values.add(literalExpression.getValue());
            }
            return LiteralType.getLiteralExpression(values.size(), IntegerType.class);
        }

        return LiteralType.getLiteralExpression(0, IntegerType.class);
    }


}