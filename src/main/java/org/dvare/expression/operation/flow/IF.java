package org.dvare.expression.operation.flow;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.BooleanLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.ConditionOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

@Operation(type = OperationType.IF)
public class IF extends ConditionOperationExpression {
    static Logger logger = LoggerFactory.getLogger(IF.class);


    public IF() {
        super(OperationType.IF);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];
            OperationExpression operation = configurationRegistry.getOperation(token);
            if (operation != null) {


                if (operation instanceof IF || operation instanceof ELSE || operation instanceof THEN || operation instanceof ENDIF) {


                    if (operation instanceof THEN) {
                        pos = operation.parse(tokens, pos, stack, contexts);
                        this.thenOperand = stack.pop();


                    } else if (operation instanceof ELSE) {
                        pos = operation.parse(tokens, pos, stack, contexts);
                        this.elseOperand = stack.pop();

                        if (elseOperand instanceof ConditionOperationExpression) {
                            return pos;
                        }

                    } else if (operation instanceof ENDIF) {
                        return pos;
                    }

                } else {
                    for (; pos < tokens.length; pos++) {
                        token = tokens[pos];
                        operation = configurationRegistry.getOperation(token);

                        if (operation instanceof THEN) {
                            this.condition = stack.pop();
                            pos--;
                            break;
                        } else if (operation != null) {
                            pos = operation.parse(tokens, pos, stack, contexts);
                        }
                    }
                }
            }
        }
        return pos;
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {

        Boolean result = toBoolean(condition.interpret(instancesBinding));
        if (result) {
            return thenOperand.interpret(instancesBinding);
        } else if (elseOperand != null) {
            return elseOperand.interpret(instancesBinding);
        }
        return new BooleanLiteral(result);
    }


}