package org.dvare.expression.operation.list;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.DataTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.VALUES)
public class ValuesOperation extends ListOperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ValuesOperation.class);


    public ValuesOperation() {
        super(OperationType.VALUES);
    }

    public ValuesOperation(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        List<?> values = extractValues(expressionBinding, instancesBinding, leftOperand);

        if (values != null) {

            if (isPairList(values)) {
                values = extractPairValues(values);
            }

            if (!rightOperand.isEmpty()) {
                if (rightOperand.size() == 1) {
                    Expression includeParam = rightOperand.get(0);
                    values = includedFilter(includeParam, expressionBinding, instancesBinding, values);

                } else if (rightOperand.size() == 2) {
                    Expression includeParam = rightOperand.get(0);
                    Expression excludeParam = rightOperand.get(1);
                    values = excludedFilter(includeParam, excludeParam, expressionBinding, instancesBinding, values);
                }
            }

            return new ListLiteral(values, dataTypeExpression);

        }
        return new NullLiteral();
    }

    private List<?> extractPairValues(List pairList) {
        List<Object> pairValues = new ArrayList<>();

        for (Object pairObject : pairList) {
            if (pairObject instanceof Pair) {
                Pair pair = (Pair) pairObject;
                if (dataTypeExpression == null) {
                    DataType dataType = DataTypeMapping.getTypeMapping(pair.getValue().getClass());
                    dataTypeExpression = DataTypeMapping.getDataTypeClass(dataType);
                }
                pairValues.add(pair.getValue());
            }
        }
        return pairValues;
    }
}