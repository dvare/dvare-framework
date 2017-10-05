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
package org.dvare.expression.operation;

import org.dvare.annotations.Type;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class ArithmeticOperationExpression extends RelationalOperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(ArithmeticOperationExpression.class);

    public ArithmeticOperationExpression(OperationType operationType) {
        super(operationType);
    }

    public static LiteralExpression evaluate(Class<? extends DataTypeExpression> dataTypeExpression,
                                             OperationExpression operationExpression, LiteralExpression left, LiteralExpression right) throws InterpretException {

        try {
            return dataTypeExpression.newInstance().evaluate(operationExpression, left, right);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return new NullLiteral();
    }

    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression leftLiteralExpression = interpretOperandLeft(instancesBinding, leftOperand);
        LiteralExpression rightLiteralExpression = interpretOperandRight(instancesBinding, rightOperand);

        if (leftLiteralExpression instanceof NullLiteral && rightLiteralExpression.getType().isAnnotationPresent(Type.class)) {
            Type type = (Type) rightLiteralExpression.getType().getAnnotation(Type.class);

            switch (type.dataType()) {

                case FloatType: {
                    leftLiteralExpression = LiteralType.getLiteralExpression(0f, rightLiteralExpression.getType());
                    break;
                }
                case IntegerType: {
                    leftLiteralExpression = LiteralType.getLiteralExpression(0, rightLiteralExpression.getType());
                    break;
                }
                case StringType: {
                    leftLiteralExpression = LiteralType.getLiteralExpression("", rightLiteralExpression.getType());
                    break;
                }

            }

        }

        if (leftLiteralExpression instanceof NullLiteral || rightLiteralExpression instanceof NullLiteral) {
            dataTypeExpression = NullType.class;
        }

        return evaluate(dataTypeExpression, this, leftLiteralExpression, rightLiteralExpression);


    }
}