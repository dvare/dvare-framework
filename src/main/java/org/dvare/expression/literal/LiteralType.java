package org.dvare.expression.literal;


import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Type;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.IllegalLiteralException;
import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.datatype.DataType;
import org.dvare.parser.ExpressionTokenizer;
import org.dvare.util.DataTypeMapping;
import org.dvare.util.TrimString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */

public class LiteralType {
    public final static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
    public final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public final static DateTimeFormatter defaultFormat = DateTimeFormatter.ofPattern("E MMM dd hh:mm:ss Z yyyy");


    public final static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final Logger logger = LoggerFactory.getLogger(LiteralType.class);
    public static String date = "\\s*(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(0[1-9]|1[0-2])-([1-9][0-9][0-9][0-9])\\s*";
    public static String date2 = "\\s*([1-9][0-9][0-9][0-9])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])\\s*";
    public static String dateTime = "\\s*(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(0[1-9]|1[0-2])-([1-9][0-9][0-9][0-9])\\-{1}(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])\\s*";

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

    public static LiteralExpression<?> getLiteralExpression(String value, int pos, String[] tokens) throws IllegalValueException {
        DataType type = computeDataType(value);
        return getLiteralExpression(value, type, pos, tokens);
    }

    public static LiteralExpression<?> getLiteralExpression(Object value, DataType type) throws IllegalValueException {
        return getLiteralExpression(value, type, 0, null);
    }

    public static LiteralExpression<?> getLiteralExpression(Object value, DataType type, int pos, String[] tokens) throws IllegalValueException {
        if (value == null) {
            throw new IllegalValueException("The Literal Expression is null");
        }


        DataType rightType = computeDataType(value.toString());
        if (type != null && (rightType == DataType.RegexType)) {
            type = rightType;
        }


        if (type == null || type.equals(DataType.UnknownType)) {
            String message;
            if (tokens != null) {
                message = String.format("\"" + value + "\" is not an Variable or Function  near \" %s \"",
                        ExpressionTokenizer.toString(tokens, pos, pos - 5));
            } else {
                message = "\"" + value + "\" is not an Variable or Function.";
            }

            throw new IllegalLiteralException(value.toString(), message);
        }

        return buildLiteralExpression(value, type);
    }

    private static LiteralExpression<?> buildLiteralExpression(Object value, DataType type) throws IllegalLiteralException {
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
                    throw new IllegalLiteralException(valueString, message, e);
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
                    throw new IllegalLiteralException(valueString, message, e);
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
                    throw new IllegalLiteralException(valueString, message, e);
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
                    throw new IllegalLiteralException(valueString, message, e);

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
                        date = LiteralType.simpleDateFormat.parse(valueString);
                    }
                } catch (ParseException e) {
                    String message = String.format("Unable to Parse literal %s to Date", valueString);
                    logger.error(message);
                    throw new IllegalLiteralException(valueString, message, e);

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
                    break;
                }
            }


            case ListType:
            case IntegerListType:
            case FloatListType:
            case StringListType:
            case BooleanListType:
            case DateTimeListType:
            case DateListType:
            case PairListType:
            case SimpleDateListType: {
                if (value.getClass().isArray()) {
                    literalExpression = new ListLiteral(Arrays.asList((Object[]) value), DataTypeMapping.getDataTypeClass(type));
                } else if (value instanceof List) {
                    literalExpression = new ListLiteral((List) value, DataTypeMapping.getDataTypeClass(type));
                }
                break;
            }
        }

        if (literalExpression != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("{} Expression : {} [{}]", literalExpression.getClass().getSimpleName(),
                        literalExpression.getType().getSimpleName(), literalExpression.getValue());
            }
            return literalExpression;
        } else {
            throw new IllegalLiteralException(null, "Literal Expression is Null");
        }
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
        } catch (DateTimeParseException ignored) {

        }


        if (value.contains(".")) {
            try {
                Float.parseFloat(value);
                return DataType.FloatType;
            } catch (NumberFormatException ignored) {
            }
        }


        try {
            Integer.parseInt(value);
            return DataType.IntegerType;
        } catch (NumberFormatException ignored) {
        }


        return DataType.UnknownType;
    }
}