package org.dvare.expression;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class NamedExpression extends Expression {
    private static final Logger logger = LoggerFactory.getLogger(NamedExpression.class);
    private String name;

    public NamedExpression(String name) {
        this.name = name;
        if (logger.isDebugEnabled()) {
            logger.debug("Named  Expression :  [{}]", name);
        }
    }


    @Override
    public String toString() {
        return name;
    }


    /*getter and setters*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
