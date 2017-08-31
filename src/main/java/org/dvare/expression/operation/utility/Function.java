/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.expression.operation.utility;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.FunctionCallException;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.StringType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.DataTypeMapping;
import org.dvare.util.TrimString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Operation(type = OperationType.FUNCTION)
public class Function extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(Function.class);


    public Function() {
        super(OperationType.FUNCTION);
    }

    public Function(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, expressionBinding, contexts);
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
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        Stack<Expression> localStack = new Stack<>();

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
                    pos = op.parse(tokens, pos, localStack, expressionBinding, contexts);
                }
            } else if (configurationRegistry.getFunction(token) != null) {
                FunctionBinding functionBinding = configurationRegistry.getFunction(token);
                FunctionExpression tableExpression = new FunctionExpression(token, functionBinding);
                localStack.add(tableExpression);

            } else {


                localStack.add(buildExpression(token, contexts));

            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }


    @Override
    public LiteralExpression interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        return interpretFunction(expressionBinding, instancesBinding);

    }


    protected LiteralExpression interpretFunction(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        FunctionExpression functionExpression = (FunctionExpression) this.leftOperand;


        Class<?> params[] = new Class[functionExpression.getParameters().size()];
        Object values[] = new Object[functionExpression.getParameters().size()];


        List<DataType> parameters = functionExpression.getBinding().getParameters();


        int counter = 0;
        for (Expression parameterExpression : functionExpression.getParameters()) {
            DataType parameter = parameters.get(counter);
            Class originalType = DataTypeMapping.getDataTypeMapping(parameter);

            if (parameterExpression instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) parameterExpression;
                LiteralExpression literalExpression;
                literalExpression = (LiteralExpression) operation.interpret(expressionBinding, instancesBinding);

                ParamValue paramsValue = buildLiteralParam(literalExpression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;

            } else if (parameterExpression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) parameterExpression;

                ParamValue paramsValue = buildVariableExpression(expressionBinding, instancesBinding, variableExpression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;

            } else if (parameterExpression instanceof LiteralExpression) {

                ParamValue paramsValue = buildLiteralParam(parameterExpression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;
            }

            counter++;

        }


        return invokeFunction(functionExpression, params, values);

    }

    private ParamValue buildVariableExpression(ExpressionBinding expressionBinding, InstancesBinding instancesBinding, VariableExpression variableExpression, Class originalType) throws InterpretException {
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
                        dataSet = new ArrayList();
                        dataSet.add(instance);
                    }
                    List<Object> listValues = new ArrayList<>();
                    for (Object dataRow : dataSet) {
                        variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
                        listValues.add(variableExpression.getValue());
                    }

                    Class type = DataTypeMapping.getDataTypeMapping(variableDataType);
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

    private ParamValue buildLiteralParam(Expression expression, Class originalType) throws InterpretException {
        ParamValue paramsValue = new ParamValue();
        if (expression instanceof ListLiteral) {
            ListLiteral listLiteral = (ListLiteral) expression;

            try {

                DataType literalDataType = toDataType(listLiteral.getType());

                if (originalType.isArray()) {

                    Class type = DataTypeMapping.getDataTypeMapping(literalDataType);
                    Object[] typedArray = Object[].class.cast(java.lang.reflect.Array.newInstance(type, listLiteral.getSize()));
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


            LiteralExpression literalExpression = (LiteralExpression) expression;


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


                Class type = DataTypeMapping.getDataTypeMapping(toDataType(literalExpression.getType()));
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

    private LiteralExpression invokeFunction(FunctionExpression functionExpression, Class<?> params[], Object values[]) throws InterpretException {

        try {
            FunctionBinding functionBinding = functionExpression.getBinding();
            String functionName = functionExpression.getName();
            Class<?> functionClass = functionBinding.getFunctionClass();
            Object classInstance = functionBinding.getFunctionInstance();
            Object value = null;


            if (functionClass == null && classInstance != null) {
                functionClass = classInstance.getClass();
            }


            if (functionClass != null) {


                Method method = MethodUtils.getAccessibleMethod(functionClass, functionName, params);
                if (method == null) {
                    params = castParams(params);
                    method = MethodUtils.getAccessibleMethod(functionClass, functionBinding.getMethodName(), params);
                }
                if (method != null) {

                    // Modifier.isStatic(method.getModifiers()
                    if (classInstance == null) {
                        classInstance = functionClass.newInstance();
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
                return new ListLiteral(list, functionExpression.binding.getReturnType());
            } else if (value.getClass().isArray()) {
                return new ListLiteral(Arrays.asList(Object[].class.cast(value)), functionExpression.binding.getReturnType());
            } else {
                return LiteralType.getLiteralExpression(value, functionExpression.binding.getReturnType());
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

    private Class[] castParams(Class[] params) throws Exception {
        for (int i = 0; i < params.length; i++) {

            Class param = params[i];
            if (!param.isPrimitive() && (param.equals(Integer.class) || param.equals(Float.class) || param.equals(Boolean.class) || param.equals(Double.class) || param.equals(Long.class))) {
                Class aClass = (Class) FieldUtils.readStaticField(params[i], "TYPE", true);
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

    private class ParamValue {
        Class<?> param;
        Object value;
    }

}