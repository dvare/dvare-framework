package org.dvare.expression.operation.list;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.PAIR)
public class PairOperation extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(PairOperation.class);


    public PairOperation() {
        super(OperationType.PAIR);
    }

    public PairOperation(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {


        pos = findNextExpression(tokens, pos + 1, stack, expressionBinding, contexts);

        if (rightOperand.size() != 2) {
            throw new ExpressionParseException(" Pair Operation must contains 2 params ");
        }


        if (logger.isDebugEnabled()) {
            logger.debug("Operation Expression Call Expression : {}", getClass().getSimpleName());
        }
        stack.push(this);
        return pos;
    }


    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();


        toStringBuilder.append(operationType.getSymbols().get(0));
        toStringBuilder.append("( ");


        if (rightOperand != null) {
            toStringBuilder.append(rightOperand.toString());
            toStringBuilder.append(" )");
        }

        return toStringBuilder.toString();
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        if (rightOperand.size() == 2) {

            Expression left = rightOperand.get(0);


            Expression right = rightOperand.get(1);


            VariableExpression leftVariableExpression = null;
            OperationExpression leftOperationExpression = null;
            if (left instanceof VariableExpression) {
                leftVariableExpression = (VariableExpression) left;

            } else if (left instanceof ChainOperationExpression) {
                leftOperationExpression = (ChainOperationExpression) left;
                Expression expression = leftOperationExpression.getLeftOperand();
                while (expression instanceof ChainOperationExpression) {
                    expression = ((ChainOperationExpression) expression).getLeftOperand();
                }
                if (expression instanceof VariableExpression) {
                    leftVariableExpression = (VariableExpression) expression;
                }
            }


            VariableExpression rightVariableExpression = null;
            OperationExpression rightOperationExpression = null;
            if (right instanceof VariableExpression) {
                rightVariableExpression = (VariableExpression) right;
                dataTypeExpression = rightVariableExpression.getType();
            } else if (right instanceof ChainOperationExpression) {
                rightOperationExpression = (ChainOperationExpression) right;
                Expression expression = rightOperationExpression.getLeftOperand();
                while (expression instanceof ChainOperationExpression) {
                    expression = ((ChainOperationExpression) expression).getLeftOperand();
                }
                if (expression instanceof VariableExpression) {
                    rightVariableExpression = (VariableExpression) expression;
                    dataTypeExpression = rightVariableExpression.getType();
                }
            }


            if (leftVariableExpression != null && rightVariableExpression != null) {

                Object leftInstance = instancesBinding.getInstance(leftVariableExpression.getOperandType());
                List leftDataSet;
                if (leftInstance instanceof List) {
                    leftDataSet = (List) leftInstance;
                } else {
                    leftDataSet = new ArrayList<>();
                    leftDataSet.add(leftInstance);
                }

                Object rightInstance = instancesBinding.getInstance(rightVariableExpression.getOperandType());
                List rightDataSet;
                if (rightInstance instanceof List) {
                    rightDataSet = (List) rightInstance;
                } else {
                    rightDataSet = new ArrayList<>();
                    rightDataSet.add(rightInstance);
                }


                List<Pair> pairs = new ArrayList<>();
                Iterator leftIterator = leftDataSet.iterator();
                Iterator rightIterator = rightDataSet.iterator();

                while (leftIterator.hasNext() && rightIterator.hasNext()) {

                    Object templeftInstance = leftIterator.next();
                    Object temprightInstance = rightIterator.next();


                    Object leftValue;
                    if (leftOperationExpression != null) {
                        instancesBinding.addInstance(leftVariableExpression.getName(), templeftInstance);
                        LiteralExpression literalExpression = (LiteralExpression) leftOperationExpression.interpret(expressionBinding, instancesBinding);
                        leftValue = literalExpression.getValue();
                    } else {
                        leftValue = getValue(templeftInstance, leftVariableExpression.getName());

                    }

                    Object rightValue;
                    if (rightOperationExpression != null) {
                        instancesBinding.addInstance(rightVariableExpression.getName(), temprightInstance);
                        LiteralExpression literalExpression = (LiteralExpression) rightOperationExpression.interpret(expressionBinding, instancesBinding);
                        rightValue = literalExpression.getValue();
                        if (literalExpression.getType() != null && !literalExpression.getType().equals(NullType.class)) {
                            dataTypeExpression = literalExpression.getType();
                        }
                    } else {
                        rightValue = getValue(temprightInstance, rightVariableExpression.getName());

                    }


                    pairs.add(Pair.of(leftValue, rightValue));
                }


                instancesBinding.addInstance(leftVariableExpression.getOperandType(), leftInstance);
                instancesBinding.addInstance(rightVariableExpression.getOperandType(), rightDataSet);

                return new ListLiteral(pairs, dataTypeExpression);
            }


        }

        return new NullLiteral();
    }


}