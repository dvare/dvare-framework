package org.dvare.expression.veriable;


import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.SimpleDateType;

import java.util.Date;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class SimpleDateVariable extends VariableExpression<Date> {


    public SimpleDateVariable(String name) {
        this(name, null);

    }

    public SimpleDateVariable(String name, Date date) {
        super(name, SimpleDateType.class, date);

    }


    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
