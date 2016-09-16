package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.annotations.OperationType;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.AGGREGATION, symbols = {"Value", "value"})
public class Value extends OperationExpression {
    static Logger logger = LoggerFactory.getLogger(Value.class);


    public Value() {
        super("Value", "value");
    }

    public Value copy() {
        return new Value();
    }


    @Override
    public Object interpret(Object aggregation, Object dataSet) throws InterpretException {
        Expression right = this.rightOperand;
        if (right instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) right;
            Object value = getValue(dataSet, variableExpression.getName());
            LiteralExpression literalExpression = LiteralType.getLiteralExpression(value, variableExpression.getType());
            return literalExpression;
        } else if (right instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) right;
            return literalExpression;
        } else if (right instanceof org.dvare.expression.operation.Operation) {
            org.dvare.expression.operation.Operation operation = (org.dvare.expression.operation.Operation) right;
            Object result = operation.interpret(aggregation, dataSet);
            if (result instanceof LiteralExpression) {
                LiteralExpression literalExpression = (LiteralExpression) result;
                return literalExpression;
            }
        }

        return null;
    }


    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {


        Expression right = this.rightOperand;
        if (right instanceof VariableExpression && !dataSet.isEmpty()) {
            VariableExpression variableExpression = (VariableExpression) right;
            Object value = getValue(dataSet.get(0), variableExpression.getName());
            LiteralExpression literalExpression = LiteralType.getLiteralExpression(value, variableExpression.getType());
            return literalExpression;
        } else if (right instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) right;
            return literalExpression;
        } else if (right instanceof org.dvare.expression.operation.Operation && !dataSet.isEmpty()) {
            org.dvare.expression.operation.Operation operation = (org.dvare.expression.operation.Operation) right;
            Object result = operation.interpret(aggregation, dataSet);
            if (result instanceof LiteralExpression) {
                LiteralExpression literalExpression = (LiteralExpression) result;
                return literalExpression;
            }
        }

        return null;
    }

}