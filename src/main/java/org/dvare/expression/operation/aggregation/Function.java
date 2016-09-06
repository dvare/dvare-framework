package org.dvare.expression.operation.aggregation;

import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//@Operation(type = OperationType.AGGREGATION, symbols = {"FunctionService", "function", "fun"})
public class Function extends OperationExpression {
    static Logger logger = LoggerFactory.getLogger(Function.class);


    public Function() {
        super("FunctionService", "function", "fun");
    }

    public Function copy() {
        return new Function();
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {
        int i = findNextExpression(tokens, pos + 1, stack, aTypeBinding, vTypeBinding);

        List<Expression> expressions = new ArrayList<Expression>(stack);
        stack.clear(); // arrayList fill with stack elements

        List<Expression> parameters = new ArrayList<>();


        FunctionExpression tabelExpression = null;
        for (Expression expression : expressions) {
            if (expression instanceof FunctionExpression) {
                tabelExpression = (FunctionExpression) expression;
            } else {
                parameters.add(expression);
            }

        }

        tabelExpression.setParameters(parameters);
        this.rightOperand = tabelExpression;

        if (this.rightOperand == null) {

            String error = null;
            if (!parameters.isEmpty()) {
                error = String.format("No FunctionService Expression Found, %s is not  TableExpression", parameters.get(parameters.size() - 1).getClass().getSimpleName());
            } else {
                error = String.format("No FunctionService Expression Found");
            }

            logger.error(error);
            throw new ExpressionParseException(error);
        }

        logger.debug("Operation Call Expression : {}", getClass().getSimpleName());

        stack.push(this);

        return i;
    }

    /*@Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {

        FunctionExpression tabelExpression = (FunctionExpression) this.rightOperand;
        LiteralExpression literalExpression = null;

        Class<?> params[] = new Class[tabelExpression.getParameters().size()];
        Object values[] = new Object[tabelExpression.getParameters().size()];

        int counter = 0;
        for (Expression expression : tabelExpression.getParameters()) {

            if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;
                List<Object> listValues = new ArrayList<>();
                for (Object dataRow : dataSet) {
                    variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
                    listValues.add(variableExpression.getValue());
                }
                params[counter] = Object[].class;
                values[counter] = listValues.toArray();
            } else if (expression instanceof LiteralExpression) {
                LiteralExpression literal = (LiteralExpression) expression;
                params[counter] = DataTypeMapping.getDataTypeMapping(literal.getType().getDataType());
                values[counter] = literal.getValue();
            }


            counter++;
        }

        try {
            Class functionClass = tabelExpression.getBinding().getTableClass();
            Object obj = functionClass.newInstance();
            Method method = functionClass.getMethod(tabelExpression.getName(), params);
            Object value = method.invoke(obj, values);

            if (value instanceof Collection) {
                Collection collection = (Collection) value;
                literalExpression = new ListLiteral(collection, tabelExpression.binding.getReturnType(), collection.size());
            } else {
                literalExpression = LiteralType.getLiteralExpression(value, tabelExpression.binding.getReturnType());
            }

        } catch (Exception e) {
            throw new InterpretException(e.getMessage());
        }

        return literalExpression;
    }*/


}