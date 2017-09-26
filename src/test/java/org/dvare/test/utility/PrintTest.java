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
import org.junit.Assert;
import org.junit.Test;

public class PrintTest {
    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "def tmp.date:DateType := date ( 12-05-2016 , dd-MM-yyyy ); " +
                "print (tmp.date)";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding();
        evaluator.aggregate(rule, instancesBinding);
        DataRow dataRow = (DataRow) instancesBinding.getInstance("log");
        Assert.assertFalse(dataRow.getData().isEmpty());
    }


}
