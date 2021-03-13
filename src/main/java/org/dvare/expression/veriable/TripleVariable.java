package org.dvare.expression.veriable;


import org.apache.commons.lang3.tuple.Triple;
import org.dvare.expression.datatype.TripleType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class TripleVariable extends VariableExpression<Triple<?, ?, ?>> {


    public TripleVariable(String name) {
        this(name, null);
    }

    public TripleVariable(String name, Triple<?, ?, ?> value) {
        super(name, TripleType.class, value);
    }


}
