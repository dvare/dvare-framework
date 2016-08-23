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


package com.dvare.config;

import com.dvare.binding.function.FunctionBinding;
import com.dvare.expression.operation.validation.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ConfigurationRegistry {

    INSTANCE;

    private final Map<String, FunctionBinding> functions = new HashMap<String, FunctionBinding>();

    private final Map<String, Operation> validationOperations = new HashMap<String, Operation>();


    public List<String> tokens() {
        List<String> tokens = new ArrayList<>();
        for (String key : validationOperations.keySet()) {
            tokens.add(key);
        }
        return tokens;
    }


    public void registerValidationOperation(Operation op) {
        for (String symbol : op.getSymbols()) {
            if (!validationOperations.containsKey(symbol))
                validationOperations.put(symbol, op);
        }
    }


    public void registerFunction(FunctionBinding binding) {
        if (!functions.containsKey(binding.getMethodName()))
            functions.put(binding.getMethodName(), binding);
    }

    public Operation getValidationOperation(String symbol) {
        return this.validationOperations.get(symbol);
    }


    public FunctionBinding getFunction(String name) {
        return this.functions.get(name);
    }


    public List<String> getfunctionNames() {
        List<String> tablenames = new ArrayList<>();
        for (String key : functions.keySet()) {
            tablenames.add(key);
        }
        return tablenames;
    }
}
