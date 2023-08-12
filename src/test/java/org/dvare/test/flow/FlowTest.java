package org.dvare.test.flow;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.dvare.test.dataobjects.ListTestModel;
import org.dvare.util.ValueFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowTest {

    private static final Logger logger = LoggerFactory.getLogger(FlowTest.class);

    @Test
    public void ifAssignmentTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");
        aggregationTypes.put("A1", "IntegerType");
        aggregationTypes.put("A2", "StringType");
        aggregationTypes.put("A3", "StringType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));

        Expression aggregate = factory.getParser().fromString("" +
                                                              "IF A0 = 2 " +
                                                              "THEN A1:=10; A2:='1(0)'; A3:='1b2' "
                                                              +
                                                              "ENDIF", contexts);

        RuleBinding rule = new RuleBinding(aggregate);

        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 2);

        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(aggregation));
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");

        Assertions.assertEquals(ValueFinder.findValue("A1", resultModel), 10);
        Assertions.assertEquals(ValueFinder.findValue("A2", resultModel), "1(0)");
        Assertions.assertEquals(ValueFinder.findValue("A3", resultModel), "1b2");
    }

    @Test
    public void ifElseAssignmentTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");
        aggregationTypes.put("A1", "IntegerType");

        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));
        Expression aggregate = factory.getParser().fromString("" +
                "IF data.V1 > 5 " +
                "THEN self.A1 := data.V1->sum () " +
                "ELSE self.A1 := data.V1->maximum ()" +
                " ENDIF", contexts);

        RuleBinding rule = new RuleBinding(aggregate);


        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 5);
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

        Assertions.assertEquals(ValueFinder.findValue("A1", resultModel), 70);
    }

    @Test
    public void ifConditionalElseTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");
        aggregationTypes.put("A1", "IntegerType");

        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));

        Expression aggregate = factory.getParser().fromString(
                "IF self.A0 <= 5 " +
                        "THEN self.A1 := data.V1->sum () " +
                        "ELSE IF self.A0 > 10" +
                        "     THEN self.A1 := data.V1->maximum () " +
                        "ENDIF", contexts);

        RuleBinding rule = new RuleBinding(aggregate);

        logger.info(aggregate.toString());

        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 12);
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

        Assertions.assertEquals(ValueFinder.findValue("A1", resultModel), 40);
    }


    @Test
    public void nustedIfElseTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");
        aggregationTypes.put("A1", "IntegerType");

        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", aggregationTypes);
        contexts.addContext("data", validationTypes);

        Expression aggregate = factory.getParser().fromString(
                "IF self.A0 > 5 " +
                        "THEN self.A1 := 0; " +
                        "     IF self.A0 > 10 " +
                        "       THEN self.A1 := data.V1->maximum () " +
                        "     ENDIF " +
                        "ELSE  self.A1 := data.V1->sum () " +
                        "ENDIF", contexts);

        RuleBinding rule = new RuleBinding(aggregate);

        logger.info(aggregate.toString());

        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 12);
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

        Object result = ValueFinder.findValue("A1", resultModel);

        Assertions.assertEquals(result, 40);
    }


    @Test
    public void ifElseIFElseTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");
        aggregationTypes.put("A1", "IntegerType");

        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));

        Expression aggregate = factory.getParser().fromString("" +
                "IF self.A0 <= 5  THEN A1 := data.V1->sum () ELSE IF self.A0 > 10 " +
                "THEN A1 := data.V1->maximum () ELSE A1 := data.V1->minimum ()" +
                " ENDIF", contexts);

        RuleBinding rule = new RuleBinding(aggregate);


        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 7);
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

        Assertions.assertEquals(ValueFinder.findValue("A1", resultModel), 10);
    }

    @Test
    public void localVariableIfElseTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));

        Expression aggregate = factory.getParser().fromString("" +
                                                              "def temp.variable:BooleanType := true "
                                                              +
                                                              "IF temp.variable = true " +
                                                              "THEN A0:=25;A0:=10 " +
                                                              "ENDIF", contexts);

        RuleBinding rule = new RuleBinding(aggregate);

        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 2);

        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(aggregation));
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");

        Assertions.assertEquals(ValueFinder.findValue("A0", resultModel), 10);
    }


    //@Test
    public void testApp11() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        Expression expression = factory.getParser().fromString("" +
                "IF self.Variable1 -> length() = 5 " +
                "THEN (self.Variable1 -> substring(3,2) -> toInteger()) " +
                "ELSE IF self.Variable1 -> length() = 4 " +
                "THEN (self.Variable1 -> substring(2,2) -> toInteger()) " +
                "ENDIF  " +
                "self.Variable1 -> notEmpty()", contexts);

        RuleBinding rule = new RuleBinding(expression);


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable1("42964");


        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable1("42453");


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable1("4245");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(valuesObject1, valuesObject2, valuesObject3));

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);

        Assertions.assertTrue(result);

    }

}
