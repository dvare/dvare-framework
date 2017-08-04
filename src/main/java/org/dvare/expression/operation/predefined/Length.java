package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Operation(type = OperationType.LENGTH)
public class Length extends ChainOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Length.class);


    public Length() {
        super(OperationType.LENGTH);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {


        Expression leftValueOperand = super.interpretOperand(this.leftOperand, expressionBinding, instancesBinding);
        LiteralExpression literalExpression = toLiteralExpression(leftValueOperand);
        if (literalExpression != null && !(literalExpression instanceof NullLiteral)) {


            if (literalExpression.getValue() == null) {
                return new NullLiteral();
            }

            switch (toDataType(literalExpression.getType())) {
                case StringType: {
                    return LiteralType.getLiteralExpression(literalExpression.getValue().toString().length(), IntegerType.class);
                }
            }

        }

        return new NullLiteral<>();
    }


}