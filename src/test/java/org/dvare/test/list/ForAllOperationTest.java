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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ForAllOperationTest {
    private static final Logger logger = LoggerFactory.getLogger(ForAllOperationTest.class);

    @Test
    public void forAllBetweenTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forAll self selfInstance | selfInstance.Variable1->substring(2,2)->toInteger() between [80,90] endForAll";


        TypeBinding typeBinding = ExpressionParser.translate(ForEachOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);

        logger.info(expression.toString());

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
    public void forAllBetweenNotTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "not forAll self selfInstance | selfInstance.Variable1->substring(2,2)->toInteger() between [80,90] endForAll";


        TypeBinding typeBinding = ExpressionParser.translate(ForEachOperation.class);
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", typeBinding);

        Expression expression = factory.getParser().fromString(exp, contexts);
        RuleBinding rule = new RuleBinding(expression);


        List<ForEachOperation> dataSet = new ArrayList<>();


        ForEachOperation eachOperation1 = new ForEachOperation();
        eachOperation1.setVariable1("D81");
        dataSet.add(eachOperation1);

        ForEachOperation eachOperation2 = new ForEachOperation();
        eachOperation2.setVariable1("D45F");
        dataSet.add(eachOperation2);

        ForEachOperation eachOperation3 = new ForEachOperation();
        eachOperation3.setVariable1("D89");
        dataSet.add(eachOperation3);


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", dataSet);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, instancesBinding);
        Assertions.assertTrue(result);

    }

    @Test
    public void forAllValuesBetweenTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forAll variable value:StringType | variable.value between [80,90] endForAll";


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", "{variable:StringListType}");

        Expression expression = factory.getParser().fromString(exp, contexts);

        //System.out.println(expression);

        Assertions.assertNotNull(expression);

    }

    @Test
    public void forAllValuesInTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forAll Variable1 value:StringType | Variable1.value in ['D81','D45F','D89'] endForAll";


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
    public void forAllInTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();

        String exp = "forAll Variable1 tmp.value:StringType | tmp.value in ['D81','D45F','D89'] endForAll";


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

