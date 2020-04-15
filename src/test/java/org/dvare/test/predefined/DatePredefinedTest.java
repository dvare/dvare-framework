package org.dvare.test.predefined;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.ArithmeticOperation;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DatePredefinedTest {


    @Test
    public void toDateTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "date ( 12-05-2016 , dd-MM-yyyy )->toString() ->toDate() = date ( 12-05-2016 , dd-MM-yyyy )";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(expression), new InstancesBinding());
        Assert.assertTrue(result);


    }

    @Test
    public void toDateTestWhenValueIsNull() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "null ->toDate() = null";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(expression), new InstancesBinding());
        Assert.assertTrue(result);


    }

    @Test
    public void dateTotoDateTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "date ( 12-05-2016 , dd-MM-yyyy )->toDate()";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        LocalDate localDate = (LocalDate) factory.getEvaluator().evaluate(new RuleBinding(expression), new InstancesBinding());
        Assert.assertEquals(localDate, LocalDate.of(2016, 5, 12));


    }

    @Test
    public void dateTimeToDateTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss ) ->toDate()";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        LocalDate localDate = (LocalDate) factory.getEvaluator().evaluate(new RuleBinding(expression), new InstancesBinding());
        Assert.assertEquals(localDate, LocalDate.of(2016, 5, 12));

    }

    @Test
    public void dateToAddYearsAndGetYearsTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "date ( 12-05-2016 , dd-MM-yyyy )->addYears(1) -> getYears() = 2017";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        boolean result = (Boolean) factory.getEvaluator().evaluate(new RuleBinding(expression), new InstancesBinding());
        Assert.assertTrue(result);

    }


    @Test
    public void dateTimeToAddYearsAndGetYearsTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss ) ->addYears(1) -> getYears()";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        int year = (Integer) factory.getEvaluator().evaluate(new RuleBinding(expression), new InstancesBinding());
        Assert.assertEquals(year, 2017);

    }


    @Test
    public void dateToAddYearsAndGetYearsTestFromVariableValue() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "Variable11 -> addYears(1) -> getYears()";
        Expression expression = factory.getParser().fromString(exp, ArithmeticOperation.class);
        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        arithmeticOperation.setVariable11(calendar.getTime());

        int result = (Integer) factory.getEvaluator().evaluate(new RuleBinding(expression), arithmeticOperation);
        Assert.assertEquals(result, 2017);

    }

    @Test
    public void dateToAddYearsAddMonthsAndAddDaysTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "date ( 12-05-2016 , dd-MM-yyyy )->addYears(1)->addMonths(2)->addDays(3)";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        LocalDate result = (LocalDate) evaluator.evaluate(rule, new InstancesBinding());
        Assert.assertEquals(result, LocalDate.of(2017, 7, 15));
    }

    @Test
    public void dateTimeToAddYearsAddMonthsAndAddDaysTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )->addYears(1)->addMonths(2)->addDays(3)";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        LocalDateTime result = (LocalDateTime) evaluator.evaluate(rule, new InstancesBinding());
        Assert.assertEquals(result, LocalDateTime.of(2017, 7, 15, 15, 30, 0));
    }


    @Test
    public void dateTimeToAddYearsAddMonthsAndAddDaysTestFromVariableValue() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "Variable11 ->addYears(1)->addMonths(2)->addDays(3)";
        Expression expression = factory.getParser().fromString(exp, ArithmeticOperation.class);
        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DATE, 12);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        arithmeticOperation.setVariable11(calendar.getTime());

        Date date = (Date) factory.getEvaluator().evaluate(new RuleBinding(expression), arithmeticOperation);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2017);
        calendar2.set(Calendar.MONTH, 7);
        calendar2.set(Calendar.DATE, 15);
        calendar2.set(Calendar.HOUR, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        Assert.assertEquals(date, calendar2.getTime());

    }

    @Test
    public void dateToSetYearsSetMonthsSetAddDaysTestFromVariableValue() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "date ( 12-05-2016 , dd-MM-yyyy )->setYear(2016)->setMonth(6)->setDay(15)";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        LocalDate result = (LocalDate) evaluator.evaluate(rule, new InstancesBinding());
        Assert.assertEquals(result, LocalDate.of(2016, 6, 15));
    }

    @Test
    public void dateTimeToSetYearsSetMonthsSetAddDaysTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )->setYear(2016)->setMonth(6)->setDay(15)";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        LocalDateTime result = (LocalDateTime) evaluator.evaluate(rule, new InstancesBinding());
        Assert.assertEquals(result, LocalDateTime.of(2016, 6, 15, 15, 30, 0));
    }


    @Test
    public void dateToSetYearsSetMonthsSetAddDaysTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "Variable11 ->setYear(2016)->setMonth(6)->setDay(15)";
        Expression expression = factory.getParser().fromString(exp, ArithmeticOperation.class);
        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DATE, 12);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        arithmeticOperation.setVariable11(calendar.getTime());

        Date date = (Date) factory.getEvaluator().evaluate(new RuleBinding(expression), arithmeticOperation);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2016);
        calendar2.set(Calendar.MONTH, 6);
        calendar2.set(Calendar.DATE, 15);
        calendar2.set(Calendar.HOUR, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        Assert.assertEquals(date, calendar2.getTime());

    }

    @Test
    public void dateToSubYearsSubMonthsAndSubDaysTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "date ( 12-05-2016 , dd-MM-yyyy )->subYears(1)->subMonths(1)->subDays(10)";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        LocalDate result = (LocalDate) evaluator.evaluate(rule, new InstancesBinding());
        Assert.assertEquals(result, LocalDate.of(2015, 4, 2));
    }

    @Test
    public void dateTimeToSubYearsSubMonthsAndSubDaysTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "dateTime ( 12-05-2016-15:30:00 , dd-MM-yyyy-HH:mm:ss )->subYears(1)->subMonths(1)->subDays(10)";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        LocalDateTime result = (LocalDateTime) evaluator.evaluate(rule, new InstancesBinding());
        Assert.assertEquals(result, LocalDateTime.of(2015, 4, 2, 15, 30, 0));
    }


    @Test
    public void dateToSubYearsSubMonthsAndSubDaysTestFromVariableValue() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "Variable11 ->subYears(1)->subMonths(1)->subDays(10)";
        Expression expression = factory.getParser().fromString(exp, ArithmeticOperation.class);
        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DATE, 12);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        arithmeticOperation.setVariable11(calendar.getTime());

        Date date = (Date) factory.getEvaluator().evaluate(new RuleBinding(expression), arithmeticOperation);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2015);
        calendar2.set(Calendar.MONTH, 4);
        calendar2.set(Calendar.DATE, 2);
        calendar2.set(Calendar.HOUR, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        Assert.assertEquals(date, calendar2.getTime());

    }
}
