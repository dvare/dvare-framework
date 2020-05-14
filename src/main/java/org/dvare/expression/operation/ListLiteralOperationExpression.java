package org.dvare.expression.operation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.List)
public class ListLiteralOperationExpression extends OperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(ListLiteralOperationExpression.class);

    protected List<Expression> expressions = new ArrayList<>();

    public ListLiteralOperationExpression() {
        super(OperationType.List);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        int newPos = pos;
        List<String> listPrams = new ArrayList<>();
        while (!tokens[newPos].equals("]")) {
            String value = tokens[newPos];
            if (!value.equals("[")) {
                listPrams.add(value);
            }
            newPos++;

            if (newPos == tokens.length) {
                throw new ExpressionParseException("Array Closing Bracket Not Found at " + pos);
            }

        }


        String[] values = listPrams.toArray(new String[0]);

        Stack<Expression> localStack = new Stack<>();
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = 0; i < values.length; i++) {
            String token = values[i];


            TokenType tokenType = findDataObject(token, contexts);


            Expression expression = null;
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {


                i = op.parse(values, i, localStack, contexts);

                expression = localStack.pop();

            } else if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                TypeBinding typeBinding = contexts.getContext(tokenType.type);
                DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                expression = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);
            } else {
                expression = LiteralType.getLiteralExpression(tokenType.token);

            }


            localStack.push(expression);


        }

        expressions.addAll(localStack);


        stack.push(this);

        return newPos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        return pos;
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        Class<? extends DataTypeExpression> dataType = null;
        List<Object> values = new ArrayList<>();
        for (Expression expression : expressions) {

            if (expression instanceof OperationExpression) {
                OperationExpression operationExpression = (OperationExpression) expression;
                LiteralExpression<?> literalExpression = operationExpression.interpret(instancesBinding);
                if (literalExpression instanceof ListLiteral) {
                    values.addAll(((ListLiteral) literalExpression).getValue());
                } else {
                    values.add(literalExpression.getValue());
                }
                if (dataType == null || toDataType(dataType).equals(DataType.NullType)) {
                    dataType = literalExpression.getType();
                }

            } else if (expression instanceof VariableExpression) {
                VariableExpression<?> variableExpression = (VariableExpression<?>) expression;
                LiteralExpression<?> literalExpression = variableExpression.interpret(instancesBinding);
                if (literalExpression instanceof ListLiteral) {
                    values.addAll(((ListLiteral) literalExpression).getValue());
                } else {
                    values.add(literalExpression.getValue());
                }
                dataType = variableExpression.getType();
            } else if (expression instanceof LiteralExpression) {
                LiteralExpression<?> literalExpression = (LiteralExpression<?>) expression;
                if (literalExpression instanceof ListLiteral) {
                    values.addAll(((ListLiteral) literalExpression).getValue());
                } else {
                    values.add(literalExpression.getValue());
                }
                if (dataType == null || toDataType(dataType).equals(DataType.NullType)) {
                    dataType = literalExpression.getType();
                }
            }


        }


        ListLiteral listLiteral = new ListLiteral(values, dataType);
        if (logger.isDebugEnabled()) {
            logger.debug("List Literal Expression : {} [{}]", toDataType(listLiteral.getType()), listLiteral.getValue());
        }
        return listLiteral;
    }

    public boolean isEmpty() {
        return expressions.isEmpty();
    }

    public Integer getSize() {
        return expressions.size();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        Iterator<Expression> iterator = expressions.iterator();

        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next().toString());
            if (iterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");

        return stringBuilder.toString();


    }


}
