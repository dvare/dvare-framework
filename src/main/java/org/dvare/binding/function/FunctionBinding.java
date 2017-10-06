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
package org.dvare.binding.function;


import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class FunctionBinding {
    private String methodName;
    private Class functionClass;
    private Object functionInstance;
    private Class<? extends DataTypeExpression> returnType;
    private List<DataType> parameters = new ArrayList<>();


    public FunctionBinding(String methodName, Class functionClass, Class<? extends DataTypeExpression> returnType) {
        this(methodName, functionClass, null, returnType, null);
    }

    public FunctionBinding(String methodName, Object functionInstance, Class<? extends DataTypeExpression> returnType) {
        this(methodName, null, functionInstance, returnType, null);
    }


    public FunctionBinding(String methodName, Class functionClass, Object functionInstance, Class<? extends DataTypeExpression> returnType, List<DataType> parameters) {
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


    public Class getFunctionClass() {
        return functionClass;
    }

    public void setFunctionClass(Class functionClass) {
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
