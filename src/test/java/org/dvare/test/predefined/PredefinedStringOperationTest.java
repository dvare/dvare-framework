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
package org.dvare.test.predefined;


import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionParser;
import org.dvare.test.dataobjects.ArithmeticOperation;
import org.junit.Assert;
import org.junit.Test;

public class PredefinedStringOperationTest {

    @Test
    public void prependAppendContainTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable5->prepend('dvare')->append('Test')->contains('framework')";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("framework");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertTrue(result);
    }


    @Test
    public void prependTestWhenValueIsNull() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable5->prepend('dvare') = null";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5(null);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertTrue(result);
    }

    @Test
    public void subStringTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "not self.Variable5->substring(2,2)->toInteger() " +
                "between [80,90] and not self.Variable5->substring(2,2)->toInteger() " +
                "between [40,50] and  not self.Variable5->substring(2,2)->toInteger() between [30,40]";


        TypeBinding typeBinding = ExpressionParser.translate(ArithmeticOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable5("D74F");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", arithmeticOperation);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);

    }

    @Test
    public void trimStringTest() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable5 -> trim() = 'dvare'";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("dvare ");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertTrue(result);
    }

    @Test
    public void trimStringTestWhenValueIsNull() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable5 -> trim() = 'dvare'";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5(null);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertFalse(result);
    }
}
