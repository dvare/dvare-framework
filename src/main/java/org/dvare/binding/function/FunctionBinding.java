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


package org.dvare.binding.function;


import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;

import java.util.ArrayList;
import java.util.List;

public class FunctionBinding {
    private String methodName;
    private Class functionClass;
    private DataTypeExpression returnType;
    private List<DataType> parameters = new ArrayList<>();
    private boolean isList = false;

    public FunctionBinding(String methodName, Class functionClass, DataTypeExpression returnType) {
        this(methodName, functionClass, returnType, false, null);
    }


    public FunctionBinding(String methodName, Class functionClass, DataTypeExpression returnType, boolean isList, List<DataType> parameters) {
        this.methodName = methodName;
        this.functionClass = functionClass;
        this.returnType = returnType;
        this.isList = isList;
        this.parameters = parameters;
    }

    public FunctionBinding copy() {
        return new FunctionBinding(methodName, functionClass, returnType, isList, parameters);
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

    public DataTypeExpression getReturnType() {
        return returnType;
    }

    public void setReturnType(DataTypeExpression returnType) {
        this.returnType = returnType;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public List<DataType> getParameters() {
        return parameters;
    }

    public void setParameters(List<DataType> parameters) {
        this.parameters = parameters;
    }
}
