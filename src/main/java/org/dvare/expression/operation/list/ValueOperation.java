package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.VALUE)
public class ValueOperation extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(ValueOperation.class);


    public ValueOperation() {
        super(OperationType.VALUE);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        if (!this.rightOperand.isEmpty()) {
            Expression right = this.rightOperand.get(0);
            if (right instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) right;

                Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
                if (instance instanceof List) {
                    List dataSet = (List) instance;
                    Object value = getValue(dataSet.get(0), variableExpression.getName());
                    return LiteralType.getLiteralExpression(value, variableExpression.getType());
                } else {
                    Object value = getValue(instance, variableExpression.getName());
                    return LiteralType.getLiteralExpression(value, variableExpression.getType());
                }


            } else if (right instanceof LiteralExpression) {
                return right;
            } else if (right instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) right;
                Object result = operation.interpret(expressionBinding, instancesBinding);
                if (result instanceof LiteralExpression) {
                    return result;
                }
            }
        }
        return new NullLiteral();
    }

}