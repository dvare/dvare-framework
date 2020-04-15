package org.dvare.expression.veriable;


import org.dvare.expression.datatype.StringType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class StringVariable extends VariableExpression<String> {


    public StringVariable(String name) {
        this(name, null);
    }

    public StringVariable(String name, String value) {
        super(name, StringType.class, value);
    }


}
