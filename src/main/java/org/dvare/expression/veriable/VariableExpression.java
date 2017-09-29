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
package org.dvare.expression.veriable;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class VariableExpression<T> extends Expression {

    protected String name;
    protected Class<? extends DataTypeExpression> type;
    protected String operandType;
    protected T value;


    VariableExpression(String name, Class<? extends DataTypeExpression> type) {
        this(name, type, null);
    }

    VariableExpression(String name, Class<? extends DataTypeExpression> type, T value) {
        this.name = name;
        this.type = type;
        this.value = value;

    }

    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        if (value == null) {
            Object instance = instancesBinding.getInstance(operandType);
            VariableType.setVariableValue(this, instance);
        }

        return LiteralType.getLiteralExpression(value, type);
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();


        if (operandType != null) {
            stringBuilder.append(operandType);
            stringBuilder.append(".");
        }
        stringBuilder.append(name);
        return stringBuilder.toString();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<? extends DataTypeExpression> getType() {
        return type;
    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }


    public String getOperandType() {
        return operandType;
    }

    public void setOperandType(String operandType) {
        this.operandType = operandType;
    }
}
