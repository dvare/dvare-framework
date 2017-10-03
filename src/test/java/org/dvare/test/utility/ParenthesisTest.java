/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
    public void testApp3() throws ExpressionParseException, InterpretException {


        String expr = "Variable1 = ('dvare (framework)')";
        String tokens[] = ExpressionTokenizer.toToken(expr);

        assertEquals(tokens.length, 5);
        assertEquals(Arrays.asList(tokens), Arrays.asList("Variable1", "=", "(", "'dvare (framework)'", ")"));

    }

    @Test
    public void testApp2() throws ExpressionParseException, InterpretException {


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
