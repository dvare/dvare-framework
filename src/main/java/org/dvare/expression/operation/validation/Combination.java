package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralDataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
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

    public Combination copy() {
        return new Combination();
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding types) throws ExpressionParseException {
        int i = parse(tokens, pos, stack, types, null);
        return pos;
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);
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
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes) throws ExpressionParseException {

        return findNextExpression(tokens, pos, stack, selfTypes, null);
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {
            String token = tokens[i];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                op = op.copy();
                if (op.getClass().equals(RightPriority.class)) {
                    return i;
                }
            } else if (configurationRegistry.getFunction(token) != null) {
                String name = token;
                FunctionBinding table = configurationRegistry.getFunction(token);
                FunctionExpression tableExpression = new FunctionExpression(name, table);
                stack.add(tableExpression);

            } else if (token.matches(selfPatten) && selfTypes != null) {
                String name = token.substring(5, token.length());
                DataType type = TypeFinder.findType(name, selfTypes);
                VariableExpression variableExpression = VariableType.getVariableType(token, type);
                stack.add(variableExpression);

            } else if (token.matches(dataPatten) && dataTypes != null) {
                String name = token.substring(5, token.length());
                DataType type = TypeFinder.findType(name, dataTypes);
                VariableExpression variableExpression = VariableType.getVariableType(token, type);
                stack.add(variableExpression);

            } else if (selfTypes != null && selfTypes.getTypes().containsKey(token)) {
                DataType type = TypeFinder.findType(token, selfTypes);
                VariableExpression variableExpression = VariableType.getVariableType(token, type);
                stack.add(variableExpression);

            } else if (token.startsWith("[")) {
                DataType variableType = null;
                List<String> values = new ArrayList<>();
                while (!tokens[++i].equals("]")) {
                    String value = tokens[i];
                    if (variableType == null) {
                        variableType = LiteralDataType.computeDataType(value);
                    }
                    values.add(value);
                }
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(values.toArray(new String[values.size()]), variableType);
                stack.add(literalExpression);
            } else {
                DataType type = LiteralDataType.computeDataType(token);
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(token, type);
                stack.add(literalExpression);
            }
        }
        return null;
    }


    @Override
    public Object interpret(List<Object> dataSet) throws InterpretException {

        List<Expression> expressions = this.leftOperand;
        DataType dataType = null;
        List<Object> values = null;
        if (expressions.get(0) instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) expressions.get(0);
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