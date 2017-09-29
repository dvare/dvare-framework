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
import org.dvare.test.dataobjects.EqualOperation;
import org.dvare.test.dataobjects.ValuesObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class MapTest {


    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        final String exp = "Variable1->substring(2,2)->values()" +
                "->map(let item:StringType -> substring(2,1) ->toInteger())->hasItem(9)";

        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString(exp, contexts);


        ValuesObject valuesObject1 = new ValuesObject();
        valuesObject1.setVariable1("42964");

        ValuesObject valuesObject2 = new ValuesObject();
        valuesObject2.setVariable1("42456");


        ValuesObject valuesObject3 = new ValuesObject();
        valuesObject3.setVariable1("424596");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(valuesObject1, valuesObject2, valuesObject3));

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);


        System.out.println(expression);

        assertTrue(result);


    }

    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        final String exp = "Variable1->substring(2,2)" +
                "->map(let item:StringType -> substring(2,1) ->toInteger())->hasItem(9)";

        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);


        RuleBinding rule = new RuleBinding(expression);


        List<ValuesObject> dataSet = new ArrayList<>();


        ValuesObject valuesObject1 = new ValuesObject();
        valuesObject1.setVariable1("42964");
        dataSet.add(valuesObject1);

        ValuesObject valuesObject2 = new ValuesObject();
        valuesObject2.setVariable1("42456");
        dataSet.add(valuesObject2);


        ValuesObject valuesObject3 = new ValuesObject();
        valuesObject3.setVariable1("424596");
        dataSet.add(valuesObject3);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);

    }


    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString(
                "Variable1->substring(2,2)-" +
                        ">values()->map(let item:StringType -> substring(2,1) ->toInteger())->hasItem(9)", contexts);


        RuleBinding rule = new RuleBinding(expression);


        List<ValuesObject> dataSet = new ArrayList<>();


        ValuesObject valuesObject1 = new ValuesObject();
        valuesObject1.setVariable1("42964");
        dataSet.add(valuesObject1);

        ValuesObject valuesObject2 = new ValuesObject();
        valuesObject2.setVariable1("42456");
        dataSet.add(valuesObject2);


        ValuesObject valuesObject3 = new ValuesObject();
        valuesObject3.setVariable1("424596");
        dataSet.add(valuesObject3);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();

        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);

    }

}
