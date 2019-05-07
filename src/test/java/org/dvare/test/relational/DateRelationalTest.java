/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2019 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.test.relational;

import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.AllVariable;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class DateRelationalTest {

    @Test
    public void testApp() throws ExpressionParseException, InterpretException, ParseException {

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
    public void testApp2() throws ExpressionParseException, InterpretException, ParseException {

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
    public void testApp3() throws ExpressionParseException, InterpretException, ParseException {

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
    public void testApp4() throws ExpressionParseException, InterpretException, ParseException {

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
    public void testApp5() throws ExpressionParseException, InterpretException, ParseException {

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
    public void testApp6() throws ExpressionParseException, InterpretException, ParseException {

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
    public void testApp7() throws ExpressionParseException, InterpretException, ParseException {

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
