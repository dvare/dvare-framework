package org.dvare.expression.operation;

import org.dvare.annotations.Operation;
import org.dvare.annotations.Type;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.IllegalPropertyValueException;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.utility.DefOperation;
import org.dvare.expression.operation.utility.EndForAll;
import org.dvare.expression.operation.utility.ExpressionSeparator;
import org.dvare.expression.veriable.*;
import org.dvare.parser.ExpressionTokenizer;
import org.dvare.util.TrimString;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.ASSIGN)
public class AssignOperationExpression extends OperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(AssignOperationExpression.class);

    public AssignOperationExpression() {
        super(OperationType.ASSIGN);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack,
                         ContextsBinding contexts) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {

            String leftString = tokens[pos - 1];

            if (stack.isEmpty() || stack.peek() instanceof AssignOperationExpression || stack.peek() instanceof ExpressionSeparator) {
                TokenType tokenType = findDataObject(leftString, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null &&
                        TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                    this.leftOperand = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);

                }
            } else {
                this.leftOperand = stack.pop();
            }


            if (!(this.leftOperand instanceof VariableExpression || this.leftOperand instanceof DefOperation)) {

                TokenType tokenType = findDataObject(leftString, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null &&
                        TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);

                    // push back
                    stack.push(this.leftOperand);

                    this.leftOperand = VariableType.getVariableExpression(
                            tokenType.token, variableType, tokenType.type);


                } else {

                    String message = String.format("Left operand of aggregation operation is not variable  near %s",
                            ExpressionTokenizer.toString(tokens, pos, pos - 5));
                    logger.error(message);
                    throw new IllegalPropertyException(message);
                }


            }

            pos = findNextExpression(tokens, pos + 1, stack, contexts);
            if (!stack.isEmpty()) {
                this.rightOperand = stack.pop();
            }


            if (logger.isDebugEnabled()) {
                logger.debug("Aggregation OperationExpression Call Expression : {}", getClass().getSimpleName());
            }

            stack.push(this);
        }


        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack,
                                      ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                if (op instanceof ExpressionSeparator || op instanceof ConditionOperationExpression ||
                        op instanceof EndForAll || op instanceof DefOperation) {
                    return pos - 1;
                }
                pos = op.parse(tokens, pos, stack, contexts);
            } else {
                Expression expression = buildExpression(token, contexts, pos, tokens);
                if (stack.isEmpty() || stack.peek() instanceof AssignOperationExpression || stack.peek() instanceof ExpressionSeparator) {
                    stack.add(expression);
                } else {
                    return pos;
                }
            }
        }

        return pos;

    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        VariableExpression<?> variable;
        Expression left = this.leftOperand;

        if (left instanceof DefOperation) {
            left = ((DefOperation) left).leftOperand;
        }

        if (left instanceof VariableExpression) {
            variable = (VariableExpression<?>) left;

            Object assignmentInstance = instancesBinding.getInstance(variable.getOperandType());

            if (assignmentInstance == null) {
                assignmentInstance = new DataRow();
                instancesBinding.addInstance(variable.getOperandType(), assignmentInstance);
            }

            variable = VariableType.setVariableValue(variable, assignmentInstance);

            Expression valueOperand = this.rightOperand;
            LiteralExpression<?> literalExpression = null;
            if (valueOperand instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) valueOperand;
                literalExpression = operation.interpret(instancesBinding);
            } else if (valueOperand instanceof VariableExpression) {
                VariableExpression<?> variableExpression = (VariableExpression<?>) valueOperand;
                literalExpression = variableExpression.interpret(instancesBinding);
            } else if (valueOperand instanceof LiteralExpression) {
                literalExpression = (LiteralExpression<?>) valueOperand;
            } else {
                literalExpression = new NullLiteral<>();
            }

            if (variable.getType().isAnnotationPresent(Type.class)) {
                Type type = (Type) variable.getType().getAnnotation(Type.class);
                DataType dataType = type.dataType();

                assignmentInstance = updateValue(assignmentInstance, dataType, variable, literalExpression);
                instancesBinding.addInstance(variable.getOperandType(), assignmentInstance);
                return literalExpression;
            }
        }

        return new NullLiteral<>();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

    private Object updateValue(Object aggregation, DataType dataType, VariableExpression<?> variableExpression, LiteralExpression<?> literalExpression) throws InterpretException {
        String variableName = variableExpression.getName();
        Object value = literalExpression.getValue();
        if (value != null) {
            switch (dataType) {
                case IntegerType: {
                    aggregation = updateIntegerVaue(aggregation, variableExpression, variableName, value);
                    break;
                }

                case FloatType: {
                    aggregation = updateFloatValue(aggregation, variableExpression, literalExpression, variableName, value);
                    break;
                }

                case StringType: {
                    aggregation = updateStringValue(aggregation, variableExpression, variableName, value);
                    break;
                }

                case PairType: {
                    aggregation = updatePairValue(aggregation, variableExpression, variableName, value);
                    break;
                }

                case TripleType: {
                    aggregation = updateTripleValue(aggregation, variableExpression, variableName, value);
                    break;
                }

                default: {

                    aggregation = updateValue(aggregation, variableExpression, literalExpression, variableName, value);
                }
            }
        } else {
            aggregation = setValue(aggregation, variableName, null);
        }
        return aggregation;
    }


    private Object updateIntegerVaue(Object aggregation, VariableExpression<?> variableExpression, String variableName, Object value) throws IllegalPropertyValueException {
        if (variableExpression instanceof ListVariable) {
            if (value instanceof List) {
                aggregation = setValue(aggregation, variableName, value);
            } else {
                List<Object> values = new ArrayList<>();
                if (value instanceof Integer) {

                    values.add(value);
                } else {
                    values.add(Integer.parseInt("" + value));
                }
                aggregation = setValue(aggregation, variableName, values);
            }

        } else if (variableExpression instanceof IntegerVariable) {
            if (value instanceof Integer) {
                aggregation = setValue(aggregation, variableName, value);
            } else {
                aggregation = setValue(aggregation, variableName, Integer.parseInt("" + value));
            }
        }
        return aggregation;
    }

    private Object updateFloatValue(Object aggregation, VariableExpression<?> variableExpression, LiteralExpression<?> literalExpression, String variableName, Object value) throws IllegalPropertyValueException {
        if (variableExpression instanceof ListVariable) {
            if (value instanceof List) {
                aggregation = setValue(aggregation, variableName, value);
            } else {
                List<Object> values = new ArrayList<>();
                if (value instanceof Float) {
                    values.add(value);
                } else {
                    values.add(Float.parseFloat("" + value));
                }
                aggregation = setValue(aggregation, variableName, values);
            }

        } else if (variableExpression instanceof FloatVariable) {
            if (value instanceof Float) {

                aggregation = setValue(aggregation, variableName, literalExpression.getValue());
            } else {
                aggregation = setValue(aggregation, variableName, Float.parseFloat("" + value));
            }
        }
        return aggregation;
    }


    private Object updateStringValue(Object aggregation, VariableExpression<?> variableExpression, String variableName, Object value) throws IllegalPropertyValueException {
        if (variableExpression instanceof ListVariable) {
            if (value instanceof List) {
                aggregation = setValue(aggregation, variableName, value);
            } else {
                aggregation = setValue(aggregation, variableName, Collections.singletonList(TrimString.trim(value.toString())));
            }

        } else if (variableExpression instanceof StringVariable) {
            if (value instanceof String) {
                value = TrimString.trim((String) value);
                aggregation = setValue(aggregation, variableName, value);
            } else {
                value = TrimString.trim(value.toString());
                aggregation = setValue(aggregation, variableName, value);
            }
        }
        return aggregation;
    }

    private Object updatePairValue(Object aggregation, VariableExpression<?> variableExpression, String variableName, Object value) throws IllegalPropertyValueException {
        if (variableExpression instanceof ListVariable) {
            if (value instanceof List) {
                aggregation = setValue(aggregation, variableName, value);
            } else {
                aggregation = setValue(aggregation, variableName, Collections.singletonList(value));
            }

        } else if (variableExpression instanceof PairVariable) {
            if (value instanceof String) {
                aggregation = setValue(aggregation, variableName, value);
            } else {
                aggregation = setValue(aggregation, variableName, value);
            }
        }
        return aggregation;
    }

    private Object updateTripleValue(Object aggregation, VariableExpression<?> variableExpression, String variableName, Object value) throws IllegalPropertyValueException {
        if (variableExpression instanceof ListVariable) {
            if (value instanceof List) {
                aggregation = setValue(aggregation, variableName, value);
            } else {
                aggregation = setValue(aggregation, variableName, Collections.singletonList(value));
            }

        } else if (variableExpression instanceof TripleVariable) {
            if (value instanceof String) {
                aggregation = setValue(aggregation, variableName, value);
            } else {
                aggregation = setValue(aggregation, variableName, value);
            }
        }
        return aggregation;
    }

    private Object updateValue(Object aggregation, VariableExpression<?> variableExpression, LiteralExpression<?> literalExpression, String variableName, Object value) throws IllegalPropertyValueException {
        if (variableExpression instanceof ListVariable) {
            if (value instanceof List) {
                aggregation = setValue(aggregation, variableName, value);
            } else {
                aggregation = setValue(aggregation, variableName, Arrays.asList(value));
            }

        } else {
            aggregation = setValue(aggregation, variableName, literalExpression.getValue());
        }
        return aggregation;
    }


}