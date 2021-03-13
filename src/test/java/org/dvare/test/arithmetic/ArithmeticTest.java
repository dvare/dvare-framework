package org.dvare.test.arithmetic;

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
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class ArithmeticTest {
    @Test
    public void integerArithmeticOperationsTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        final String exp = "Variable1 = (7 + 3)" +
                " And Variable1 != ( 30 - 10)" +
                " And Variable2 = (4 * 5)" +
                " And Variable1 = ( Variable2 / 2 )" +
                " And Variable1 = ( Variable1 min Variable2 )" +
                " And Variable2 = ( Variable1 max Variable2 )";

        TypeBinding typeBinding = ExpressionParser.translate(ArithmeticOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable1(10);
        arithmeticOperation.setVariable2(20);

        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", arithmeticOperation);
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);
    }

    @Test
    public void floatArithmeticOperationsTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable3 = (5.0 + 5.0)" +
                " And Variable3 = ( 20.0 - 10.0)" +
                " And Variable4 = (4.0 * 5.0)" +
                " And Variable3 = (Variable4 / 2.0 )" +
                " And Variable3 = ( Variable3 min Variable4 )" +
                " And Variable4 = ( Variable3 max Variable4 )";

        TypeBinding typeBinding = ExpressionParser.translate(ArithmeticOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);
        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable3(10.0f);
        arithmeticOperation.setVariable4(20.0f);

        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", arithmeticOperation);
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);
    }

    @Test
    public void integerNegtiveValueAddTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable1 = (-20 + 10)";

        TypeBinding typeBinding = ExpressionParser.translate(ArithmeticOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);
        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable1(-10);


        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", arithmeticOperation);
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);
    }


    @Test
    public void integerPowerTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable1 = (Variable2 pow 4)";

        TypeBinding typeBinding = ExpressionParser.translate(ArithmeticOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);
        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable1(16);
        arithmeticOperation.setVariable2(2);


        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", arithmeticOperation);
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);
    }

}
