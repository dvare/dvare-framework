package org.dvare.expression.operation;

import org.dvare.annotations.Type;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.util.InstanceUtils;
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

    public static LiteralExpression<?> evaluate(
            Class<? extends DataTypeExpression> dataTypeExpression, OperationExpression operationExpression,
            LiteralExpression<?> left, LiteralExpression<?> right) {

        try {
            return new InstanceUtils<DataTypeExpression>().newInstance(dataTypeExpression).evaluate(operationExpression, left, right);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new NullLiteral<>();
    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> leftLiteralExpression = interpretOperandLeft(instancesBinding, leftOperand);
        LiteralExpression<?> rightLiteralExpression = interpretOperandRight(instancesBinding, rightOperand);

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