package org.dvare.builder;


import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class VariableBuilder {

    private String name;
    private DataType type;
    private final String operandType = "self";

    public VariableBuilder() {
    }

    public VariableBuilder(String name, DataType type) {
        this.name = name;
        this.type = type;
    }


    public VariableBuilder variableName(String name) {
        this.name = name;
        return this;
    }

    public VariableBuilder variableType(DataType type) {
        this.type = type;
        return this;
    }

    public VariableExpression<?> build() throws IllegalPropertyException {
        return VariableType.getVariableExpression(name, type, operandType);

    }
}
