package org.dvare.test.aggregation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        MaxTest.class,
        MinTest.class,
        ModeTest.class,
        MedianTest.class,
        MeanTest.class,
        SumTest.class,

})
public class AggregationTest {

}
