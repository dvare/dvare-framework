/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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


import org.dvare.annotations.ClassFinder;
import org.dvare.annotations.FunctionMethod;
import org.dvare.annotations.FunctionService;
import org.dvare.annotations.Operation;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.util.DataTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RuleConfigurationProvider {
    private static Logger logger = LoggerFactory.getLogger(RuleConfigurationProvider.class);
    private final String baseOperationPackage = "org.dvare.expression.operation";
    public ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
    private String[] functionBasePackages;

    public RuleConfigurationProvider(String[] functionBasePackages) {
        this.functionBasePackages = functionBasePackages;
    }

    public void init() {
        operationInit();
        functionInit();
    }

    private void functionInit() {
        if (functionBasePackages != null) {
            for (String functionPackage : functionBasePackages) {
                List<Class<?>> classes = ClassFinder.findAnnotated(functionPackage, FunctionService.class);
                for (Class _class : classes) {
                    for (Method method : _class.getMethods()) {
                        if (method.isAnnotationPresent(FunctionMethod.class)) {
                            String functionName = method.getName();
                            FunctionMethod functionMethod = method.getAnnotation(FunctionMethod.class);
                            DataType returnType = functionMethod.returnType();
                            DataType[] parameters = functionMethod.parameters();
                            Class<? extends DataTypeExpression> returnTypeExpression = DataTypeMapping.getDataTypeClass(returnType);
                            FunctionBinding functionBinding = new FunctionBinding(functionName, _class, returnTypeExpression);
                            functionBinding.setParameters(Arrays.asList(parameters));
                            configurationRegistry.registerFunction(functionBinding);
                        }
                    }
                }
            }
        }
    }

    private void operationInit() {
        List<Class<?>> classes = ClassFinder.findAnnotated(baseOperationPackage, org.dvare.annotations.Operation.class);
        for (Class _class : classes) {
            Annotation annotation = _class.getAnnotation(org.dvare.annotations.Operation.class);
            if (annotation != null && annotation instanceof org.dvare.annotations.Operation) {
                configurationRegistry.registerOperation(_class, ((Operation) annotation).type().getSymbols());
            }
        }
    }


}
