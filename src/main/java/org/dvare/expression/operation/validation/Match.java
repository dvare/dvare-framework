package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.*;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.list.MapOperation;
import org.dvare.expression.operation.list.ValuesOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TrimString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.Match)
public class Match extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(Match.class);

    protected List<Expression> leftOperand;

    public Match() {
        super(OperationType.Match);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contextss) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, expressionBinding, contextss);

        computeParam(this.leftOperand);
        stack.push(this);
        return pos;
    }


    private void computeParam(List<Expression> expressions) throws ExpressionParseException {


        String error = null;


        if (expressions == null || expressions.size() < 2) {
            error = "Match Operation minimum two parameter";

        }


        if (error != null && !error.isEmpty()) {
            logger.error(error);
            throw new ExpressionParseException(error);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());
        }
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {
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
                    pos = op.parse(tokens, pos, localStack, expressionBinding, contexts);
                }
            } else {

                localStack.add(buildExpression(token, contexts));

            }
        }
        return pos;
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {


        List<Expression> expressions = this.leftOperand;
        DataType dataType = null;
        List values = new ArrayList<>();

        /* values to match */

        Expression valueParam = expressions.get(0);
        if (valueParam instanceof VariableExpression) {

            VariableExpression variableExpression = (VariableExpression) valueParam;

            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
            List dataSet;
            if (instance instanceof List) {
                dataSet = (List) instance;
            } else {
                dataSet = new ArrayList<>();
                dataSet.add(instance);
            }


            dataType = toDataType(variableExpression.getType());
            for (Object dataRow : dataSet) {
                variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
//                if (variableExpression.getValue() != null) {
                    values.add(variableExpression.getValue());
//                }
            }

        } else if (valueParam instanceof ValuesOperation || valueParam instanceof MapOperation) {

            Object interpret = valueParam.interpret(expressionBinding, instancesBinding);
            if (interpret instanceof LiteralExpression) {
                LiteralExpression literalExpression = (LiteralExpression) interpret;
                if (literalExpression instanceof ListLiteral) {
                    ListLiteral listLiteral = (ListLiteral) literalExpression;
                    if (listLiteral.getValue() != null) {
                        values = listLiteral.getValue();
                        dataType = toDataType(listLiteral.getType());
                    }
                }
            }
        }
        
        
        
        
        
        /*match params*/


        List matchParams = null;
        Expression paramsExpression = expressions.get(1);
        if (paramsExpression instanceof LiteralExpression) {
            matchParams = buildMatchParams((LiteralExpression) paramsExpression);


        } else if (paramsExpression instanceof OperationExpression) {
            OperationExpression operationExpression = (OperationExpression) expressions.get(1);
            Object interpret = operationExpression.interpret(expressionBinding, instancesBinding);
            if (interpret instanceof LiteralExpression) {
                matchParams = buildMatchParams((LiteralExpression) interpret);
            }
        } else if (paramsExpression instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) paramsExpression;
            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
            variableExpression = VariableType.setVariableValue(variableExpression, instance);
            matchParams = new ArrayList();
            matchParams.add(variableExpression.getValue());

        }


        Boolean fullSetMatchWithParam = false;
        if (expressions.size() > 2) {
            if (expressions.get(2) instanceof BooleanLiteral) {
                fullSetMatchWithParam = ((BooleanLiteral) expressions.get(2)).getValue();
            }
        }


        return match(dataType, values, matchParams, fullSetMatchWithParam);
    }


/*    public static void main(String[] args) throws Exception {

        Match match = new Match();

        List messages = Arrays.asList("H",null, null,"V");
        List params=Arrays.asList("H","V", null);

        LiteralExpression result = match.match(DataType.StringType,messages ,params , false);
        LiteralExpression result2 = match.match(DataType.StringType, messages,params, true);


        System.out.println(match.toBoolean(result) && match.toBoolean(result2));

    }*/


    private LiteralExpression match(DataType dataType, List values, List matchParams, Boolean fullSetMatchWithParam) throws InterpretException {

        if (dataType != null && matchParams != null && values != null) {

            if (matchParams.isEmpty() && values.isEmpty()) {
                return LiteralType.getLiteralExpression(true, BooleanType.class);
            }


            switch (dataType) {
                case StringType: {
                    List<String> valueStringList = (List<String>) values;


                    try {
                        List<String> combStringList = (List<String>) matchParams;

                        List<String> combSet = new ArrayList<>();
                        for (String value : combStringList) {
                            if (value != null) {
                                combSet.add(TrimString.trim(value));
                            } else {
                                combSet.add(null);
                            }
                        }

                        if (fullSetMatchWithParam) {

                            for (String value : valueStringList) {


                                value = TrimString.trim(value);

                                if (!combStringList.contains(value)) {
                                    return LiteralType.getLiteralExpression(false, BooleanType.class);
                                }


                            }

                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                        } else {
                            List<String> valuesList = new ArrayList<>();
                            for (String value : valueStringList) {
                                if (value != null) {
                                    valuesList.add(TrimString.trim(value));
                                } else {
                                    valuesList.add(null);
                                }
                            }
                            boolean result = valuesList.containsAll(combSet);
                            return LiteralType.getLiteralExpression(result, BooleanType.class);
                        }


                    } catch (ClassCastException e) {
                        logger.error(e.getMessage(), e);
                    }

                }

                case BooleanType: {
                    List<Boolean> valueStringList = (List<Boolean>) values;


                    try {
                        List<Boolean> combList = (List<Boolean>) matchParams;
                        if (fullSetMatchWithParam) {
                            for (Boolean value : valueStringList) {

                                if (!combList.contains(value)) {
                                    return LiteralType.getLiteralExpression(false, BooleanType.class);
                                }
                            }
                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                        } else {

                            boolean result = valueStringList.containsAll(combList);
                            return LiteralType.getLiteralExpression(result, BooleanType.class);
                        }
                    } catch (ClassCastException e) {
                        logger.error(e.getMessage(), e);
                    }

                }
                case IntegerType: {
                    List<Integer> valueStringList = (List<Integer>) values;


                    try {
                        List<Integer> combList = (List<Integer>) matchParams;

                        if (fullSetMatchWithParam) {
                            for (Integer value : valueStringList) {
                                if (!combList.contains(value)) {
                                    return LiteralType.getLiteralExpression(false, BooleanType.class);
                                }
                            }
                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                        } else {
                            boolean result = valueStringList.containsAll(combList);
                            return LiteralType.getLiteralExpression(result, BooleanType.class);
                        }


                    } catch (ClassCastException e) {
                        List<Float> combList = (List<Float>) matchParams;

                        boolean result = valueStringList.containsAll(combList);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    }

                }

                case FloatType: {
                    List<Float> valueStringList = (List<Float>) values;


                    try {
                        List<Float> combList = (List<Float>) matchParams;


                        if (fullSetMatchWithParam) {
                            for (Float value : valueStringList) {
                                if (!combList.contains(value)) {
                                    return LiteralType.getLiteralExpression(false, BooleanType.class);
                                }
                            }
                            return LiteralType.getLiteralExpression(true, BooleanType.class);
                        } else {
                            boolean result = valueStringList.containsAll(combList);
                            return LiteralType.getLiteralExpression(result, BooleanType.class);
                        }


                    } catch (ClassCastException e) {
                        List<Integer> combList = (List<Integer>) matchParams;
                        boolean result = valueStringList.containsAll(combList);
                        return LiteralType.getLiteralExpression(result, BooleanType.class);
                    }
                }

            }
        }
        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }

    private List buildMatchParams(LiteralExpression literalExpression) {
        List matchParams = new ArrayList();
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

}