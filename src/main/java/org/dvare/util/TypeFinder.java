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


import org.apache.commons.lang3.reflect.FieldUtils;
import org.dvare.binding.model.TypeBinding;
import org.dvare.expression.datatype.DataType;
import org.dvare.parser.ExpressionParser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

public class TypeFinder {
    public static DataType findType(String name, TypeBinding typeBinding) {
        DataType variableType = null;
        if (name.contains(".")) {

            String fields[] = name.split("\\.");

            Iterator<String> iterator = Arrays.asList(fields).iterator();

            TypeBinding childType = typeBinding;
            while (iterator.hasNext()) {
                String field = iterator.next();

                if (iterator.hasNext()) {

                    Object newType = childType.getDataType(field);
                    if (newType instanceof TypeBinding) {
                        childType = (TypeBinding) newType;
                    } else if (newType instanceof Class) {

                        childType = ExpressionParser.translate((Class) newType);
                    }


                } else {


                    Object newType = childType.getDataType(field);
                    if (newType instanceof DataType) {
                        variableType = (DataType) newType;
                    }
                }

            }


        } else {

            Object newType = typeBinding.getDataType(name);
            if (newType instanceof DataType) {

                variableType = (DataType) newType;
            }
        }
        return variableType;
    }


    private static DataType findType(String name, Class type) {
        DataType variableType = null;
        if (name.contains(".")) {

            String fields[] = name.split(".");

            Iterator<String> iterator = Arrays.asList(fields).iterator();

            Class childType = type;
            while (iterator.hasNext()) {
                String field = iterator.next();

                if (iterator.hasNext()) {

                    Field newType = FieldUtils.getDeclaredField(childType, field, true);
                    childType = newType.getType();

                } else {
                    Field newType = FieldUtils.getDeclaredField(childType, field, true);
                    if (newType != null) {
                        variableType = typeMapping(newType.getType());
                    }
                }

            }


        } else {

            Field newType = FieldUtils.getDeclaredField(type, name, true);
            if (newType != null) {
                variableType = typeMapping(newType.getType());

            }
        }
        return variableType;
    }


    private static DataType typeMapping(Class type) {

        String simpleName = type.getSimpleName();

        if (simpleName.equals("int")) {
            return DataType.IntegerType;
        }

        simpleName = simpleName.substring(0, 1).toUpperCase() +
                simpleName.substring(1).toLowerCase();
        simpleName = simpleName + "Type";

        return DataType.valueOf(simpleName);

    }


}
