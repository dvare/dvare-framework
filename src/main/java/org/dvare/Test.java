package org.dvare;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

    public static void main(String args[]) {

        String otherPatten = ".{1,}\\..{1,}";
        Pattern pattern = Pattern.compile(otherPatten);
        Matcher matcher = pattern.matcher("datahammad");

        if (matcher.find()) {

            System.out.println(matcher.groupCount());

            String nameStr = matcher.group(0);
            System.out.println(nameStr);
        }


    }

}
