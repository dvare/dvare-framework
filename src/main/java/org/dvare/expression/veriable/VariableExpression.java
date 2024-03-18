package org.dvare.expression.veriable;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;

import java.util.List;

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
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        if (instancesBinding != null) {
            Object instance = instancesBinding.getInstance(operandType);
            if (instance instanceof List) {
                instance = ((List) instance).isEmpty() ? null : ((List) instance).get(0);
            }
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
