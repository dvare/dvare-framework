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

    public static DataType getDataTypeListDataType(String type) {
        return getDataTypeListDataType(DataType.valueOf(type));
    }

    public static DataType getDataTypeListDataType(DataType type) {

        switch (type) {
            case BooleanType:
                return DataType.BooleanListType;
            case FloatType:
                return DataType.FloatListType;
            case IntegerType:
                return DataType.IntegerListType;
            case StringType:
                return DataType.StringListType;
            case DateTimeType:
                return DataType.DateTimeListType;
            case DateType:
                return DataType.DateListType;
            case SimpleDateType:
                return DataType.SimpleDateListType;
            case PairType:
                return DataType.PairListType;

        }
        return type;

    }


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


            case SimpleDateType:
            case SimpleDateListType: {
                return SimpleDateType.class;
            }

            case PairType:
            case PairListType: {
                return PairType.class;
            }

            case TripleType:
            case TripleListType: {
                return TripleType.class;
            }

            case RegexType: {
                return RegexType.class;
            }

        }
        return null;

    }

    public static DataType getTypeMapping(Class<?> type) {
        return getTypeMapping(type.getSimpleName());
    }

    public static DataType getTypeMapping(String type) {

        switch (type) {
            case "Boolean":
            case "boolean": {
                return DataType.BooleanType;
            }

            case "Boolean[]":
            case "boolean[]": {
                return DataType.BooleanListType;
            }


            case "Integer":
            case "int": {
                return DataType.IntegerType;
            }


            case "Integer[]":
            case "int[]": {
                return DataType.IntegerListType;
            }


            case "Float":
            case "float": {
                return DataType.FloatType;
            }

            case "Float[]":
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


            case "Pair":
            case "PairImpl": {
                return DataType.PairType;
            }

            case "Pair[]":
            case "PairImpl[]": {
                return DataType.PairListType;
            }

            case "Triple":
            case "TripleImpl": {
                return DataType.TripleType;
            }

            case "Triple[]":
            case "TripleImpl[]": {
                return DataType.TripleListType;
            }
        }
        return null;

    }

    public static Class<?> getDataTypeMapping(String type) {
        return getDataTypeMapping(DataType.valueOf(type));
    }

    public static Class<?> getDataTypeMapping(DataType type) {

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
            case PairType: {
                return Pair.class;
            }
            case PairListType: {
                return Pair[].class;
            }
            case TripleType: {
                return Triple.class;
            }
            case TripleListType: {
                return Triple[].class;
            }
            case RegexType: {
                return String.class;
            }

        }
        return null;

    }


}
