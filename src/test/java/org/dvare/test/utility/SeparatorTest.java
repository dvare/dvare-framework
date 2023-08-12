package org.dvare.test.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.junit.jupiter.api.Test;

public class SeparatorTest {
    @Test
    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");
        aggregationTypes.put("A1", "IntegerType");

        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));

        Expression aggregate = factory.getParser().fromString("self.A0 :=  data.V1 ->sum () ; self.A1 :=data.V1-> maximum (  )", contexts);


        RuleBinding rule = new RuleBinding(aggregate);


        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 0);
        aggregation.put("A1", 0);


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

        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(aggregation));
        instancesBinding.addInstance("data", dataSet);
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");

        assertEquals(ValueFinder.findValue("A0", resultModel), 70);
        assertEquals(ValueFinder.findValue("A1", resultModel), 40);
    }

    @Test
    public void testApp_2() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "StringType");
        aggregationTypes.put("A1", "StringType");
        aggregationTypes.put("A2", "StringType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));

        Expression aggregate = factory.getParser()
            .fromString("A0:='1a';A1:='2b';A2:='3c'", contexts);

        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(new HashMap<>()));

        RuleBinding rule = new RuleBinding(aggregate);
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");

        assertEquals(ValueFinder.findValue("A0", resultModel), "1a");
        assertEquals(ValueFinder.findValue("A1", resultModel), "2b");
        assertEquals(ValueFinder.findValue("A2", resultModel), "3c");
    }
}
