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


package org.dvare.spring;

import org.dvare.config.RuleConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DvareConfigFactoryBean implements FactoryBean<RuleConfiguration>, ApplicationContextAware {
    private String[] functionPackages;
    private ApplicationContext applicationContext;
    private AutowireCapableBeanFactory autowireCapableBeanFactory;


    public DvareConfigFactoryBean(String[] functionBasePackages) {
        this.functionPackages = functionPackages;
    }

    public DvareConfigFactoryBean() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
    }

    @Override
    public RuleConfiguration getObject() throws Exception {

        return new RuleConfiguration(functionPackages);
    }


    @Override
    public Class<RuleConfiguration> getObjectType() {
        return RuleConfiguration.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public String[] getFunctionPackages() {
        return functionPackages;
    }

    public void setFunctionPackages(String[] functionPackages) {
        this.functionPackages = functionPackages;
    }
}
