package org.dvare.expression.operation.validation;

import org.dvare.annotations.OperationType;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.Operation;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"Found", "found"})
public class Found extends OperationExpression {
    protected List<Expression> leftOperand;

    public Found() {
        super("Found", "found");
    }

    public Found copy() {
        return new Found();
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding types) throws ExpressionParseException {
        int i = findNextExpression(tokens, pos + 1, stack, types, null);
        List<Expression> expressions = new ArrayList<Expression>(stack);
        stack.clear();
        this.leftOperand = expressions;
        stack.push(this);
        return i;
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        int i = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);
        List<Expression> expressions = new ArrayList<Expression>(stack);
        stack.clear();
        this.leftOperand = expressions;
        stack.push(this);
        return i;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        return computeFunctionParam(tokens, pos, stack, selfTypes, dataTypes);
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes) throws ExpressionParseException {

        return computeFunctionParam(tokens, pos, stack, selfTypes, null);
    }


    private Integer computeFunctionParam(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {
            String token = tokens[i];

            Operation op = configurationRegistry.getOperation(token);
            if (op != null) {
                op = op.copy();
                if (op.getClass().equals(RightPriority.class)) {
                    return i;
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

            }
        }
        return null;
    }


    @Override
    public Object interpret(Object selfRow, Object dataRow) throws InterpretException {

        return interpretLocal(selfRow, dataRow);
    }

    @Override
    public Object interpret(final Object selfRow) throws InterpretException {
        return interpretLocal(selfRow, null);
    }


    private Object interpretLocal(Object selfRow, Object dataRow) throws InterpretException {
        if (leftOperand != null) {
            for (Expression expression : leftOperand) {

                VariableExpression variableExpression = null;
                if (expression instanceof VariableExpression) {
                    variableExpression = (VariableExpression) expression;

                    String name = variableExpression.getName();
                    if (selfRow != null && name.matches(selfPatten)) {
                        name = name.substring(5, name.length());
                        variableExpression.setName(name);
                        variableExpression = VariableType.setVariableValue(variableExpression, selfRow);
                    } else if (dataRow != null && name.matches(dataPatten)) {
                        name = name.substring(5, name.length());
                        variableExpression.setName(name);
                        variableExpression = VariableType.setVariableValue(variableExpression, selfRow);
                    } else {
                        variableExpression = VariableType.setVariableValue(variableExpression, selfRow);
                    }
                }

                if (variableExpression.getValue() == null) {
                    return false;
                }
            }
        }

        return true;
    }

}