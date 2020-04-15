package org.dvare;


import org.dvare.test.ClassFinderTest;
import org.dvare.test.aggregation.AggregationTest;
import org.dvare.test.arithmetic.ArithmeticTest;
import org.dvare.test.builder.BuilderTest;
import org.dvare.test.flow.FlowTest;
import org.dvare.test.list.ListTest;
import org.dvare.test.predefined.PredefinedTest;
import org.dvare.test.relational.RelationalTest;
import org.dvare.test.utility.UtilityTest;
import org.dvare.test.variable.VariableTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        AggregationTest.class,
        ArithmeticTest.class,
        BuilderTest.class,
        FlowTest.class,
        ListTest.class,
        PredefinedTest.class,
        RelationalTest.class,
        UtilityTest.class,
        VariableTest.class,
        ClassFinderTest.class,
})
public class AppTest {


}



