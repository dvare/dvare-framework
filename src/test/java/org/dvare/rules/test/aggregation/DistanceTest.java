package org.dvare.rules.test.aggregation;

import junit.framework.TestCase;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.util.ValueFinder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DistanceTest extends TestCase {

    @Test
    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        Expression aggregate = factory.getParser().fromString("protocol := value(data.protocol - self.protocol) ; network := value(data.network - self.network) ", Distance.class, Distance.class);


        RuleBinding rule = new RuleBinding(aggregate);
        List<RuleBinding> rules = new ArrayList<>();
        rules.add(rule);


        Distance aggregation = new Distance();
        aggregation.protocol = 10;
        aggregation.network = 10;


        Distance data = new Distance();
        data.protocol = 5;
        data.network = 10;


        RuleEvaluator evaluator = factory.getEvaluator();
        Object resultModel = evaluator.evaluate(rules, aggregation, data);


        System.out.println("network: " + ValueFinder.findValue("network", resultModel));
        System.out.println("protocol: " + ValueFinder.findValue("protocol", resultModel));


    }


    public class Distance {
        public Integer protocol;
        public Integer network;
    }
}
