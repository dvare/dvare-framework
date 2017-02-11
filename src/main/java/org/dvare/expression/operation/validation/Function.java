package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.DataTypeMapping;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.FUNCTION)
public class Function extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(Function.class);


    public Function() {
        super(OperationType.FUNCTION);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        FunctionExpression functionExpression = (FunctionExpression) stack.pop();


        this.leftOperand = functionExpression;


        if (this.leftOperand == null) {

            String error = null;
            if (!functionExpression.getParameters().isEmpty()) {
                error = String.format("No Table Expression Found, %s is not  TableExpression", functionExpression.getParameters().get(functionExpression.getParameters().size() - 1).getClass().getSimpleName());
            } else {
                error = "No Table Expression Found";
            }

            logger.error(error);
            throw new ExpressionParseException(error);
        }

        logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());


        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        Stack<Expression> localStack = new Stack<>();

        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

                if (op.getClass().equals(RightPriority.class)) {


                    List<Expression> expressions = new ArrayList<>(localStack);
                    List<Expression> parameters = new ArrayList<>();
                    FunctionExpression functionExpression = null;
                    for (Expression expression : expressions) {
                        if (expression instanceof FunctionExpression) {
                            functionExpression = (FunctionExpression) expression;
                        } else {
                            parameters.add(expression);
                        }
                    }

                    if (functionExpression != null) {
                        functionExpression.setParameters(parameters);
                        stack.push(functionExpression);
                    }

                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, localStack, contexts);
                }
            } else if (configurationRegistry.getFunction(token) != null) {
                FunctionBinding functionBinding = configurationRegistry.getFunction(token);
                FunctionExpression tableExpression = new FunctionExpression(token, functionBinding);
                localStack.add(tableExpression);

            } else {


                TokenType tokenType = findDataObject(token, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                    VariableExpression variableExpression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);
                    localStack.add(variableExpression);

                } else {
                    LiteralExpression literalExpression = LiteralType.getLiteralExpression(token);
                    localStack.add(literalExpression);
                }


            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {
        return interpretFunction(instancesBinding);

    }


    private Object interpretFunction(InstancesBinding instancesBinding) throws InterpretException {

        FunctionExpression functionExpression = (FunctionExpression) this.leftOperand;


        Class<?> params[] = new Class[functionExpression.getParameters().size()];
        Object values[] = new Object[functionExpression.getParameters().size()];


        List<DataType> parameters = functionExpression.getBinding().getParameters();


        int counter = 0;
        for (Expression expression : functionExpression.getParameters()) {
            DataType dataType = parameters.get(counter);
            Class originalType = DataTypeMapping.getDataTypeMapping(dataType);

            if (expression instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) expression;
                LiteralExpression literalExpression;
                literalExpression = (LiteralExpression) operation.interpret(instancesBinding);

                ParamValue paramsValue = buildLiteralParam(literalExpression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;

            } else if (expression instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) expression;
                Object instance = instancesBinding.getInstance(variableExpression.getOperandType());

                try {
                    DataType variableDataType = variableExpression.getType().getDataType();

                    if (originalType != null) {
                        if (originalType.isArray()) {


                            List<Object> listValues = new ArrayList<>();

                            List dataSet;
                            if (instance instanceof Collection) {
                                dataSet = (List) instance;
                            } else {
                                dataSet = new ArrayList();
                                dataSet.add(instance);
                            }

                            for (Object dataRow : dataSet) {
                                variableExpression = VariableType.setVariableValue(variableExpression, dataRow);
                                listValues.add(variableExpression.getValue());
                            }

                            Class type = DataTypeMapping.getDataTypeMapping(variableDataType);
                            Object[] typedArray = (Object[]) Array.newInstance(type, dataSet.size());
                            Object value = listValues.toArray(typedArray);
                            params[counter] = originalType;
                            values[counter] = value;

                        } else {


                            List dataSet;
                            if (instance instanceof Collection) {
                                dataSet = (List) instance;
                                if (!dataSet.isEmpty()) {
                                    variableExpression = VariableType.setVariableValue(variableExpression, dataSet.get(0));
                                }
                            } else {
                                variableExpression = VariableType.setVariableValue(variableExpression, instance);
                            }



                        /*
                            variableExpression = VariableType.setVariableValue(variableExpression, DATA_ROW);
                        */

                            Object value = variableExpression.getValue();
                            params[counter] = originalType;
                            values[counter] = value;
                        }

                    }

                } catch (Exception e) {
                    throw new InterpretException(e);
                }


            } else if (expression instanceof LiteralExpression) {

                ParamValue paramsValue = buildLiteralParam(expression, originalType);
                params[counter] = paramsValue.param;
                values[counter] = paramsValue.value;
            }

            counter++;

        }


        return invokeFunction(functionExpression, params, values);

    }


    private Object invokeFunction(FunctionExpression functionExpression, Class<?> params[], Object values[]) throws InterpretException {

        try {
            FunctionBinding functionBinding = functionExpression.getBinding();
            String functionName = functionExpression.getName();
            Class<?> functionClass = functionBinding.getFunctionClass();
            Object value;
            if (functionClass != null) {
                Object classInstance = functionClass.newInstance();
                Method method = functionClass.getMethod(functionName, params);
                value = method.invoke(classInstance, values);
            } else {
                Object classInstance = functionBinding.getFunctionInstance();
                Method method = classInstance.getClass().getMethod(functionName, params);
                value = method.invoke(classInstance, values);
            }

            if (value instanceof Collection) {
                Collection collection = (Collection) value;
                return new ListLiteral<>(collection, functionExpression.binding.getReturnType(), collection.size());
            } else {
                return LiteralType.getLiteralExpression(value, functionExpression.binding.getReturnType());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InterpretException(e);
        }

    }


    private ParamValue buildLiteralParam(Expression expression, Class originalType) throws InterpretException {
        ParamValue paramsValue = new ParamValue();
        if (expression instanceof ListLiteral) {
            ListLiteral listLiteral = (ListLiteral) expression;

            try {

                DataType literalDataType = listLiteral.getType().getDataType();

                if (originalType.isArray()) {

                    Class type = DataTypeMapping.getDataTypeMapping(literalDataType);
                    Object[] typedArray = (Object[]) java.lang.reflect.Array.newInstance(type, listLiteral.getSize());
                    Object value = ((List) listLiteral.getValue()).toArray(typedArray);

                    paramsValue.param = originalType;
                    paramsValue.value = value;

                } else {
                    Object value = ((List) listLiteral.getValue()).get(0);
                    paramsValue.param = originalType;
                    paramsValue.value = value;
                }


            } catch (Exception e) {
                throw new InterpretException(e.getMessage(), e);
            }


        } else {
            LiteralExpression literalExpression = (LiteralExpression) expression;
            if (originalType.isArray()) {
                List<Object> listValues = new ArrayList<>();
                listValues.add(literalExpression.getValue());
                Class type = DataTypeMapping.getDataTypeMapping(literalExpression.getType().getDataType());
                Object[] typedArray = (Object[]) java.lang.reflect.Array.newInstance(type, 1);
                Object value = listValues.toArray(typedArray);
                paramsValue.param = originalType;
                paramsValue.value = value;

            } else {
                paramsValue.param = DataTypeMapping.getDataTypeMapping(literalExpression.getType().getDataType());
                if (paramsValue.param == null) {
                    paramsValue.param = originalType;
                }
                paramsValue.value = literalExpression.getValue();

            }


        }
        return paramsValue;
    }

    @Override
    public String toString() {

        if (this.leftOperand instanceof FunctionExpression) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("function");
            stringBuilder.append("(");

            FunctionExpression functionExpression = (FunctionExpression) this.leftOperand;


            stringBuilder.append(functionExpression.getName());
            stringBuilder.append(", ");

            for (Expression expression : functionExpression.getParameters()) {
                stringBuilder.append(expression);
                stringBuilder.append(", ");
            }

            stringBuilder.append(")");
            return stringBuilder.toString();
        }
        return super.toString();
    }

    private class ParamValue {
        Class<?> param;
        Object value;
    }

}