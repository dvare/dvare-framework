package org.dvare.builder;


import org.dvare.expression.Expression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.logical.And;
import org.dvare.expression.operation.logical.Implies;
import org.dvare.expression.operation.logical.Not;
import org.dvare.expression.operation.logical.OR;
import org.dvare.expression.operation.validation.*;

public class OperationBuilder {

    private OperationType operationType;

    private Expression leftOperand;

    private Expression rightOperand;


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

    public OperationExpression build() {
        OperationExpression operationExpression = null;
        switch (operationType) {
            case NOT: {
                operationExpression = new Not();
                operationExpression.setLeftOperand(leftOperand);
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
                operationExpression = new Greater();
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
                operationExpression = new Less();
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


        }
        return operationExpression;
    }

}
