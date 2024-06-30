package org.dvare.test.list;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.parser.ExpressionParser;
import org.dvare.test.dataobjects.ListTestModel;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistinctOperationTest {

    private void testDistinctOperation(List<String> input, List<String> expected) throws ExpressionParseException, InterpretException {
        var factory = new RuleConfiguration();

        var rule = "Variable1->distinct()";

        var typeBinding = ExpressionParser.translate(ListTestModel.class);
        var contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);


        var expression = factory.getParser().fromString(rule, contexts);

        var ruleBinding = new RuleBinding(expression);

        var dataSet = new ArrayList<ListTestModel>();

        for (var i : input) {
            ListTestModel v = new ListTestModel();
            v.setVariable1(i);
            dataSet.add(v);
        }

        var instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        var evaluator = factory.getEvaluator();
        @SuppressWarnings("unchecked") var result = (List<String>) evaluator.evaluate(ruleBinding, instancesBinding);

        assertEquals(expected.size(), result.size());

        for (var i = 0; i < expected.size(); i++) {
            var r = result.get(i);
            var e = expected.get(i);
            assertEquals(e, r);
        }
    }

    @Test
    public void testDistinctOperation() throws ExpressionParseException, InterpretException {
        var params = List.of(
                new AbstractMap.SimpleImmutableEntry<>(
                        List.of("1", "1", "1", "1", "1"),
                        List.of("1")
                ),
                new AbstractMap.SimpleImmutableEntry<>(
                        List.of("1", "2", "3", "4", "5"),
                        List.of("1", "2", "3", "4", "5")
                ),
                new AbstractMap.SimpleImmutableEntry<>(
                        List.of("1", "1", "2", "2", "2", "3", "4", "4", "5"),
                        List.of("1", "2", "3", "4", "5")
                )
        );

        for (var p : params) {
            testDistinctOperation(p.getKey(), p.getValue());
        }
    }
}
