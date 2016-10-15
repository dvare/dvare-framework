package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.annotations.OperationType;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.AGGREGATION, symbols = {"First", "first"})
public class First extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(First.class);


    public First() {
        super("First", "first");
    }

    public First copy() {
        return new First();
    }

    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {


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