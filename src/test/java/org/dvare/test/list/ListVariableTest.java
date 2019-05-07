/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2019 DVARE (Data Validation and Aggregation Rule Engine)
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
package org.dvare.test.list;


import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ListVariableTest {

    @Test
    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "testStringArrayVariable -> notEmpty() and testStringArrayVariable = ['5', '9']";

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(ListVariableTestObject.class));

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        ListVariableTestObject testObject = new ListVariableTestObject();
        testObject.setTestStringArrayVariable(new String[]{"5", "9"});

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", testObject);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);

    }

    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "testStringListVariable -> notEmpty() and testStringListVariable = ['5', '9']";

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(ListVariableTestObject.class));

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        ListVariableTestObject testObject = new ListVariableTestObject();
        testObject.setTestStringListVariable(Arrays.asList("5", "9"));

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", testObject);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);

    }


    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListVariableTestObject.class);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", new ListVariableTestObject());

        RuleEvaluator evaluator = factory.getEvaluator();

        String exp = "testStringListVariable -> isEmpty()";
        Expression expression = factory.getParser().fromString(exp, contexts);

        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        Assert.assertTrue(result);

        exp = "testStringListVariable -> notEmpty()";
        expression = factory.getParser().fromString(exp, contexts);

        result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        Assert.assertFalse(result);
    }


    public class ListVariableTestObject {

        private String[] testStringArrayVariable;
        private List<String> testStringListVariable;

        public String[] getTestStringArrayVariable() {
            return testStringArrayVariable;
        }

        public void setTestStringArrayVariable(String[] testStringArrayVariable) {
            this.testStringArrayVariable = testStringArrayVariable;
        }

        public List<String> getTestStringListVariable() {
            return testStringListVariable;
        }

        public void setTestStringListVariable(List<String> testStringListVariable) {
            this.testStringListVariable = testStringListVariable;
        }
    }


}
