package org.dvare.expression.operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
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
    SUB_YEARS("SubYears", "subYears"), SUB_MONTHS("SubMonths", "subMonths"), SUB_DAYS("SubDays", "subDays"),//
    SET_YEAR("SetYear", "setYear"), SET_MONTH("SetMonth", "setMonth"), SET_DAY("SetDay", "setDay"),//

    GET_YEAR("GetYear", "getYear", "GetYears", "getYears"), //

    LENGTH("Length", "length"),//
    SUBSTRING("substring", "Substring"), APPEND("append", "Append"), PREPEND("prepend", "Prepend"),//
    CONTAINS("contains", "Contains"), TRIM("trim", "Trim"), //
    STARTS_WITH("startsWith", "Startswith", "StartsWith", "startswith"), ENDS_WITH("endsWith", "Endswith", "EndsWith", "endswith"),//

    //Condition

    IF("IF", "if"), THEN("THEN", "then"),//
    ELSE("ELSE", "else"), ENDIF("ENDIF", "endif"),//
    // List
    FORALL("forAll", "ForAll"), END_FORALL("endForAll", "EndForAll"), FOREACH("forEach", "ForEach"), END_FOREACH("endForEach", "EndForEach"), //

    VALUES("Values", "values"), FILTER("Filter", "filter"), MAP("Map", "map"), SORT("Sort", "sort"), //
    GET_ITEM("GetItem", "getItem"), HAS_ITEM("HasItem", "hasItem"), ITEM_POSITION("ItemPosition", "itemPosition", "itemPos"),//

    FIRST("First", "first"), LAST("Last", "last"),//
    SIZE("Size", "size"), NOT_EMPTY("notEmpty", "NotEMPTY"), IS_EMPTY("isEmpty", "ISEMPTY"),//

    MAXIMUM("Maximum", "maximum"), MINIMUM("Minimum", "minimum"),//
    MEAN("Mean", "mean", "Avg", "avg"), MEDIAN("Median", "median"), MODE("Mode", "mode"),//
    SUM("Sum", "sum"),//


    PAIR_List("Pair", "pair", "PairList", "pairList"), TO_PAIR("toPair", "ToPair"), KEYS("Keys", "keys"),
    TO_KEY("toKey", "ToKey", "getKey", "GetKey"), TO_VALUE("toValue", "ToValue", "getValue", "GetValue"),

    // initialization
    DEF("Def", "def"), LET("let", "Let"),

    SEPARATOR(";"),//
    PRINT("print", "Print", "Log", "log");

    private final List<String> symbols = new ArrayList<>();

    OperationType(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
    }

    public List<String> getSymbols() {
        return symbols;
    }

}
