package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.util.ClassUtils;
import org.dvare.util.DataTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.INVOKE)
public class Invoke extends Function {
    private static final Logger logger = LoggerFactory.getLogger(Invoke.class);


    public Invoke() {
        super(OperationType.INVOKE);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contexts);


        if (logger.isDebugEnabled()) {

            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());

        }
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

            if (token.contains("#")) {

                String[] parts = token.split("#");
                String variable = parts[0];

                rightOperand = buildExpression(variable, contexts, pos, tokens);

                String functionName = parts[1];

                FunctionBinding functionBinding = new FunctionBinding(functionName, null, null, null, null);
                leftOperand = new FunctionExpression(functionName, functionBinding);


            } else if (op != null) {

                if (op.getClass().equals(RightPriority.class)) {


                    List<Expression> expressions = new ArrayList<>(localStack);

                    if (leftOperand != null) {
                        FunctionExpression functionExpression = (FunctionExpression) leftOperand;
                        List<Expression> parameters = new ArrayList<>(expressions);
                        functionExpression.setParameters(parameters);
                        stack.push(functionExpression);
                    } else {
                        throw new ExpressionParseException("Function Name not Found");
                    }

                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, localStack, contexts);
                }
            } else {


                localStack.add(buildExpression(token, contexts, pos, tokens));

            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        Object value = null;
        if (rightOperand instanceof VariableExpression) {

            VariableExpression<?> variableExpression = (VariableExpression<?>) rightOperand;
            LiteralExpression<?> literalExpression = variableExpression.interpret(instancesBinding);
            value = literalExpression.getValue();

        } else if (rightOperand instanceof LiteralExpression<?>) {
            value = ((LiteralExpression<?>) rightOperand).getValue();
        }

        if (value != null) {

            Class<?> functionClass = value.getClass();

            FunctionExpression functionExpression = (FunctionExpression) this.leftOperand;
            FunctionBinding functionBinding = functionExpression.getBinding();

            functionBinding.setFunctionClass(functionClass);
            functionBinding.setFunctionInstance(value);


            Method method = ClassUtils.getAccessibleMethod(functionClass, functionBinding.getMethodName());

            if (method == null) {
                List<Class<?>> parameters = new ArrayList<>();
                for (Expression expression : functionExpression.getParameters()) {
                    LiteralExpression<?> literalExpression = expression.interpret(instancesBinding);
                    Object value1 = literalExpression.getValue();
                    parameters.add(value1.getClass());
                }

                Class<?>[] params = parameters.toArray(new Class[0]);

                method = ClassUtils.getAccessibleMethod(functionClass, functionBinding.getMethodName(), params);

                if (method == null) {
                    for (int i = 0; i < params.length; i++) {

                        if (!params[i].isPrimitive()) {
                            try {
                                Class<?> aClass = (Class<?>) ClassUtils.readStaticField(params[i], "TYPE", true);
                                params[i] = aClass;
                            } catch (IllegalAccessException ignored) {
                            }
                        }
                    }
                    method = ClassUtils.getAccessibleMethod(functionClass, functionBinding.getMethodName(), params);
                }
            }

            if (method != null) {
                List<DataType> parameters = new ArrayList<>();
                for (Class<?> type : method.getParameterTypes()) {
                    DataType dataType = DataTypeMapping.getTypeMapping(type);
                    parameters.add(dataType);
                }
                functionBinding.setParameters(parameters);
                Class<?> returnClass = method.getReturnType();
                if (!returnClass.equals(Void.class)) {

                    DataType returnType = DataTypeMapping.getTypeMapping(returnClass);

                    Class<? extends DataTypeExpression> dataTypeExpressionClass = DataTypeMapping.getDataTypeClass(returnType);
                    if (dataTypeExpressionClass != null) {

                        functionBinding.setReturnType(dataTypeExpressionClass);

                    }

                }
            } else {
                throw new InterpretException("Method Param not match");
            }


        }
        return interpretFunction(instancesBinding);

    }


}