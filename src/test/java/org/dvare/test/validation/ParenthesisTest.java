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
import org.dvare.test.dataobjects.ForEachOperation;
import org.dvare.test.dataobjects.Parenthesis;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class ParenthesisTest extends TestCase {
    @Test
    public void testApp() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 = 'A'" +
                " AND ( ( Variable2 = 'O'" +
                " AND Variable3 = 'R' )" +
                " OR ( Variable4 = 'G'" +
                " AND Variable5 = 'M' ) )";
        Expression expression = factory.getParser().fromString(expr, Parenthesis.class);

        RuleBinding rule = new RuleBinding(expression);

        Parenthesis parenthesis = new Parenthesis();
        parenthesis.setVariable1("A");
        parenthesis.setVariable2("O");
        parenthesis.setVariable3("R");
        parenthesis.setVariable4("K");
        parenthesis.setVariable5("M");


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, parenthesis);
        assertTrue(result);
    }


    @Test
    public void testApp03() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "not self.Variable1->substring(2,2)->toInteger() between [80,90] and not self.Variable1->substring(2,2)->toInteger() between [40,50] and  not self.Variable1->substring(2,2)->toInteger() between [30,40]";


        TypeBinding typeBinding = ExpressionParser.translate(ForEachOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        ForEachOperation eachOperation1 = new ForEachOperation();
        eachOperation1.setVariable1("D74F");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", eachOperation1);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);

    }

}
