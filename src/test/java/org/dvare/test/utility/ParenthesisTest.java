package org.dvare.test.utility;


import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionTokenizer;
import org.dvare.test.dataobjects.Parenthesis;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParenthesisTest {
    @Test
    public void testApp() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 = 'A'" +
                " AND ( ( Variable2 = 'O'" +
                " AND Variable3 = 'R' )" +
                " OR ( Variable4 = 'G'" +
                " AND Variable5 = 'M' ) )";
        Expression expression = factory.getParser().fromString(expr, Parenthesis.class);

        RuleBinding rule = new RuleBinding(expression);


        Parenthesis parenthesis = new Parenthesis();
        parenthesis.setVariable1("A");
        parenthesis.setVariable2("O");
        parenthesis.setVariable3("R");
        parenthesis.setVariable4("K");
        parenthesis.setVariable5("M");


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, parenthesis);
        assertTrue(result);
    }

    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {


        String expr = "Variable1 = ('dvare(framework)')";

        String tokens[] = ExpressionTokenizer.toToken(expr);

        assertEquals(tokens.length, 5);

    }

    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {


        String expr = "Variable1 = ('dvare(framework)' )";

        String tokens[] = ExpressionTokenizer.toToken(expr);

        assertEquals(tokens.length, 5);

    }


    @Test
    public void testApp3() throws ExpressionParseException, InterpretException {


        String expr = "Variable1 = ( 'dvare(framework)')";

        String tokens[] = ExpressionTokenizer.toToken(expr);

        assertEquals(tokens.length, 5);

    }

    @Test
    public void testApp4() throws ExpressionParseException, InterpretException {


        String expr = "Variable1 = ('dvare (framework)')";
        String tokens[] = ExpressionTokenizer.toToken(expr);

        assertEquals(tokens.length, 5);
        assertEquals(Arrays.asList(tokens), Arrays.asList("Variable1", "=", "(", "'dvare (framework)'", ")"));

    }

    @Test
    public void testApp5() throws ExpressionParseException, InterpretException {


        RuleConfiguration factory = new RuleConfiguration();

        String expr = "Variable1 = 'dvare(framework)' and Variable2 = 'dvare framework'";

        Expression expression = factory.getParser().fromString(expr, Parenthesis.class);

        RuleBinding rule = new RuleBinding(expression);


        Parenthesis parenthesis = new Parenthesis();
        parenthesis.setVariable1("dvare(framework)");
        parenthesis.setVariable2("dvare framework");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, parenthesis);
        assertTrue(result);
    }


}
