package org.dvare.test;

import org.dvare.annotations.ClassFinder;
import org.dvare.annotations.Type;
import org.dvare.util.FunctionProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ClassFinderTest {

    @Test
    public void test1() {

        List<?> classes = ClassFinder.find("org.dvare.annotations");
        Assertions.assertTrue(classes.contains(ClassFinder.class));

    }

    @Test
    public void test2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ClassFinder.find("com.net");
        });

    }


    @Test
    public void test3() {
        List<?> classes = ClassFinder.findAnnotated("org.dvare.expression.datatype", Type.class);
        Assertions.assertFalse(classes.isEmpty());
    }

    @Test
    public void test4() {

        List<?> classes = ClassFinder.find(
                "org.dvare.util");
        Assertions.assertTrue(classes.contains(FunctionProvider.class));
    }


}
