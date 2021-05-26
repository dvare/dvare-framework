package org.dvare.test.list;


import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListLiteralOperationTest {


    @Test
    public void variableLiteralArrayIsNotEmptyTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> types = new HashMap<>();
        types.put("variable", "IntegerType");

        Expression expression = factory.getParser().fromString("[5,variable,15] -> notEmpty() and [5,variable,15] = [5,10,15]", types);

        Map<String, Object> values = new HashMap<>();
        values.put("variable", 10);

        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(values));

        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(expression), instancesBinding);


        assertTrue(result);
    }

    @Test
    public void variableLiteralArrayIsEmptyTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> types = new HashMap<>();
        types.put("variable", "IntegerType");

        Expression expression = factory.getParser().fromString("[5,variable,15] -> isEmpty()", types);

        Map<String, Object> values = new HashMap<>();
        values.put("variable", 10);

        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(values));

        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(expression), instancesBinding);


        assertFalse(result);
    }


    @Test
    public void variableLiteralArraySizeTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        Map<String, String> types = new HashMap<>();
        types.put("variable", "IntegerListType");

        Expression expression = factory.getParser().fromString("[5,variable,20] -> size() = 4", types);

        Map<String, Object> values = new HashMap<>();
        values.put("variable", new Integer[]{10, 15});

        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", new DataRow(values));

        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(expression), instancesBinding);


        assertTrue(result);
    }


}
