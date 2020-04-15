package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.utility.LetOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.MAP)
public class MapOperation extends ListOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(MapOperation.class);


    public MapOperation() {
        super(OperationType.MAP);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        if (leftOperand instanceof PairOperation) {
            List<?> values = extractValues(instancesBinding, leftOperand);
            return new ListLiteral(values, dataTypeExpression);
        } else {
            List<?> values = extractValues(instancesBinding, leftOperand);
            if (values != null) {
                if (rightOperand.isEmpty() || !(rightOperand.get(0) instanceof ChainOperationExpression)) {
                    return new ListLiteral(values, dataTypeExpression);
                } else {
                    ChainOperationExpression chainOperationExpression = (ChainOperationExpression) rightOperand.get(0);


                    Expression leftExpression = chainOperationExpression.getLeftOperand();

                    while (leftExpression instanceof OperationExpression && !(leftExpression instanceof LetOperation)) {
                        leftExpression = ((OperationExpression) leftExpression).getLeftOperand();
                    }


                    if (leftExpression instanceof LetOperation) {
                        leftExpression = LetOperation.class.cast(leftExpression).getVariableExpression();
                    }


                    if (leftExpression instanceof VariableExpression) {
                        VariableExpression variableExpression = (VariableExpression) leftExpression;
                        String name = variableExpression.getName();
                        String operandType = variableExpression.getOperandType();


                        List<Object> mappedValues = new ArrayList<>();

                        for (Object value : values) {

                            instancesBinding.addInstanceItem(operandType, name, value);

                            Object chainOperationInterpret = chainOperationExpression.interpret(instancesBinding);
                            instancesBinding.removeInstanceItem(operandType, name);


                            if (chainOperationInterpret instanceof LiteralExpression) {

                                LiteralExpression literalExpression = (LiteralExpression) chainOperationInterpret;
                                if (!(literalExpression instanceof NullLiteral) && literalExpression.getType() != null && !literalExpression.getType().equals(dataTypeExpression)) {
                                    dataTypeExpression = literalExpression.getType();

                                }

                                Object mappedValue = literalExpression.getValue();
                                mappedValues.add(mappedValue);


                            }


                        }


                        return new ListLiteral(mappedValues, dataTypeExpression);


                    }

                }

            }


        }
        return new NullLiteral();
    }


}