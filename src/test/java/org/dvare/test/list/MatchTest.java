package org.dvare.test.list;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.test.dataobjects.ListTestModel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;


public class MatchTest {
    private static final Logger logger = LoggerFactory.getLogger(MapTest.class);

    @Test
    public void matchInsideAndExistTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable1("42459");


        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable1("42964");


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable1("42964");


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(valuesObject1, valuesObject2, valuesObject3));

        Expression expression = factory.getParser().fromString("" +
                "match(self.Variable1, ['42964','42459','42453'] , true) " + //value inside
                "and match(self.Variable1 , ['42459','42964'])", contexts); //value exists


        logger.info(expression.toString());

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);

        assertTrue(result);


        expression = factory.getParser().fromString("" +
                "match(self.Variable1, ['42964','42459'] , true , true) ", contexts);

        logger.info(expression.toString());

        result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);

        assertTrue(result);


    }


    @Test(expected = ExpressionParseException.class)
    public void matchParamErrorTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        factory.getParser().fromString("" +
                "match(self.Variable1)", contexts); //value exists
    }

    @Test(expected = ExpressionParseException.class)
    public void insideCombParamErrorTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        factory.getParser().fromString("" +
                "insideComb(self.Variable1)", contexts); //value exists
    }

    @Test(expected = ExpressionParseException.class)
    public void combExistsParamErrorTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        factory.getParser().fromString("" +
                "combExists(self.Variable1)", contexts); //value exists
    }

    @Test(expected = ExpressionParseException.class)
    public void insideExistsCombParamErrorTest() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();


        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        factory.getParser().fromString("" +
                "InsideExistsComb(self.Variable1)", contexts); //value exists
    }

    @Test
    public void insideCombAndcombExistsTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);

        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable2(42459);


        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable2(42964);


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable2(42964);


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(valuesObject1, valuesObject2, valuesObject3));


        Expression expression = factory.getParser().fromString("" +
                "insideComb(self.Variable2,[42964,42459,42453]) " +
                "and combExists(self.Variable2,[42964,42459])", contexts);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);

        assertTrue(result);

        expression = factory.getParser().fromString("" +
                "InsideExistsComb(self.Variable2,[42964,42459]) ", contexts);

        result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);

        assertTrue(result);

    }


    @Test
    public void insideExistsCombTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable3(42459.0f);


        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable3(42964.0f);


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable3(42964.0f);


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(valuesObject1, valuesObject2, valuesObject3));

        Expression expression = factory.getParser().fromString("" +
                "InsideExistsComb(self.Variable3,[42964.0,42459.0])", contexts);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);

        assertTrue(result);

        expression = factory.getParser().fromString("" +
                "insideComb(self.Variable3,[42964.0,42459.0,42453.0]) " +
                "and combExists(self.Variable3,[42964.0,42459.0])", contexts);


        result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);


        assertTrue(result);
    }

    @Test
    public void insideExistsCombArrayTest() throws ExpressionParseException, InterpretException {
        RuleConfiguration factory = new RuleConfiguration();

        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", ListTestModel.class);


        ListTestModel valuesObject1 = new ListTestModel();
        valuesObject1.setVariable4(LocalDate.of(2016, 6, 15));


        ListTestModel valuesObject2 = new ListTestModel();
        valuesObject2.setVariable4(LocalDate.of(2016, 7, 15));


        ListTestModel valuesObject3 = new ListTestModel();
        valuesObject3.setVariable4(LocalDate.of(2016, 6, 15));


        InstancesBinding instancesBinding = new InstancesBinding();
        instancesBinding.addInstance("self", Arrays.asList(valuesObject1, valuesObject2, valuesObject3));

        Expression expression = factory.getParser().fromString("" +
                "InsideExistsComb(self.Variable4,[date ( 15-06-2016 , dd-MM-yyyy ) , date ( 15-07-2016 , dd-MM-yyyy )])", contexts);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);

        assertTrue(result);

        expression = factory.getParser().fromString("" +
                "insideComb(self.Variable4,[date ( 15-05-2016 , dd-MM-yyyy ),date ( 15-06-2016 , dd-MM-yyyy ),date ( 15-07-2016 , dd-MM-yyyy )]) " +
                "and combExists(self.Variable4,[date ( 15-06-2016 , dd-MM-yyyy ) , date ( 15-07-2016 , dd-MM-yyyy )])", contexts);


        result = (Boolean) evaluator.evaluate(new RuleBinding(expression), instancesBinding);


        assertTrue(result);
    }

}
