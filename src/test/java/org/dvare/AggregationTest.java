package org.dvare;

import org.dvare.test.aggregation.*;
import org.dvare.test.list.SizeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
     /*   FunctionTestExclude.class,*/
        SumTest.class,
        MinTest.class,
        MaxTest.class,
        MeanTest.class,
        ModeTest.class,
        MedianTest.class,
        SemicolonTest.class,
        ConditionTest.class,
        SizeTest.class,
        ConditionChainTest.class})
public class AggregationTest {

}
