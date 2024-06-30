package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.FunctionCallException;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.datatype.StringType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.ClassUtils;
import org.dvare.util.DataTypeMapping;
import org.dvare.util.InstanceUtils;
import org.dvare.util.TrimString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.FUNCTION)
public class Function extends OperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(Function.class);


    public Function() {
        super(OperationType.FUNCTION);
    }

    public Function(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        FunctionExpression functionExpression = (FunctionExpression) stack.pop();


        this.leftOperand = functionExpression;


        if (this.leftOperand == null) {

            String error;
            if (!functionExpression.getParameters().isEmpty()) {
                error = String.format("No Function Expression Found, %s is not  Function Expression", functionExpression.getParameters().get(functionExpression.getParameters().size() - 1).getClass().getSimpleName());
            } else {
                error = "No Table Expression Found";
            }

            logger.error(error);
            throw new ExpressionParseException(error);
        }
        if (logger.isDebugEnabled()) {

            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());

        }
        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        Stack<Expression> localStack = new Stack<>();
        boolean first = true;
        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

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

                    if (functionExpression != null) {
                        functionExpression.setParameters(parameters);
                        stack.push(functionExpression);
                    }

                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, localStack, contexts);
                }
            } else if (configurationRegistry.getFunction(token) != null && first) {
                FunctionBinding functionBinding = configurationRegistry.getFunction(token);
                FunctionExpression functionExpression = new FunctionExpression(token, functionBinding);
                localStack.add(functionExpression);
                first = false;
            } else {

                Expression expression = buildExpression(token, contexts, pos, tokens);
                if (first && expression instanceof VariableExpression) {
                    FunctionExpression functionExpression = new FunctionExpression(expression, null);
                    localStack.add(functionExpression);
                    first = false;
                } else {
                    localStack.add(expression);
                }


            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        return interpretFunction(instancesBinding);

    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }


    protected LiteralExpression<?> interpretFunction(InstancesBinding instancesBinding) throws InterpretException {

        FunctionExpression functionExpression = (FunctionExpression) this.leftOperand;

        FunctionBinding functionBinding = functionExpression.getBinding();

        if (functionBinding == null) {
            LiteralExpression<?> literalExpression = functionExpression.getName().interpret(instancesBinding);
            ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
            functionBinding = configurationRegistry.getFunction(literalExpression.getValue().toString());
        }


        if (functionBinding == null) {
            return new NullLiteral<>();
        }

        List<Expression> functionParameters = functionExpression.getParameters();

        int parmsSize = functionParameters.size();
        Class<?>[] params = new Class[parmsSize];
        Object[] values = new Object[parmsSize];


        List<DataType> parameters = functionBinding.getParameters();


        int counter = 0;
        for (Expression parameterExpression : functionParameters) {
            DataType parameter = parameters.get(counter);
            Class originalType = DataTypeMapping.getDataTypeMapping(parameter);

            if (parameterExpression instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) parameterExpression;
                LiteralExpression<?> literalExpression;
                literalExpression = operation.interpret(instancesBinding);

                ParamValue paramsValue = buildLiteralParam(literalExpression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;

            } else if (parameterExpression instanceof VariableExpression) {
                VariableExpression<?> variableExpression = (VariableExpression<?>) parameterExpression;

                ParamValue paramsValue = buildVariableExpression(instancesBinding, variableExpression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;

            } else if (parameterExpression instanceof LiteralExpression<?>) {

                ParamValue paramsValue = buildLiteralParam(parameterExpression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;
            }

            counter++;

        }


        return invokeFunction(functionBinding, params, values);

    }

    private ParamValue buildVariableExpression(InstancesBinding instancesBinding, VariableExpression<?> variableExpression, Class<?> originalType) throws InterpretException {
        ParamValue paramsValue = new ParamValue();

        Object instance = instancesBinding.getInstance(variableExpression.getOperandType());

        try {
            DataType variableDataType = toDataType(variableExpression.getType());

            if (originalType != null) {
                if (originalType.isArray()) {


                    List dataSet;
                    if (instance instanceof Collection) {
                        dataSet = (List) instance;
                    } else {
                        dataSet = new ArrayList<>();
                        dataSet.add(instance);
                    }
                    List<Object> listValues = new ArrayList<>();
                    for (Object dataRow : dataSet) {
                        variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
                        listValues.add(variableExpression.getValue());
                    }

                    Class<?> type = DataTypeMapping.getDataTypeMapping(variableDataType);
                    Object[] typedArray = (Object[]) Array.newInstance(type, dataSet.size());
                    Object value = listValues.toArray(typedArray);
                    paramsValue.param = originalType;
                    paramsValue.value = value;

                } else {

                    List dataSet;
                    if (instance instanceof List) {
                        dataSet = (List) instance;
                        if (!dataSet.isEmpty()) {
                            variableExpression = VariableType.setVariableValue(variableExpression, dataSet.get(0));
                        }
                    } else {
                        variableExpression = VariableType.setVariableValue(variableExpression, instance);
                    }

                    Object value = variableExpression.getValue();
                    paramsValue.param = originalType;
                    paramsValue.value = value;
                }

            }

        } catch (Exception e) {
            throw new InterpretException(e);
        }
        return paramsValue;
    }

    private ParamValue buildLiteralParam(Expression expression, Class<?> originalType) throws InterpretException {
        ParamValue paramsValue = new ParamValue();
        if (expression instanceof ListLiteral) {
            ListLiteral listLiteral = (ListLiteral) expression;

            try {

                DataType literalDataType = toDataType(listLiteral.getType());

                if (originalType.isArray()) {

                    Class<?> type = DataTypeMapping.getDataTypeMapping(literalDataType);
                    Object[] typedArray = (Object[]) Array.newInstance(type, listLiteral.getSize());
                    Object value = listLiteral.getValue().toArray(typedArray);

                    paramsValue.param = originalType;
                    paramsValue.value = value;

                } else {
                    Object value = null;
                    if (!listLiteral.isEmpty()) {
                        value = listLiteral.getValue().get(0);
                    }


                    paramsValue.param = originalType;
                    paramsValue.value = value;
                }
            } catch (Exception e) {
                throw new InterpretException(e.getMessage(), e);
            }


        } else {


            LiteralExpression<?> literalExpression = (LiteralExpression<?>) expression;


            if (originalType.isArray()) {
                List<Object> listValues = new ArrayList<>();

                if (literalExpression.getType().equals(StringType.class)) {
                    Object value = literalExpression.getValue();
                    if (value != null) {
                        listValues.add(TrimString.trim(value.toString()));
                    }

                } else {
                    listValues.add(literalExpression.getValue());
                }


                Class<?> type = DataTypeMapping.getDataTypeMapping(toDataType(literalExpression.getType()));
                Object[] typedArray = (Object[]) java.lang.reflect.Array.newInstance(type, 1);
                Object value = listValues.toArray(typedArray);
                paramsValue.param = originalType;
                paramsValue.value = value;

            } else {
                paramsValue.param = DataTypeMapping.getDataTypeMapping(toDataType(literalExpression.getType()));
                if (paramsValue.param == null) {
                    paramsValue.param = originalType;
                }

                if (literalExpression.getType().equals(StringType.class)) {
                    Object value = literalExpression.getValue();
                    if (value != null) {
                        paramsValue.value = TrimString.trim(value.toString());
                    }

                } else {
                    paramsValue.value = literalExpression.getValue();
                }


            }


        }
        return paramsValue;
    }

    private LiteralExpression<?> invokeFunction(FunctionBinding functionBinding, Class<?>[] params, Object[] values) throws InterpretException {

        try {

            String functionName = functionBinding.getMethodName();
            Class<? extends DataTypeExpression> returnType = functionBinding.getReturnType();
            Class<?> functionClass = functionBinding.getFunctionClass();
            Object classInstance = functionBinding.getFunctionInstance();
            Object value = null;


            if (functionClass == null && classInstance != null) {
                functionClass = classInstance.getClass();
            }


            if (functionClass != null) {


                Method method = ClassUtils.getAccessibleMethod(functionClass, functionName, params);
                if (method == null) {
                    castParams(params);
                    method = ClassUtils.getAccessibleMethod(functionClass, functionBinding.getMethodName(), params);
                }
                if (method != null) {

                    // Modifier.isStatic(method.getModifiers()
                    if (classInstance == null) {
                        classInstance = new InstanceUtils<>().newInstance(functionClass);
                    }
                    value = method.invoke(classInstance, values);


                } else {
                    throw new InterpretException("Function \"" + functionName + "\" parameter not match near " + this.toString());
                }

            }

            if (value == null) {
                return new NullLiteral<>();
            }

            if (value instanceof List) {
                List list = (List) value;
                return new ListLiteral(list, returnType);
            } else if (value.getClass().isArray()) {
                return new ListLiteral(Arrays.asList((Object[]) value), returnType);
            } else {
                return LiteralType.getLiteralExpression(value, returnType);
            }
        } catch (InvocationTargetException e) {
            Throwable target = e.getTargetException();
            if (target instanceof InterpretException) {
                throw new FunctionCallException(target.getMessage(), target);
            } else {
                throw new InterpretException(e.getMessage(), e);
            }
        } catch (Exception e) {

            throw new InterpretException(e.getMessage(), e);

        }


    }

    private Class<?>[] castParams(Class<?>[] params) throws Exception {
        for (int i = 0; i < params.length; i++) {

            Class<?> param = params[i];
            if (!param.isPrimitive() && (param.equals(Integer.class) || param.equals(Float.class) || param.equals(Boolean.class) || param.equals(Double.class) || param.equals(Long.class))) {
                Class<?> aClass = (Class<?>) ClassUtils.readStaticField(params[i], "TYPE", true);
                if (aClass != null) {
                    params[i] = aClass;
                }

            }

        }
        return params;
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

            Iterator<Expression> expressionIterator = functionExpression.getParameters().iterator();
            while (expressionIterator.hasNext()) {
                Expression expression = expressionIterator.next();
                stringBuilder.append(expression);
                if (expressionIterator.hasNext()) {
                    stringBuilder.append(", ");
                }
            }

            stringBuilder.append(")");
            return stringBuilder.toString();
        }
        return super.toString();
    }

    private static class ParamValue {
        Class<?> param;
        Object value;
    }

}