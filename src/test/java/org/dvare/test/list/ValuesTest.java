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
import org.dvare.test.dataobjects.ListTestModel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ValuesTest {

    private static final Logger logger = LoggerFactory.getLogger(ValuesTest.class);

    @Test
    public void valuesComparisonTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Variable1->substring(2,2)" +
                "->toInteger()->values()->hasItem(29)", contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = new ArrayList<>();


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable1("42964");
        dataSet.add(valuesObject1);

        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable1("42456");
        dataSet.add(valuesObject2);


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable1("424596");
        dataSet.add(valuesObject3);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


        expression = factory.getParser().fromString("Variable1->toInteger()->toString()->substring(2,2)->toInteger()->values() = [29,24,24]", contexts);

        rule = new RuleBinding(expression);

        result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


    }

    @Test
    public void valuesMappingAndNotEmptyTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("" +
                "Variable1->toInteger()" +
                "->values(let item:IntegerType ->toString()->substring(5,1) = '3' " +
                "or let item:IntegerType ->toString()->substring(5,1) = '4')" +
                "->notEmpty()", contexts);

        Expression expression1 = factory.getParser().fromString(expression.toString(), contexts);
        assertNotNull(expression1);
        logger.info(expression1.toString());
        assertEquals(expression.toString(), expression1.toString());


        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = new ArrayList<>();


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable1("42964");
        dataSet.add(valuesObject1);

        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable1("42453");
        dataSet.add(valuesObject2);


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


    }

    @Test
    public void conditionalValuesTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Variable1->toInteger()" +
                "->values(let item:IntegerType ->toString()->substring(5,1) in ['3','4','9'], " +
                "let item:IntegerType ->toString()->substring(4,1)->toInteger() != 5 )" +
                "->size() = 2", contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = new ArrayList<>();


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable1("42964");
        dataSet.add(valuesObject1);

        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable1("42453");
        dataSet.add(valuesObject2);


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable1("42459");
        dataSet.add(valuesObject3);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);

    }

    @Test
    public void functionParamValuesTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration("org.dvare.util");


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString(" fun ( addFunction , self.Variable2," +
                "self.Variable1-> substring(2,2) ->toInteger()) -> values()->notEmpty()", contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = new ArrayList<>();


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable1("42964");
        valuesObject1.setVariable2(10);
        dataSet.add(valuesObject1);

        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable1("42453");
        valuesObject2.setVariable2(15);
        dataSet.add(valuesObject2);


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable1("42459");
        valuesObject3.setVariable2(20);
        dataSet.add(valuesObject3);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);


        assertTrue(result);

    }

    @Test
    public void arrayValuesTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        RuleBinding rule = new RuleBinding(factory.getParser().fromString("[2 ,6 , 8] -> values() -> notEmpty() ",
                new ContextsBinding()));

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, new InstancesBinding());
        assertTrue(result);

    }


}
