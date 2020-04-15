package org.dvare.test.utility;


import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionParser;
import org.dvare.test.dataobjects.Function;
import org.dvare.util.ValueFinder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;


public class FunctionTest {

    private static final Logger logger = LoggerFactory.getLogger(FunctionTest.class);

    @Test
    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun ( addFiveFunction , Variable2, Variable3 )", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(15);
        function.setVariable2(10);
        function.setVariable3("test 2nd argument");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);

        assertTrue(result);


    }

    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable3 = fun ( addFunction , Variable1, Variable2 )->toString()", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(5);
        function.setVariable2(10);
        function.setVariable3("15");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);

        assertTrue(result);
    }

    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun( addTenFunction , Variable2, [4,5,6] )", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable2(10);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);

        assertTrue(result);
    }

    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = 20 and fun ( addTenFunction , Variable2, [4,5,6] )->toString() = '20'", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable2(10);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);

        assertTrue(result);
    }

    @Test
    public void testApp4() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        String types = "{Variable1:IntegerType, Variable3:StringType }";

        Expression expression = configuration.getParser().
                fromString("Variable1 = fun ( addTenFunction , Variable3->toInteger(), [4,5,6] )", types);
        RuleBinding rule = new RuleBinding(expression);


        Function function = new Function();
        function.setVariable1(20);
        function.setVariable3("10");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);

        assertTrue(result);
    }

    @Test
    public void testApp5() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun ( addTenFunction , Variable3->toInteger(), [4,5,6] ) and fun ( addTenFunction , Variable3->toInteger(), [4,5,6] ) <= 20", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable3("10");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);

        assertTrue(result);
    }

    @Test
    public void testApp6() throws ExpressionParseException, InterpretException, ClassNotFoundException {

        RuleConfiguration factory = new RuleConfiguration(new String[]{"org.dvare.util"});


        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));

        Expression aggregate = factory.getParser().fromString("A0 := fun ( addRowsFunction , V1  )", contexts);


        RuleBinding rule = new RuleBinding(aggregate);


        Map<String, Object> bindings = new HashMap<>();
        bindings.put("A0", "0");

        List<Object> dataSet = new ArrayList<>();

        Map<String, Object> d1 = new HashMap<>();
        d1.put("V1", "10");
        dataSet.add(new DataRow(d1));

        Map<String, Object> d2 = new HashMap<>();
        d2.put("V1", "20");
        dataSet.add(new DataRow(d2));

        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(bindings));
        instancesBinding.addInstance("data", dataSet);
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");

        // System.out.println(ValueFinder.findValue("A0", resultModel));

        boolean result = ValueFinder.findValue("A0", resultModel).equals(30);

        assertTrue(result);
    }

    @Test
    public void testApp7() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});


        Expression expression = configuration.getParser().fromString("Variable2 := fun( addTenFunction , 5, [self.Variable1] )", Function.class);
        RuleBinding rule = new RuleBinding(expression);


        Function function = new Function();

        RuleEvaluator evaluator = configuration.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", function);

        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");

        boolean result = ValueFinder.findValue("Variable2", resultModel).equals(15);

        assertTrue(result);
    }

    @Test
    public void testAp8() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun( Variable3 , Variable2, [4,5,6] )", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        logger.info(expression.toString());

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable2(10);
        function.setVariable3("addTenFunction");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);

        assertTrue(result);
    }

}
