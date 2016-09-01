package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.annotations.OperationType;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalOperationException;
import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.parser.ExpressionTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.AGGREGATION, symbols = {"=", "assign"})
public class Equals extends AssignOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Equals.class);

    public Equals() {
        super("=", "assign");
    }

    public Equals copy() {
        return new Equals();
    }


    protected boolean isLegalOperation(Expression expression, DataType dataType) {

        Annotation annotation = expression.getClass().getAnnotation(Operation.class);
        if (annotation != null && annotation instanceof Operation) {
            Operation operation = (Operation) annotation;
            DataType dataTypes[] = operation.dataTypes();
            if (Arrays.asList(dataTypes).contains(dataType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            pos = parseOperands(tokens, pos, stack, aTypeBinding, vTypeBinding);

            Expression left = this.leftOperand;
            Expression right = this.rightOperand;

            if (left instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) left;

                if (variableExpression.getType() != null && !isLegalOperation(right, variableExpression.getType().getDataType())) {
                    String message = String.format("Aggregation ValidationOperation %s not possible on type %s at %s", this.getClass().getSimpleName(), variableExpression.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos));
                    logger.error(message);
                    throw new IllegalOperationException(message);
                }

            } else {
                String message = String.format("Left operand of aggregation operation is not variable  near %s", ExpressionTokenizer.toString(tokens, pos));
                logger.error(message);
                throw new IllegalPropertyException(message);
            }


            logger.debug("Aggregation ValidationOperation Call Expression : {}", getClass().getSimpleName());

            stack.push(this);

            return pos;
        }


        throw new ExpressionParseException("Cannot assign literal to variable");
    }

    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {

        VariableExpression variable = null;
        Expression left = this.leftOperand;
        String veriableName = null;
        if (left instanceof VariableExpression) {
            variable = (VariableExpression) left;
            variable = VariableType.setVariableValue(variable, aggregation);
            veriableName = variable.getName();
        }

        LiteralExpression<?> literalExpression = null;
        Expression right = this.rightOperand;

        if (right instanceof AggregationOperation) {
            AggregationOperation operation = (AggregationOperation) right;
            literalExpression = (LiteralExpression) operation.interpret(aggregation, dataSet);
        }


        switch (variable.getType().getDataType()) {
            case IntegerType: {
                if (literalExpression.getValue() instanceof Integer) {


                    aggregation = setValue(aggregation, veriableName, literalExpression.getValue());


                } else {
                    aggregation = setValue(aggregation, veriableName, new Integer("" + literalExpression.getValue()));


                }
                break;
            }

            case FloatType: {
                if (literalExpression.getValue() instanceof Float) {

                    aggregation = setValue(aggregation, veriableName, literalExpression.getValue());
                } else {
                    aggregation = setValue(aggregation, veriableName, new Float("" + literalExpression.getValue()));
                }
                break;
            }

            default: {
                aggregation = setValue(aggregation, veriableName, literalExpression.getValue());
            }
        }


        return aggregation;
    }


}