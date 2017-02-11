package org.dvare.rules.test.validation.dataobjects;

import java.util.ArrayList;
import java.util.List;

public class ForEachOperation {
    public String Variable1;
    public Integer Variable2;
    public ArithmeticOperation Variable3;
    public List<ArithmeticOperation> Variable4 = new ArrayList<>();


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

    public ArithmeticOperation getVariable3() {
        return Variable3;
    }

    public void setVariable3(ArithmeticOperation variable3) {
        Variable3 = variable3;
    }

    public List<ArithmeticOperation> getVariable4() {
        return Variable4;
    }

    public void setVariable4(List<ArithmeticOperation> variable4) {
        Variable4 = variable4;
    }

}
