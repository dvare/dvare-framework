package org.dvare.rules.test.aggregation;

import junit.framework.TestCase;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.AggregationRuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.util.ValueFinder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FunctionTestExclude extends TestCase {
    static Logger logger = LoggerFactory.getLogger(FunctionTestExclude.class);

    @Test
    public void testApp() throws ExpressionParseException, InterpretException, ClassNotFoundException {

        RuleConfiguration factory = new RuleConfiguration(new String[]{"org.dvare.rules.util"});


        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");


        Expression aggregate = factory.getAggregationParser().fromString("A0 := fun ( addRowsFunction , V1  )", aggregationTypes, validationTypes);


        RuleBinding rule = new RuleBinding(aggregate);
        List<RuleBinding> rules = new ArrayList<>();
        rules.add(rule);

        Map<String, Object> bindings = new HashMap<>();
        bindings.put("A0", "0");

        List<Object> dataSet = new ArrayList<>();

        Map<String, Object> d1 = new HashMap<>();
        d1.put("V1", "10");
        dataSet.add(new DataRow(d1));

        Map<String, Object> d2 = new HashMap<>();
        d2.put("V1", "20");
        dataSet.add(new DataRow(d2));

        AggregationRuleEvaluator evaluator = factory.getAggregationEvaluator();
        Object resultModel = evaluator.evaluate(rules, new DataRow(bindings), dataSet);

        System.out.println(ValueFinder.findValue("A0", resultModel));

        boolean result = ValueFinder.findValue("A0", resultModel).equals(30);

        assertTrue(result);
    }


}
