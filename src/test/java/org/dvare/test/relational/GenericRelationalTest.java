package org.dvare.test.relational;

import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.Function;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenericRelationalTest {


    @Test
    public void functionLessGraterTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration();


        Expression expression = configuration.getParser().fromString("Variable1 < 20 implies Variable2 >= 30 ", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(15);
        function.setVariable2(30);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }
}
