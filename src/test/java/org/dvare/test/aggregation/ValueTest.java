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

import java.util.HashMap;
import java.util.Map;

public class ValueTest extends TestCase {

    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");
        validationTypes.put("V2", "IntegerType");


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));

        Expression aggregate = factory.getParser().fromString("A0 := value ( data.V1 * data.V2 * self.A0)", contexts);

        RuleBinding rule = new RuleBinding(aggregate);


        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 2);


        Map<String, Object> dataset = new HashMap<>();
        dataset.put("V1", 5);
        dataset.put("V2", 5);


        RuleEvaluator evaluator = factory.getEvaluator();


        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(aggregation));
        instancesBinding.addInstance("data", new DataRow(dataset));
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");


        boolean result = ValueFinder.findValue("A0", resultModel).equals(50);

        assertTrue(result);
    }


}
