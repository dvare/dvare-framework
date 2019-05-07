/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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

        List classes = ClassFinder.find(
                "org.dvare.annotations");
        Assert.assertTrue(classes.contains(ClassFinder.class));

    }

    @Test(expected = IllegalArgumentException.class)
    public void test2() {
        ClassFinder.find("com.net");
    }


    @Test
    public void test3() {
        List classes = ClassFinder.findAnnotated(
                "org.dvare.expression.datatype", Type.class);
        Assert.assertTrue(!classes.isEmpty());
    }

    @Test
    public void test4() {
        List classes = ClassFinder.find(
                "org.apache.commons.lang3.tuple");
        Assert.assertTrue(classes.contains(Pair.class));
    }


}
