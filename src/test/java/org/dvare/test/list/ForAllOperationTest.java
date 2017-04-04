/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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


package org.dvare.test.list;

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
import org.junit.Assert;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ForAllOperationTest extends TestCase {

    public void testApp() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "self->forAll selfInstance selfInstance.Variable1->substring(2,2)->toInteger() between [80,90] endForAll";


        TypeBinding typeBinding = ExpressionParser.translate(ForEachOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
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


    public void testApp1() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "not self->forAll selfInstance  selfInstance.Variable1->substring(2,2)->toInteger() between [80,90] endForAll";


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

   /* public void testApp2() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "self.Variable1->forAll temp.variableItem temp.variableItem->contains('D') endForAll";


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

    }*/


}

