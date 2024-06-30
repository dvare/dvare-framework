package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.TrimString;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_INTEGER, dataTypes = {DataType.StringType})
public class ToInteger extends ChainOperationExpression {


    public ToInteger() {
        super(OperationType.TO_INTEGER);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LiteralExpression<?> literalExpression = super.interpretOperand(leftOperand, instancesBinding);
        if (!(literalExpression instanceof NullLiteral) && literalExpression.getValue() != null) {

            Object value = literalExpression.getValue();

            switch (toDataType(literalExpression.getType())) {
                case StringType: {
                    String stringValue = TrimString.trim(value.toString());
                    DataType valueType = LiteralType.computeDataType(stringValue);

                    switch (valueType) {
                        case IntegerType:
                            return LiteralType.getLiteralExpression(Integer.parseInt(stringValue), IntegerType.class);
                        case FloatType:
                            return LiteralType.getLiteralExpression(Math.round(Float.parseFloat(stringValue)), IntegerType.class);
                    }
                }

                case IntegerType: {
                    if (value instanceof Integer) {
                        return LiteralType.getLiteralExpression((Integer) value, IntegerType.class);
                    } else {
                        DataType valueType = LiteralType.computeDataType(value.toString());
                        if (valueType.equals(DataType.IntegerType)) {
                            return LiteralType.getLiteralExpression(Integer.parseInt(value.toString()), IntegerType.class);
                        }
                    }

                }
                case FloatType: {
                    if (value instanceof Float) {
                        return LiteralType.getLiteralExpression(Math.round((Float) value), IntegerType.class);
                    } else if (value != null) {
                        DataType valueType = LiteralType.computeDataType(value.toString());
                        if (valueType.equals(DataType.FloatType)) {
                            return LiteralType.getLiteralExpression(Math.round(Float.parseFloat(value.toString())), IntegerType.class);
                        }

                    }

                }
            }

        }

        return new NullLiteral<>();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}