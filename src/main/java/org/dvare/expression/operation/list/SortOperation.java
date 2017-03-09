package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.utility.GetExpOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Operation(type = OperationType.SORT)
public class SortOperation extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(SortOperation.class);


    public SortOperation() {
        super(OperationType.SORT);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        List<Object> values = null;
        if ((leftOperand instanceof ValuesOperation || leftOperand instanceof MapOperation || leftOperand instanceof GetExpOperation)) {
            OperationExpression valueOperation = (OperationExpression) leftOperand;
            Object interpret = valueOperation.interpret(expressionBinding, instancesBinding);

            if (interpret instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) interpret;
                values = listLiteral.getValue();
                dataTypeExpression = listLiteral.getType();

            }


        }

        if (values != null) {
            if (rightOperand.isEmpty()) {
                values.sort(null);
                return new ListLiteral(values, dataTypeExpression);

            } else if (rightOperand.get(0) instanceof ChainOperationExpression) {


                ChainOperationExpression chainOperationExpression = (ChainOperationExpression) rightOperand.get(0);


                Expression leftExpression = chainOperationExpression.getLeftOperand();

                while (leftExpression instanceof OperationExpression) {
                    leftExpression = ((OperationExpression) leftExpression).getLeftOperand();
                }


                if (leftExpression instanceof VariableExpression) {
                    VariableExpression variableExpression = (VariableExpression) leftExpression;


                    values.sort((o1, o2) -> {

                        try {
                            ValueType c1 = buildCompareValue(expressionBinding, instancesBinding, chainOperationExpression, variableExpression, o1);

                            ValueType c2 = buildCompareValue(expressionBinding, instancesBinding, chainOperationExpression, variableExpression, o2);

                            if (c1 != null && c1.value != null && c2 != null && c2.value != null) {
                                switch (toDataType(c1.type)) {


                                    case StringType: {

                                        String i1 = (String) c1.value;

                                        String i2 = (String) c2.value;

                                        return i1.compareTo(i2);
                                    }

                                    case IntegerType: {

                                        Integer i1 = (Integer) c1.value;

                                        Integer i2 = (Integer) c2.value;

                                        return i1.compareTo(i2);
                                    }


                                    case FloatType: {

                                        Float i1 = (Float) c1.value;

                                        Float i2 = (Float) c2.value;

                                        return i1.compareTo(i2);
                                    }

                                    case DateType: {

                                        LocalDate i1 = (LocalDate) c1.value;

                                        LocalDate i2 = (LocalDate) c2.value;

                                        return i1.compareTo(i2);
                                    }

                                    case DateTimeType: {

                                        LocalDateTime i1 = (LocalDateTime) c1.value;

                                        LocalDateTime i2 = (LocalDateTime) c2.value;

                                        return i1.compareTo(i2);
                                    }


                                    case SimpleDateType: {

                                        Date i1 = (Date) c1.value;

                                        Date i2 = (Date) c2.value;

                                        return i1.compareTo(i2);
                                    }
                                }

                            }


                        } catch (Exception e) {
                            return 0;
                        }
                        return 0;
                    });


                    return new ListLiteral(values, dataTypeExpression);


                }

            }
        }
        return new NullLiteral();
    }


    private ValueType buildCompareValue(ExpressionBinding expressionBinding, InstancesBinding instancesBinding, ChainOperationExpression chainOperationExpression, VariableExpression variableExpression, Object value) throws InterpretException {
        String name = variableExpression.getName();
        String operandType = variableExpression.getOperandType();
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

        Object chainOperationInterpret = chainOperationExpression.interpret(expressionBinding, instancesBinding);

        if (chainOperationInterpret instanceof LiteralExpression) {

            LiteralExpression literalExpression = (LiteralExpression) chainOperationInterpret;


            ValueType paramValue = new ValueType();
            paramValue.value = literalExpression.getValue();
            paramValue.type = literalExpression.getType();

            return paramValue;


        }
        return null;
    }


    private class ValueType {
        Class<? extends DataTypeExpression> type;
        Object value;
    }


}