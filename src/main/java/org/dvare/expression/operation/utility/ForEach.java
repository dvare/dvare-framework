package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Operation(type = OperationType.FOREACH)
public class ForEach extends ForAll {
    private static Logger logger = LoggerFactory.getLogger(ForEach.class);


    public ForEach() {
        super(OperationType.FOREACH);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {


        Object object = instancesBinding.getInstance(((NamedExpression) referenceContext).getName());
        if (object instanceof List) {
            List instances = (List) object;

            List results = new ArrayList<>();

            for (Object instance : instances) {
                instancesBinding.addInstance(((NamedExpression) derivedContext).getName(), instance);


                Object interpret = leftOperand.interpret(expressionBinding, instancesBinding);
                LiteralExpression literalExpression = (LiteralExpression) interpret;
                if (literalExpression.getType() != null && !(literalExpression.getType().equals(NullType.class))) {
                    dataTypeExpression = literalExpression.getType();
                    results.add(literalExpression.getValue());
                }


            }

            instancesBinding.removeInstance(((NamedExpression) derivedContext).getName());

            return new ListLiteral(results, dataTypeExpression);

        }

        return new NullLiteral();
    }


}
