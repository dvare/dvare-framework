package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.DataTypeMapping;
import org.dvare.util.Triple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.MIDDLES)
public class MiddlesOperation extends ListOperationExpression {

    public MiddlesOperation() {
        super(OperationType.MIDDLES);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        List<?> pairs = extractValues(instancesBinding, leftOperand);

        if (pairs != null && isTripleList(pairs)) {
            List<?> pairKeys = extractPairKeys(pairs);
            return new ListLiteral(pairKeys, dataTypeExpression);
        }

        return new NullLiteral<>();
    }


    private List<Object> extractPairKeys(List<?> triplesList) throws InterpretException {
        List<Object> pairKeys = new ArrayList<>();
        if (triplesList != null && !triplesList.isEmpty()) {
            dataTypeExpression = null;
            for (Object tripleObject : triplesList) {
                if (tripleObject instanceof Triple<?, ?, ?>) {
                    Triple<?, ?, ?> triple = (Triple<?, ?, ?>) tripleObject;
                    if (dataTypeExpression == null && triple.getMiddle() != null) {
                        DataType dataType = DataTypeMapping.getTypeMapping(triple.getMiddle().getClass());
                        dataTypeExpression = DataTypeMapping.getDataTypeClass(dataType);
                    }
                    pairKeys.add(triple.getMiddle());
                }
            }
        }
        return pairKeys;
    }

}
