package org.dvare.test.predefined;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.ArithmeticOperation;
import org.junit.Assert;
import org.junit.Test;

public class ToIntegerTest {


    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();


        String expr = "Variable5->substring(2,2)->toInteger() between [80,90] " +
                "and Variable5->substring(3,2)->toInteger() between [30,50]";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("9845");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertTrue(result);
    }


    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String expr = "5 -> toInteger() = 5 " +
                "and 5.0 -> toInteger() = 5 " +
                "and '5' -> toInteger() = 5 " +
                "and null -> toInteger() = null ";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), new InstancesBinding());
        Assert.assertTrue(result);
    }


    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 -> toInteger() = 5 " +
                "and Variable3 -> toInteger() = 5 " +
                "and Variable5 -> toInteger() = 5 " +
                "and Variable2 ->toString() -> toInteger() = null ";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable1(5);
        ArithmeticOperation.setVariable3(5.0f);
        ArithmeticOperation.setVariable5("5");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertTrue(result);
    }

    @Test
    public void testApp4() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 ->toString() -> toInteger() = 5 " +
                "and Variable3 ->toString() -> toInteger() = 5 " +
                "and Variable5 ->toString() -> toInteger() = 5 ";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable1(5);
        ArithmeticOperation.setVariable3(5.0f);
        ArithmeticOperation.setVariable5("5");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertTrue(result);
    }
}
