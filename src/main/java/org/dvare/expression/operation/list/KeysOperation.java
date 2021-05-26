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
import org.dvare.util.Pair;
import org.dvare.util.Triple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.KEYS)
public class KeysOperation extends ListOperationExpression {

    public KeysOperation() {
        super(OperationType.KEYS);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        List<?> pairs = extractValues(instancesBinding, leftOperand);

        if (pairs != null && isPairList(pairs)) {
            List<?> pairKeys = extractPairKeys(pairs);
            return new ListLiteral(pairKeys, dataTypeExpression);
        } else if (pairs != null && isTripleList(pairs)) {
            List<?> triplesKeys = extractTripleKeys(pairs);
            return new ListLiteral(triplesKeys, dataTypeExpression);
        }

        return new NullLiteral<>();
    }


    private List<Object> extractPairKeys(List<?> pairList) throws InterpretException {
        List<Object> pairKeys = new ArrayList<>();
        if (pairList != null && !pairList.isEmpty()) {
            dataTypeExpression = null;
            for (Object pairObject : pairList) {
                if (pairObject instanceof Pair<?, ?>) {
                    Pair<?, ?> pair = (Pair<?, ?>) pairObject;
                    if (dataTypeExpression == null && pair.getKey() != null) {
                        DataType dataType = DataTypeMapping.getTypeMapping(pair.getKey().getClass());
                        dataTypeExpression = DataTypeMapping.getDataTypeClass(dataType);
                    }
                    pairKeys.add(pair.getKey());
                }
            }
        }
        return pairKeys;
    }


    private List<Object> extractTripleKeys(List<?> triplesList) throws InterpretException {
        List<Object> pairKeys = new ArrayList<>();
        if (triplesList != null && !triplesList.isEmpty()) {
            dataTypeExpression = null;
            for (Object tripleObject : triplesList) {
                if (tripleObject instanceof Triple<?, ?, ?>) {
                    Triple<?, ?, ?> triple = (Triple<?, ?, ?>) tripleObject;
                    if (dataTypeExpression == null && triple.getLeft() != null) {
                        DataType dataType = DataTypeMapping.getTypeMapping(triple.getLeft().getClass());
                        dataTypeExpression = DataTypeMapping.getDataTypeClass(dataType);
                    }
                    pairKeys.add(triple.getLeft());
                }
            }
        }
        return pairKeys;
    }

}
