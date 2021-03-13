package org.dvare.expression.veriable;


import org.apache.commons.lang3.tuple.Pair;
import org.dvare.expression.datatype.PairType;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class PairVariable extends VariableExpression<Pair<?, ?>> {


    public PairVariable(String name) {
        this(name, null);
    }

    public PairVariable(String name, Pair<?, ?> value) {
        super(name, PairType.class, value);
    }


}
