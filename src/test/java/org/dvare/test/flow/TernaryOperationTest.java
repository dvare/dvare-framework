package org.dvare.test.flow;

import java.util.HashMap;
import java.util.Map;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionParser;
import org.dvare.util.ValueFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TernaryOperationTest {

    @Test
    public void ternaryTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();
        Expression ruleExpression = factory.getParser().fromString("false ?(4 : 5) = 5", new ContextsBinding());

        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(ruleExpression), new InstancesBinding());
        Assertions.assertTrue(result);
    }

    @Test
    public void ternaryRelationalTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();
        Map<String, String> types = new HashMap<>();
        types.put("variable", "IntegerType");
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(types));
        Expression ruleExpression = factory.getParser().fromString("self.variable = 9 ?(4 : 5) = 4", contexts);

        Map<String, Object> values = new HashMap<>();
        values.put("variable", 9);
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(values));

        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(ruleExpression), instancesBinding);
        Assertions.assertTrue(result);
    }

    @Test
    public void ternaryChainTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();
        Expression ruleExpression = factory.getParser().fromString("false ?(4 : '5'->toInteger()) = 5", new ContextsBinding());
        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(ruleExpression), new InstancesBinding());
        Assertions.assertTrue(result);
    }

    @Test
    public void booleanVariableTernaryTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        Map<String, String> types = new HashMap<>();
        types.put("condition", "BooleanType");
        types.put("variable", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(types));

        Expression ruleExpression = factory.getParser().fromString("self.variable := self.condition ? (4 : 5)", contexts);

        RuleBinding rule = new RuleBinding(ruleExpression);

        Map<String, Object> values = new HashMap<>();
        values.put("condition", true);
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(values));

        RuleEvaluator evaluator = factory.getEvaluator();
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");

        Object result = ValueFinder.findValue("variable", resultModel);

        Assertions.assertEquals(result, 4);
    }


    @Test
    public void booleanVariableAssignTernaryTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> types = new HashMap<>();
        types.put("condition", "IntegerType");
        types.put("variable", "IntegerType");

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(types));

        Expression ruleExpression = factory.getParser().fromString("self.variable := self.condition = 9 ? (4 : 5)", contexts);
        RuleBinding rule = new RuleBinding(ruleExpression);

        Map<String, Object> values = new HashMap<>();
        values.put("condition", 9);
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(values));

        RuleEvaluator evaluator = factory.getEvaluator();
        Object resultModel = evaluator.aggregate(rule, instancesBinding).getInstance("self");

        Object result = ValueFinder.findValue("variable", resultModel);

        Assertions.assertEquals(result, 4);
    }

}
