package org.dvare.util;

import org.dvare.annotations.FunctionMethod;
import org.dvare.annotations.FunctionService;
import org.dvare.expression.datatype.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionService
public class FunctionProvider {
    private static final Logger logger = LoggerFactory.getLogger(FunctionProvider.class);


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




