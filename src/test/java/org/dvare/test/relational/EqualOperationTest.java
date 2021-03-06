package org.dvare.test.relational;

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
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EqualOperationTest {

    @Test
    public void equalTest() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable1 = Variable1" +
                " And Variable1 = 'A' And Variable2 = 2" +
                " And Variable3 = 3.2" +
                " And Variable4 = false" +
                " And Variable5 = date ( 12-05-2016 , dd-MM-yyyy )" +
                " And Variable6 = dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )" +
                " And Variable7 = {A1.*}";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", EqualOperation.class);

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
    public void notEqualTest() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable1 != 'A'" +
                " And Variable2 != 2" +
                " And Variable3 != 3.2" +
                " And Variable4 != false" +
                " And Variable5 <> date( 12-05-2016 , dd-MM-yyyy )" +
                " And Variable6 <> dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )" +
                " And Variable7 != {A1.*}";

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

    @Test
    public void equalTestForStringLiteral() throws ExpressionParseException, InterpretException {

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

    @Test
    public void equalTestForToString() throws ExpressionParseException, InterpretException {

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

    @Test
    public void nustedVariableEqualTest() throws ExpressionParseException, InterpretException {

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


    @Test
    public void arrayEqualNotEqualTest() throws ExpressionParseException, InterpretException {

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
