package org.dvare.test.list;

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
import org.dvare.test.dataobjects.ForEachOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ForEachOperationTest {

    @Test
    public void forEachInstancesTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forEach self selfInstance | selfInstance.Variable1->substring(2,2)->toInteger() endForEach  = [81,85,89]";


        TypeBinding typeBinding = ExpressionParser.translate(ForEachOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        List<ForEachOperation> dataset = new ArrayList<>();


        ForEachOperation eachOperation1 = new ForEachOperation();
        eachOperation1.setVariable1("D81");
        dataset.add(eachOperation1);

        ForEachOperation eachOperation2 = new ForEachOperation();
        eachOperation2.setVariable1("D85");
        dataset.add(eachOperation2);

        ForEachOperation eachOperation3 = new ForEachOperation();
        eachOperation3.setVariable1("D89");
        dataset.add(eachOperation3);


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataset);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assertions.assertTrue(result);

    }


    @Test
    public void forEachValuesTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forEach Variable1 value:StringType | " +
                "Variable1.value->substring(2,2)->toInteger() endForEach  = [81,45,89]";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(ForEachOperation.class));

        Expression expression = factory.getParser().fromString(exp, contexts);


        ForEachOperation eachOperation1 = new ForEachOperation();
        eachOperation1.setVariable1("D81");


        ForEachOperation eachOperation2 = new ForEachOperation();
        eachOperation2.setVariable1("D45F");


        ForEachOperation eachOperation3 = new ForEachOperation();
        eachOperation3.setVariable1("D89");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(eachOperation1, eachOperation2, eachOperation3));

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        Assertions.assertTrue(result);

    }

    @Test
    public void forEachValuesCompareTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forEach Variable1 tmp.value:StringType | " +
                "tmp.value->substring(2,2)->toInteger() endForEach  = [81,45,89]";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ExpressionParser.translate(ForEachOperation.class));

        Expression expression = factory.getParser().fromString(exp, contexts);


        ForEachOperation eachOperation1 = new ForEachOperation();
        eachOperation1.setVariable1("D81");


        ForEachOperation eachOperation2 = new ForEachOperation();
        eachOperation2.setVariable1("D45F");


        ForEachOperation eachOperation3 = new ForEachOperation();
        eachOperation3.setVariable1("D89");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(eachOperation1, eachOperation2, eachOperation3));

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);
        Assertions.assertTrue(result);

    }


}

