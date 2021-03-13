package org.dvare.config;


import org.dvare.binding.function.FunctionBinding;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.parser.ExpressionParser;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */

public class RuleConfiguration {

    private final ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

    private ExpressionParser expressionParser;


    public RuleConfiguration(String... functionBasePackages) {
        new RuleConfigurationProvider(functionBasePackages).init();
    }


    public RuleEvaluator getEvaluator() {
        return new RuleEvaluator();
    }


    public ExpressionParser getParser() {
        if (expressionParser == null) {
            expressionParser = new ExpressionParser();
        }
        return expressionParser;
    }


    public void registerFunction(FunctionBinding binding) {
        configurationRegistry.registerFunction(binding);
    }


    public ConfigurationRegistry getConfigurationRegistry() {
        return configurationRegistry;
    }


}
