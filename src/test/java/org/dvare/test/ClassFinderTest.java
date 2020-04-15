package org.dvare.test;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.ClassFinder;
import org.dvare.annotations.Type;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ClassFinderTest {

    @Test
    public void test1() {

        List<?> classes = ClassFinder.find("org.dvare.annotations");
        Assert.assertTrue(classes.contains(ClassFinder.class));

    }

    @Test(expected = IllegalArgumentException.class)
    public void test2() {
        ClassFinder.find("com.net");
    }


    @Test
    public void test3() {
        List<?> classes = ClassFinder.findAnnotated("org.dvare.expression.datatype", Type.class);
        Assert.assertFalse(classes.isEmpty());
    }

    @Test
    public void test4() {
        List classes = ClassFinder.find(
                "org.apache.commons.lang3.tuple");
        Assert.assertTrue(classes.contains(Pair.class));
    }


}
