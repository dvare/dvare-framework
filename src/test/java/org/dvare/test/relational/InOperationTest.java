package org.dvare.test.relational;


import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.InOperation;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertTrue;

public class InOperationTest {
    @Test
    public void allVariableInArrayTest() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable1 in ['A','B']" +
                " And Variable2 in [2,3]" +
                " And Variable3 in [3.1,3.2]" +
                " And Variable4 in [true,false]" +
                " /* And Variable5 in [12-05-2016,13-05-2016]" +
                " And Variable6 in [12-05-2016-15:30:00,13-05-2016-15:30:00] */" +
                " And Variable7 in [{B1.*},{A1.*}]";

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

    @Test
    public void allVariableNotInArrayTest() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable1 notIn ['B','C']" +
                " And Variable2 notIn [3,4]" +
                " And Variable3 notIn [3.1,3.5]" +
                " And Variable4 notIn [true,true]" +
                " And Variable5 notIn [16-05-2016,14-05-2016]" +
                " And Variable6 notIn [16-05-2016-15:30:00,14-05-2016-15:30:00]" +
                " And Variable7 notIn [{B1.*},{C1.*}]";

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


    @Test
    public void variableInVariableValueArrayTest() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable2 in [2,Variable2 + 2 ,Variable1->toInteger(),Variable2 + 3 ]";

        Expression expression = factory.getParser().fromString(exp, InOperation.class);
        RuleBinding rule = new RuleBinding(expression);

        InOperation inOperation = new InOperation();
        inOperation.setVariable1("3");
        inOperation.setVariable2(2);
        /**/
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, inOperation);
        assertTrue(result);
    }

    @Test
    public void VariableInArrayTest() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable1 in [ null, '4']";

        Expression expression = factory.getParser().fromString(exp, InOperation.class);
        RuleBinding rule = new RuleBinding(expression);

        InOperation inOperation = new InOperation();
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, inOperation);
        assertTrue(result);
    }

    @Test
    public void VariableInNullArrayTest() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        String exp = "Variable2 in [ null, 4]";

        Expression expression = factory.getParser().fromString(exp, InOperation.class);
        RuleBinding rule = new RuleBinding(expression);

        InOperation inOperation = new InOperation();
        inOperation.setVariable2(4);
        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, inOperation);
        assertTrue(result);
    }

    @Test
    public void arrayInNotInTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration configuration = new RuleConfiguration();

        Expression expression = configuration.getParser().fromString("" +
                "[2.1,2.4] in [2.1,2.4,2.9] and [2,4] in [2,3,4] and ['2','4'] in ['2','4'] and " +
                "[2.1,2.3] notIn [2.1,2.2,2.5] and [2,4] notIn [2,3,7] and ['4'] notIn ['2','3']", new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);

        RuleEvaluator evaluator = configuration.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, new InstancesBinding());
        assertTrue(result);
    }

}
