package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.*;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.utility.LeftPriority;
import org.dvare.expression.operation.utility.RightPriority;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.util.TrimString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.MATCH)
public class Match extends OperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(Match.class);

    protected List<Expression> leftOperand;

    public Match() {
        super(OperationType.MATCH);
    }

    public Match(OperationType operationType) {
        super(operationType);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contextss) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contextss);

        computeParam(leftOperand);
        stack.push(this);
        return pos;
    }


    private void computeParam(List<Expression> expressions) throws ExpressionParseException {


        if (expressions == null || expressions.size() < 2) {
            String error = "Match Operation minimum two parameter";
            logger.error(error);
            throw new ExpressionParseException(error);
        }


        if (logger.isDebugEnabled()) {
            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());
        }
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        Stack<Expression> localStack = new Stack<>();
        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                if (op instanceof RightPriority) {
                    this.leftOperand = new ArrayList<>(localStack);
                    return pos;
                } else if (!(op instanceof LeftPriority)) {
                    pos = op.parse(tokens, pos, localStack, contexts);
                }
            } else {

                localStack.add(buildExpression(token, contexts, pos, tokens));

            }
        }
        return pos;
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {


        List<Expression> expressions = this.leftOperand;



        /* values to match */

        Expression valueParam = expressions.get(0);
        List<?> values = buildValues(instancesBinding, valueParam);
        DataType dataType = toDataType(dataTypeExpression);


        /*match params*/
        Expression paramsExpression = expressions.get(1);
        List<?> matchParams = buildMatchParams(instancesBinding, paramsExpression);


        Boolean insideCombination = false;
        Boolean combinationExist = false;


        if (expressions.size() == 2) {

            combinationExist = true;  //backward compatibility


        } else {
            if (expressions.size() > 2) {
                if (expressions.get(2) instanceof BooleanLiteral) {
                    insideCombination = ((BooleanLiteral) expressions.get(2)).getValue();
                }
            }


            if (expressions.size() > 3) {
                if (expressions.get(3) instanceof BooleanLiteral) {
                    combinationExist = ((BooleanLiteral) expressions.get(3)).getValue();
                }
            }
        }


        return match(dataType, values, matchParams, insideCombination, combinationExist);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }


    protected List<?> buildValues(InstancesBinding instancesBinding, Expression valueParam) throws InterpretException {

        OperationExpression operationExpression = new ValuesOperation();
        operationExpression.setLeftOperand(valueParam);

        Object interpret = operationExpression.interpret(instancesBinding);

        if (interpret instanceof ListLiteral) {

            ListLiteral listLiteral = (ListLiteral) interpret;
            dataTypeExpression = listLiteral.getType();
            return listLiteral.getValue();
        }

        return null;
    }


    protected List<?> buildMatchParams(InstancesBinding instancesBinding, Expression paramsExpression) throws InterpretException {
        List matchParams = null;
        if (paramsExpression instanceof LiteralExpression) {
            matchParams = buildMatchParams((LiteralExpression<?>) paramsExpression);


        } else if (paramsExpression instanceof OperationExpression) {
            OperationExpression operationExpression = (OperationExpression) paramsExpression;
            LiteralExpression<?> interpret = operationExpression.interpret(instancesBinding);

            matchParams = buildMatchParams(interpret);

        } else if (paramsExpression instanceof VariableExpression) {
            VariableExpression<?> variableExpression = (VariableExpression<?>) paramsExpression;
            LiteralExpression<?> literalExpression = variableExpression.interpret(instancesBinding);
            matchParams = new ArrayList<>();
            matchParams.add(literalExpression.getValue());

        }
        return matchParams;
    }


    protected LiteralExpression<?> match(DataType dataType, List<?> values, List<?> matchParams, Boolean insideCombination, Boolean combinationExist) throws InterpretException {

        if (dataType != null && matchParams != null && values != null) {

            if (matchParams.isEmpty() && values.isEmpty()) {
                return LiteralType.getLiteralExpression(true, BooleanType.class);
            }


            switch (dataType) {
                case StringType: {


                    List<String> stringValues = (List<String>) values;
                    List<String> matchParamsStringValues = (List<String>) matchParams;


                    if (insideCombination && combinationExist) {
                        boolean result = insideCombinationString(stringValues, matchParamsStringValues) &&
                                combinationExistString(stringValues, matchParamsStringValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);

                    } else if (insideCombination) {
                        boolean result = insideCombinationString(stringValues, matchParamsStringValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    } else if (combinationExist) {
                        boolean result = combinationExistString(stringValues, matchParamsStringValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    }


                }


                case IntegerType: {


                    List<Integer> integerValues = (List<Integer>) values;


                    List<Integer> matchParamsIntegerValues = (List<Integer>) matchParams;

                    if (insideCombination && combinationExist) {
                        boolean result = insideCombinationInteger(integerValues, matchParamsIntegerValues) &&
                                integerValues.containsAll(matchParamsIntegerValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    } else if (insideCombination) {
                        boolean result = insideCombinationInteger(integerValues, matchParamsIntegerValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    } else if (combinationExist) {
                        boolean result = integerValues.containsAll(matchParamsIntegerValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    }


                }

                case FloatType: {
                    List<Float> floatValues = (List<Float>) values;

                    List<Float> matchParamsFloatValues = (List<Float>) matchParams;

                    if (insideCombination && combinationExist) {
                        boolean result = insideCombinationFloat(floatValues, matchParamsFloatValues) &&
                                floatValues.containsAll(matchParamsFloatValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    } else if (insideCombination) {
                        boolean result = insideCombinationFloat(floatValues, matchParamsFloatValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    } else if (combinationExist) {
                        boolean result = floatValues.containsAll(matchParamsFloatValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    }

                }

                default: {


                    List<Object> objectValues = (List<Object>) values;
                    List<Object> matchParamsValues = (List<Object>) matchParams;

                    if (insideCombination && combinationExist) {
                        return LiteralType.getLiteralExpression(
                                insideCombination(objectValues, matchParamsValues)
                                        && objectValues.containsAll(matchParamsValues), BooleanType.class);
                    } else if (insideCombination) {
                        return LiteralType.getLiteralExpression(
                                insideCombination(objectValues, matchParamsValues), BooleanType.class);
                    } else if (combinationExist) {

                        boolean result = objectValues.containsAll(matchParamsValues);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    }


                }
            }
        }
        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }


    private Boolean insideCombinationString(List<String> stringValues, List<String> matchParamsStringValues)
            throws InterpretException {

        for (String value : stringValues) {
            value = TrimString.trim(value);
            if (!matchParamsStringValues.contains(value)) {
                return false;
            }
        }
        return true;
    }

    private Boolean combinationExistString(List<String> stringValues, List<String> matchParamsStringValues)
            throws InterpretException {

        List<String> stringValuesTrimed = new ArrayList<>();
        for (String value : stringValues) {
            if (value != null) {
                stringValuesTrimed.add(TrimString.trim(value));
            } else {
                stringValuesTrimed.add(null);
            }
        }

        List<String> matchParamsStringValuesTrimed = new ArrayList<>();
        for (String value : matchParamsStringValues) {
            if (value != null) {
                matchParamsStringValuesTrimed.add(TrimString.trim(value));
            } else {
                matchParamsStringValuesTrimed.add(null);
            }
        }

        return stringValuesTrimed.containsAll(matchParamsStringValuesTrimed);

    }

    private Boolean insideCombination(List<?> values, List<?> matchParamsBValues) throws InterpretException {
        for (Object value : values) {

            if (!matchParamsBValues.contains(value)) {
                return false;
            }
        }
        return true;

    }

    private Boolean insideCombinationInteger(List<Integer> integerValues, List<Integer> matchParamsIntegerValues) throws InterpretException {
        for (Integer value : integerValues) {
            if (!matchParamsIntegerValues.contains(value)) {
                return false;
            }
        }
        return true;

    }

    private Boolean insideCombinationFloat(List<Float> floatValues, List<Float> matchParamsFloatValues) throws InterpretException {
        for (Float value : floatValues) {
            if (!matchParamsFloatValues.contains(value)) {
                return false;
            }
        }
        return true;

    }


    private List<?> buildMatchParams(LiteralExpression<?> literalExpression) {
        List matchParams = new ArrayList<>();
        if (literalExpression instanceof ListLiteral) {
            ListLiteral listLiteral = (ListLiteral) literalExpression;
            if (listLiteral.getValue() != null) {
                matchParams = listLiteral.getValue();
            }
        } else if (!(literalExpression instanceof NullLiteral)) {
            matchParams.add(literalExpression.getValue());
        }

        return matchParams;
    }


    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        toStringBuilder.append(operationType.getTokens().get(0));
        toStringBuilder.append("( ");
        if (leftOperand != null && !leftOperand.isEmpty()) {
            for (Expression expression : leftOperand) {
                toStringBuilder.append(expression.toString());
                toStringBuilder.append(" , ");
            }
        }

        toStringBuilder.append(" )");
        return toStringBuilder.toString();
    }

    public List<Expression> getLeftListOperand() {
        return leftOperand;
    }

    public void setLeftListOperand(List<Expression> leftOperand) {
        this.leftOperand = leftOperand;
    }


   /* public static void main(String[] args) throws Exception {

        Match match = new Match();


        List messages = Arrays.asList("H",null, null,"V");
        List params=Arrays.asList("H","V", null);

        LiteralExpression result = match.match(DataType.StringType,messages ,params , false);
        LiteralExpression result2 = match.match(DataType.StringType, messages,params, true);


        System.out.println(match.toBoolean(result) && match.toBoolean(result2));

    }*/
}