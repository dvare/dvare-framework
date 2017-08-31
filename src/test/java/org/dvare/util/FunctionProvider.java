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
package org.dvare.util;

import org.dvare.annotations.FunctionMethod;
import org.dvare.annotations.FunctionService;
import org.dvare.expression.datatype.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionService
public class FunctionProvider {
    private static Logger logger = LoggerFactory.getLogger(FunctionProvider.class);


    @FunctionMethod(returnType = DataType.IntegerType, parameters = {DataType.IntegerType, DataType.IntegerType})
    public Integer addFunction(Integer variable, Integer variable2) {
        if (logger.isDebugEnabled()) {
            logger.debug("inside addFunction with arguments : " + variable + " and " + variable2);
        }
        return variable + variable2;
    }


    @FunctionMethod(returnType = DataType.IntegerType, parameters = {DataType.IntegerType, DataType.StringType})
    public Integer addFiveFunction(Integer variable, String variable2) {
        if (logger.isDebugEnabled()) {
            logger.debug("inside addFiveFuntion with arguments : " + variable + " and " + variable2);
        }
        return variable + 5;
    }


    @FunctionMethod(returnType = DataType.IntegerType, parameters = {DataType.IntegerListType})
    public Integer addRowsFunction(Integer[] rows) {
        if (logger.isDebugEnabled()) {
            logger.debug("inside addRowsFunction with rows : " + rows.length);
        }
        Integer result = 0;
        for (Object row : rows) {

            if (row instanceof Integer) {
                result = result + (Integer) row;
            }
        }

        return result;
    }


    @FunctionMethod(returnType = DataType.IntegerType, parameters = {DataType.IntegerType, DataType.IntegerListType})
    public Integer addTenFunction(Integer variable, Integer[] values) {
        if (logger.isDebugEnabled()) {
            logger.debug("addTenFunction with arguments : " + variable + " and " + values.length + " list arguments");
        }
        return variable + 10;
    }


}




