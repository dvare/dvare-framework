package org.dvare.test.spring;

import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.rule.RuleBinding;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.spring.DvareConfigFactoryBean;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringTest {

    @Test
    public void testApp() throws ExpressionParseException, InterpretException {

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringTest.class);

        RuleConfiguration configuration = context.getBean(RuleConfiguration.class);

        Expression expression = configuration.getParser().
                fromString("false ->toBoolean()", new ContextsBinding());

        RuleBinding rule = new RuleBinding(expression);

        boolean result = (Boolean) configuration.getEvaluator().evaluate(rule, null);

        Assert.assertFalse(result);


    }

    @Bean
    public DvareConfigFactoryBean ruleConfiguration() {
        return new DvareConfigFactoryBean(new String[]{"org.dvare.util"});
    }

}
