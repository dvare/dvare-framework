/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.test.utility;


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
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class DefTest {
    @Test
    public void testApp() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = " def newVariable:IntegerType "
                + " := 4 + 5 ; " +
                " newVariable > 7";


        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());


        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, new InstancesBinding());
        assertTrue(result);
    }


    @Test(expected = ExpressionParseException.class)
    public void testApp2() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = " def newVariable:IntegerType := 4 + 5 ; " +
                "def result:BooleanType := false ; " +
                "def result:BooleanType := newVariable > 7";


        factory.getParser().fromString(exp, new ContextsBinding());


        exp = " def newVariable:IntegerType := 4 + 5 ; " +
                "def result:BooleanType := false ; " +
                "def result := newVariable > 7";


        factory.getParser().fromString(exp, new ContextsBinding());

    }

    @Test(expected = ExpressionParseException.class)
    public void testApp2_1() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = " def newVariable:IntegerType := 4 + 5 ; " +
                "def result:BooleanType := false ; " +
                "def result := newVariable > 7";


        factory.getParser().fromString(exp, new ContextsBinding());

    }

    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");
        validationTypes.put("V2", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));

        Expression aggregate = factory.getParser().fromString("" +
                        "def temp.variable:IntegerType :=  data.V1->first() * 10 " +
                        "self.A0 := temp.variable"
                , contexts);

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

    @Test
    public void testApp4() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");
        validationTypes.put("V2", "IntegerType");


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));

        //Expression aggregate = factory.getParser().fromString("def temp.variable:IntegerType :=  10 * (data.V1->first()) ; self.A0 := temp.variable", aggregationTypes, validationTypes);
        Expression aggregate = factory.getParser().fromString("def temp.variable:IntegerType :=  10 * data.V1->first() * data.V2->first() - 100  / 10 * self.A0" +
                " self.A0 := temp.variable", contexts);

        RuleBinding rule = new RuleBinding(aggregate);


        Map<String, Object> aggregation = new HashMap<>();
        aggregation.put("A0", 2);


        Map<String, Object> dataset = new HashMap<>();
        dataset.put("V1", 3);
        dataset.put("V2", 5);


        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(aggregation));
        instancesBinding.addInstance("data", new DataRow(dataset));
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");


        boolean result = ValueFinder.findValue("A0", resultModel).equals(10);

        assertTrue(result);
    }

    @Test
    public void testApp5() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");
        validationTypes.put("V2", "IntegerType");


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(aggregationTypes));
        contexts.addContext("data", ExpressionParser.translate(validationTypes));

        Expression aggregate = factory.getParser().fromString("" +
                "def temp.variable:BooleanType := true " +
                "IF temp.variable = true " +
                "THEN A0 := 25 " +
                "ENDIF", contexts);

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


        boolean result = ValueFinder.findValue("A0", resultModel).equals(25);

        assertTrue(result);
    }


}
