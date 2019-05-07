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
package org.dvare.test.dataobjects;

import java.time.LocalDate;

public class ListTestModel {

    public String Variable1;
    public Integer Variable2;
    public Float Variable3;
    public LocalDate Variable4;


    public String getVariable1() {
        return Variable1;
    }

    public void setVariable1(String variable1) {
        Variable1 = variable1;
    }

    public Integer getVariable2() {
        return Variable2;
    }

    public void setVariable2(Integer variable2) {
        Variable2 = variable2;
    }

    public Float getVariable3() {
        return Variable3;
    }

    public void setVariable3(Float variable3) {
        Variable3 = variable3;
    }

    public LocalDate getVariable4() {
        return Variable4;
    }

    public void setVariable4(LocalDate variable4) {
        Variable4 = variable4;
    }
}
