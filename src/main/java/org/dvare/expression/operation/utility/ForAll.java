/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.list.ValuesOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.FORALL)
public class ForAll extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(ForAll.class);

    protected Expression referenceContext;
    protected NamedExpression derivedContext;


    public ForAll() {
        super(OperationType.FORALL);
    }

    public ForAll(OperationType operationType) {
        super(operationType);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contextsBinding) throws ExpressionParseException {

        String derivedContextsBinding = null;

        pos++;

        if (pos + 2 < tokens.length && tokens[pos + 2].equals("|")) {

            referenceContext = new NamedExpression(tokens[pos++]);
            derivedContext = new NamedExpression(tokens[pos++]);

        } else if (pos + 1 < tokens.length && tokens[pos + 1].equals("|")) {
            referenceContext = new NamedExpression("self");
            derivedContext = new NamedExpression(tokens[pos]);

        }
        pos++;


        TokenType tokenType = findDataObject(((NamedExpression) referenceContext).getName(), contextsBinding);

        if (tokenType.type != null && contextsBinding.getContext(tokenType.type) != null &&
                TypeFinder.findType(tokenType.token, contextsBinding.getContext(tokenType.type)) != null) {


            DataType variableType = TypeFinder.findType(tokenType.token, contextsBinding.getContext(tokenType.type));
            VariableExpression variableExpression = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);

            derivedContextsBinding = variableExpression.getName();
            referenceContext = variableExpression;


            String parts[] = derivedContext.getName().split(":");
            if (parts.length == 2) {

                String name = parts[0].trim();
                String type = parts[1].trim();


                DataType dataType = DataType.valueOf(type);

                TypeBinding typeBinding = new TypeBinding();                             // new context
                typeBinding.addTypes(name, dataType);
                contextsBinding.addContext(derivedContextsBinding, typeBinding);

                derivedContext = new NamedExpression(name);
            }


        } else {

            TypeBinding typeBinding = contextsBinding.getContext(((NamedExpression) referenceContext).getName());
            if (contextsBinding.getContext(((NamedExpression) referenceContext).getName()) != null) {

                derivedContextsBinding = derivedContext.getName();

                contextsBinding.addContext(derivedContextsBinding, typeBinding);

            }
        }

        pos = findNextExpression(tokens, pos, stack, contextsBinding);

        this.leftOperand = stack.pop();

        contextsBinding.removeContext(derivedContextsBinding);


        if (logger.isDebugEnabled()) {
            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());

        }
        stack.push(this);


        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack
            , ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;


        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {
                if (op.getClass().equals(EndForAll.class) || op.getClass().equals(EndForEach.class)) {
                    return pos;
                } else if (!op.getClass().equals(LeftPriority.class)) {
                    pos = op.parse(tokens, pos, stack, contexts);
                }
            } else {


                stack.add(buildExpression(token, contexts));

            }
        }

        throw new ExpressionParseException("Function Closing Bracket Not Found");
    }


    @Override
    public LiteralExpression interpret(
            InstancesBinding instancesBinding) throws InterpretException {

        if (derivedContext != null) {
            if (referenceContext instanceof NamedExpression) {
                Object object = instancesBinding.getInstance(((NamedExpression) referenceContext).getName());
                if (object instanceof List) {
                    String derivedInstanceBinding = derivedContext.getName();

                    List instances = (List) object;

                    List<Boolean> results = new ArrayList<>();

                    for (Object instance : instances) {
                        instancesBinding.addInstance(derivedInstanceBinding, instance);

                        LiteralExpression result = leftOperand.interpret(instancesBinding);
                        results.add(toBoolean(result));

                    }

                    instancesBinding.removeInstance(derivedInstanceBinding);
                    Boolean result = results.stream().allMatch(Boolean::booleanValue);
                    return LiteralType.getLiteralExpression(result, BooleanType.class);

                }

            } else if (referenceContext instanceof VariableExpression) {

                String derivedInstanceBinding = ((VariableExpression) referenceContext).getName();


                ValuesOperation valuesOperation = new ValuesOperation();
                valuesOperation.setLeftOperand(referenceContext);
                Object interpret = valuesOperation.interpret(instancesBinding);

                if (interpret instanceof ListLiteral) {
                    List<Boolean> results = new ArrayList<>();
                    List<?> values = ((ListLiteral) interpret).getValue();
                    for (Object value : values) {

                        DataRow instance = new DataRow();
                        instance.addData(derivedContext.getName(), value);

                        instancesBinding.addInstance(derivedInstanceBinding, instance);

                        LiteralExpression result = leftOperand.interpret(instancesBinding);
                        results.add(toBoolean(result));

                    }

                    instancesBinding.removeInstance(derivedInstanceBinding);
                    Boolean result = results.stream().allMatch(Boolean::booleanValue);
                    return LiteralType.getLiteralExpression(result, BooleanType.class);
                }


                instancesBinding.removeInstance(derivedInstanceBinding);

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