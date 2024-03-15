package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.PairType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.PAIR_LIST)
public class PairOperation extends AggregationOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(PairOperation.class);


    public PairOperation() {
        super(OperationType.PAIR_LIST);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {


        pos = findNextExpression(tokens, pos + 1, stack, contexts);

        if (rightOperand.size() != 2) {
            throw new ExpressionParseException(" Pair Operation must contains 2 params ");
        }


        if (logger.isDebugEnabled()) {
            logger.debug("Operation Expression Call Expression : {}", getClass().getSimpleName());
        }
        stack.push(this);
        return pos;
    }


    private ListLiteral buildValues(InstancesBinding instancesBinding, Expression valueParam) throws InterpretException {

        OperationExpression operationExpression = new ValuesOperation();
        operationExpression.setLeftOperand(valueParam);

        Object interpret = operationExpression.interpret(instancesBinding);

        if (interpret instanceof ListLiteral) {

            return (ListLiteral) interpret;
        }

        return null;
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        if (rightOperand.size() == 2) {

            Expression keyParam = rightOperand.get(0);
            Expression valueParam = rightOperand.get(1);


            ListLiteral keyListLiteral = buildValues(instancesBinding, keyParam);
            ListLiteral valueListLiteral = buildValues(instancesBinding, valueParam);

            if (keyListLiteral != null && valueListLiteral != null) {

                List<?> keys = keyListLiteral.getValue();
                List<?> values = valueListLiteral.getValue();


                Iterator<?> leftIterator = keys.iterator();
                Iterator<?> rightIterator = values.iterator();

                // dataTypeExpression = valueListLiteral.getType();

                List<Pair> pairs = new ArrayList<>();
                while (leftIterator.hasNext() && rightIterator.hasNext()) {

                    Object leftValue = leftIterator.next();
                    Object rightValue = rightIterator.next();

                    pairs.add(Pair.of(leftValue, rightValue));
                }

                return new ListLiteral(pairs, PairType.class);
            }


        }


        return new NullLiteral<>();
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}