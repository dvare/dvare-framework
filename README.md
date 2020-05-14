## Dvare Framework [![Build Status](https://travis-ci.org/dvare/dvare-framework.svg?branch=master)](https://travis-ci.org/dvare/dvare-framework) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4c684251c3984e33a88bbfd0cd4e4df8)](https://www.codacy.com/app/hammadirshad/dvare-framework?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dvare/dvare-framework&amp;utm_campaign=Badge_Grade) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.dvare/dvare-framework/badge.svg?style=flat)](http://search.maven.org/#artifactdetails|org.dvare|dvare-framework|3.0|) [![Javadocs](http://www.javadoc.io/badge/org.dvare/dvare-framework.svg)](http://www.javadoc.io/doc/org.dvare/dvare-framework) [![Coverage Status](https://coveralls.io/repos/github/dvare/dvare-framework/badge.svg?branch=master)](https://coveralls.io/github/dvare/dvare-framework?branch=master)
A Lightweight Java business rule expression language.


## Example

```java
public class ArithmeticOperationTest {
    
    public void testApp() throws ExpressionParseException, InterpretException {

        RuleConfiguration factory = new RuleConfiguration();
        String expr = "Variable5->substring(2,2)->toInteger() between [80,90] and" +
         " Variable5->substring(3,2)->toInteger() in [45,46]";
        Expression expression = factory.getParser().fromString(expr, ArithmeticOperation.class);
        RuleBinding rule = new RuleBinding(expression);
        ArithmeticOperation arithmeticOperation = new ArithmeticOperation();
        arithmeticOperation.setVariable5("D845");

        RuleEvaluator evaluator = factory.getEvaluator();
        boolean result = (Boolean) evaluator.evaluate(rule, arithmeticOperation);
        assertTrue(result);
    }
 }
```

## Current version

* The current stable version is `3.0` : 

In order to use snapshot versions, you need to add the following maven repository in your `pom.xml`:

 Maven dependency:
```xml
<dependencies>
        <dependency>
            <groupId>org.dvare</groupId>
            <artifactId>dvare-framework</artifactId>
            <version>3.0</version>
        </dependency>         
</dependencies>
```

## License
Dvare Framework  is released under the [![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT).

```
The MIT License (MIT)

Copyright (c) 2016-2019 DVARE (Data Validation and Aggregation Rule Engine)

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
THE SOFTWARE.
```

