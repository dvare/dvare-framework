package org.dvare.parser;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.BooleanExpression;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.CompositeOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.DataTypeMapping;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ExpressionParser {
    private static final Logger logger = LoggerFactory.getLogger(ExpressionParser.class);

    private final ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

    public static TypeBinding translate(String types) {
        TypeBinding typeBinding = new TypeBinding();


        if (types != null) {

            if (types.startsWith("{")) {
                types = types.substring(1);
            }
            if (types.endsWith("}")) {
                types = types.substring(0, types.length() - 1);
            }
            String[] variables = types.split(",");

            for (String variable : variables) {
                if (variable != null && variable.contains(":")) {
                    String[] variableTokens = variable.split(":");
                    String name = variableTokens[0].trim();
                    String type = variableTokens[1].trim();
                    typeBinding.addTypes(name, DataType.valueOf(type));
                }
            }
        }
        return typeBinding;
    }

    public static TypeBinding translate(Map<String, String> types) {
        TypeBinding typeBinding = new TypeBinding();
        for (String name : types.keySet()) {
            DataType dataType = DataType.valueOf(types.get(name));
            typeBinding.addTypes(name, dataType);
        }
        return typeBinding;
    }

    public static TypeBinding translate(Class<?> types) {
        TypeBinding typeBinding = new TypeBinding();


        Field[] fields = FieldUtils.getAllFields(types);
        for (Field field : fields) {
            String type = field.getType().getSimpleName();
            String name = field.getName();
            DataType dataType = DataTypeMapping.getTypeMapping(type);
            if (dataType != null) {
                typeBinding.addTypes(name, dataType);
            } else {


                if (field.getType().equals(List.class)) {
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) genericType;
                        Class<?> parameterizeClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                        dataType = DataTypeMapping.getTypeMapping(parameterizeClass);
                        if (dataType != null) {
                            dataType = DataTypeMapping.getTypeMapping(parameterizeClass.getSimpleName() + "[]");
                            if (dataType != null) {
                                typeBinding.addTypes(name, dataType);
                            }
                        } else {
                            typeBinding.addTypes(name, parameterizeClass);
                        }


                    } else {

                        typeBinding.addTypes(name, field.getType());
                    }

                }

            }

        }
        return typeBinding;
    }


    public Expression fromString(String expr, Map<String, String> types) throws ExpressionParseException {
        TypeBinding typeBinding = translate(types);
        return fromString(expr, typeBinding);
    }


    public Expression fromString(String expr, String types) throws ExpressionParseException {
        TypeBinding typeBinding = translate(types);
        return fromString(expr, typeBinding);
    }


    public Expression fromString(String expr, Class<?> type) throws ExpressionParseException {

        TypeBinding typeBinding = translate(type);
        return fromString(expr, typeBinding);
    }


    public Expression fromString(String expr, TypeBinding typeBinding) throws ExpressionParseException {
        ContextsBinding contextsBinding = new ContextsBinding();
        contextsBinding.addContext("self", typeBinding);

        return fromString(expr, contextsBinding);
    }


    public Expression fromString(String expr, ContextsBinding contexts) throws ExpressionParseException {

        if (expr != null && !expr.isEmpty()) {
            Stack<Expression> stack = new Stack<>();

            String[] tokens = ExpressionTokenizer.toToken(expr);

            if (tokens.length == 1) {
                if (tokens[0].equalsIgnoreCase("true")) {
                    return new BooleanExpression("true", true);
                } else if (tokens[0].equalsIgnoreCase("false")) {
                    return new BooleanExpression("false", false);
                }

            }
            for (int pos = 0; pos < tokens.length - 1; pos++) {
                String token = tokens[pos];
                OperationExpression op = configurationRegistry.getOperation(token);
                if (op != null) {
                    pos = op.parse(tokens, pos, stack, contexts);
                } else {


                    OperationExpression.TokenType tokenType = OperationExpression.findDataObject(token, contexts);
                    if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                        TypeBinding typeBinding = contexts.getContext(tokenType.type);
                        DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                        VariableExpression<?> variableExpression = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);
                        stack.add(variableExpression);
                    } else {
                        LiteralExpression<?> literalExpression = LiteralType.getLiteralExpression(token);
                        stack.push(literalExpression);
                    }


                }
            }
            if (stack.empty()) {
                throw new ExpressionParseException("Unable to Parse Expression");
            }

            Expression expression;

            if (stack.size() > 1) {

                expression = new CompositeOperationExpression(stack);

            } else {
                expression = stack.pop();
            }


            return expression;

        } else {
            String message = "Expression is null or Empty";
            logger.error(message);
            throw new ExpressionParseException(message);
        }

    }

}