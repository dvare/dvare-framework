package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.MAP)
public class MapOperation extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(MapOperation.class);


    public MapOperation() {
        super(OperationType.MAP);
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {


        if ((leftOperand instanceof ValuesOperation || leftOperand instanceof MapOperation) && !rightOperand.isEmpty() && rightOperand.get(0) instanceof ChainOperationExpression) {


            ChainOperationExpression chainOperationExpression = (ChainOperationExpression) rightOperand.get(0);


            Expression leftExpression = chainOperationExpression.getLeftOperand();

            while (leftExpression instanceof OperationExpression) {
                leftExpression = ((OperationExpression) leftExpression).getLeftOperand();
            }


            if (leftExpression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) leftExpression;
                String name = variableExpression.getName();
                String operandType = variableExpression.getOperandType();


                OperationExpression valueOperation = (OperationExpression) leftOperand;
                Object interpret = valueOperation.interpret(instancesBinding);

                if (interpret instanceof ListLiteral) {
                    ListLiteral listLiteral = (ListLiteral) interpret;
                    List values = listLiteral.getValue();
                    dataTypeExpression = listLiteral.getType();

                    List<Object> mappedValues = new ArrayList<>();

                    for (Object value : values) {


                        Object instance = instancesBinding.getInstance(operandType);
                        if (instance == null || !(instance instanceof DataRow)) {
                            DataRow dataRow = new DataRow();
                            dataRow.addData(name, value);
                            instancesBinding.addInstance(operandType, dataRow);
                        } else {
                            DataRow dataRow = (DataRow) instance;
                            dataRow.addData(name, value);
                            instancesBinding.addInstance(operandType, dataRow);
                        }

                        Object chainOperationInterpret = chainOperationExpression.interpret(instancesBinding);

                        if (chainOperationInterpret instanceof LiteralExpression) {

                            LiteralExpression literalExpression = (LiteralExpression) chainOperationInterpret;
                            if (!(literalExpression instanceof NullLiteral) && literalExpression.getType() != null && !literalExpression.getType().equals(dataTypeExpression)) {
                                dataTypeExpression = literalExpression.getType();

                            }

                            Object mapdedvalue = literalExpression.getValue();
                            mappedValues.add(mapdedvalue);


                        }


                    }


                    return new ListLiteral(mappedValues, dataTypeExpression);

                }
            }

        }
        return new NullLiteral();
    }


}