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
        }

        return null;
    }

}