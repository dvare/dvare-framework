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
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionParser;
import org.dvare.rules.test.validation.dataobjects.ArithmeticOperation;
import org.dvare.rules.test.validation.dataobjects.EqualOperation;
import org.dvare.rules.test.validation.dataobjects.Function;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EqualOperationTest extends TestCase {
    @Test
    public void testApp() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable1 = Variable1" +
                " And Variable1 = 'A' And Variable2 = 2" +
                " And Variable3 = 3.2" +
                " And Variable4 = false" +
                " And Variable5 = date ( 12-05-2016 , dd-MM-yyyy )" +
                " And Variable6 = dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )" +
                " And Variable7 = R'A1.*'";


        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        EqualOperation equalOperation = new EqualOperation();
        equalOperation.setVariable1("'A'");
        equalOperation.setVariable2(2);
        equalOperation.setVariable3(3.2f);
        equalOperation.setVariable4(false);
        equalOperation.setVariable5(dateFormat.parse("12-05-2016"));
        equalOperation.setVariable6(dateTimeFormat.parse("12-05-2016-15:30:00"));
        equalOperation.setVariable7("A1B2");


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, equalOperation);
        assertTrue(result);
    }


    @Test
    public void testApp01() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.rules.util"});


        Expression expression = configuration.getParser().fromString("Variable3='15'", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(15);
        function.setVariable3("15");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }

    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration(new String[]{"org.dvare.rules.util"});


        Expression expression = configuration.getParser().fromString("Variable3->toString() = Variable1->toString()", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(15);
        function.setVariable3("15");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }

    @Test
    public void testApp12() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "self.Variable1 = self.Variable8.Variable1->toString()";


        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        EqualOperation equalOperation = new EqualOperation();
        equalOperation.setVariable1("'9'");

        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable1(9);
        equalOperation.setVariable8(arithmeticOperation);


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, equalOperation);
        assertTrue(result);
    }


}
