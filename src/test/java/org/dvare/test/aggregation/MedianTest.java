package org.dvare.test.aggregation;

import junit.framework.TestCase;
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
import org.dvare.util.ValueFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedianTest extends TestCase {

    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));
        Expression aggregate = factory.getParser().fromString("self.A0 := data.V1->median (  )", contexts);

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

        boolean result = ValueFinder.findValue("A0", resultModel).equals(30);

        assertTrue(result);
    }


    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        Expression expression = factory.getParser().fromString("[10,20,40,55] -> median () = 30", new ContextsBinding());


        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(expression), new InstancesBinding(new HashMap<>()));

        assertTrue(result);
    }

}
