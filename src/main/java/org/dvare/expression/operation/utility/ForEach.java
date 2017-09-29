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
package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.list.ValuesOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.FOREACH)
public class ForEach extends ForAll {
    private static Logger logger = LoggerFactory.getLogger(ForEach.class);


    public ForEach() {
        super(OperationType.FOREACH);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {

        //context forALL
        if (derivedContext != null) {
            if (referenceContext instanceof NamedExpression) {
                Object object = instancesBinding.getInstance(((NamedExpression) referenceContext).getName());
                if (object instanceof List) {
                    List instances = (List) object;

                    List results = new ArrayList<>();

                    for (Object instance : instances) {
                        instancesBinding.addInstance(derivedContext.getName(), instance);


                        Object interpret = leftOperand.interpret(instancesBinding);
                        LiteralExpression literalExpression = (LiteralExpression) interpret;
                        if (literalExpression.getType() != null && !(literalExpression.getType().equals(NullType.class))) {
                            dataTypeExpression = literalExpression.getType();
                            results.add(literalExpression.getValue());
                        }


                    }

                    instancesBinding.removeInstance(derivedContext.getName());

                    return new ListLiteral(results, dataTypeExpression);

                }
            }
            //variableForAll
            else if (referenceContext instanceof VariableExpression) {

                String derivedInstanceBinding = ((VariableExpression) referenceContext).getName();

                ValuesOperation valuesOperation = new ValuesOperation();
                valuesOperation.setLeftOperand(referenceContext);
                Object interpret = valuesOperation.interpret(instancesBinding);

                if (interpret instanceof ListLiteral) {
                    List results = new ArrayList<>();
                    List<?> values = ((ListLiteral) interpret).getValue();
                    for (Object value : values) {

                        final String selfPatten = ".{1,}\\..{1,}";
                        if (!derivedContext.getName().matches(selfPatten)) {

                            Object instance = instancesBinding.getInstance(derivedContext.getName());
                            if (instance == null) {
                                instance = new DataRow();
                            }
                            if (instance instanceof DataRow) {
                                DataRow dataRow = (DataRow) instance;
                                dataRow.addData(derivedContext.getName(), value);
                                instancesBinding.addInstance(derivedInstanceBinding, dataRow);
                            }

                        } else {
                            TokenType derivedTokenType = buildTokenType(derivedContext.getName());
                            Object instance = instancesBinding.getInstance(derivedTokenType.type);
                            if (instance == null) {
                                instance = new DataRow();
                            }
                            if (instance instanceof DataRow) {
                                DataRow dataRow = (DataRow) instance;
                                dataRow.addData(derivedTokenType.token, value);
                                instancesBinding.addInstance(derivedTokenType.type, dataRow);
                            }

                        }


                        LiteralExpression literalExpression = leftOperand.interpret(instancesBinding);
                        if (literalExpression.getType() != null && !(literalExpression.getType().equals(NullType.class))) {
                            dataTypeExpression = literalExpression.getType();
                            results.add(literalExpression.getValue());
                        }

                    }

                    instancesBinding.removeInstance(derivedInstanceBinding);

                    return new ListLiteral(results, dataTypeExpression);
                }


                instancesBinding.removeInstance(derivedInstanceBinding);
            }
        }
        return new NullLiteral();
    }


}
