package org.dvare;

import org.dvare.test.list.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ValuesTest.class,
        PairTest.class,
        FirstTest.class,
        LastTest.class,
        MatchTest.class
})
public class ListTest {
}
