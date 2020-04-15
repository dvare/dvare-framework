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

public class PairListTest {

    @Test
    public void pairTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString("Pair (Variable1,Variable2)", contexts);

        assertNotNull(expression);


        assertEquals(expression.toString().trim(), "Pair(self.Variable1, self.Variable2)");

    }

    @Test
    public void toPairOperationTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString("toPair(Variable1,Variable2)", contexts);

        assertNotNull(expression);


    }

    @Test
    public void toPairToKeyTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString("toPair(Variable1,Variable2) -> toKey() = '42964'", contexts);

        assertNotNull(expression);


        ListTestModel dataRow = new ListTestModel();
        dataRow.setVariable1("42964");
        dataRow.setVariable2(1);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataRow);


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        assertTrue(result);

        expression = factory.getParser().fromString("toPair(Variable1,Variable2) -> toValue() = 1", contexts);
        assertNotNull(expression);

        evaluator = factory.getEvaluator();
        result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        assertTrue(result);
    }

    @Test(expected = ExpressionParseException.class)
    public void variableToPairTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);
        factory.getParser().fromString("Pair (Variable1)", contexts);


    }

    @Test
    public void pairListToKeysTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Pair (Variable2,Variable1) -> Keys() ->notEmpty()", contexts);

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


        expression = factory.getParser().fromString("Pair (Variable2,Variable1) -> Keys() ->notEmpty() and Pair (Variable2,Variable1) -> Keys() = [1,3,2]", contexts);

        rule = new RuleBinding(expression);

        result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


    }

    @Test
    public void pairListToValuesTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Pair (Variable2,Variable1) -> values() ->notEmpty()", contexts);

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

        // [{1,42964},{3,42456},{2,42459}]
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


        expression = factory.getParser().fromString("Pair (Variable2,Variable1) -> values() ->notEmpty() and Pair (Variable2,Variable1) -> values() = ['42964','42456','42459']", contexts);

        rule = new RuleBinding(expression);

        result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


        expression = factory.getParser().fromString("Pair (Variable2,Variable1->substring(1,4)) -> values() = ['4296','4245','4245']", contexts);

        rule = new RuleBinding(expression);

        result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);

    }

    @Test
    public void pairListWithPredefineFuntionTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Pair (Variable2,Variable1->substring(1,4)) -> values() = ['4296','4245','4245']", contexts);

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

        // [{1,42964},{3,42456},{2,42459}]
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);


    }

    @Test
    public void pairValuesLastTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Pair (Variable2,Variable1) -> values() ->last() = '42459'", contexts);

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
    public void pairListSortByKeysAndCompareByLastKeyTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Pair (Variable2,Variable1) ->sort() -> keys() ->last() = 3", contexts);

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
    public void pairListSortByKeysAndCompareByLastValueTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        Expression expression = factory.getParser().fromString("Pair (Variable2,Variable1) ->sort() -> values() ->last() = '42456'", contexts);

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
    public void pairLestFilterTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        Expression expression = factory.getParser().fromString("" +
                "Pair (Variable2,Variable1) ->filter(let temp:PairType ->getKey() ->toInteger() != 3) " +
                "->sort() -> values() ->last() = '42459'", contexts);

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
    public void pairListFilterAndVariableAssignmentTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        Expression expression = factory.getParser().fromString("" +
                "def tmp.pairList:PairListType := Pair (Variable2,Variable1) ->filter(let tmp:PairType ->getKey() ->toInteger() != 3) " +
                "tmp.pairList ->values() ->notEmpty() and tmp.pairList ->values() = ['42964','42459']", contexts);

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
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        assertTrue(result);
    }
}
