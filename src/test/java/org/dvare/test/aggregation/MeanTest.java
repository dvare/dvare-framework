package org.dvare.test.aggregation;

import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.util.ValueFinder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class MeanTest {
    @Test
    public void dataSetMeanTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();
        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "FloatType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", aggregationTypes);
        contexts.addContext("data", validationTypes);
        Expression aggregate = factory.getParser().fromString("self.A0 := data.V1->mean (  )", contexts);


        RuleBinding rule = new RuleBinding(aggregate);


        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 0);

        List<Object> dataSet = new ArrayList<>();
        Map<String, Object> d1 = new HashMap<>();
        d1.put("V1", 10);
        dataSet.add(new DataRow(d1));


        Map<String, Object> d2 = new HashMap<>();
        d2.put("V1", 20);
        dataSet.add(new DataRow(d2));

        Map<String, Object> d3 = new HashMap<>();
        d3.put("V1", 40);
        dataSet.add(new DataRow(d3));

        Map<String, Object> d4 = new HashMap<>();
        d4.put("V1", 55);
        dataSet.add(new DataRow(d4));

        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(aggregation));
        instancesBinding.addInstance("data", dataSet);
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");


        boolean result = ((Float) ValueFinder.findValue("A0", resultModel)).compareTo(31.00f) == 0;

        assertTrue(result);
    }

    @Test
    public void floatArrayMeanTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        Expression expression = factory.getParser().fromString("[10.0,20.0,40.0,55.0] -> mean () = 31.25", new ContextsBinding());


        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(expression), new InstancesBinding(new HashMap<>()));

        assertTrue(result);
    }

    @Test
    public void integerArrayMeanTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        Expression expression = factory.getParser().fromString("[10,20,40,55] -> mean () = 31", new ContextsBinding());


        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(expression), new InstancesBinding(new HashMap<>()));

        assertTrue(result);
    }


}
