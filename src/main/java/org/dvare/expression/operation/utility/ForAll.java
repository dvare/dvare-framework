package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.IterationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.list.ValuesOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.FORALL)
public class ForAll extends IterationOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(ForAll.class);


    public ForAll() {
        super(OperationType.FORALL);
    }

    public ForAll(OperationType operationType) {
        super(operationType);
    }


    @Override
    public LiteralExpression<?> interpret(
            InstancesBinding instancesBinding) throws InterpretException {

        //context forALL
        if (derivedContext != null) {
            if (referenceContext instanceof NamedExpression) {
                Object object = instancesBinding.getInstance(((NamedExpression) referenceContext).getName());
                if (object instanceof List) {
                    String derivedInstanceBinding = derivedContext.getName();

                    List instances = (List) object;

                    List<Boolean> results = new ArrayList<>();

                    for (Object instance : instances) {
                        instancesBinding.addInstance(derivedInstanceBinding, instance);

                        LiteralExpression<?> result = leftOperand.interpret(instancesBinding);
                        results.add(toBoolean(result));

                    }

                    instancesBinding.removeInstance(derivedInstanceBinding);
                    Boolean result = results.stream().allMatch(Boolean::booleanValue);
                    return LiteralType.getLiteralExpression(result, BooleanType.class);

                }

                //variableForAll
            } else if (referenceContext instanceof VariableExpression) {

                String derivedInstanceBinding = ((VariableExpression<?>) referenceContext).getName();

                ValuesOperation valuesOperation = new ValuesOperation();
                valuesOperation.setLeftOperand(referenceContext);
                Object interpret = valuesOperation.interpret(instancesBinding);

                if (interpret instanceof ListLiteral) {
                    List<Boolean> results = new ArrayList<>();
                    List<?> values = ((ListLiteral) interpret).getValue();
                    for (Object value : values) {


                        final String selfPatten = ".{1,}\\..{1,}";
                        if (!derivedContext.getName().matches(selfPatten)) {

                            Object instance = instancesBinding.getInstance(derivedContext.getName());
                            if (instance == null) {
                                instance = new DataRow();
                            }
                            if (instance instanceof DataRow) {
                                DataRow dataRow = (DataRow) instance;
                                dataRow.addData(derivedContext.getName(), value);
                                instancesBinding.addInstance(derivedInstanceBinding, dataRow);
                            }

                        } else {
                            TokenType derivedTokenType = buildTokenType(derivedContext.getName());
                            Object instance = instancesBinding.getInstance(derivedTokenType.type);
                            if (instance == null) {
                                instance = new DataRow();
                            }
                            if (instance instanceof DataRow) {
                                DataRow dataRow = (DataRow) instance;
                                dataRow.addData(derivedTokenType.token, value);
                                instancesBinding.addInstance(derivedTokenType.type, dataRow);
                            }

                        }


                        LiteralExpression<?> result = leftOperand.interpret(instancesBinding);
                        results.add(toBoolean(result));

                    }

                    instancesBinding.removeInstance(derivedInstanceBinding);
                    Boolean result = results.stream().allMatch(Boolean::booleanValue);
                    return LiteralType.getLiteralExpression(result, BooleanType.class);
                }

                //instancesBinding.removeInstance(derivedInstanceBinding);

            }
        }
        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }


    @Override
    public String toString() {

        StringBuilder toStringBuilder = new StringBuilder();

        toStringBuilder.append(this.getSymbols().get(0));


        toStringBuilder.append(" ");
        toStringBuilder.append(referenceContext.toString());

        toStringBuilder.append(" ");
        toStringBuilder.append(derivedContext.toString());


        toStringBuilder.append(" ");
        toStringBuilder.append("|");
        toStringBuilder.append(" ");

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" ");
        }

        return toStringBuilder.toString();


    }
}