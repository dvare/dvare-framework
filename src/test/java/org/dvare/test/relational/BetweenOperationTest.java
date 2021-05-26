package org.dvare.test.relational;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.BetweenOperation;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BetweenOperationTest {
    @Test
    public void betweenIntegerFloatDateTest() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable1 between [2,4]" +
                " And Variable2 between [3.1,3.3]" +
                " And Variable3 between [12-05-2016,15-06-2016] ";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", BetweenOperation.class);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        BetweenOperation betweenOperation = new BetweenOperation();
        betweenOperation.setVariable1(2);
        betweenOperation.setVariable2(3.2f);
        betweenOperation.setVariable3(dateFormat.parse("28-05-2016"));


        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", betweenOperation);
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        assertTrue(result);
    }


}
