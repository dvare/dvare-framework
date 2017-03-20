package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.utility.GetExpOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.IS_EMPTY)
public class IsEmpty extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(IsEmpty.class);


    public IsEmpty() {
        super(OperationType.IS_EMPTY);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        Expression right = leftOperand;

        if (right instanceof PairOperation || right instanceof ListOperationExpression || right instanceof GetExpOperation) {
            OperationExpression valuesOperation = (OperationExpression) right;
            Object valuesResult = valuesOperation.interpret(expressionBinding, instancesBinding);
            if (valuesResult instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) valuesResult;
                return LiteralType.getLiteralExpression(listLiteral.getSize() == 0, BooleanType.class);
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
            return LiteralType.getLiteralExpression(values.size() == 0, BooleanType.class);
        }

        return LiteralType.getLiteralExpression(true, BooleanType.class);
    }


}