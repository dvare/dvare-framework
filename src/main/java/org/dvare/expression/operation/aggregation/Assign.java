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
import org.dvare.expression.operation.AssignOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.parser.ExpressionTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.AGGREGATION, symbols = {":=", "assign", "update"})
public class Assign extends AssignOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Assign.class);

    public Assign() {
        super(":=", "assign", "update");
    }

    public Assign copy() {
        return new Assign();
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
                    String message = String.format("Aggregation OperationExpression %s not possible on type %s at %s", this.getClass().getSimpleName(), variableExpression.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos));
                    logger.error(message);
                    throw new IllegalOperationException(message);
                }

            } else {
                String message = String.format("Left operand of aggregation operation is not variable  near %s", ExpressionTokenizer.toString(tokens, pos));
                logger.error(message);
                throw new IllegalPropertyException(message);
            }


            logger.debug("Aggregation OperationExpression Call Expression : {}", getClass().getSimpleName());

            stack.push(this);

            return pos;
        }


        throw new ExpressionParseException("Cannot assign literal to variable");
    }

    private VariableExpression updateOprand(Object aggregation) throws InterpretException {
        VariableExpression variable = null;
        Expression left = this.leftOperand;

        if (left instanceof VariableExpression) {
            variable = (VariableExpression) left;
            variable = VariableType.setVariableValue(variable, aggregation);

        }
        return variable;
    }

    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {
        VariableExpression variable = updateOprand(aggregation);
        String variableName = variable.getName();

        LiteralExpression<?> literalExpression = null;
        Expression right = this.rightOperand;

        if (right instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) right;
            literalExpression = (LiteralExpression) operation.interpret(aggregation, dataSet);
        }

        DataType dataType = variable.getType().getDataType();

        aggregation = updateValue(aggregation, dataType, variableName, literalExpression);

        return aggregation;
    }

    @Override
    public Object interpret(Object aggregation, Object dataSet) throws InterpretException {

        VariableExpression variable = updateOprand(aggregation);
        String variableName = variable.getName();


        Expression right = this.rightOperand;
        LiteralExpression<?> literalExpression = null;
        if (right instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) right;
            literalExpression = (LiteralExpression) operation.interpret(aggregation, dataSet);
        }


        DataType dataType = variable.getType().getDataType();

        aggregation = updateValue(aggregation, dataType, variableName, literalExpression);

        return aggregation;
    }

    private Object updateValue(Object aggregation, DataType dataType, String variableName, LiteralExpression<?> literalExpression) throws InterpretException {
        switch (dataType) {
            case IntegerType: {
                if (literalExpression.getValue() instanceof Integer) {


                    aggregation = setValue(aggregation, variableName, literalExpression.getValue());


                } else {
                    aggregation = setValue(aggregation, variableName, new Integer("" + literalExpression.getValue()));


                }
                break;
            }

            case FloatType: {
                if (literalExpression.getValue() instanceof Float) {

                    aggregation = setValue(aggregation, variableName, literalExpression.getValue());
                } else {
                    aggregation = setValue(aggregation, variableName, new Float("" + literalExpression.getValue()));
                }
                break;
            }

            default: {
                aggregation = setValue(aggregation, variableName, literalExpression.getValue());
            }
        }
        return aggregation;
    }

}