/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.rules.test.validation;


import junit.framework.TestCase;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.rules.test.validation.dataobjects.InOperation;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InOperationTest extends TestCase {
    @Test
    public void testApp() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();
        String exp = "Variable1 in ['A','B']" +
                " And Variable2 in [2,3]" +
                " And Variable3 in [3.1,3.2]" +
                " And Variable4 in [true,false]" +
                " And Variable5 in [12-05-2016,13-05-2016]" +
                " And Variable6 in [12-05-2016-15:30:00,13-05-2016-15:30:00]" +
                " And Variable7 in [R'B1.*',R'A1.*']";


        Expression expression = factory.getParser().fromString(exp, InOperation.class);
        RuleBinding rule = new RuleBinding(expression);

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        InOperation inOperation = new InOperation();
        inOperation.setVariable1("'A'");
        inOperation.setVariable2(2);
        inOperation.setVariable3(3.2f);
        inOperation.setVariable4(false);
        inOperation.setVariable5(dateFormat.parse("12-05-2016"));
        inOperation.setVariable6(dateTimeFormat.parse("12-05-2016-15:30:00"));
        inOperation.setVariable7("A1B2");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, inOperation);
        assertTrue(result);
    }

}
