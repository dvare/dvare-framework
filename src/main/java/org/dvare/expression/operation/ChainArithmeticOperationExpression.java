package org.dvare.expression.operation;

import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralDataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.validation.RightPriority;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public abstract class ChainArithmeticOperationExpression extends EqualityOperationExpression {


    protected List<Expression> rightOperand = null;

    public ChainArithmeticOperationExpression(String symbol) {
        this.symbols.add(symbol);
    }

    public ChainArithmeticOperationExpression(List<String> symbols) {
        this.symbols.addAll(symbols);
    }

    public ChainArithmeticOperationExpression(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
    }

    protected int parseOperands(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        String leftString = tokens[pos - 1];
        pos = pos + 1;


        DataType variableType;
        if (dataTypes != null && leftString.matches(dataPatten)) {
            leftString = leftString.substring(5, leftString.length());
            variableType = TypeFinder.findType(leftString, dataTypes);
            leftType = DATA;
        } else {
            if (leftString.matches(selfPatten)) {
                leftString = leftString.substring(5, leftString.length());
            }
            leftType = SELF;
            variableType = TypeFinder.findType(leftString, selfTypes);

        }


        // computing expression left side̵

        Expression left;
        if (stack.isEmpty()) {
            if (leftType != null && leftType.equals(SELF) && selfTypes.getTypes().containsKey(leftString)) {
                left = VariableType.getVariableType(leftString, variableType);
            } else if (leftType != null && leftType.equals(DATA) && dataTypes.getTypes().containsKey(leftString)) {
                left = VariableType.getVariableType(leftString, variableType);
            } else {
                left = LiteralType.getLiteralExpression(leftString, variableType);
            }

        } else {
            left = stack.pop();
        }

        this.leftOperand = left;


        if (left instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) left;
            dataType = variableExpression.getType();
        } else if (left instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) left;
            dataType = literalExpression.getType();
        }

        return pos;
    }


    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        pos = parseOperands(tokens, pos, stack, selfTypes, dataTypes);
        pos = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);
        List<Expression> expressions = new ArrayList<>(stack);
        stack.clear(); // arrayList fill with stack elements
        computeFunction(expressions);
        stack.push(this);
        return pos;
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes) throws ExpressionParseException {
        pos = parseOperands(tokens, pos, stack, selfTypes, null);
        pos = findNextExpression(tokens, pos + 1, stack, selfTypes);
        List<Expression> expressions = new ArrayList<>(stack);
        stack.clear(); // arrayList fill with stack elements
        computeFunction(expressions);
        stack.push(this);
        return pos;
    }


    private void computeFunction(List<Expression> expressions) throws ExpressionParseException {
        List<Expression> parameters = new ArrayList<>();
        for (Expression expression : expressions) {
            parameters.add(expression);
        }
        this.rightOperand = parameters;
        if (this.rightOperand.isEmpty()) {

            String error = String.format("Parameter Found of Operation %s not Found", this.getClass().getSimpleName());
            logger.error(error);
            throw new ExpressionParseException(error);
        }

        logger.debug("Operation Expression Call Expression : {}", getClass().getSimpleName());

    }


    private Integer computeFunctionParam(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {
            String token = tokens[i];
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                op = op.copy();
                if (op.getClass().equals(RightPriority.class)) {
                    return i;
                }
            } else {
                DataType type = LiteralDataType.computeDataType(token);
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(token, type);
                stack.add(literalExpression);
            }
        }
        return null;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        return computeFunctionParam(tokens, pos, stack, selfTypes, dataTypes);
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes) throws ExpressionParseException {

        return computeFunctionParam(tokens, pos, stack, selfTypes, null);
    }


    public void interpretOperand(final Object selfRow, final Object dataRow) throws InterpretException {


        Object leftDataRow;
        if (leftType != null && leftType.equals(SELF)) {
            leftDataRow = selfRow;
        } else if (leftType != null && leftType.equals(DATA)) {
            leftDataRow = dataRow;
        } else {
            leftDataRow = selfRow;
        }

        Expression leftExpression = null;
        Expression left = this.leftOperand;
        if (left instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) left;

            LiteralExpression literalExpression = null;
            if (selfRow != null && dataRow != null) {
                literalExpression = (LiteralExpression) operation.interpret(selfRow, dataRow);
            } else {
                literalExpression = (LiteralExpression) operation.interpret(selfRow);
            }

            if (literalExpression != null) {
                dataType = literalExpression.getType();
            }
            leftExpression = literalExpression;

        } else if (left instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) left;
            variableExpression = VariableType.setVariableValue(variableExpression, leftDataRow);
            if (variableExpression != null) {
                dataType = variableExpression.getType();
            }
            leftExpression = variableExpression;
        } else if (left instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) left;
            if (literalExpression != null) {
                dataType = literalExpression.getType();
            }
            leftExpression = literalExpression;

        }

        this.leftValueOperand = leftExpression;

    }

}