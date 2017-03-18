package org.dvare;

import org.dvare.test.list.FirstTest;
import org.dvare.test.list.LastTest;
import org.dvare.test.list.PairTest;
import org.dvare.test.list.ValuesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ValuesTest.class,
        PairTest.class,
        FirstTest.class,
        LastTest.class
})
public class ListTest {
}
