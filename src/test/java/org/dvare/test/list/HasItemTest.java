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

import static org.junit.Assert.assertTrue;

public class HasItemTest {
    @Test
    public void dataSetHasItemTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        String exp = "Variable1->hasItem('42456')";

        Expression expression = factory.getParser().fromString(exp, contexts);

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
    }

    @Test
    public void dataSetArithmeticHasItemTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        String exp = "Variable2->hasItem(42000+964)";

        Expression expression = factory.getParser().fromString(exp, contexts);

        RuleBinding rule = new RuleBinding(expression);


        List<ListTestModel> dataSet = new ArrayList<>();


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable2(42964);
        dataSet.add(valuesObject1);

        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable2(42456);
        dataSet.add(valuesObject2);


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable2(424596);
        dataSet.add(valuesObject3);

        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);
    }

    @Test
    public void dataSetPredefineOperationHasItemTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        String exp = "Variable1->hasItem(let tmp.item:StringType ->Contains('96'))";

        Expression expression = factory.getParser().fromString(exp, contexts);

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
    }
}
