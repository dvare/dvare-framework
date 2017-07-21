package org.dvare.test.list;

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

public class SizeTest extends TestCase {

    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");


//        Expression aggregate = factory.getParser().fromString("A0 := value(data.V1->values()->size())", aggregationTypes, validationTypes);
        Expression aggregate = factory.getParser().fromString("A0 := data.V1->values()->size()", aggregationTypes, validationTypes);


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
        d3.put("V1", 30);
        dataSet.add(new DataRow(d3));

        Map<String, Object> d4 = new HashMap<>();
        d4.put("V1", 40);
        dataSet.add(new DataRow(d4));

        RuleEvaluator evaluator = factory.getEvaluator();
        Object resultModel = evaluator.aggregate(rule, new DataRow(aggregation), dataSet);


        System.out.println(ValueFinder.findValue("A0", resultModel));

        boolean result = ValueFinder.findValue("A0", resultModel).equals(4);
        assertTrue(result);
    }


    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");


        Expression aggregate = factory.getParser().fromString("A0 := data.V1->size()", aggregationTypes, validationTypes);


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
        d3.put("V1", 30);
        dataSet.add(new DataRow(d3));

        Map<String, Object> d4 = new HashMap<>();
        d4.put("V1", 40);
        dataSet.add(new DataRow(d4));

        RuleEvaluator evaluator = factory.getEvaluator();
        Object resultModel = evaluator.aggregate(rule, new DataRow(aggregation), dataSet);


        System.out.println(ValueFinder.findValue("A0", resultModel));

        boolean result = ValueFinder.findValue("A0", resultModel).equals(4);
        assertTrue(result);
    }
}
