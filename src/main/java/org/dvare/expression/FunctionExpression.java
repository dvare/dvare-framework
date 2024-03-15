package org.dvare.expression;


import org.dvare.binding.function.FunctionBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class FunctionExpression extends Expression {
    private static final Logger logger = LoggerFactory.getLogger(FunctionExpression.class);
    public FunctionBinding binding;
    public Expression name;
    private List<Expression> parameters = new ArrayList<>();

    public FunctionExpression(String name, FunctionBinding binding) {
        this(new NamedExpression(name), binding);
    }

    public FunctionExpression(Expression name, FunctionBinding binding) {
        this.name = name;
        this.binding = binding;
        if (logger.isDebugEnabled()) {
            logger.debug("Function Expression Name  Expression : [{}]", name);
        }
    }

    /*getter and Setters*/


    public void addParameter(Expression parameter) {
        this.parameters.add(parameter);
    }

    public Expression getName() {
        return name;
    }

    public void setName(Expression name) {
        this.name = name;
    }

    public List<Expression> getParameters() {
        return parameters;
    }

    public void setParameters(List<Expression> parameters) {
        this.parameters = parameters;
    }

    public FunctionBinding getBinding() {
        return binding;
    }

    public void setBinding(FunctionBinding binding) {
        this.binding = binding;
    }


    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
