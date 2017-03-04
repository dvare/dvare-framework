/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.test.validation;

import junit.framework.TestCase;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.builder.ExpressionBuilder;
import org.dvare.builder.LiteralBuilder;
import org.dvare.builder.OperationBuilder;
import org.dvare.builder.VariableBuilder;
import org.dvare.config.RuleConfiguration;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.test.dataobjects.EqualOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

public class BuilderTest extends TestCase {
    private static Logger logger = LoggerFactory.getLogger(BuilderTest.class);


    public void testApp() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        OperationExpression stringEqualExpression = new OperationBuilder().operation(OperationType.EQUAL)//
                .leftOperand(new VariableBuilder().variableType(DataType.StringType).variableName("Variable1").build())//
                .rightOperand(new LiteralBuilder().literalType(DataType.StringType).literalValue("'A'").build()).build();


        OperationExpression integerEqualExpression = new OperationBuilder().operation(OperationType.EQUAL)//
                .leftOperand(new VariableBuilder().variableType(DataType.IntegerType).variableName("Variable2").build())//
                .rightOperand(new LiteralBuilder().literalType(DataType.IntegerType).literalValue("2").build()).build();


        OperationExpression andExpression = new OperationBuilder().operation(OperationType.AND)//
                .leftOperand(stringEqualExpression)//
                .rightOperand(integerEqualExpression).build();

        Expression expression = new ExpressionBuilder().expression(andExpression).build();


        if (logger.isDebugEnabled()) {
            logger.debug(expression.toString());
        }
        RuleBinding rule = new RuleBinding(expression);


        EqualOperation equalOperation = new EqualOperation();
        equalOperation.setVariable1("'A'");
        equalOperation.setVariable2(2);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, equalOperation);
        assertTrue(result);
    }

    public void testApp2() throws ExpressionParseException, InterpretException, ParseException {

        RuleConfiguration factory = new RuleConfiguration();


        Expression left = new LiteralBuilder().literalType(DataType.BooleanType).literalValue("true").build();


        Expression right = new LiteralBuilder().literalType(DataType.BooleanType).literalValue("true").build();


        OperationExpression andExpression = new OperationBuilder().operation(OperationType.AND)//
                .leftOperand(left)//
                .rightOperand(right).build();

        Expression expression = new ExpressionBuilder().expression(andExpression).build();


        if (logger.isDebugEnabled()) {
            logger.debug(expression.toString());
        }
        RuleBinding rule = new RuleBinding(expression);


        EqualOperation equalOperation = new EqualOperation();
        equalOperation.setVariable1("'A'");
        equalOperation.setVariable2(2);

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, equalOperation);
        assertTrue(result);
    }


}
