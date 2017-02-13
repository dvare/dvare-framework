package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.annotations.Type;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalOperationException;
import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.AssignOperationExpression;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.parser.ExpressionTokenizer;
import org.dvare.util.TrimString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Stack;

@Operation(type = OperationType.ASSIGN)
public class Assign extends AssignOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Assign.class);

    public Assign() {
        super(OperationType.ASSIGN);
    }


    protected boolean isLegalOperation(Expression expression, DataType dataType) {

        if (expression instanceof ChainOperationExpression) {
            return true;
        }

        Annotation annotation = expression.getClass().getAnnotation(Operation.class);
        if (annotation != null) {
            Operation operation = (Operation) annotation;
            DataType dataTypes[] = operation.dataTypes();
            if (Arrays.asList(dataTypes).contains(dataType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            pos = super.parse(tokens, pos, stack, contexts);

            Expression left = this.leftOperand;
            Expression right = this.rightOperand;

            if (left instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) left;

                DataType dataType = null;

                if (variableExpression.getType().isAnnotationPresent(Type.class)) {
                    Type type = (Type) variableExpression.getType().getAnnotation(Type.class);
                    dataType = type.dataType();
                }


                if (dataType != null && !isLegalOperation(right, dataType)) {
                    String message = String.format("Aggregation OperationExpression %s not possible on type %s at %s", this.getClass().getSimpleName(), dataType, ExpressionTokenizer.toString(tokens, pos));
                    logger.error(message);
                    throw new IllegalOperationException(message);
                }

            } else {
                String message = String.format("Left operand of aggregation operation is not variable  near %s", ExpressionTokenizer.toString(tokens, pos));
                logger.error(message);
                throw new IllegalPropertyException(message);
            }


            logger.debug("Aggregation OperationExpression Call Expression : {}", getClass().getSimpleName());
/*
            stack.push(this);*/

            return pos;
        }


        throw new ExpressionParseException("Cannot assign literal to variable");
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {

        VariableExpression variable = null;
        Expression left = this.leftOperand;

        if (left instanceof VariableExpression) {
            variable = (VariableExpression) left;

            Object aggregation = instancesBinding.getInstance(variable.getOperandType());

            variable = VariableType.setVariableValue(variable, aggregation);


            Expression right = this.rightOperand;
            LiteralExpression<?> literalExpression = null;
            if (right instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) right;
                literalExpression = (LiteralExpression) operation.interpret(instancesBinding);
            }


            if (variable.getType().isAnnotationPresent(Type.class)) {
                Type type = (Type) variable.getType().getAnnotation(Type.class);


                DataType dataType = type.dataType();


                aggregation = updateValue(aggregation, dataType, variable.getName(), literalExpression);
                instancesBinding.addInstance(variable.getOperandType(), aggregation);
            }
        }

        return instancesBinding;
    }

    private Object updateValue(Object aggregation, DataType dataType, String variableName, LiteralExpression<?> literalExpression) throws InterpretException {
        Object value = literalExpression.getValue();
        if (value != null) {
            switch (dataType) {
                case IntegerType: {
                    if (value instanceof Integer) {
                        aggregation = setValue(aggregation, variableName, value);
                    } else {
                        aggregation = setValue(aggregation, variableName, new Integer("" + value));
                    }
                    break;
                }

                case FloatType: {
                    if (value instanceof Float) {

                        aggregation = setValue(aggregation, variableName, literalExpression.getValue());
                    } else {
                        aggregation = setValue(aggregation, variableName, new Float("" + value));
                    }
                    break;
                }

                case StringType: {
                    if (value instanceof String) {

                        value = TrimString.trim((String) value);
                        aggregation = setValue(aggregation, variableName, value);
                    } else {
                        value = TrimString.trim(value.toString());
                        aggregation = setValue(aggregation, variableName, value);
                    }
                    break;
                }

                default: {
                    aggregation = setValue(aggregation, variableName, literalExpression.getValue());
                }
            }
        } else {
            aggregation = setValue(aggregation, variableName, null);
        }
        return aggregation;
    }

}