package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
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
@Operation(type = OperationType.VALUES)
public class ValuesOperation extends ListOperationExpression {

    public ValuesOperation() {
        super(OperationType.VALUES);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        List<?> values = extractValues(instancesBinding, leftOperand);

        if (values != null) {

            if (isPairList(values)) {
                values = extractPairValues(values);
            } else if (isTripleList(values)) {
                values = extractTripleValues(values);
            }

            if (!rightOperand.isEmpty()) {
                if (rightOperand.size() == 1) {
                    Expression includeParam = rightOperand.get(0);
                    values = includedFilter(includeParam, instancesBinding, values);

                } else if (rightOperand.size() == 2) {
                    Expression includeParam = rightOperand.get(0);
                    Expression excludeParam = rightOperand.get(1);
                    values = excludedFilter(includeParam, excludeParam, instancesBinding, values);
                }
            }

            return new ListLiteral(values, dataTypeExpression);

        }
        return new NullLiteral<>();
    }

    private List<?> extractPairValues(List<?> pairList) {
        List<Object> pairValues = new ArrayList<>();
        if (pairList != null && !pairList.isEmpty()) {
            dataTypeExpression = null;
            for (Object pairObject : pairList) {
                if (pairObject instanceof Pair) {
                    Pair<?, ?> pair = (Pair<?, ?>) pairObject;
                    if (dataTypeExpression == null && pair.getValue() != null) {
                        DataType dataType = DataTypeMapping.getTypeMapping(pair.getValue().getClass());
                        dataTypeExpression = DataTypeMapping.getDataTypeClass(dataType);
                    }
                    pairValues.add(pair.getValue());
                }
            }
        }
        return pairValues;
    }

    private List<?> extractTripleValues(List<?> tripleList) {
        List<Object> pairValues = new ArrayList<>();
        if (tripleList != null && !tripleList.isEmpty()) {
            dataTypeExpression = null;
            for (Object pairObject : tripleList) {
                if (pairObject instanceof Triple) {
                    Triple<?, ?, ?> pair = (Triple<?, ?, ?>) pairObject;
                    if (dataTypeExpression == null && pair.getRight() != null) {
                        DataType dataType = DataTypeMapping.getTypeMapping(pair.getRight().getClass());
                        dataTypeExpression = DataTypeMapping.getDataTypeClass(dataType);
                    }
                    pairValues.add(pair.getRight());
                }
            }
        }
        return pairValues;
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }
}
