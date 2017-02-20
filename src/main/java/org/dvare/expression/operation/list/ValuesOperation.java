package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.VALUES)
public class ValuesOperation extends AggregationOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ValuesOperation.class);


    public ValuesOperation() {
        super(OperationType.VALUES);
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {


        Expression right = this.leftOperand;
        if (right instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) right;
            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());
            List dataSet;
            if (instance instanceof List) {
                dataSet = (List) instance;
            } else {
                dataSet = new ArrayList<>();
                dataSet.add(instance);
            }

            List<Object> values = new ArrayList<>();

            for (Object object : dataSet) {
                Object value = getValue(object, variableExpression.getName());
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(value, variableExpression.getType());
                values.add(literalExpression.getValue());
            }


            return new ListLiteral(values, variableExpression.getType());


        } else if (right instanceof ChainOperationExpression) {

            ChainOperationExpression operationExpression = (ChainOperationExpression) right;

            Expression expression = operationExpression.getLeftOperand();

            while (expression instanceof ChainOperationExpression) {
                expression = ((ChainOperationExpression) expression).getLeftOperand();
            }


            if (expression instanceof VariableExpression) {


                VariableExpression variableExpression = (VariableExpression) expression;
                String operandType = variableExpression.getOperandType();
                Class dataTypeExpression = variableExpression.getType();
                Object instance = instancesBinding.getInstance(operandType);


                List dataSet;
                if (instance instanceof List) {
                    dataSet = (List) instance;
                } else {
                    dataSet = new ArrayList<>();
                    dataSet.add(instance);
                }

                List<Object> values = new ArrayList<>();

                for (Object object : dataSet) {


                    instancesBinding.addInstance(operandType, object);

                    LiteralExpression literalExpression = (LiteralExpression) operationExpression.interpret(instancesBinding);


                    if (literalExpression.getType() != null && !literalExpression.getType().equals(NullType.class)) {
                        dataTypeExpression = literalExpression.getType();
                    }


                    values.add(literalExpression.getValue());
                }


                instancesBinding.addInstance(operandType, instance);


                return new ListLiteral(values, dataTypeExpression);


            }


        }

        return new NullLiteral();
    }

}