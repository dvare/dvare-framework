/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2019 DVARE (Data Validation and Aggregation Rule Engine)
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
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.util.DataTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.INVOKE)
public class Invoke extends Function {
    private static Logger logger = LoggerFactory.getLogger(Invoke.class);


    public Invoke() {
        super(OperationType.INVOKE);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contexts);


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

        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);

            if (token.contains("#")) {

                String parts[] = token.split("#");
                String variable = parts[0];

                rightOperand = buildExpression(variable, contexts);

                String functionName = parts[1];

                FunctionBinding functionBinding = new FunctionBinding(functionName, null, null, null, null);
                leftOperand = new FunctionExpression(functionName, functionBinding);


            } else if (op != null) {

                if (op.getClass().equals(RightPriority.class)) {


                    List<Expression> expressions = new ArrayList<>(localStack);
                    List<Expression> parameters = new ArrayList<>();

                    if (leftOperand != null) {
                        FunctionExpression functionExpression = (FunctionExpression) leftOperand;

                        for (Expression expression : expressions) {
                            parameters.add(expression);
                        }

                        functionExpression.setParameters(parameters);
                        stack.push(functionExpression);
                    } else {
                        throw new ExpressionParseException("Function Name not Found");
                    }

                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, localStack, contexts);
                }
            } else {


                localStack.add(buildExpression(token, contexts));

            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        Object value = null;
        if (rightOperand instanceof VariableExpression) {

            VariableExpression variableExpression = (VariableExpression) rightOperand;
            LiteralExpression literalExpression = variableExpression.interpret(instancesBinding);
            value = literalExpression.getValue();

        } else if (rightOperand instanceof LiteralExpression) {
            value = LiteralExpression.class.cast(rightOperand).getValue();
        }

        if (value != null) {

            Class functionClass = value.getClass();

            FunctionExpression functionExpression = (FunctionExpression) this.leftOperand;
            FunctionBinding functionBinding = functionExpression.getBinding();

            functionBinding.setFunctionClass(functionClass);
            functionBinding.setFunctionInstance(value);


            Method method = MethodUtils.getAccessibleMethod(functionClass, functionBinding.getMethodName());

            if (method == null) {
                List<Class> parameters = new ArrayList<>();
                for (Expression expression : functionExpression.getParameters()) {
                    LiteralExpression literalExpression = expression.interpret(instancesBinding);
                    Object value1 = literalExpression.getValue();
                    parameters.add(value1.getClass());
                }

                Class[] params = parameters.toArray(new Class[parameters.size()]);

                method = MethodUtils.getAccessibleMethod(functionClass, functionBinding.getMethodName(), params);

                if (method == null) {
                    for (int i = 0; i < params.length; i++) {

                        if (!params[i].isPrimitive()) {
                            try {
                                Class aClass = (Class) FieldUtils.readStaticField(params[i], "TYPE", true);
                                params[i] = aClass;
                            } catch (IllegalAccessException ignored) {
                            }
                        }
                    }
                    method = MethodUtils.getAccessibleMethod(functionClass, functionBinding.getMethodName(), params);
                }
            }

            if (method != null) {
                List<DataType> parameters = new ArrayList<>();
                for (Class type : method.getParameterTypes()) {
                    DataType dataType = DataTypeMapping.getTypeMapping(type);
                    parameters.add(dataType);
                }
                functionBinding.setParameters(parameters);
                Class returnClass = method.getReturnType();
                if (returnClass != null && !returnClass.equals(Void.class)) {

                    DataType returnType = DataTypeMapping.getTypeMapping(returnClass);

                    Class dataTypeExpressionClass = DataTypeMapping.getDataTypeClass(returnType);
                    if (dataTypeExpressionClass != null) {

                        functionBinding.setReturnType(dataTypeExpressionClass);

                    }

                }
            } else {
                throw new InterpretException("Method Param not match");
            }


        }
        return interpretFunction(instancesBinding);

    }


}