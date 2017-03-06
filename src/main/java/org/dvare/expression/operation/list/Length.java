package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.utility.GetExpOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.LENGTH)
public class Length extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Length.class);


    public Length() {
        super(OperationType.LENGTH);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        Expression right = leftOperand;
        if (right instanceof ValuesOperation || right instanceof MapOperation) {
            OperationExpression valuesOperation = (OperationExpression) right;
            Object valuesResult = valuesOperation.interpret(expressionBinding, instancesBinding);
            if (valuesResult instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) valuesResult;
                return LiteralType.getLiteralExpression(listLiteral.getSize(), IntegerType.class);
            }
        } else if (right instanceof GetExpOperation) {

            OperationExpression valuesOperation = (OperationExpression) right;
            Object valuesResult = valuesOperation.interpret(expressionBinding, instancesBinding);
            if (valuesResult instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) valuesResult;

                return LiteralType.getLiteralExpression(listLiteral.getSize(), IntegerType.class);
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

        return new NullLiteral();
    }


}