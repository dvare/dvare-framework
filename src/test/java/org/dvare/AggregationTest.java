package org.dvare;

import org.dvare.test.aggregation.*;
import org.dvare.test.list.FirstTest;
import org.dvare.test.list.LastTest;
import org.dvare.test.list.LengthTest;
import org.dvare.test.list.ValuesTest;
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
        LengthTest.class,
        ConditionChainTest.class,
        ValuesTest.class,
        DistanceTest.class})
public class AggregationTest {

}
