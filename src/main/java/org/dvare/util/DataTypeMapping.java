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
package org.dvare.util;

import org.dvare.expression.datatype.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public class DataTypeMapping {

    public static Class<? extends DataTypeExpression> getDataTypeClass(String type) {
        return getDataTypeClass(DataType.valueOf(type));
    }

    public static Class<? extends DataTypeExpression> getDataTypeClass(DataType type) {

        switch (type) {
            case BooleanType:
            case BooleanListType: {
                return BooleanType.class;

            }
            case FloatType:
            case FloatListType: {

                return FloatType.class;
            }
            case IntegerType:
            case IntegerListType: {
                return IntegerType.class;

            }
            case StringType:
            case StringListType: {

                return StringType.class;
            }
            case DateTimeType:
            case DateTimeListType: {

                return DateTimeType.class;
            }
            case DateType:
            case DateListType: {
                return DateType.class;

            }
            case RegexType: {

                return RegexType.class;
            }

        }
        return null;

    }

    public static DataType getTypeMapping(Class type) {
        return getTypeMapping(type.getSimpleName());
    }

    public static DataType getTypeMapping(String type) {

        switch (type) {
            case "Boolean": {
                return DataType.BooleanType;
            }


            case "Boolean[]": {
                return DataType.BooleanListType;
            }


            case "boolean": {
                return DataType.BooleanType;
            }

            case "boolean[]": {
                return DataType.BooleanListType;
            }


            case "Integer": {
                return DataType.IntegerType;
            }


            case "Integer[]": {
                return DataType.IntegerListType;
            }


            case "int": {
                return DataType.IntegerType;
            }

            case "int[]": {
                return DataType.IntegerListType;
            }


            case "Float": {
                return DataType.FloatType;
            }


            case "Float[]": {
                return DataType.FloatListType;
            }
            case "float": {
                return DataType.FloatType;
            }

            case "float[]": {
                return DataType.FloatListType;
            }

            case "String": {
                return DataType.StringType;
            }

            case "String[]": {
                return DataType.StringListType;
            }

            case "Date": {
                return DataType.SimpleDateType;
            }

            case "Date[]": {
                return DataType.SimpleDateListType;
            }

            case "LocalDate": {
                return DataType.DateType;
            }


            case "LocalDate[]": {
                return DataType.DateListType;
            }

            case "LocalDateTime": {
                return DataType.DateTimeType;
            }

            case "LocalDateTime[]": {
                return DataType.DateTimeListType;
            }

        }
        return null;

    }

    public static Class getDataTypeMapping(String type) {
        return getDataTypeMapping(DataType.valueOf(type));
    }

    public static Class getDataTypeMapping(DataType type) {

        switch (type) {
            case BooleanType: {
                return Boolean.class;

            }
            case BooleanListType: {
                return Boolean[].class;

            }
            case FloatType: {

                return Float.class;
            }
            case FloatListType: {

                return Float[].class;
            }
            case IntegerType: {
                return Integer.class;

            }
            case IntegerListType: {
                return Integer[].class;

            }
            case StringType: {

                return String.class;
            }
            case StringListType: {

                return String[].class;
            }
            case DateTimeType: {

                return LocalDateTime.class;
            }
            case DateTimeListType: {

                return LocalDateTime[].class;
            }
            case DateType: {
                return LocalDate.class;

            }
            case DateListType: {
                return LocalDate[].class;

            }
            case SimpleDateType: {
                return Date.class;

            }
            case SimpleDateListType: {
                return Date[].class;

            }

            case RegexType: {

                return String.class;
            }

        }
        return null;

    }


}
