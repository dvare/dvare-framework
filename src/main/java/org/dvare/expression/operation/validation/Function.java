package org.dvare.expression.operation.validation;

import org.dvare.annotations.OperationType;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralDataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.DataTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"FunctionService", "function", "fun"})
public class Function extends OperationExpression {
    static Logger logger = LoggerFactory.getLogger(Function.class);


    public Function() {
        super("FunctionService", "function", "fun");
    }

    public Function copy() {
        return new Function();
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 1, stack, typeBinding);

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
        this.leftOperand = tabelExpression;


        if (this.leftOperand == null) {

            String error = null;
            if (!parameters.isEmpty()) {
                error = String.format("No Table Expression Found, %s is not  TableExpression", parameters.get(parameters.size() - 1).getClass().getSimpleName());
            } else {
                error = String.format("No Table Expression Found");
            }

            logger.error(error);
            throw new ExpressionParseException(error);
        }

        logger.debug("Operation Call Expression : {}", getClass().getSimpleName());

        stack.push(this);

        return i;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {
            String token = tokens[i];

            Operation op = configurationRegistry.getValidationOperation(token);
            if (op != null) {
                op = op.copy();
                // we found an operation

                if (op.getClass().equals(RightPriority.class)) {
                    return i;
                }


            } else if (configurationRegistry.getFunction(token) != null) {
                String name = token;
                FunctionBinding table = configurationRegistry.getFunction(token);
                FunctionExpression tableExpression = new FunctionExpression(name, table);
                stack.add(tableExpression);

            } else if (findType(token, typeBinding) != null) {
                DataType dataType = findType(token, typeBinding);
                VariableExpression variableExpression = VariableType.getVariableType(token, dataType);
                stack.add(variableExpression);

            } else if (!token.equals(",")) {
                DataType dataType = LiteralDataType.computeDataType(token);
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(token, dataType);
                stack.add(literalExpression);
            }
        }
        return null;
    }


    @Override
    public Object interpret(Object object) throws InterpretException {

        FunctionExpression tabelExpression = (FunctionExpression) this.leftOperand;


        Class<?> params[] = new Class[tabelExpression.getParameters().size()];
        Object values[] = new Object[tabelExpression.getParameters().size()];

        int counter = 0;
        for (Expression expression : tabelExpression.getParameters()) {

            if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;
                variableExpression = VariableType.setVariableValue(variableExpression, object);
                params[counter] = DataTypeMapping.getDataTypeMapping(variableExpression.getType().getDataType());
                values[counter] = variableExpression.getValue();
            } else if (expression instanceof LiteralExpression) {
                LiteralExpression literalExpression = (LiteralExpression) expression;
                params[counter] = DataTypeMapping.getDataTypeMapping(literalExpression.getType().getDataType());
                values[counter] = literalExpression.getValue();
            }

            counter++;
        }

        Object result = invokeFunction(tabelExpression, params, values);
        return result;
    }


    private Object invokeFunction(FunctionExpression tabelExpression, Class<?> params[], Object values[]) {

        try {
            Class functionClass = tabelExpression.getBinding().getTableClass();
            Object obj = functionClass.newInstance();
            Method method = functionClass.getMethod(tabelExpression.getName(), params);
            Object value = method.invoke(obj, values);

            if (value instanceof Collection) {
                Collection collection = (Collection) value;
                return new ListLiteral<>(collection, tabelExpression.binding.getReturnType(), collection.size());
            } else {
                return LiteralType.getLiteralExpression(value, tabelExpression.binding.getReturnType());
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}