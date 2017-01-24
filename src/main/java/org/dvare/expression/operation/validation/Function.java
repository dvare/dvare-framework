package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.IllegalPropertyValueException;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.DataTypeMapping;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.FUNCTION)
public class Function extends OperationExpression {
    static Logger logger = LoggerFactory.getLogger(Function.class);


    public Function() {
        super(OperationType.FUNCTION);
    }

    public Function copy() {
        return new Function();
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);
        FunctionExpression functionExpression = (FunctionExpression) stack.pop();


        this.leftOperand = functionExpression;


        if (this.leftOperand == null) {

            String error = null;
            if (!functionExpression.getParameters().isEmpty()) {
                error = String.format("No Table Expression Found, %s is not  TableExpression", functionExpression.getParameters().get(functionExpression.getParameters().size() - 1).getClass().getSimpleName());
            } else {
                error = String.format("No Table Expression Found");
            }

            logger.error(error);
            throw new ExpressionParseException(error);
        }

        logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());


        stack.push(this);
        return pos;
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
                    List<Expression> parameters = new ArrayList<>();
                    FunctionExpression functionExpression = null;
                    for (Expression expression : expressions) {
                        if (expression instanceof FunctionExpression) {
                            functionExpression = (FunctionExpression) expression;
                        } else {
                            parameters.add(expression);
                        }
                    }

                    functionExpression.setParameters(parameters);
                    stack.push(functionExpression);


                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, localStack, selfTypes, dataTypes);
                }
            } else if (configurationRegistry.getFunction(token) != null) {
                String name = token;
                FunctionBinding table = configurationRegistry.getFunction(token);
                FunctionExpression tableExpression = new FunctionExpression(name, table);
                localStack.add(tableExpression);

            } else if (token.matches(selfPatten) && selfTypes != null) {
                String name = token.substring(5, token.length());
                DataType type = TypeFinder.findType(name, selfTypes);
                VariableExpression variableExpression = VariableType.getVariableType(token, type);
                localStack.add(variableExpression);

            } else if (token.matches(dataPatten) && dataTypes != null) {
                String name = token.substring(5, token.length());
                DataType type = TypeFinder.findType(name, dataTypes);
                VariableExpression variableExpression = VariableType.getVariableType(token, type);
                localStack.add(variableExpression);

            } else if (selfTypes != null && selfTypes.getTypes().containsKey(token)) {
                DataType type = TypeFinder.findType(token, selfTypes);
                VariableExpression variableExpression = VariableType.getVariableType(token, type);
                localStack.add(variableExpression);

            } else if (dataTypes != null && dataTypes.getTypes().containsKey(token)) {
                DataType type = TypeFinder.findType(token, dataTypes);
                VariableExpression variableExpression = VariableType.getVariableType(token, type);
                localStack.add(variableExpression);

            } else {

                if (token.equals("[")) {
                    OperationExpression operationExpression = new ListOperationExpression();
                    pos = operationExpression.parse(tokens, pos, localStack, selfTypes, dataTypes);
                    Expression literalExpression = localStack.pop();
                    localStack.add(literalExpression);

                } else {
                    DataType type = LiteralType.computeDataType(token);
                    LiteralExpression literalExpression = LiteralType.getLiteralExpression(token, type);
                    localStack.add(literalExpression);
                }

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


    private Object interpretFunction(final Object selfRow, final Object dataRow) throws InterpretException {
        FunctionExpression functionExpression = (FunctionExpression) this.leftOperand;


        Class<?> params[] = new Class[functionExpression.getParameters().size()];
        Object values[] = new Object[functionExpression.getParameters().size()];
        List<DataType> parameters = functionExpression.getBinding().getParameters();
        int counter = 0;
        for (Expression expression : functionExpression.getParameters()) {
            DataType dataType = parameters.get(counter);
            Class originalType = DataTypeMapping.getDataTypeMapping(dataType);

            if (expression instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) expression;
                LiteralExpression literalExpression;
                if (selfRow != null && dataRow != null) {
                    literalExpression = (LiteralExpression) operation.interpret(selfRow, dataRow);
                } else {
                    literalExpression = (LiteralExpression) operation.interpret(selfRow);
                }

                ParamValue paramsValue = buildLiteralParam(literalExpression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;

            } else if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;
                variableExpression = buildVariableParam(variableExpression, selfRow, dataRow);

                values[counter] = variableExpression.getValue();
                params[counter] = DataTypeMapping.getDataTypeMapping(variableExpression.getType().getDataType());

            } else if (expression instanceof LiteralExpression) {

                ParamValue paramsValue = buildLiteralParam(expression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;
            }

            counter++;
        }

        Object result = invokeFunction(functionExpression, params, values);
        return result;
    }


    @Override
    public Object interpret(List<Object> dataSet) throws InterpretException {
        Object result = interpretListFunction(null, dataSet);
        return result;
    }


    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {

        Object result = interpretListFunction(aggregation, dataSet);
        return result;
    }


    private Object interpretListFunction(Object selfRow, List<Object> dataSet) throws InterpretException {

        FunctionExpression functionExpression = (FunctionExpression) this.leftOperand;


        Class<?> params[] = new Class[functionExpression.getParameters().size()];
        Object values[] = new Object[functionExpression.getParameters().size()];


        List<DataType> parameters = functionExpression.getBinding().getParameters();


        int counter = 0;
        for (Expression expression : functionExpression.getParameters()) {
            DataType dataType = parameters.get(counter);
            Class originalType = DataTypeMapping.getDataTypeMapping(dataType);

            if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;


                try {
                    DataType variableDataType = variableExpression.getType().getDataType();

                    if (originalType.isArray()) {


                        List listValues = new ArrayList<>();

                        for (Object dataRow : dataSet) {
                            variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
                            listValues.add(variableExpression.getValue());
                        }

                        Class type = DataTypeMapping.getDataTypeMapping(variableDataType);
                        Object[] typedArray = (Object[]) Array.newInstance(type, dataSet.size());
                        Object value = listValues.toArray(typedArray);
                        params[counter] = originalType;
                        values[counter] = value;

                    } else {


                        variableExpression = buildVariableParam(variableExpression, selfRow, dataSet.get(0));

                        /*
                            variableExpression = VariableType.setVariableValue(variableExpression, DATA_ROW);
                        */

                        Object value = variableExpression.getValue();
                        params[counter] = originalType;
                        values[counter] = value;
                    }

                } catch (Exception e) {
                    throw new InterpretException(e);
                }


            } else if (expression instanceof LiteralExpression) {

                ParamValue paramsValue = buildLiteralParam(expression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;
            }

            counter++;

        }


        Object functionReturn = invokeFunction(functionExpression, params, values);

        return functionReturn;
    }


    private Object invokeFunction(FunctionExpression functionExpression, Class<?> params[], Object values[]) {

        try {
            Class functionClass = functionExpression.getBinding().getFunctionClass();
            Object obj = functionClass.newInstance();

            Method method = functionClass.getMethod(functionExpression.getName(), params);
            Object value = method.invoke(obj, values);

            if (value instanceof Collection) {
                Collection collection = (Collection) value;
                return new ListLiteral<>(collection, functionExpression.binding.getReturnType(), collection.size());
            } else {
                return LiteralType.getLiteralExpression(value, functionExpression.binding.getReturnType());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    private VariableExpression buildVariableParam(VariableExpression variableExpression, final Object selfRow, final Object dataRow) throws IllegalPropertyValueException {
        String name = variableExpression.getName();
        if (selfRow != null && name.matches(selfPatten)) {
            name = name.substring(5, name.length());
            variableExpression = VariableType.setVariableValue(variableExpression, selfRow);
            variableExpression.setName(name);
        } else if (dataRow != null && name.matches(dataPatten)) {
            name = name.substring(5, name.length());
            variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
            variableExpression.setName(name);
        } else {
            variableExpression = VariableType.setVariableValue(variableExpression, selfRow);
        }
        return variableExpression;
    }

    private ParamValue buildLiteralParam(Expression expression, Class originalType) throws InterpretException {
        ParamValue paramsValue = new ParamValue();
        if (expression instanceof ListLiteral) {
            ListLiteral listLiteral = (ListLiteral) expression;

            try {

                DataType literalDataType = listLiteral.getType().getDataType();

                if (originalType.isArray()) {

                    Class type = DataTypeMapping.getDataTypeMapping(literalDataType);
                    Object[] typedArray = (Object[]) java.lang.reflect.Array.newInstance(type, listLiteral.getSize());
                    Object value = ((List) listLiteral.getValue()).toArray(typedArray);

                    paramsValue.param = originalType;
                    paramsValue.value = value;

                } else {
                    Object value = ((List) listLiteral.getValue()).get(0);
                    paramsValue.param = originalType;
                    paramsValue.value = value;
                }


            } catch (Exception e) {
                throw new InterpretException(e);
            }


        } else {
            LiteralExpression literalExpression = (LiteralExpression) expression;
            if (originalType.isArray()) {
                List listValues = new ArrayList<>();
                listValues.add(literalExpression.getValue());
                Class type = DataTypeMapping.getDataTypeMapping(literalExpression.getType().getDataType());
                Object[] typedArray = (Object[]) java.lang.reflect.Array.newInstance(type, 1);
                Object value = listValues.toArray(typedArray);
                paramsValue.param = originalType;
                paramsValue.value = value;

            } else {
                paramsValue.param = DataTypeMapping.getDataTypeMapping(literalExpression.getType().getDataType());
                paramsValue.value = literalExpression.getValue();

            }


        }
        return paramsValue;
    }

    @Override
    public String toString() {

        if (this.leftOperand instanceof FunctionExpression) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("function");
            stringBuilder.append("(");

            FunctionExpression functionExpression = (FunctionExpression) this.leftOperand;


            stringBuilder.append(functionExpression.getName());
            stringBuilder.append(", ");

            for (Expression expression : functionExpression.getParameters()) {
                stringBuilder.append(expression);
                stringBuilder.append(", ");
            }

            stringBuilder.append(")");
            return stringBuilder.toString();
        }
        return super.toString();
    }

    private class ParamValue {
        Class<?> param;
        Object value;
    }

}