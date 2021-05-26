package org.dvare.test.relational;

import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.AllVariable;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateRelationalTest {

    @Test
    public void dateAndTimeEqualsTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable5  = date ( 12-05-2016 , dd-MM-yyyy ) " +
                "and Variable6  = date ( 12-05-2016 , dd-MM-yyyy ) " +
                "and Variable7 = dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss ) ";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", AllVariable.class);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        AllVariable allVariable = new AllVariable();

        allVariable.setVariable5(Date.from(LocalDate.of(2016, 5, 12)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allVariable.setVariable6(LocalDate.of(2016, 5, 12));
        allVariable.setVariable7(LocalDateTime.of(2016, 5, 12, 15, 30, 0));


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, allVariable);
        assertTrue(result);
    }


    @Test
    public void dateAndTimeNotEqualsTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable5  != date ( 12-05-2016 , dd-MM-yyyy ) " +
                "and Variable6 != date ( 12-05-2016 , dd-MM-yyyy ) " +
                "and Variable7 != dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss ) ";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", AllVariable.class);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        AllVariable allVariable = new AllVariable();
        allVariable.setVariable5(Date.from(LocalDate.of(2017, 5, 12)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allVariable.setVariable6(LocalDate.of(2017, 5, 12));
        allVariable.setVariable7(LocalDateTime.of(2017, 5, 12, 15, 30, 0));


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, allVariable);
        assertTrue(result);
    }

    @Test
    public void dateAndTimeLessGreaterTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "(Variable5 > date ( 12-05-2016 , dd-MM-yyyy ) " +
                "or Variable5 >= date ( 12-05-2016 , dd-MM-yyyy )) " +
                "and (Variable6 > date ( 12-05-2016 , dd-MM-yyyy ) " +
                "or Variable6 >= date ( 12-05-2016 , dd-MM-yyyy )) " +
                "and (Variable7 > dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss ) " +
                "or Variable7 >= dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )) ";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", AllVariable.class);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        AllVariable allVariable = new AllVariable();

        allVariable.setVariable5(Date.from(LocalDate.of(2017, 5, 12)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allVariable.setVariable6(LocalDate.of(2017, 5, 12));
        allVariable.setVariable7(LocalDateTime.of(2017, 5, 12, 15, 30, 0));


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, allVariable);
        assertTrue(result);
    }


    @Test
    public void dateAndTimeLessEqualGreaterEqualTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "(Variable5 < date ( 12-05-2016 , dd-MM-yyyy ) " +
                "or Variable5 <= date ( 12-05-2016 , dd-MM-yyyy )) " +
                "and (Variable6 < date ( 12-05-2016 , dd-MM-yyyy ) " +
                "or Variable6 <= date ( 12-05-2016 , dd-MM-yyyy ))  " +
                "and (Variable7 < dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )" +
                "or Variable7 <= dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )) ";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", AllVariable.class);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        AllVariable allVariable = new AllVariable();

        allVariable.setVariable5(Date.from(LocalDate.of(2015, 5, 12)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allVariable.setVariable6(LocalDate.of(2015, 5, 12));
        allVariable.setVariable7(LocalDateTime.of(2015, 5, 12, 15, 30, 0));


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, allVariable);
        assertTrue(result);
    }

    @Test
    public void dateAndTimeBetweenTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable5 between [date ( 12-05-2015 , dd-MM-yyyy ),  date ( 12-05-2017 , dd-MM-yyyy )]" +
                "and Variable6 between [date ( 12-05-2015 , dd-MM-yyyy ),  date ( 12-05-2017 , dd-MM-yyyy )] " +
                "and Variable7 between [ dateTime ( 12-05-2015-15:30:00 , dd-MM-yyyy-HH:mm:ss ), " +
                "dateTime ( 12-05-2017-15:30:00 , dd-MM-yyyy-HH:mm:ss )] ";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", AllVariable.class);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        AllVariable allVariable = new AllVariable();

        allVariable.setVariable5(Date.from(LocalDate.of(2016, 5, 12)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allVariable.setVariable6(LocalDate.of(2016, 5, 12));
        allVariable.setVariable7(LocalDateTime.of(2016, 5, 12, 15, 30, 0));


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, allVariable);
        assertTrue(result);
    }

    @Test
    public void dateAndTimeInTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable5 in [date ( 12-05-2016 , dd-MM-yyyy ),  date ( 12-05-2017 , dd-MM-yyyy )] " +
                "and Variable6 in [date ( 12-05-2016 , dd-MM-yyyy ),  date ( 12-05-2017 , dd-MM-yyyy )] " +
                "and Variable7 in [ dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss ), " +
                "dateTime ( 12-05-2017-15:30:00 , dd-MM-yyyy-HH:mm:ss )] ";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", AllVariable.class);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        AllVariable allVariable = new AllVariable();

        allVariable.setVariable5(Date.from(LocalDate.of(2017, 5, 12)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allVariable.setVariable6(LocalDate.of(2017, 5, 12));
        allVariable.setVariable7(LocalDateTime.of(2017, 5, 12, 15, 30, 0));


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, allVariable);
        assertTrue(result);
    }

    @Test
    public void dateAndTimeNotInTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "Variable5 notIn [date ( 12-05-2016 , dd-MM-yyyy ),  date ( 12-05-2017 , dd-MM-yyyy )] " +
                "and Variable6 notIn [date ( 12-05-2016 , dd-MM-yyyy ),  date ( 12-05-2017 , dd-MM-yyyy )] " +
                "and Variable7 notIn [ dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss ), " +
                "dateTime ( 12-05-2017-15:30:00 , dd-MM-yyyy-HH:mm:ss )] ";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", AllVariable.class);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);

        AllVariable allVariable = new AllVariable();
        allVariable.setVariable5(Date.from(LocalDate.of(2015, 5, 12)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allVariable.setVariable6(LocalDate.of(2015, 5, 12));
        allVariable.setVariable7(LocalDateTime.of(2015, 5, 12, 15, 30, 0));


        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, allVariable);
        assertTrue(result);
    }
}
