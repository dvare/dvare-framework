package org.dvare.test.list;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FirstTest.class,
        ForAllOperationTest.class,
        ForEachOperationTest.class,
        GetItemTest.class,
        HasItemTest.class,
        ItemPositionTest.class,
        LastTest.class,
        ListLiteralOperationTest.class,
        ListVariableTest.class,
        MatchTest.class,
        PairListTest.class,
        SizeTest.class,
        ValuesTest.class,
        FilterTest.class,
        MapTest.class,
})
public class ListTest {
}
