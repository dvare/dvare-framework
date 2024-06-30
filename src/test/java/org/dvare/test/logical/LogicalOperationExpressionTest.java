package org.dvare.test.logical;

import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.parser.ExpressionParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogicalOperationExpressionTest {

    public final String T = "true";
    public final String F = "false";
    public final String V1 = "V1";
    public final String V2 = "V2";
    public final String AND = "and";
    public final String OR = "or";
    public final String IMPLIES = "implies";
    public final String NOT = "not";

    static class Params {
        public final String LEFT;
        public final String OPERATOR;
        public final String RIGHT;
        public final boolean EXPECTED;

        public final Boolean LEFT_VALUE;
        public final Boolean RIGHT_VALUE;

        public final String CONTEXT = "self";
        public final String BOOLEAN_TYPE = "BooleanType";

        public Params(String left, String operator, String right, boolean expected) {
            this(left, null, operator, right, null, expected);
        }

        public Params(String left, Boolean leftValue, String operator, String right, Boolean rightValue, boolean expected) {
            LEFT = left;
            OPERATOR = operator;
            RIGHT = right;
            EXPECTED = expected;
            LEFT_VALUE = leftValue;
            RIGHT_VALUE = rightValue;
        }
    }

    private void logicalParams(Params params) throws ExpressionParseException, InterpretException {
        var factory = new RuleConfiguration();

        ContextsBinding contexts = new ContextsBinding();
        if (params.LEFT_VALUE != null && params.RIGHT_VALUE != null) {
            contexts.addContext(params.CONTEXT, ExpressionParser.translate(String.format("%s:%s,%s:%s", params.LEFT, params.BOOLEAN_TYPE, params.RIGHT, params.BOOLEAN_TYPE)));
        } else if (params.LEFT_VALUE != null) {
            contexts.addContext(params.CONTEXT, ExpressionParser.translate(String.format("%s:%s", params.LEFT, params.BOOLEAN_TYPE)));
        } else if (params.RIGHT_VALUE != null) {
            contexts.addContext(params.CONTEXT, ExpressionParser.translate(String.format("%s:%s", params.RIGHT, params.BOOLEAN_TYPE)));
        }

        String ruleText;
        if (params.LEFT == null) {
            ruleText = String.format("%s %s", params.OPERATOR, params.RIGHT);
        } else {
            ruleText = String.format("%s %s %s", params.LEFT, params.OPERATOR, params.RIGHT);
        }
        var ruleExp = factory.getParser().fromString(ruleText, contexts);

        var rule = new RuleBinding(ruleExp);

        var evaluator = factory.getEvaluator();
        var instancesBinding = new InstancesBinding(new HashMap<>());

        List<Object> dataSet = new ArrayList<>();
        Map<String, Object> d = new HashMap<>();

        if (params.LEFT_VALUE != null) {
            d.put(params.LEFT, params.LEFT_VALUE);
        }

        if (params.RIGHT_VALUE != null) {
            d.put(params.RIGHT, params.RIGHT_VALUE);
        }

        dataSet.add(new DataRow(d));
        instancesBinding.addInstance(params.CONTEXT, dataSet);

        Object result = evaluator.evaluate(rule, instancesBinding);
        Assertions.assertEquals(Boolean.class, result.getClass());

        var booleanResult = (Boolean) result;
        Assertions.assertEquals(params.EXPECTED, booleanResult, ruleText);
    }

    private void logicalParams(List<Params> params) throws ExpressionParseException, InterpretException {
        for (var param : params) {
            logicalParams(param);
        }
    }

    @Test
    public void literals() throws ExpressionParseException, InterpretException {
        var params = List.of(
                new Params(T, AND, T, true),
                new Params(F, AND, T, false),
                new Params(T, AND, F, false),
                new Params(F, AND, F, false),
                new Params(T, OR, T, true),
                new Params(F, OR, T, true),
                new Params(T, OR, F, true),
                new Params(F, OR, F, false),
                new Params(T, IMPLIES, T, true),
                new Params(F, IMPLIES, T, true),
                new Params(T, IMPLIES, F, false),
                new Params(F, IMPLIES, F, true),
                new Params(null, NOT, T, false),
                new Params(null, NOT, F, true)
        );

        logicalParams(params);
    }

    @Test
    public void variables() throws ExpressionParseException, InterpretException {
        var params = List.of(
                new Params(V1, true, AND, V2, true, true),
                new Params(V1, true, AND, V2, false, false),
                new Params(V1, false, AND, V2, true, false),
                new Params(V1, false, AND, V2, false, false),
                new Params(V1, true, OR, V2, true, true),
                new Params(V1, false, OR, V2, true, true),
                new Params(V1, true, OR, V2, false, true),
                new Params(V1, false, OR, V2, false, false),
                new Params(V1, true, IMPLIES, V2, true, true),
                new Params(V1, false, IMPLIES, V2, true, true),
                new Params(V1, true, IMPLIES, V2, false, false),
                new Params(V1, false, IMPLIES, V2, false, true),
                new Params(null, null, NOT, V1, true, false),
                new Params(null, null, NOT, V2, false, true)
        );

        logicalParams(params);
    }

    @Test
    public void literalsAndVariables() throws ExpressionParseException, InterpretException {
        var params = List.of(
                new Params(V1, true, AND, T, null, true),
                new Params(V1, false, AND, T, null, false),
                new Params(V1, true, AND, F, null, false),
                new Params(V1, false, AND, F, null, false),
                new Params(V1, true, OR, T, null, true),
                new Params(V1, false, OR, T, null, true),
                new Params(V1, true, OR, F, null, true),
                new Params(V1, false, OR, F, null, false),
                new Params(V1, true, IMPLIES, T, null, true),
                new Params(V1, false, IMPLIES, T, null, true),
                new Params(V1, true, IMPLIES, F, null, false),
                new Params(V1, false, IMPLIES, F, null, true),

                new Params(T, null, AND, V2, true, true),
                new Params(F, null, AND, V2, true, false),
                new Params(T, null, AND, V2, false, false),
                new Params(F, null, AND, V2, false, false),
                new Params(T, null, OR, V2, true, true),
                new Params(F, null, OR, V2, true, true),
                new Params(T, null, OR, V2, false, true),
                new Params(F, null, OR, V2, false, false),
                new Params(T, null, IMPLIES, V2, true, true),
                new Params(F, null, IMPLIES, V2, true, true),
                new Params(T, null, IMPLIES, V2, false, false),
                new Params(F, null, IMPLIES, V2, false, true)
        );

        logicalParams(params);
    }
}
