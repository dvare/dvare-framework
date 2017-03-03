/*The MIT License (MIT)

Copyright (c) 2016-2017 Muhammad Hammad

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


package org.dvare.test.validation;


import junit.framework.TestCase;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.Function;
import org.dvare.util.ValueFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FunctionTest extends TestCase {


    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

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


    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable3 = fun ( addFunction , Variable1, Variable2 )->toString()", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(5);
        function.setVariable2(10);
        function.setVariable3("15");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }


    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun( addTenFunction , Variable2, [4,5,6] )", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable2(10);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }


    public void testApp21() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = 20 and fun ( addTenFunction , Variable2, [4,5,6] )->toString() = '20'", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable2(10);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }


    public void testApp3() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun ( addTenFunction , Variable3->toInteger(), [4,5,6] )", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable3("10");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }


    public void testApp31() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable1 = fun ( addTenFunction , Variable3->toInteger(), [4,5,6] ) and fun ( addTenFunction , Variable3->toInteger(), [4,5,6] ) <= 20", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(20);
        function.setVariable3("10");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }


    public void testApp4() throws ExpressionParseException, InterpretException, ClassNotFoundException {

        RuleConfiguration factory = new RuleConfiguration(new String[]{"org.dvare.util"});


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
        Object resultModel = evaluator.aggregate(rules, new DataRow(bindings), dataSet);

        System.out.println(ValueFinder.findValue("A0", resultModel));

        boolean result = ValueFinder.findValue("A0", resultModel).equals(30);

        assertTrue(result);
    }


    public void testApp5() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.util"});

        Expression expression = configuration.getParser().fromString("Variable2 := fun( addTenFunction , 5, data.Variable1 )", Function.class, Function.class);
        RuleBinding rule = new RuleBinding(expression);


        Function function = new Function();


        List<Object> dataSet = new ArrayList<>();
        Function function1 = new Function();
        function1.setVariable1(4);
        dataSet.add(function1);


        Function function2 = new Function();
        function2.setVariable1(5);
        dataSet.add(function2);


        Function function3 = new Function();
        function3.setVariable1(6);
        dataSet.add(function3);

        RuleEvaluator evaluator = configuration.getEvaluator();
        Object resultModel = evaluator.aggregate(rule, function, dataSet);
        boolean result = ValueFinder.findValue("Variable2", resultModel).equals(15);

        assertTrue(result);
    }
}
