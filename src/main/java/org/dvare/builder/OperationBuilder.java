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
package org.dvare.builder;


import org.dvare.expression.Expression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.arithmetic.*;
import org.dvare.expression.operation.logical.And;
import org.dvare.expression.operation.logical.Implies;
import org.dvare.expression.operation.logical.Not;
import org.dvare.expression.operation.logical.OR;
import org.dvare.expression.operation.relational.*;

import java.util.List;

public class OperationBuilder {

    private OperationType operationType;

    private Expression leftOperand;

    private Expression rightOperand;

    private List<Expression> parameters;

    public OperationBuilder() {
    }

    public OperationBuilder(OperationType operationType) {
        this.operationType = operationType;
    }


    public OperationBuilder(OperationType operationType, Expression leftOperand, Expression rightOperand) {
        this.operationType = operationType;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;

    }

    public OperationBuilder(OperationType operationType, Expression leftOperand, List<Expression> parameters) {
        this.operationType = operationType;
        this.leftOperand = leftOperand;
        this.parameters = parameters;

    }

    public OperationBuilder operation(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public OperationBuilder leftOperand(Expression leftOperand) {
        this.leftOperand = leftOperand;
        return this;
    }

    public OperationBuilder rightOperand(Expression rightOperand) {
        this.rightOperand = rightOperand;
        return this;
    }

    public OperationBuilder parameters(List<Expression> parameters) {
        this.parameters = parameters;
        return this;
    }

    public OperationExpression build() {
        OperationExpression operationExpression = null;
        switch (operationType) {
            case NOT: {
                operationExpression = new Not();
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case AND: {
                operationExpression = new And();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case OR: {
                operationExpression = new OR();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case IMPLIES: {
                operationExpression = new Implies();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case EQUAL: {
                operationExpression = new Equals();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case NOT_EQUAL: {
                operationExpression = new NotEquals();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case GREATER: {
                operationExpression = new GreaterThan();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }


            case GREATER_EQUAL: {
                operationExpression = new GreaterEqual();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case LESS: {
                operationExpression = new LessThan();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case LESS_EQUAL: {
                operationExpression = new LessEqual();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case ADD: {
                operationExpression = new Add();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case SUBTRACT: {
                operationExpression = new Subtract();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case MUL: {
                operationExpression = new Multiply();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }


            case DIVIDE: {
                operationExpression = new Divide();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }

            case MAX: {
                operationExpression = new Max();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }
            case MIN: {
                operationExpression = new Min();
                operationExpression.setLeftOperand(leftOperand);
                operationExpression.setRightOperand(rightOperand);
                break;
            }
        }
        return operationExpression;
    }

}
