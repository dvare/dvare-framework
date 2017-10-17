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
package org.dvare.test.predefined;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.ArithmeticOperation;
import org.junit.Assert;
import org.junit.Test;

public class ToIntegerTest {


    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();


        String expr = "Variable5->substring(2,2)->toInteger() between [80,90] " +
                "and Variable5->substring(3,2)->toInteger() between [30,50]";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable5("9845");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertTrue(result);
    }


    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String expr = "5 -> toInteger() = 5 " +
                "and 5.0 -> toInteger() = 5 " +
                "and '5' -> toInteger() = 5 " +
                "and null -> toInteger() = null ";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), new InstancesBinding());
        Assert.assertTrue(result);
    }


    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 -> toInteger() = 5 " +
                "and Variable3 -> toInteger() = 5 " +
                "and Variable5 -> toInteger() = 5 " +
                "and Variable2 ->toString() -> toInteger() = null ";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable1(5);
        ArithmeticOperation.setVariable3(5.0f);
        ArithmeticOperation.setVariable5("5");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertTrue(result);
    }

    @Test
    public void testApp4() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 ->toString() -> toInteger() = 5 " +
                "and Variable3 ->toString() -> toInteger() = 5 " +
                "and Variable5 ->toString() -> toInteger() = 5 ";

        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);

        RuleBinding rule = new RuleBinding(expression);

        ArithmeticOperation ArithmeticOperation = new ArithmeticOperation();
        ArithmeticOperation.setVariable1(5);
        ArithmeticOperation.setVariable3(5.0f);
        ArithmeticOperation.setVariable5("5");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, ArithmeticOperation);
        Assert.assertTrue(result);
    }
}
