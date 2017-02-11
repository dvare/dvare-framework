/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.parser;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.BooleanExpression;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ExpressionParser {
    static Logger logger = LoggerFactory.getLogger(ExpressionParser.class);

    private ConfigurationRegistry configurationRegistry = null;

    public ExpressionParser(RuleConfiguration ruleConfiguration) {
        configurationRegistry = ruleConfiguration.getConfigurationRegistry();
    }

    public static TypeBinding translate(Class types) {
        TypeBinding typeBinding = new TypeBinding();

        Field fields[] = FieldUtils.getAllFields(types);
        for (Field field : fields) {
            String type = field.getType().getSimpleName();
            String name = field.getName();
            DataType dataType = DataTypeMapping.getTypeMapping(type);
            if (dataType != null) {
                typeBinding.addTypes(name, dataType);
            } else {

                Type genericType = field.getGenericType();
                if (genericType != null && genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Class<?> parameterizedClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];


                    typeBinding.addTypes(name, parameterizedClass);

                } else {

                    typeBinding.addTypes(name, field.getType());
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

    public Expression fromString(String expr, HashMap<String, String> type) throws ExpressionParseException {
        TypeBinding typeBinding = translate(type);
        return fromString(expr, typeBinding);
    }

    public Expression fromString(String expr, Class type) throws ExpressionParseException {

        TypeBinding typeBinding = translate(type);
        return fromString(expr, typeBinding);
    }

    public Expression fromString(String expr, TypeBinding typeBinding) throws ExpressionParseException {
        return fromString(expr, typeBinding, null);
    }

    public Expression fromString(String expr, Map<String, String> aTypes, Map<String, String> vTypes) throws ExpressionParseException {
        TypeBinding vTypeBinding = ExpressionParser.translate(vTypes);
        TypeBinding aTypeBinding = ExpressionParser.translate(aTypes);
        return fromString(expr, aTypeBinding, vTypeBinding);
    }

    public Expression fromString(String expr, Class atype, Class vtype) throws ExpressionParseException {

        TypeBinding aTypes = ExpressionParser.translate(atype);
        TypeBinding vTypes = ExpressionParser.translate(vtype);
        return fromString(expr, aTypes, vTypes);
    }

    public Expression fromString(String expr, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        ContextsBinding contexts = new ContextsBinding();
        contexts.addContext("self", selfTypes);
        contexts.addContext("data", dataTypes);
        return fromString(expr, contexts);
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
            for (int i = 0; i < tokens.length - 1; i++) {
                String token = tokens[i];
                OperationExpression op = configurationRegistry.getOperation(token);
                if (op != null) {
                    i = op.parse(tokens, i, stack, contexts);
                } else {


                    OperationExpression.TokenType tokenType = OperationExpression.findDataObject(token, contexts);
                    if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                        TypeBinding typeBinding = contexts.getContext(tokenType.type);
                        DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                        VariableExpression variableExpression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);
                        stack.add(variableExpression);
                    }


                }
            }
            if (stack.empty()) {
                throw new ExpressionParseException("Unable to Parse Expression");
            }
            Expression expression = stack.pop();

           /* if (expression instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) expression;
                Node<String> root = operation.AST();
                TreePrinter.printNode(root);
            }*/
            return expression;

        } else {
            String message = String.format("Expression is null or Empty");
            logger.error(message);
            throw new ExpressionParseException(message);
        }

    }

}