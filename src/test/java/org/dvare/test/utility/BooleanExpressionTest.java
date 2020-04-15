package org.dvare.test.utility;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.junit.Assert;
import org.junit.Test;

public class BooleanExpressionTest {

    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "true";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        InstancesBinding instancesBinding = new InstancesBinding();
        Object result = factory.getEvaluator().evaluate(new RuleBinding(expression), instancesBinding);

        Assert.assertEquals(result, true);
    }
}
