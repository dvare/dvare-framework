/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.test.flow;

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
import org.dvare.test.dataobjects.ValuesObject;
import org.dvare.util.ValueFinder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FlowTest {
    private static Logger logger = LoggerFactory.getLogger(FlowTest.class);

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

        boolean result = ValueFinder.findValue("A1", resultModel).equals(40);

        assertTrue(result);
    }

    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {

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

        boolean result = ValueFinder.findValue("A1", resultModel).equals(10);

        assertTrue(result);
    }


    @Test
    public void testApp4() throws ExpressionParseException, InterpretException {

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

        assertEquals(result, 40);
    }

    //@Test
    public void testApp11() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ValuesObject.class);


        Expression expression = factory.getParser().fromString("" +
                "IF self.Variable1 -> length() = 5 " +
                "THEN (self.Variable1 -> substring(3,2) -> toInteger()) " +
                "ELSE IF self.Variable1 -> length() = 4 " +
                "THEN (self.Variable1 -> substring(2,2) -> toInteger()) " +
                "ENDIF  " +
                "self.Variable1 -> notEmpty()", contexts);

        RuleBinding rule = new RuleBinding(expression);


        ValuesObject valuesObject1 = new ValuesObject();
        valuesObject1.setVariable1("42964");


        ValuesObject valuesObject2 = new ValuesObject();
        valuesObject2.setVariable1("42453");


        ValuesObject valuesObject3 = new ValuesObject();
        valuesObject3.setVariable1("4245");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(valuesObject1, valuesObject2, valuesObject3));

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);


        assertTrue(result);

    }

}
