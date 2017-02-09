package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
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

    public Combination copy() {
        return new Combination();
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 2, stack, selfTypes, dataTypes); //+1 fro (

        stack.push(this);
        return i;
    }


    private void computeParam(List<Expression> expressions) throws ExpressionParseException {


        String error = null;


        if (expressions == null || expressions.isEmpty()) {
            error = "No Parameters Found Expression Found";

        } else if (expressions.size() != 2) {

            error = "two param need in combination function ";
        } else {

            if (!(expressions.get(0) instanceof VariableExpression)) {
                error = "First param of combination function must be variable";
            }

            if (!(expressions.get(1) instanceof ListLiteral)) {
                error = "Second param of combination functio must be variable";
            }


        }


        if (error != null && !error.isEmpty()) {
            logger.error(error);
            throw new ExpressionParseException(error);
        }



        logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());

    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        Stack<Expression> localStack = new Stack<>();
        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                op = op.copy();
                if (op.getClass().equals(RightPriority.class)) {
                    List<Expression> expressions = new ArrayList<>(localStack);
                    this.leftOperand = expressions;
                    return pos;
                } else {
                    pos = op.parse(tokens, pos, localStack, selfTypes, dataTypes);
                }
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

            }  else {

                LiteralExpression literalExpression = LiteralType.getLiteralExpression(token);
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