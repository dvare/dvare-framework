package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ListOperationExpression;
import org.dvare.expression.operation.OperationType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christoph Laaber
 * @since 2024-04-19
 */
@Operation(type = OperationType.DISTINCT)
public class DistinctOperation extends ListOperationExpression {

    public DistinctOperation() {
        super(OperationType.DISTINCT);
    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        List<?> values = extractValues(instancesBinding, leftOperand);

        if (values != null) {
            var distinctValues = values
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            return new ListLiteral(distinctValues, dataTypeExpression);
        }

        return new NullLiteral<>();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}
