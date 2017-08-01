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
