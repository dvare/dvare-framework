package org.dvare.expression.operation.validation;

import org.dvare.annotations.OperationType;
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
import org.dvare.expression.operation.Operation;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.DataTypeMapping;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"Function", "function", "fun"})
public class Function extends OperationExpression {
    static Logger logger = LoggerFactory.getLogger(Function.class);


    public Function() {
        super("Function", "function", "fun");
    }

    public Function copy() {
        return new Function();
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);
        List<Expression> expressions = new ArrayList<Expression>(stack);
        stack.clear(); // arrayList fill with stack elements
        computeFunction(expressions);
        stack.push(this);
        return i;
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding types) throws ExpressionParseException {
        int i = findNextExpression(tokens, pos + 1, stack, types);
        List<Expression> expressions = new ArrayList<Expression>(stack);
        stack.clear(); // arrayList fill with stack elements
        computeFunction(expressions);
        stack.push(this);
        return i;
    }


    private void computeFunction(List<Expression> expressions) throws ExpressionParseException {
        List<Expression> parameters = new ArrayList<>();
        FunctionExpression tabelExpression = null;
        for (Expression expression : expressions) {
            if (expression instanceof FunctionExpression) {
                tabelExpression = (FunctionExpression) expression;
            } else {
                parameters.add(expression);
            }

        }

        tabelExpression.setParameters(parameters);
        this.leftOperand = tabelExpression;


        if (this.leftOperand == null) {

            String error = null;
            if (!parameters.isEmpty()) {
                error = String.format("No Table Expression Found, %s is not  TableExpression", parameters.get(parameters.size() - 1).getClass().getSimpleName());
            } else {
                error = String.format("No Table Expression Found");
            }

            logger.error(error);
            throw new ExpressionParseException(error);
        }

        logger.debug("Operation Call Expression : {}", getClass().getSimpleName());

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

            } else if (dataTypes != null && dataTypes.getTypes().containsKey(token)) {
                DataType type = TypeFinder.findType(token, dataTypes);
                VariableExpression variableExpression = VariableType.getVariableType(token, type);
                stack.add(variableExpression);

            } else if (!token.equals(",")) {
                String type = LiteralDataType.computeType(token);
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(token, DataType.valueOf(type));
                stack.add(literalExpression);
            }
        }
        return null;
    }

    @Override
    public Object interpret(Object selfRow) throws InterpretException {
        Object result = interpretFunction(selfRow, null);
        return result;
    }

    @Override
    public Object interpret(final Object selfRow, final Object dataRow) throws InterpretException {
        Object result = interpretFunction(selfRow, dataRow);
        return result;
    }

    @Override
    public Object interpret(List<Object> dataSet) throws InterpretException {

        FunctionExpression tabelExpression = (FunctionExpression) this.leftOperand;


        Class<?> params[] = new Class[tabelExpression.getParameters().size()];
        Object values[] = new Object[tabelExpression.getParameters().size()];

        int counter = 0;
        for (Expression expression : tabelExpression.getParameters()) {

            if (expression instanceof VariableExpression) {

                VariableExpression variableExpression = (VariableExpression) expression;

                Class type = DataTypeMapping.getDataTypeMapping(variableExpression.getType().getDataType());

                List<Object> listValues = new ArrayList<>();

                for (Object dataRow : dataSet) {
                    variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
                    listValues.add(variableExpression.getValue());
                }

                //  params[counter] = DataTypeMapping.getDataTypeMappingArray(variableExpression.getType().getDataType());
                params[counter] = Object[].class;
                values[counter] = listValues.toArray();

            } else if (expression instanceof LiteralExpression) {
                LiteralExpression literal = (LiteralExpression) expression;
                params[counter] = DataTypeMapping.getDataTypeMapping(literal.getType().getDataType());
                values[counter] = literal.getValue();
            }

            counter++;
        }


        Object result = invokeFunction(tabelExpression, params, values);

        return result;
    }


    private Object interpretFunction(final Object selfRow, final Object dataRow) throws InterpretException {
        FunctionExpression tabelExpression = (FunctionExpression) this.leftOperand;


        Class<?> params[] = new Class[tabelExpression.getParameters().size()];
        Object values[] = new Object[tabelExpression.getParameters().size()];

        int counter = 0;
        for (Expression expression : tabelExpression.getParameters()) {

            if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;

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


                values[counter] = variableExpression.getValue();
                params[counter] = DataTypeMapping.getDataTypeMapping(variableExpression.getType().getDataType());

            } else if (expression instanceof LiteralExpression) {
                LiteralExpression literalExpression = (LiteralExpression) expression;
                values[counter] = literalExpression.getValue();
                params[counter] = DataTypeMapping.getDataTypeMapping(literalExpression.getType().getDataType());
            }

            counter++;
        }

        Object result = invokeFunction(tabelExpression, params, values);
        return result;
    }


    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {

        FunctionExpression tabelExpression = (FunctionExpression) this.leftOperand;

        Class<?> params[] = new Class[tabelExpression.getParameters().size()];
        Object values[] = new Object[tabelExpression.getParameters().size()];

        int counter = 0;
        for (Expression expression : tabelExpression.getParameters()) {

            if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;
                List<Object> listValues = new ArrayList<>();
                for (Object dataRow : dataSet) {
                    variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
                    listValues.add(variableExpression.getValue());
                }
                params[counter] = Object[].class;
                values[counter] = listValues.toArray();
            } else if (expression instanceof LiteralExpression) {
                LiteralExpression literal = (LiteralExpression) expression;
                params[counter] = DataTypeMapping.getDataTypeMapping(literal.getType().getDataType());
                values[counter] = literal.getValue();
            }


            counter++;
        }


        Object functionReturn = invokeFunction(tabelExpression, params, values);

        return functionReturn;
    }


    private Object invokeFunction(FunctionExpression tabelExpression, Class<?> params[], Object values[]) {

        try {
            Class functionClass = tabelExpression.getBinding().getTableClass();
            Object obj = functionClass.newInstance();
            Method method = functionClass.getMethod(tabelExpression.getName(), params);
            Object value = method.invoke(obj, values);

            if (value instanceof Collection) {
                Collection collection = (Collection) value;
                return new ListLiteral<>(collection, tabelExpression.binding.getReturnType(), collection.size());
            } else {
                return LiteralType.getLiteralExpression(value, tabelExpression.binding.getReturnType());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}