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


package org.dvare.expression.operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum OperationType {

    ABSOLUTE("Abs", "abs"), COMBINATION("Combination", "combination", "comb"), FUNCTION("Function", "function", "fun"),//
    LEFT_PRIORITY("("), RIGHT_PRIORITY(")"),//
    TO_DATE("ToDate", "toDate"), TO_DAY("today", "ToDay"),//
    FOUND("Found", "found"), POWER("Pow", "pow", "^"),//
    NOT("not", "NOT", "Not", "!"), AND("and", "AND", "And", "&&"), OR("or", "OR", "||"), IMPLIES("implies", "Implies", "=>"),//
    EQUAL("=", "eq"), NOT_EQUAL("!=", "ne", "<>"),//
    LESS("<", "lt"), LESS_EQUAL("<=", "le"), GREATER(">", "gt"), GREATER_EQUAL(">=", "ge"),//
    IN("in", "IN", "In"), BETWEEN("between", "Between"), //
    MUL("Mul", "mul", "*"), DIVIDE("Div", "div", "/"), ADD("Add", "add", "+"), SUBTRACT("Sub", "sub", "-"),//
    MAX("Max", "max"), MIN("Min", "min"),//
    TO_INTEGER("toInteger", "ToInteger"), TO_STRING("toString", "ToString"),//
    SUBSTRING("substring", "Substring"), APPEND("append", "Append"), PREPEND("prepend", "Prepend"), CONTAINS("contains", "Contains"),//
    STARTS_WITH("startsWith", "Startswith", "StartsWith", "startswith"), ENDS_WITH("endsWith", "Endswith", "EndsWith", "endswith"),//
    IF("IF", "if", "ELSEIF", "elseif"), THEN("THEN", "then"), ELSE("ELSE", "else"), ENDIF("ENDIF", "endif"),//
    ASSIGN(":=", "assign", "update"), COUNT("Count", "count"), FIRST("First", "first"), LAST("Last", "last"),//
    MAXIMUM("Maximum", "maximum"), MINIMUM("Minimum", "minimum"),//
    MEAN("Mean", "mean", "Avg", "avg"), MEDIAN("Median", "median"), MODE("Mode", "mode"),//
    SUM("Sum", "sum"), VALUE("Value", "value"), COLON(";");

    private List<String> symbols = new ArrayList<>();

    OperationType() {

    }

    OperationType(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }
}
