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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TripleListTest {

    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString("Triple (Variable1,Variable2,Variable3)", contexts);

        assertNotNull(expression);


        assertEquals(expression.toString().trim(), "Triple(self.Variable1, self.Variable2, self.Variable3)");

    }

    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString("toTriple(Variable1,Variable2,Variable3)", contexts);

        assertNotNull(expression);


    }

    @Test
    public void testApp2P1() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString("toTriple(Variable1,Variable2,Variable3) -> toLeft() = '42964'", contexts);

        assertNotNull(expression);


        ListTestModel dataRow = new ListTestModel();
        dataRow.setVariable1("42964");
        dataRow.setVariable2(1);
        dataRow.setVariable3(1.4f);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataRow);


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        assertTrue(result);

        expression = factory.getParser().fromString("toTriple(Variable1,Variable2,Variable3) -> toMiddle() = 1", contexts);
        assertNotNull(expression);

        evaluator = factory.getEvaluator();
        result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        assertTrue(result);

        expression = factory.getParser().fromString("toTriple(Variable1,Variable2,Variable3) -> toRight() = 1.4", contexts);
        assertNotNull(expression);

        evaluator = factory.getEvaluator();
        result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        assertTrue(result);
    }

    @Test(expected = ExpressionParseException.class)
    public void testApp3() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);
        factory.getParser().fromString("Triple (Variable1)", contexts);


    }

    @Test
    public void testApp4() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Triple (Variable2,Variable1,Variable3) -> Keys() ->notEmpty()", contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = getListTestModels();

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


        expression = factory.getParser().fromString("Triple (Variable2,Variable1,Variable3) -> Keys() ->notEmpty() and Triple (Variable2,Variable1,Variable3) -> lefts() = [1,3,2]", contexts);

        rule = new RuleBinding(expression);

        result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


    }

    @Test
    public void testApp5() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Triple (Variable2,Variable1,Variable3) -> values() ->notEmpty()", contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = getListTestModels();

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        // [{1,42964},{3,42456},{2,42459}]
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


        expression = factory.getParser().fromString("Triple (Variable2,Variable1,Variable3) -> middles() ->notEmpty() and Triple (Variable2,Variable1,Variable3) -> middles() = ['42964','42456','42459']", contexts);

        rule = new RuleBinding(expression);

        result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


        expression = factory.getParser().fromString("Triple (Variable2,Variable3,Variable1->substring(1,4)) -> values() = ['4296','4245','4245']", contexts);

        rule = new RuleBinding(expression);

        result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);

    }

    @Test
    public void testApp6() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Triple (Variable2,Variable3,Variable1->substring(1,4)) -> values() = ['4296','4245','4245']", contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = getListTestModels();

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        // [{1,42964},{3,42456},{2,42459}]
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


    }

    @Test
    public void testApp7() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Triple (Variable2,Variable3,Variable1) -> values() ->last() = '42459'", contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = getListTestModels();
        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);

    }

    @Test
    public void testApp8() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Triple (Variable2,Variable1,Variable3) ->sort() -> keys() ->last() = 3", contexts);

        RuleBinding rule = new RuleBinding(expression);

        List<ListTestModel> dataSet = getListTestModels();

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);

    }

    @Test
    public void testApp9() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Triple (Variable2,Variable3,Variable1) ->sort() -> values() ->last() = '42456'", contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = new ArrayList<>();


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable1("42964");
        valuesObject1.setVariable2(1);
        dataSet.add(valuesObject1);

        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable1("42456");
        valuesObject2.setVariable2(3);
        dataSet.add(valuesObject2);


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable1("42459");
        valuesObject3.setVariable2(2);
        dataSet.add(valuesObject3);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);

    }

    @Test
    public void testApp10() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        Expression expression = factory.getParser().fromString("" +
                "Triple (Variable2,Variable3, Variable1) ->filter(let tmp.tripleVariable:TripleType ->getKey() ->toInteger() != 3) " +
                "->sort() -> values() ->last() = '42459'", contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = getListTestModels();

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);

    }


    @Test
    public void testApp11() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        Expression expression = factory.getParser().fromString("" +
                "def tmp.tripleList:TripleListType := tripleList (Variable2,Variable3,Variable1) ->filter(let tmp:TripleType ->getKey() ->toInteger() != 3) " +
                "tmp.tripleList ->values() ->notEmpty() and tmp.tripleList ->values() = ['42964','42459']", contexts);

        List<ListTestModel> dataSet = getListTestModels();

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        assertTrue(result);
    }


    private List<ListTestModel> getListTestModels() {
        List<ListTestModel> dataSet = new ArrayList<>();

        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable1("42964");
        valuesObject1.setVariable2(1);
        valuesObject1.setVariable3(1.4f);
        dataSet.add(valuesObject1);

        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable1("42456");
        valuesObject2.setVariable2(3);
        valuesObject2.setVariable3(1.5f);
        dataSet.add(valuesObject2);


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable1("42459");
        valuesObject3.setVariable2(2);
        valuesObject3.setVariable3(1.6f);

        dataSet.add(valuesObject3);
        return dataSet;
    }

}
