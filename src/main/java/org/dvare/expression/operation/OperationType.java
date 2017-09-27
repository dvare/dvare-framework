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
package org.dvare.expression.operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum OperationType {

    List("["), ASSIGN(":=", "assign", "update"),
    INVOKE("Invoke", "invoke"),
    FUNCTION("Function", "function", "fun"),//

    Match("Match", "match"),//
    INSIDE_COMB("InsideComb", "insideComb"), COMB_EXISTS("CombExists", "combExists"),//
    BOTH_INSIDE_EXISTS_COMB("InsideExistsComb", "insideExistsComb"), //
    LEFT_PRIORITY("("), RIGHT_PRIORITY(")"),//
    DATE("Date", "date"), DATE_TIME("DateTime", "dateTime"), TO_DAY("today"),//
    POWER("Pow", "pow", "^"),//
    NOT("not", "NOT", "Not", "!"), AND("and", "AND", "And", "&&"), OR("or", "OR", "||"), IMPLIES("implies", "Implies", "=>"),//
    EQUAL("=", "eq"), NOT_EQUAL("!=", "ne", "<>"),//
    LESS("<", "lt"), LESS_EQUAL("<=", "le"), GREATER(">", "gt"), GREATER_EQUAL(">=", "ge"),//
    IN("in", "IN", "In"), NOT_IN("notIn", "NOTIN", "NotIn"), BETWEEN("between", "Between"), //
    MUL("Mul", "mul", "*"), DIVIDE("Div", "div", "/"), ADD("Add", "add", "+"), SUBTRACT("Sub", "sub", "-"),//
    MAX("Max", "max"), MIN("Min", "min"),//

    //Chain Operations

    TO_INTEGER("toInteger", "ToInteger"), TO_STRING("toString", "ToString"), TO_Boolean("toBoolean", "ToBoolean"),
    TO_DATE("ToDate", "toDate"), ADD_YEARS("AddYears", "addYears"), ADD_MONTHS("AddMonths", "addMonths"), ADD_DAYS("AddDays", "addDays"),//
    SET_YEAR("SetYear", "setYear"), SET_MONTH("SetMonth", "setMonth"), SET_DAY("SetDay", "setDay"),//

    GET_YEARS("GetYears", "getYears"), //

    LENGTH("Length", "length"),//
    SUBSTRING("substring", "Substring"), APPEND("append", "Append"), PREPEND("prepend", "Prepend"), CONTAINS("contains", "Contains"),//
    STARTS_WITH("startsWith", "Startswith", "StartsWith", "startswith"), ENDS_WITH("endsWith", "Endswith", "EndsWith", "endswith"),//

    //Condition

    IF("IF", "if"), THEN("THEN", "then"),//
    ELSE("ELSE", "else"), ENDIF("ENDIF", "endif"),//
    // List
    FORALL("forAll", "ForAll"), END_FORALL("endForAll", "EndForAll"), FOREACH("forEach", "ForEach"), END_FOREACH("endForEach", "EndForEach"), //

    VALUES("Values", "values"), FILTER("Filter", "filter"), MAP("Map", "map"), SORT("Sort", "sort"), //
    GET_ITEM("GetItem", "getItem"), HAS_ITEM("HasItem", "hasItem"), ITEM_POSITION("ItemPosition", "itemPosition"),//

    FIRST("First", "first"), LAST("Last", "last"),//
    SIZE("Size", "size"), NOT_EMPTY("notEmpty", "NotEMPTY"), IS_EMPTY("isEmpty", "ISEMPTY"),//

    MAXIMUM("Maximum", "maximum"), MINIMUM("Minimum", "minimum"),//
    MEAN("Mean", "mean", "Avg", "avg"), MEDIAN("Median", "median"), MODE("Mode", "mode"),//
    SUM("Sum", "sum"), VALUE("Value", "value"), COLON(";"),//


    PAIR_List("Pair", "pair", "PairList", "pairList"), TO_PAIR("toPair", "ToPair"), KEYS("Keys", "keys"),
    TO_KEY("toKey", "ToKey", "getKey", "GetKey"), TO_VALUE("toValue", "ToValue", "getValue", "GetValue"),

    // initialization
    DEF("Def", "def"), LET("let", "Let"),

    PRINT("print", "Print", "Log", "log");

    private List<String> symbols = new ArrayList<>();

    OperationType(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
    }

    public List<String> getSymbols() {
        return symbols;
    }

}
