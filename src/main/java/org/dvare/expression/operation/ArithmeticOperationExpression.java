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
package org.dvare.expression.operation;

import org.dvare.annotations.Type;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class ArithmeticOperationExpression extends RelationalOperationExpression {


    public ArithmeticOperationExpression(OperationType operationType) {
        super(operationType);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        Expression leftExpression = interpretOperandLeft(instancesBinding, leftOperand);

        if (leftExpression == null)
            return new NullLiteral();


        LiteralExpression<?> rightExpression = (LiteralExpression) interpretOperandRight(instancesBinding, rightOperand);
        ;

        LiteralExpression left = toLiteralExpression(leftExpression);
        LiteralExpression right = toLiteralExpression(rightExpression);

        if (left instanceof NullLiteral && right instanceof NullLiteral) {
            return new NullLiteral();
        }

        if (left instanceof NullLiteral && right.getType().isAnnotationPresent(Type.class)) {
            Type type = (Type) right.getType().getAnnotation(Type.class);

            switch (type.dataType()) {

                case FloatType: {
                    left = LiteralType.getLiteralExpression(0f, right.getType());
                    break;
                }
                case IntegerType: {
                    left = LiteralType.getLiteralExpression(0, right.getType());
                    break;
                }
                case StringType: {
                    left = LiteralType.getLiteralExpression("", right.getType());
                    break;
                }

            }

        }

        if (left instanceof NullLiteral || right instanceof NullLiteral) {
            dataTypeExpression = NullType.class;
        }

        try {
            return dataTypeExpression.newInstance().evaluate(this, left, right);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


}