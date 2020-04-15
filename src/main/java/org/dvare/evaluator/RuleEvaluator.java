package org.dvare.evaluator;


import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.literal.LiteralExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class RuleEvaluator {
    static Logger logger = LoggerFactory.getLogger(RuleEvaluator.class);


    public Object evaluate(RuleBinding rule, Object object) throws InterpretException {
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", object);
        return evaluate(rule, instancesBinding);
    }

    public Object evaluate(RuleBinding rule, InstancesBinding instancesBinding) throws InterpretException {

        LiteralExpression literalExpression = rule.getExpression().interpret(instancesBinding);

        if (literalExpression.getValue() != null) {
            return literalExpression.getValue();
        }

        return null;
    }


    public InstancesBinding aggregate(RuleBinding rule, Object object) throws InterpretException {
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", object);

        return aggregate(rule, instancesBinding);
    }


    public InstancesBinding aggregate(RuleBinding rule, InstancesBinding instancesBinding) throws InterpretException {

        rule.getExpression().interpret(instancesBinding);
        return instancesBinding;
    }
}
