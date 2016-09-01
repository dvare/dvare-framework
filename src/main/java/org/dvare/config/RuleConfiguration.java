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


package org.dvare.config;


import org.dvare.binding.function.FunctionBinding;
import org.dvare.evaluator.AggregationRuleEvaluator;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.parser.AggregationExpressionParser;
import org.dvare.parser.ExpressionParser;

public class RuleConfiguration {

    private static RuleConfigurationProvider configurationProvider;
    private String[] functionBasePackages;
    private ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
    private ExpressionParser expressionParser;
    private AggregationExpressionParser aggregationExpressionParser;

    public RuleConfiguration() {
        this(null);
    }

    public RuleConfiguration(String[] functionBasePackages) {
        this.functionBasePackages = functionBasePackages;
        if (configurationProvider == null) {
            configurationProvider = new RuleConfigurationProvider(configurationRegistry, functionBasePackages);
        }
    }

    public RuleEvaluator getEvaluator() {
        return new RuleEvaluator();
    }

    public AggregationRuleEvaluator getAggregationEvaluator() {
        return new AggregationRuleEvaluator();
    }


    public ExpressionParser getParser() {
        if (expressionParser == null) {
            expressionParser = new ExpressionParser(this);
        }
        return expressionParser;
    }

    public AggregationExpressionParser getAggregationParser() {
        if (aggregationExpressionParser == null) {
            aggregationExpressionParser = new AggregationExpressionParser(this);
        }
        return aggregationExpressionParser;
    }

    public ConfigurationRegistry getConfigurationRegistry() {
        return configurationRegistry;
    }

    public void registerFunction(FunctionBinding binding) {
        configurationRegistry.registerFunction(binding);
    }

    public String[] getFunctionBasePackages() {
        return functionBasePackages;
    }

    public void setFunctionBasePackages(String[] functionBasePackages) {
        this.functionBasePackages = functionBasePackages;
    }

}
