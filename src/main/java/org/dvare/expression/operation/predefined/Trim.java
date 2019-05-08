/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.StringType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muhammad Hammad
 * @since 2019-05-08
 */
@Operation(type = OperationType.TRIM)
public class Trim extends ChainOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Length.class);

    public Trim() {
        super(OperationType.TRIM);
    }

    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {
            if (toDataType(literalExpression.getType()) == DataType.StringType) {
                Object value = literalExpression.getValue();
                if (value != null) {
                    return LiteralType.getLiteralExpression(value.toString().trim(), StringType.class);
                }
            }
        }
        return new NullLiteral<>();
    }


}