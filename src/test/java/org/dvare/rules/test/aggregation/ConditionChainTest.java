package org.dvare.rules.test.aggregation;

import junit.framework.TestCase;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.model.TypeBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.util.ValueFinder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionChainTest extends TestCase {

    @Test
    public void testApp0() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, Object> validationTypes = new HashMap<>();
        validationTypes.put("V1", DataType.IntegerType);


        Expression expression = factory.getParser().fromString("self.V1 -> values()->hasItem(20)", new TypeBinding(validationTypes));

        RuleBinding rule = new RuleBinding(expression);


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
        boolean result = (Boolean) evaluator.evaluate(rule, dataSet);
        assertTrue(result);
    }


    @Test
    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");
        aggregationTypes.put("A1", "IntegerType");

        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");


        Expression aggregate = factory.getParser().fromString("IF data.V1 -> values()->hasItem(20) THEN self.A1 := data.V1->sum () ENDIF", aggregationTypes, validationTypes);

        RuleBinding rule = new RuleBinding(aggregate);
        List<RuleBinding> rules = new ArrayList<>();
        rules.add(rule);


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
        Object resultModel = evaluator.aggregate(rules, new DataRow(new HashMap<>()), dataSet);

        boolean result = ValueFinder.findValue("A1", resultModel).equals(70);

        assertTrue(result);
    }


    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");
        aggregationTypes.put("A1", "IntegerType");

        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");


        Expression aggregate = factory.getParser().fromString("self.A1 := data.V1->values()->getItem(data.V1 -> length() - 1)", aggregationTypes, validationTypes);

        RuleBinding rule = new RuleBinding(aggregate);


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
        Object resultModel = evaluator.aggregate(rule, new DataRow(new HashMap<>()), dataSet);

        boolean result = ValueFinder.findValue("A1", resultModel).equals(20);

        assertTrue(result);
    }
}


