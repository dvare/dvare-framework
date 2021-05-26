package org.dvare.test.predefined;


import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionParser;
import org.dvare.test.dataobjects.ArithmeticOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PredefinedOperationTest {

    @Test
    public void testApp() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 = null && Variable5 = ''";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable5->substring(2,2) = 'va'";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("dvare");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable5->startswith('dva')";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("dvare");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp2P1() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable5->length() = 5";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("dvare");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 = 1 implies (Variable5->startswith('dv') or Variable5->startswith('dva'))";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("dvare");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp4() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable5->startswith('dva') = true or Variable5->startswith('dva') = true";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("dvare");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp5() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable5->endswith('re') = true";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("dvare");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp6() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();


        String expr = "not ( Variable5->toInteger() in [ 68 ,  71 ,  78 ] )";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("75");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test

    public void testApp7() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();


        String expr = "not ( Variable5->toInteger() between [ 68 , 78 ] )";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("79");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp8() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();


        String expr = "not (Variable5->toInteger() between [ 68 , 78 ] or Variable5->toInteger() between [ 81 , 89 ])";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("79");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp9() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String expr = "Variable5->substring(2,2)->toInteger() between [80,90] and Variable5->substring(3,2)->toInteger() in [45,46]";

        TypeBinding typeBinding = ExpressionParser.translate(ArithmeticOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);
        Expression expression = factory.getParser().fromString(expr, typeBinding);
        RuleBinding rule = new RuleBinding(expression);
        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable5("D845");


        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", arithmeticOperation);
        boolean result = (Boolean) evaluator.evaluate(rule, arithmeticOperation);
        Assertions.assertTrue(result);
    }

    @Test
    public void testApp10() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();
        String expr = "Variable5->substring(2,2)->toInteger() between [80,90] and Variable5->substring(3,2)->toInteger() in [45,46]";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);
        RuleBinding rule = new RuleBinding(expression);
        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable5("D845");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, arithmeticOperation);
        Assertions.assertTrue(result);
    }


}
