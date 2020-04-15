package org.dvare.config;


import org.dvare.annotations.ClassFinder;
import org.dvare.annotations.FunctionMethod;
import org.dvare.annotations.FunctionService;
import org.dvare.annotations.Operation;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.util.DataTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class RuleConfigurationProvider {
    private static final Logger logger = LoggerFactory.getLogger(RuleConfigurationProvider.class);
    private final String[] functionBasePackages;
    public ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

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
                            if (configurationRegistry.getFunction(functionBinding.getMethodName()) == null) {
                                configurationRegistry.registerFunction(functionBinding);
                            } else {
                                logger.error("Overloaded Function Method \"" + functionBinding.getMethodName() + "\" with params " + functionBinding.getParameters() + " Ignored.");
                            }


                        }
                    }
                }
            }
        }
    }

    private void operationInit() {
        List<Class<?>> classes = ClassFinder.findAnnotated(OperationExpression.class.getPackage().getName(), org.dvare.annotations.Operation.class);
        for (Class _class : classes) {
            Annotation annotation = _class.getAnnotation(org.dvare.annotations.Operation.class);
            if (annotation != null && annotation instanceof org.dvare.annotations.Operation) {
                configurationRegistry.registerOperation(_class, ((Operation) annotation).type().getSymbols());
            }
        }
    }


}
