package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TrimString;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Operation(type = OperationType.COMBINATION)
public class Combination extends OperationExpression {
    static Logger logger = LoggerFactory.getLogger(Combination.class);

    protected List<Expression> leftOperand;

    public Combination() {
        super(OperationType.COMBINATION);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contextss) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 1, stack, contextss);
        List<Expression> expressions = new ArrayList<Expression>(stack);
        stack.clear();
        computeParam(expressions);
        stack.push(this);
        return i;
    }


    private void computeParam(List<Expression> expressions) throws ExpressionParseException {


        String error = null;


        if (expressions == null || expressions.isEmpty()) {
            error = String.format("No Parameters Found Expression Found");

        } else if (expressions.size() != 2) {

            error = String.format("two param need in combination function ");
        } else {

            if (!(expressions.get(0) instanceof VariableExpression)) {
                error = String.format("First param of combination function must be variable");
            }

            if (!(expressions.get(1) instanceof ListLiteral)) {
                error = String.format("Second param of combination functio must be variable");
            }


        }


        if (error != null && !error.isEmpty()) {
            logger.error(error);
            throw new ExpressionParseException(error);
        }

        this.leftOperand = expressions;

        logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());

    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                if (op.getClass().equals(RightPriority.class)) {
                    return pos;
                }
            } else if (configurationRegistry.getFunction(token) != null) {
                FunctionBinding table = configurationRegistry.getFunction(token);
                FunctionExpression tableExpression = new FunctionExpression(token, table);
                stack.add(tableExpression);

            } else {

                TokenType tokenType = findDataObject(token, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null && contexts.getContext(tokenType.type).getDataType(tokenType.token) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                    VariableExpression variableExpression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);
                    stack.add(variableExpression);

                } else {

                    LiteralExpression literalExpression = LiteralType.getLiteralExpression(token);
                    stack.add(literalExpression);
                }


            }
        }
        return null;
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {


        List<Expression> expressions = this.leftOperand;
        DataType dataType = null;
        List<Object> values = null;
        if (expressions.get(0) instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) expressions.get(0);

            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
            List<Object> dataSet;
            if (instance instanceof Collection) {
                dataSet = (List) instance;
            } else {
                dataSet = new ArrayList<>();
                dataSet.add(instance);
            }

            values = new ArrayList<>();
            dataType = variableExpression.getType().getDataType();
            for (Object dataRow : dataSet) {
                variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
                if (variableExpression.getValue() != null) {
                    values.add(variableExpression.getValue());
                }
            }

        }


        List<Object> comb = null;
        if (expressions.get(1) instanceof ListLiteral) {
            ListLiteral listLiteral = (ListLiteral) expressions.get(1);
            if (dataType == null) {
                listLiteral.getType().getDataType();
            }

            if (listLiteral.getValue() instanceof Collection) {
                comb = (List) listLiteral.getValue();
            }
        }


        if (comb != null && values != null) {


            switch (dataType) {
                case StringType: {
                    List<String> valueSet = (List<String>) (List<?>) values;
                    TreeSet<String> treeSet = new TreeSet<>();
                    for (String value : valueSet) {
                        treeSet.add(TrimString.trim(value));
                    }


                    try {
                        List<String> testValues = (List<String>) (List<?>) comb;


                        List<String> testSet = new ArrayList<>();
                        for (String value : testValues) {
                            testSet.add(TrimString.trim(value));
                        }

                        return treeSet.containsAll(testSet);

                    } catch (ClassCastException e) {
                        logger.error(e.getMessage(), e);
                    }

                }

                case IntegerType: {
                    List<Integer> valueSet = (List<Integer>) (List<?>) values;
                    TreeSet<Integer> treeSet = new TreeSet<>();
                    treeSet.addAll(valueSet);

                    try {
                        List<Integer> testSet = (List<Integer>) (List<?>) comb;
                        return treeSet.containsAll(testSet);

                    } catch (ClassCastException e) {
                        List<Float> testSet = (List<Float>) (List<?>) comb;
                        return treeSet.containsAll(testSet);
                    }

                }

                case FloatType: {
                    List<Float> valueSet = (List<Float>) (List<?>) values;


                    TreeSet<Float> treeSet = new TreeSet<>();
                    treeSet.addAll(valueSet);

                    try {
                        List<Float> testSet = (List<Float>) (List<?>) comb;
                        return treeSet.containsAll(testSet);
                    } catch (ClassCastException e) {
                        List<Integer> testSet = (List<Integer>) (List<?>) comb;
                        return treeSet.containsAll(testSet);
                    }
                }
                default: {
                    return false;
                }
            }


        }
        return false;
    }
}