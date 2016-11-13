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


package org.dvare.expression.literal;

import org.dvare.exceptions.parser.IllegalValueException;
import org.dvare.expression.datatype.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LiteralType {
    public final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    static Logger logger = LoggerFactory.getLogger(LiteralType.class);

    public static LiteralExpression<?> getLiteralExpression(String values[]) throws IllegalValueException {
        if (values == null || values.length < 1) {
            throw new IllegalValueException("Empty List Found");
        }

        DataType type = LiteralDataType.computeDataType(values[0]);
        return getLiteralExpression(values, type);
    }

    public static LiteralExpression<?> getLiteralExpression(String values[], DataType type) throws IllegalValueException {
        LiteralExpression literalExpression = null;

        if (values == null || values.length < 1) {
            throw new IllegalValueException("Empty List Found");
        }

        DataType rightType = LiteralDataType.computeDataType(values[0]);
        if (rightType != null) {
            type = rightType;
        }


        switch (type) {

            case BooleanType: {

                List<Boolean> list = new ArrayList<>();
                for (String value : values) {
                    list.add(Boolean.parseBoolean(value));
                }
                literalExpression = new ListLiteral(list, new BooleanType(), list.size());
                break;
            }
            case FloatType: {
                List<Float> list = new ArrayList<>();


                for (String value : values) {
                    try {
                        list.add(Float.parseFloat(value));
                    } catch (NumberFormatException e) {
                        String message = String.format("Unable to Parse literal %s to Float", value);
                        logger.error(message);
                        throw new IllegalValueException(message);
                    }
                }

                literalExpression = new ListLiteral<List<Float>>(list, new FloatType(), list.size());
                break;
            }
            case IntegerType: {
                List<Integer> list = new ArrayList<>();
                for (String value : values) {
                    try {
                        list.add(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        String message = String.format("Unable to Parse Literal \" %s \" to Integer", value);
                        logger.error(message);
                        throw new IllegalValueException(message);
                    }
                }
                literalExpression = new ListLiteral<List<Integer>>(list, new IntegerType(), list.size());
                break;
            }
            case StringType: {

                List<String> list = new ArrayList<>();
                for (String value : values) {
                    list.add(value);
                }
                literalExpression = new ListLiteral<List<String>>(list, new StringType(), list.size());
                break;
            }
            case RegexType: {

                List<String> list = new ArrayList<>();
                for (String value : values) {
                    value = value.substring(1, value.length()).trim();
                    list.add(value);
                }
                literalExpression = new ListLiteral<List<String>>(list, new RegexType(), list.size());
                break;
            }

            case DateTimeType: {

                List<Date> list = new ArrayList<>();
                for (String value : values) {
                    Date date = null;
                    try {
                        date = dateTimeFormat.parse(value);
                    } catch (ParseException e) {
                        String message = String.format(" Unable to Parse literal %s to Date Time", value);
                        logger.error(message);
                        throw new IllegalValueException(message);
                    }
                    list.add(date);
                }
                literalExpression = new ListLiteral<List<Date>>(list, new DateType(), list.size());
                break;
            }

            case DateType: {


                List<Date> list = new ArrayList<>();
                for (String value : values) {

                    Date date = null;
                    try {
                        date = dateFormat.parse(value);
                    } catch (ParseException e) {
                        String message = String.format(" Unable to Parse literal %s to Date", value);
                        logger.error(message);
                        throw new IllegalValueException(message);

                    }
                    list.add(date);
                }

                literalExpression = new ListLiteral<List<Date>>(list, new DateType(), list.size());
                break;
            }

        }
        logger.debug("List Litral Expression : {} [{}]", literalExpression.getType().getDataType(), literalExpression.getValue());
        return literalExpression;
    }


    public static LiteralExpression<?> getLiteralExpression(String value) throws IllegalValueException {
        DataType type = LiteralDataType.computeDataType(value);
        return getLiteralExpression(value, type);
    }

    public static LiteralExpression<?> getLiteralExpression(Object value, DataType type) throws IllegalValueException {
        if (value == null) {
            throw new IllegalValueException("The provided string must not be null");
        }

        String valueString = value.toString();


        DataType rightType = LiteralDataType.computeDataType(valueString);
        if (rightType != null) {
            type = rightType;
        }

        LiteralExpression literalExpression = null;


        switch (type) {


            case BooleanType: {
                if (value instanceof Boolean) {
                    literalExpression = new BooleanLiteral(value);
                } else {
                    literalExpression = new BooleanLiteral(Boolean.parseBoolean(valueString));
                }
                break;
            }
            case FloatType: {
                try {
                    if (value instanceof Float) {
                        literalExpression = new FloatLiteral(value);
                    } else {
                        literalExpression = new FloatLiteral(Float.parseFloat(valueString));
                    }
                } catch (NumberFormatException e) {
                    String message = String.format("Unable to Parse literal %s to Float", valueString);
                    logger.error(message);
                    throw new IllegalValueException(message);
                }
                break;
            }
            case IntegerType: {

                try {
                    if (value instanceof Integer) {
                        literalExpression = new IntegerLiteral(value);
                    } else {
                        literalExpression = new IntegerLiteral(Integer.parseInt(valueString));
                    }
                } catch (NumberFormatException e) {
                    String message = String.format("Unable to Parse literal %s to Integer", valueString);
                    logger.error(message);
                    throw new IllegalValueException(message);
                }
                break;
            }
            case StringType: {

                literalExpression = new StringLiteral(valueString);
                break;
            }
            case RegexType: {
                valueString = valueString.substring(1, valueString.length()).trim();
                literalExpression = new RegexLiteral(valueString);
                break;
            }

            case DateTimeType: {
                Date date = null;
                try {

                    if (value instanceof Date) {
                        date = (Date) value;
                    } else {
                        date = dateTimeFormat.parse(valueString);
                    }

                } catch (ParseException e) {
                    String message = String.format("Unable to Parse literal %s to Date Time", valueString);
                    logger.error(message);
                    throw new IllegalValueException(message);
                }
                literalExpression = new DateTimeLiteral(date);
                break;
            }

            case DateType: {

                Date date = null;
                try {
                    if (value instanceof Date) {
                        date = (Date) value;
                    } else {
                        date = dateFormat.parse(valueString);
                    }
                } catch (ParseException e) {
                    String message = String.format("Unable to Parse literal %s to Date", valueString);
                    logger.error(message);
                    throw new IllegalValueException(message);

                }
                literalExpression = new DateLiteral(date);
                break;
            }
            case NullType: {
                literalExpression = new NullLiteral();
                break;
            }


        }
        logger.debug("{} Expression : {} [{}]", literalExpression.getClass().getSimpleName(), literalExpression.getType().getDataType(), literalExpression.getValue());


        return literalExpression;
    }


    public static LiteralExpression<?> getLiteralExpression(Object value, DataTypeExpression type) {

        if (value == null) {
            return new NullLiteral();
        }
        return new LiteralExpression<>(value, type);
    }
}
