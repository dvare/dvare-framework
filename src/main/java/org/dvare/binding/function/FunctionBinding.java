package org.dvare.binding.function;


import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class FunctionBinding {
    private String methodName;
    private Class<?> functionClass;
    private Object functionInstance;
    private Class<? extends DataTypeExpression> returnType;
    private List<DataType> parameters;


    public FunctionBinding(String methodName, Class<?> functionClass, Class<? extends DataTypeExpression> returnType) {
        this(methodName, functionClass, null, returnType, null);
    }

    public FunctionBinding(String methodName, Object functionInstance, Class<? extends DataTypeExpression> returnType) {
        this(methodName, null, functionInstance, returnType, null);
    }


    public FunctionBinding(String methodName, Class<?> functionClass, Object functionInstance, Class<? extends DataTypeExpression> returnType, List<DataType> parameters) {
        this.methodName = methodName;
        this.functionClass = functionClass;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public FunctionBinding copy() {
        return new FunctionBinding(methodName, functionClass, functionInstance, returnType, parameters);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public Class<?> getFunctionClass() {
        return functionClass;
    }

    public void setFunctionClass(Class<?> functionClass) {
        this.functionClass = functionClass;
    }

    public Class<? extends DataTypeExpression> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<? extends DataTypeExpression> returnType) {
        this.returnType = returnType;
    }


    public List<DataType> getParameters() {
        return parameters;
    }

    public void setParameters(List<DataType> parameters) {
        this.parameters = parameters;
    }

    public Object getFunctionInstance() {
        return functionInstance;
    }

    public void setFunctionInstance(Object functionInstance) {
        this.functionInstance = functionInstance;
    }
}
