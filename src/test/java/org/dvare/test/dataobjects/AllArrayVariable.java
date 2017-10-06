/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.test.dataobjects;


import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class AllArrayVariable {

    public String[] Variable1;
    public Integer[] Variable2;
    public Float[] Variable3;
    public Boolean[] Variable4;
    public Date[] Variable5;
    public LocalDate[] Variable6;
    public LocalDateTime[] Variable7;
    public Pair[] Variable8;

    public String[] getVariable1() {
        return Variable1;
    }

    public void setVariable1(String[] variable1) {
        Variable1 = variable1;
    }

    public Integer[] getVariable2() {
        return Variable2;
    }

    public void setVariable2(Integer[] variable2) {
        Variable2 = variable2;
    }

    public Float[] getVariable3() {
        return Variable3;
    }

    public void setVariable3(Float[] variable3) {
        Variable3 = variable3;
    }

    public Boolean[] getVariable4() {
        return Variable4;
    }

    public void setVariable4(Boolean[] variable4) {
        Variable4 = variable4;
    }

    public Date[] getVariable5() {
        return Variable5;
    }

    public void setVariable5(Date[] variable5) {
        Variable5 = variable5;
    }

    public LocalDate[] getVariable6() {
        return Variable6;
    }

    public void setVariable6(LocalDate[] variable6) {
        Variable6 = variable6;
    }

    public LocalDateTime[] getVariable7() {
        return Variable7;
    }

    public void setVariable7(LocalDateTime[] variable7) {
        Variable7 = variable7;
    }

    public Pair[] getVariable8() {
        return Variable8;
    }

    public void setVariable8(Pair[] variable8) {
        Variable8 = variable8;
    }
}
