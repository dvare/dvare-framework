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
package org.dvare.expression.datatype;

import org.dvare.annotations.OperationMapping;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.util.DataTypeMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */

public abstract class DataTypeExpression extends Expression {

    protected DataType dataType;

    public DataTypeExpression(DataType dataType) {
        this.dataType = dataType;


    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }


    public LiteralExpression evaluate(OperationExpression operationExpression, LiteralExpression left, LiteralExpression right) throws InterpretException {


        try {
            String methodName = getMethodName(operationExpression.getClass());
            if (methodName != null) {
                Method method = this.getClass().getMethod(methodName, LiteralExpression.class, LiteralExpression.class);
                Object result = method.invoke(this, left, right);

                if (result != null) {
                    DataType type = DataTypeMapping.getTypeMapping(result.getClass());

                    if (type == null) {
                        type = LiteralType.computeDataType(result.toString());
                        if (type == null) {
                            type = this.getDataType();
                        }
                    }
                    return LiteralType.getLiteralExpression(result, type);
                }
            }
            return new NullLiteral();

        } catch (Exception m) {
            throw new InterpretException(m);
        }
    }


    private String getMethodName(Class operation) {
        for (Method method : this.getClass().getMethods()) {

            if (method.isAnnotationPresent(OperationMapping.class)) {
                Annotation annotation = method.getAnnotation(OperationMapping.class);
                OperationMapping operationMapping = (OperationMapping) annotation;
                if (Arrays.asList(operationMapping.operations()).contains(operation)) {
                    return method.getName();
                }

            }
        }
        return null;
    }


    public abstract boolean equal(LiteralExpression left, LiteralExpression right);


    public abstract boolean notEqual(LiteralExpression left, LiteralExpression right);


    public abstract boolean less(LiteralExpression left, LiteralExpression right);


    public abstract boolean lessEqual(LiteralExpression left, LiteralExpression right);


    public abstract boolean greater(LiteralExpression left, LiteralExpression right);


    public abstract boolean greaterEqual(LiteralExpression left, LiteralExpression right);


    public abstract boolean in(LiteralExpression left, LiteralExpression right);


    public abstract boolean notIn(LiteralExpression left, LiteralExpression right);


    public abstract boolean between(LiteralExpression left, LiteralExpression right);


}