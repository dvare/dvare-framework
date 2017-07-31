/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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


package org.dvare.test.relational;

import junit.framework.TestCase;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.parser.ExpressionParser;
import org.dvare.test.dataobjects.ArithmeticOperation;
import org.dvare.test.dataobjects.EqualOperation;
import org.dvare.test.dataobjects.Function;
import org.dvare.test.dataobjects.NotEqualOperation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EqualOperationTest extends TestCase {

    public void testApp() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable1 = Variable1" +
                " And Variable1 = 'A' And Variable2 = 2" +
                " And Variable3 = 3.2" +
                " And Variable4 = false" +
                " And Variable5 = date ( 12-05-2016 , dd-MM-yyyy )" +
                " And Variable6 = dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )" +
                " And Variable7 = {A1.*}";


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


    public void testApp_1() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable1 != 'A'" +
                " And Variable2 != 2" +
                " And Variable3 != 3.2" +
                " And Variable4 != false" +
                " And Variable5 <> date( 12-05-2016 , dd-MM-yyyy )" +
                " And Variable6 <> dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )" +
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
        boolean result = (Boolean) evaluator.evaluate(rule, notEqualOperation);
        assertTrue(result);
    }

    public void testApp1() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration();


        Expression expression = configuration.getParser().fromString("Variable3='15'", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(15);
        function.setVariable3("15");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }


    public void testApp2() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration();


        Expression expression = configuration.getParser().fromString("Variable3->toString() = Variable1->toString()", Function.class);
        RuleBinding rule = new RuleBinding(expression);

        Function function = new Function();
        function.setVariable1(15);
        function.setVariable3("15");

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, function);
        assertTrue(result);
    }


    public void testApp3() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "self.Variable1 = self.Variable10.Variable1->toString()";


        TypeBinding typeBinding = ExpressionParser.translate(EqualOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        EqualOperation equalOperation = new EqualOperation();
        equalOperation.setVariable1("'9'");

        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable1(9);
        equalOperation.setVariable10(arithmeticOperation);


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, equalOperation);
        assertTrue(result);
    }


    public void testApp4() throws ExpressionParseException, InterpretException {

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


    public void testApp5() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration();

        Expression expression = configuration.getParser().fromString("" +
                "[2.1,2.4] = [2.1,2.4] and [2,4] = [2,4] and ['2','4'] = ['2','4'] and " +
                "[2.1,2.3] != [2.1,2.4] and [2,4] != [2,3] and ['2','4'] != ['2','3']", new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, new InstancesBinding());
        assertTrue(result);
    }


}
