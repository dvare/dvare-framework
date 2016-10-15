package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.annotations.OperationType;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.AGGREGATION, symbols = {"Count", "count"})
public class Count extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Count.class);


    public Count() {
        super("Count", "count");
    }

    public Count copy() {
        return new Count();
    }

    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {

        LiteralExpression literalExpression = LiteralType.getLiteralExpression(dataSet.size(), new IntegerType());

        return literalExpression;
    }


}