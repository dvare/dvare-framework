package org.dvare.config;

import org.dvare.binding.function.FunctionBinding;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.util.InstanceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */

public enum ConfigurationRegistry {

    INSTANCE;

    private final Map<String, FunctionBinding> functions = new HashMap<>();

    private final Map<String, Class<? extends OperationExpression>> operations = new HashMap<>();

    public List<String> tokens() {
        return new ArrayList<>(operations.keySet());
    }

    public void registerOperation(Class<? extends OperationExpression> op, List<String> symbols) {
        for (String symbol : symbols) {
            if (!operations.containsKey(symbol)) {
                operations.put(symbol, op);
            }
        }
    }

    public void registerFunction(FunctionBinding binding) {
        // if (!functions.containsKey(binding.getMethodName()))
        functions.put(binding.getMethodName(), binding);
    }

    public OperationExpression getOperation(String symbol) {
        Class<? extends OperationExpression> operationExpression = operations.get(symbol);
        if (operationExpression != null) {
            try {
                return new InstanceUtils<OperationExpression>().newInstance(operationExpression);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public FunctionBinding getFunction(String name) {
        FunctionBinding functionBinding = this.functions.get(name);
        if (functionBinding != null) {
            return functionBinding.copy();
        }
        return null;
    }

    public List<String> getFunctionNames() {
        return new ArrayList<>(functions.keySet());
    }
}
