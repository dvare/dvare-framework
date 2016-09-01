/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.config;


import org.dvare.annotations.*;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.operation.aggregation.AggregationOperation;
import org.dvare.expression.operation.condition.ConditionOperation;
import org.dvare.expression.operation.validation.ValidationOperation;
import org.dvare.util.DataTypeMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RuleConfigurationProvider {

    public ConfigurationRegistry configurationRegistry = null;
    private String[] functionBasePackages;
    private String baseOperationPackage = "org.dvare.expression.operation";

    public RuleConfigurationProvider(ConfigurationRegistry configurationRegistry, String[] functionBasePackages) {
        this.configurationRegistry = configurationRegistry;
        this.functionBasePackages = functionBasePackages;
        operationInit();
        functionInit();
    }


    private void functionInit() {
        if (functionBasePackages != null) {
            for (String functionPackage : functionBasePackages) {

                List<Class<?>> classes = ClassFinder.findAnnotated(functionPackage, FunctionService.class);
                for (Class _class : classes) {
                    try {


                        for (Method method : _class.getMethods()) {

                            if (method.isAnnotationPresent(FunctionMethod.class)) {

                                String functionName = method.getName();

                                FunctionMethod functionMethod = (FunctionMethod) method.getAnnotation(FunctionMethod.class);
                                DataType returnType = functionMethod.returnType();
                                DataType[] parameters = functionMethod.parameters();

                                Class returnTypeExpression = DataTypeMapping.getDataTypeClass(returnType);
                                DataTypeExpression returnTypeInstance = (DataTypeExpression) returnTypeExpression.newInstance();

                                FunctionBinding functionBinding = new FunctionBinding(functionName, _class, returnTypeInstance);

                                if (parameters != null) {
                                    List<DataTypeExpression> parameterExpressions = new ArrayList<>();
                                    for (DataType parameter : parameters) {
                                        Class parameterExpression = DataTypeMapping.getDataTypeClass(parameter);
                                        DataTypeExpression parameterExpressionInstance = (DataTypeExpression) parameterExpression.newInstance();
                                        parameterExpressions.add(parameterExpressionInstance);
                                    }
                                    functionBinding.setParameters(parameterExpressions);
                                }

                                configurationRegistry.registerFunction(functionBinding);
                            }
                        }

                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }


            }
        }
    }

    private void operationInit() {
        List<Class<?>> classes = ClassFinder.findAnnotated(baseOperationPackage, Operation.class);
        for (Class _class : classes) {
            try {

                Annotation annotation = _class.getAnnotation(Operation.class);
                if (annotation != null && annotation instanceof Operation) {
                    Operation operation = (Operation) annotation;
                    if (operation.type().equals(OperationType.VALIDATION)) {
                        configurationRegistry.registerValidationOperation((ValidationOperation) _class.newInstance());
                    } else if (operation.type().equals(OperationType.AGGREGATION)) {
                        configurationRegistry.registerAggregationOperation((AggregationOperation) _class.newInstance());
                    } else if (operation.type().equals(OperationType.CONDITION)) {
                        configurationRegistry.registerConditionOperation((ConditionOperation) _class.newInstance());
                    }
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }


    }


}
