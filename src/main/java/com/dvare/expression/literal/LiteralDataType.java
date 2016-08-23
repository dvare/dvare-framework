/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package com.dvare.expression.literal;

import com.dvare.expression.datatype.DataType;

public class LiteralDataType {
    static String date = "\\s*(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(0[1-9]|1[0-2])-([1-9][0-9][0-9][0-9])\\s*";
    static String dateTime = "\\s*(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(0[1-9]|1[0-2])-([1-9][0-9][0-9][0-9])\\-{1}(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])\\s*";


    public static String computeType(String value) {
        if ("true".equals(value) || "false".equals(value)) {
            return "BooleanType";
        }
        if (value.startsWith("'")) {
            return "StringType";
        }
        if (value.startsWith("R'")) {
            return "RegexType";
        }
        if (value.matches(date)) {
            return "DateType";


        }
        if (value.matches(dateTime)) {
            return "DateTimeType";


        }
        if (value.contains(".")) {
            try {
                Float.parseFloat(value);
                return "FloatType";
            } catch (NumberFormatException e) {
            }
        }


        try {
            Integer.parseInt(value);
            return "IntegerType";
        } catch (NumberFormatException e) {
        }


        return null;
    }


    public static DataType computeDataType(String value) {
        if ("true".equals(value) || "false".equals(value)) {
            return DataType.BooleanType;
        }
        if (value.startsWith("'")) {
            return DataType.StringType;
        }
        if (value.startsWith("R'")) {
            return DataType.RegexType;
        }
        if (value.matches(date)) {
            return DataType.DateType;


        }
        if (value.matches(dateTime)) {
            return DataType.DateTimeType;


        }


        if (value.contains(".")) {
            try {
                Float.parseFloat(value);
                return DataType.FloatType;
            } catch (NumberFormatException e) {
            }
        }


        try {
            Integer.parseInt(value);
            return DataType.IntegerType;
        } catch (NumberFormatException e) {
        }


        return null;
    }
}
