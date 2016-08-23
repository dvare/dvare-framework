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


package com.dvare.rules.test.validation;


import com.dvare.binding.rule.RuleBinding;
import com.dvare.config.RuleConfiguration;
import com.dvare.evaluator.RuleEvaluator;
import com.dvare.exceptions.interpreter.InterpretException;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;
import com.dvare.rules.test.validation.dataobjects.NotEqualOperation;
import junit.framework.TestCase;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NotEqualOperationTest extends TestCase {
    @Test
    public void testApp() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable1 != 'A'" +
                " And Variable2 != 2" +
                " And Variable3 != 3.2" +
                " And Variable4 != false" +
                " And Variable5 <> toDate ( 12-05-2016 , dd-MM-yyyy )" +
                " And Variable6 <> toDate ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )" +
                " And Variable7 != R'A1.*'";

        Expression expression = factory.getParser().fromString(exp, NotEqualOperation.class);
        RuleBinding rule = new RuleBinding(expression);

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        NotEqualOperation notEqualOperation = new NotEqualOperation();
        notEqualOperation.setVariable1("'B'");
        notEqualOperation.setVariable2(3);
        notEqualOperation.setVariable3(3.1f);
        notEqualOperation.setVariable4(true);
        notEqualOperation.setVariable5(dateFormat.parse("13-05-2016"));
        notEqualOperation.setVariable6(dateTimeFormat.parse("13-05-2016-15:30:00"));
        notEqualOperation.setVariable7("A2B2");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = evaluator.evaluate(rule, notEqualOperation);
        assertTrue(result);
    }

}
