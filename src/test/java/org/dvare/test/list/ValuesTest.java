package org.dvare.test.list;


import junit.framework.TestCase;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
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

import java.util.ArrayList;
import java.util.List;

public class ValuesTest extends TestCase {

    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Variable1->substring(2,2)->values()->map(let item:StringType -> substring(2,1) ->toInteger())->hasItem(9)", contexts);

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

    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        ExpressionBinding expressionBinding = new ExpressionBinding();

        Expression expression = factory.getParser().fromString(
                "putExp(testExpression,Variable1->substring(2,2)->values()->map(let item:StringType -> substring(2,1) ->toInteger()))" +
                        "getExp(testExpression)->hasItem(9)", expressionBinding, contexts);


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

        boolean result = (Boolean) evaluator.evaluate(rule, expressionBinding, instancesBinding);
        assertTrue(result);

    }

    public void testApp3() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        ExpressionBinding expressionBinding = new ExpressionBinding();

        Expression expression = factory.getParser().fromString(
                "putExp(testExpression,Variable1->substring(2,2)->values())" +
                        "getExp(testExpression)->map(let item:StringType -> substring(2,1) ->toInteger())->hasItem(9)", expressionBinding, contexts);


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
        boolean result = (Boolean) evaluator.evaluate(rule, expressionBinding, instancesBinding);
        assertTrue(result);

    }

    public void testApp4() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Variable1->substring(2,2)->toInteger()->values()->hasItem(29)", contexts);

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


    public void testApp5() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Variable1->substring(2,2)->toInteger()->values()->filter(let item:StringType != '29')->hasItem(29)", contexts);

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
        assertFalse(result);

    }

}
