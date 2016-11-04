package org.dvare.builder;


import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;

public class VariableBuilder {

    private String name;

    private DataType type;

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

    public VariableExpression build() throws IllegalPropertyException {
        VariableExpression variableExpression = VariableType.getVariableType(name, type);
        return variableExpression;
    }
}
