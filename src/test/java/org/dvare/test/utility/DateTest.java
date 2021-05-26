package org.dvare.test.utility;


import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateTest {
    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "date ( 12-05-2016 , dd-MM-yyyy ) < today()";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, new InstancesBinding());
        Assertions.assertTrue(result);
    }


    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "(date ( 12-05-2016 , dd-MM-yyyy ) - date ( 12-05-1990 , dd-MM-yyyy )) = date(01-01-0026 , dd-MM-yyyy)";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, new InstancesBinding());
        Assertions.assertTrue(result);
    }

    @Test

    public void testApp3() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "'12-05-2016' ->toDate() = date ( 12-05-2016 , dd-MM-yyyy )";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, new InstancesBinding());
        Assertions.assertTrue(result);
    }


}
