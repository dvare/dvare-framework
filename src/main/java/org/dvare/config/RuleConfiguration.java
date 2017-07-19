/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.evaluator.RuleEvaluator;
import org.dvare.parser.ExpressionParser;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class RuleConfiguration {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(RuleConfiguration.class);
    private ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

    private ExpressionParser expressionParser;


    public RuleConfiguration() {
        this(null);
    }


    public RuleConfiguration(String[] functionBasePackages) {
        this(functionBasePackages, false);
    }


    public RuleConfiguration(String[] functionBasePackages, boolean silentMode) {

        if (silentMode) {
            try {

                Class.forName("org.apache.log4j.LogManager");
                List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
                loggers.add(LogManager.getRootLogger());
                for (Logger logger : loggers) {
                    if (logger.getName().startsWith("org.dvare") || logger.getName().equals("root")) {
                        logger.setLevel(Level.OFF);
                    }
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
        }


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
