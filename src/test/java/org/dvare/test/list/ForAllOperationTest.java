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
import org.dvare.binding.model.TypeBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionParser;
import org.dvare.test.dataobjects.ForEachOperation;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ForAllOperationTest {
    private static Logger logger = LoggerFactory.getLogger(ForAllOperationTest.class);

    @Test
    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forAll self selfInstance | selfInstance.Variable1->substring(2,2)->toInteger() between [80,90] endForAll";


        TypeBinding typeBinding = ExpressionParser.translate(ForEachOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);

        logger.info(expression.toString());

        RuleBinding rule = new RuleBinding(expression);


        List<ForEachOperation> dataset = new ArrayList<>();


        ForEachOperation eachOperation1 = new ForEachOperation();
        eachOperation1.setVariable1("D81");
        dataset.add(eachOperation1);

        ForEachOperation eachOperation2 = new ForEachOperation();
        eachOperation2.setVariable1("D85");
        dataset.add(eachOperation2);

        ForEachOperation eachOperation3 = new ForEachOperation();
        eachOperation3.setVariable1("D89");
        dataset.add(eachOperation3);


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataset);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);

    }

    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "not forAll self selfInstance | selfInstance.Variable1->substring(2,2)->toInteger() between [80,90] endForAll";


        TypeBinding typeBinding = ExpressionParser.translate(ForEachOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        List<ForEachOperation> dataSet = new ArrayList<>();


        ForEachOperation eachOperation1 = new ForEachOperation();
        eachOperation1.setVariable1("D81");
        dataSet.add(eachOperation1);

        ForEachOperation eachOperation2 = new ForEachOperation();
        eachOperation2.setVariable1("D45F");
        dataSet.add(eachOperation2);

        ForEachOperation eachOperation3 = new ForEachOperation();
        eachOperation3.setVariable1("D89");
        dataSet.add(eachOperation3);


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assert.assertTrue(result);

    }

    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forAll variable value:StringType | variable.value between [80,90] endForAll";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", "{variable:StringListType}");

        Expression expression = factory.getParser().fromString(exp, contexts);

        //System.out.println(expression);

        Assert.assertNotNull(expression);

    }

    @Test
    public void testApp4() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forAll Variable1 value:StringType | Variable1.value in ['D81','D45F','D89'] endForAll";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(ForEachOperation.class));

        Expression expression = factory.getParser().fromString(exp, contexts);


        ForEachOperation eachOperation1 = new ForEachOperation();
        eachOperation1.setVariable1("D81");


        ForEachOperation eachOperation2 = new ForEachOperation();
        eachOperation2.setVariable1("D45F");


        ForEachOperation eachOperation3 = new ForEachOperation();
        eachOperation3.setVariable1("D89");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(eachOperation1, eachOperation2, eachOperation3));

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        Assert.assertTrue(result);

    }

    @Test
    public void testApp5() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forAll Variable1 tmp.value:StringType | tmp.value in ['D81','D45F','D89'] endForAll";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(ForEachOperation.class));

        Expression expression = factory.getParser().fromString(exp, contexts);


        ForEachOperation eachOperation1 = new ForEachOperation();
        eachOperation1.setVariable1("D81");


        ForEachOperation eachOperation2 = new ForEachOperation();
        eachOperation2.setVariable1("D45F");


        ForEachOperation eachOperation3 = new ForEachOperation();
        eachOperation3.setVariable1("D89");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(eachOperation1, eachOperation2, eachOperation3));

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        Assert.assertTrue(result);

    }
}

