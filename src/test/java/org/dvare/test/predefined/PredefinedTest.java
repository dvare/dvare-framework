package org.dvare.test.predefined;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        DatePredefinedTest.class,
        PredefinedOperationTest.class,
        PredefinedStringOperationTest.class,
        ToIntegerTest.class,

})
public class PredefinedTest {
}
