package org.dvare.test.relational;

import org.dvare.test.utility.ParenthesisTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        EqualOperationTest.class,
        InOperationTest.class,
        BetweenOperationTest.class,
        ParenthesisTest.class,
        DateRelationalTest.class,
        GenericRelationalTest.class,
})
public class RelationalTest {

}