/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
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
package org.dvare.evaluator;


import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.literal.LiteralExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class RuleEvaluator {
    static Logger logger = LoggerFactory.getLogger(RuleEvaluator.class);


    public Object evaluate(RuleBinding rule, Object object) throws InterpretException {
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", object);
        return evaluate(rule, instancesBinding);
    }

    public Object evaluate(RuleBinding rule, InstancesBinding instancesBinding) throws InterpretException {

        LiteralExpression literalExpression = rule.getExpression().interpret(instancesBinding);

        if (literalExpression.getValue() != null) {
            return literalExpression.getValue();
        }

        return null;
    }


    public InstancesBinding aggregate(RuleBinding rule, Object object) throws InterpretException {
        InstancesBinding instancesBinding = new InstancesBinding(new HashMap<>());
        instancesBinding.addInstance("self", object);

        return aggregate(rule, instancesBinding);
    }


    public InstancesBinding aggregate(RuleBinding rule, InstancesBinding instancesBinding) throws InterpretException {

        rule.getExpression().interpret(instancesBinding);
        return instancesBinding;
    }
}
