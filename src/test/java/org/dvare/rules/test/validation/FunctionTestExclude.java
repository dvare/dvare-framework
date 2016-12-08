/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.rules.test.validation;


import junit.framework.TestCase;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.rules.test.validation.dataobjects.Function;
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
    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.rules.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun ( addFiveFunction , Variable2, Variable3 )", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(15);
        function.setVariable2(10);
        function.setVariable3("test 2nd argument");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }

    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.rules.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun( addTenFunction , Variable2, [4,5,6] )", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable2(10);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }

    @Test
    public void testApp21() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.rules.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = 20 and fun ( addTenFunction , Variable2, [4,5,6] )->toString() = '20'", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable2(10);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }

    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.rules.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun ( addTenFunction , Variable3->toInteger(), [4,5,6] )", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable3("10");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }

    @Test
    public void testApp4() throws ExpressionParseException, InterpretException, ClassNotFoundException {

        RuleConfiguration factory = new RuleConfiguration(new String[]{"org.dvare.rules.util"});


        Map<String, String> aggregationTypes = new HashMap<>();
        aggregationTypes.put("A0", "IntegerType");


        Map<String, String> validationTypes = new HashMap<>();
        validationTypes.put("V1", "IntegerType");


        Expression aggregate = factory.getParser().fromString("A0 := fun ( addRowsFunction , V1  )", aggregationTypes, validationTypes);


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

        RuleEvaluator evaluator = factory.getEvaluator();
        Object resultModel = evaluator.evaluate(rules, new DataRow(bindings), dataSet);

        System.out.println(ValueFinder.findValue("A0", resultModel));

        boolean result = ValueFinder.findValue("A0", resultModel).equals(30);

        assertTrue(result);
    }
}
