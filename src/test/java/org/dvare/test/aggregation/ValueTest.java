package org.dvare.test.aggregation;

import junit.framework.TestCase;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.util.ValueFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValueTest extends TestCase {

    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");
        validationTypes.put("V2", "IntegerType");

        Expression aggregate = factory.getParser().fromString("A0 := value ( data.V1 * data.V2 * self.A0)", aggregationTypes, validationTypes);

        RuleBinding rule = new RuleBinding(aggregate);
        List<RuleBinding> rules = new ArrayList<>();
        rules.add(rule);


        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 2);


        Map<String, Object> dataset = new HashMap<>();
        dataset.put("V1", 5);
        dataset.put("V2", 5);


        RuleEvaluator evaluator = factory.getEvaluator();
        Object resultModel = evaluator.aggregate(rules, new DataRow(aggregation), new DataRow(dataset));


        boolean result = ValueFinder.findValue("A0", resultModel).equals(50);

        assertTrue(result);
    }


}
