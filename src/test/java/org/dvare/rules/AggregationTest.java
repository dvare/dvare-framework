package org.dvare.rules;

import org.dvare.rules.test.aggregation.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
     /*   FunctionTestExclude.class,*/
        SumTest.class,
        MinTest.class,
        MaxTest.class,
        FirstTest.class,
        LastTest.class,
        MeanTest.class,
        ModeTest.class,
        MedianTest.class,
        SemicolonTest.class,
        ConditionTest.class,
        CountTest.class,

        DistanceTest.class})
public class AggregationTest {

}
