/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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


package org.dvare.expression.literal;

import javafx.util.Pair;
import org.dvare.annotations.Type;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.SimpleDateType;
import org.dvare.util.TrimString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class LiteralType {
    public final static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
    public final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public final static DateTimeFormatter defaultFormat = DateTimeFormatter.ofPattern("E MMM dd hh:mm:ss Z yyyy");
    public static String date = "\\s*(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(0[1-9]|1[0-2])-([1-9][0-9][0-9][0-9])\\s*";
    public static String date2 = "\\s*([1-9][0-9][0-9][0-9])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])\\s*";


    public static String dateTime = "\\s*(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(0[1-9]|1[0-2])-([1-9][0-9][0-9][0-9])\\-{1}(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])\\s*";
    private static Logger logger = LoggerFactory.getLogger(LiteralType.class);


    public static LiteralExpression<?> getLiteralExpression(Object value, Class<?> dataTypeExpression) throws InterpretException {

        if (value == null) {
            return new NullLiteral();
        }

        DataType dataType = null;
        if (dataTypeExpression.isAnnotationPresent(Type.class)) {
            Type type = dataTypeExpression.getAnnotation(Type.class);
            dataType = type.dataType();
        }

        try {
            return buildLiteralExpression(value, dataType);
        } catch (IllegalValueException e) {
            throw new InterpretException(e.getMessage(), e);
        }

    }

    public static LiteralExpression<?> getLiteralExpression(String value) throws IllegalValueException {
        DataType type = computeDataType(value);
        return getLiteralExpression(value, type);
    }


    public static LiteralExpression<?> getLiteralExpression(Object value, DataType type) throws IllegalValueException {
        if (value == null) {
            throw new IllegalValueException("The Literal Expression is null");
        }


        DataType rightType = computeDataType(value.toString());
        if (type != null && (rightType == DataType.RegexType)) {
            type = rightType;
        }


        if (type == null || type.equals(DataType.UnknownType)) {
            throw new IllegalValueException("Unable to parse Literal \"" + value + "\" of type is Unknown Type");
        }


        return buildLiteralExpression(value, type);
    }

    private static LiteralExpression<?> buildLiteralExpression(Object value, DataType type) throws IllegalValueException {
        LiteralExpression literalExpression = null;
        String valueString = value.toString();
        switch (type) {


            case BooleanType: {
                if (value instanceof Boolean) {
                    literalExpression = new BooleanLiteral((Boolean) value);
                } else {
                    literalExpression = new BooleanLiteral(Boolean.parseBoolean(valueString));
                }
                break;
            }
            case FloatType: {
                try {
                    if (value instanceof Float) {
                        literalExpression = new FloatLiteral((Float) value);
                    } else {
                        literalExpression = new FloatLiteral(Float.parseFloat(valueString));
                    }
                } catch (NumberFormatException e) {
                    String message = String.format("Unable to Parse literal %s to Float", valueString);
                    logger.error(message);
                    throw new IllegalValueException(message, e);
                }
                break;
            }
            case IntegerType: {

                try {
                    if (value instanceof Integer) {
                        literalExpression = new IntegerLiteral((Integer) value);
                    } else {
                        literalExpression = new IntegerLiteral(Integer.parseInt(valueString));
                    }
                } catch (NumberFormatException e) {
                    String message = String.format("Unable to Parse literal %s to Integer", valueString);
                    logger.error(message);
                    throw new IllegalValueException(message, e);
                }
                break;
            }
            case StringType: {
                valueString = TrimString.trim(valueString);
                literalExpression = new StringLiteral(valueString);
                break;
            }
            case RegexType: {
                valueString = valueString.substring(1, valueString.length() - 1).trim();
                literalExpression = new RegexLiteral(valueString);
                break;
            }

            case DateTimeType: {
                LocalDateTime localDateTime;
                try {

                    if (value instanceof LocalDateTime) {
                        localDateTime = (LocalDateTime) value;
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    } else {
                        try {
                            localDateTime = LocalDateTime.parse(valueString, dateTimeFormat);
                        } catch (DateTimeParseException e) {
                            localDateTime = LocalDateTime.parse(valueString, DateTimeFormatter.ISO_DATE_TIME);
                        }
                    }

                } catch (DateTimeParseException e) {
                    String message = String.format("Unable to Parse literal %s to Date Time", valueString);
                    logger.error(message);
                    throw new IllegalValueException(message, e);
                }
                literalExpression = new DateTimeLiteral(localDateTime);
                break;
            }

            case DateType: {

                LocalDate localDate;
                try {
                    if (value instanceof LocalDate) {
                        localDate = (LocalDate) value;
                    } else if (value instanceof Date) {

                        Date date = (Date) value;
                        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    } else {
                        try {
                            localDate = LocalDate.parse(valueString, dateFormat);
                        } catch (DateTimeParseException e) {
                            localDate = LocalDate.parse(valueString, DateTimeFormatter.ISO_DATE);
                        }
                    }
                } catch (Exception e) {
                    String message = String.format("Unable to Parse literal %s to Date", valueString);
                    logger.error(message);
                    throw new IllegalValueException(message, e);

                }
                literalExpression = new DateLiteral(localDate);
                break;
            }

            case SimpleDateType: {

                Date date;
                try {
                    if (value instanceof Date) {
                        date = (Date) value;
                    } else {
                        date = SimpleDateType.dateFormat.parse(valueString);
                    }
                } catch (ParseException e) {
                    String message = String.format("Unable to Parse literal %s to Date", valueString);
                    logger.error(message);
                    throw new IllegalValueException(message, e);

                }
                literalExpression = new SimpleDateLiteral(date);
                break;
            }

            case NullType: {
                literalExpression = new NullLiteral();
                break;
            }
            case PairType: {
                if (value instanceof Pair) {
                    literalExpression = new PairLiteral((Pair) value);
                }
            }


            case IntegerListType:
                break;
            case FloatListType:
                break;
            case StringListType:
                break;
            case BooleanListType:
                break;
            case DateTimeListType:
                break;
            case DateListType:
                break;
            case ListType:
                break;
        }

        if (literalExpression != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("{} Expression : {} [{}]", literalExpression.getClass().getSimpleName(), literalExpression.getType().getSimpleName(), literalExpression.getValue());
            }
            return literalExpression;
        } else {
            throw new IllegalValueException("Literal Expression is Null");
        }
    }

    public static void main(String arfs[]) {

    }

    public static DataType computeDataType(String value) {

        if ("null".equals(value) || "NULL".equals(value)) {
            return DataType.NullType;
        }

        if ("true".equals(value) || "false".equals(value)) {
            return DataType.BooleanType;
        }
        if (value.startsWith("'") && value.endsWith("'")) {
            return DataType.StringType;
        }
        if (value.startsWith("{") && value.endsWith("}")) {
            return DataType.RegexType;
        }
        if (value.matches(date) || value.matches(date2)) {
            return DataType.DateType;
        }


        if (value.matches(dateTime)) {
            return DataType.DateTimeType;
        }

        try {
            LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
            return DataType.DateType;
        } catch (DateTimeParseException e) {

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

        return DataType.UnknownType;
    }
}
