package org.dvare.test.utility;


import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintTest {

    private static final Logger logger = LoggerFactory.getLogger(PrintTest.class);

    @Test
    public void testApp1() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();
        String exp = "def tmp.date:DateType := date ( 12-05-2016 , dd-MM-yyyy ); " +
                "print (tmp.date)";
        Expression expression = factory.getParser().fromString(exp, new ContextsBinding());
        logger.info(expression.toString());
        RuleBinding rule = new RuleBinding(expression);
        RuleEvaluator evaluator = factory.getEvaluator();
        InstancesBinding instancesBinding = new InstancesBinding();
        evaluator.aggregate(rule, instancesBinding);
        DataRow dataRow = (DataRow) instancesBinding.getInstance("log");
        Assert.assertFalse(dataRow.getData().isEmpty());
    }


}
