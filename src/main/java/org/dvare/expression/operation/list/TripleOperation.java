package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.TripleType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.Triple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TRIPLE_LIST)
public class TripleOperation extends AggregationOperationExpression {


    public TripleOperation() {
        super(OperationType.TRIPLE_LIST);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {


        pos = findNextExpression(tokens, pos + 1, stack, contexts);

        if (rightOperand.size() != 3) {
            throw new ExpressionParseException(" Triple Operation must contains 2 params ");
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

        if (rightOperand.size() == 3) {

            Expression leftParam = rightOperand.get(0);
            Expression middleParam = rightOperand.get(1);
            Expression rightParam = rightOperand.get(2);


            ListLiteral leftListLiteral = buildValues(instancesBinding, leftParam);
            ListLiteral middleListLiteral = buildValues(instancesBinding, middleParam);
            ListLiteral rightListLiteral = buildValues(instancesBinding, rightParam);

            if (leftListLiteral != null && middleListLiteral != null && rightListLiteral != null) {

                List<?> lefts = leftListLiteral.getValue();
                List<?> middles = middleListLiteral.getValue();
                List<?> rights = rightListLiteral.getValue();


                Iterator<?> leftIterator = lefts.iterator();
                Iterator<?> middleIterator = middles.iterator();
                Iterator<?> rightIterator = rights.iterator();

                // dataTypeExpression = valueListLiteral.getType();

                List<Triple<?, ?, ?>> pairs = new ArrayList<>();
                while (leftIterator.hasNext() && rightIterator.hasNext()) {

                    Object leftValue = leftIterator.next();
                    Object middleValue = middleIterator.next();
                    Object rightValue = rightIterator.next();

                    pairs.add(Triple.of(leftValue, middleValue, rightValue));
                }

                return new ListLiteral(pairs, TripleType.class);
            }


        }


        return new NullLiteral<>();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}
