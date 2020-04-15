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

import static org.junit.Assert.assertFalse;

public class FilterTest {

    @Test
    public void dataSetStringToIntegerAndFilterOperation() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        final String exp = "Variable1->substring(2,2)->toInteger()" +
                "->filter(let item:StringType != '29')->hasItem(29)";

        TypeBinding typeBinding = ExpressionParser.translate(ListTestModel.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


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
        assertFalse(result);

    }
}
