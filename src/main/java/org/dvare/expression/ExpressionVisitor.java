package org.dvare.expression;

import org.dvare.expression.datatype.*;
import org.dvare.expression.literal.*;
import org.dvare.expression.operation.*;
import org.dvare.expression.operation.aggregation.*;
import org.dvare.expression.operation.arithmetic.*;
import org.dvare.expression.operation.flow.*;
import org.dvare.expression.operation.list.*;
import org.dvare.expression.operation.logical.And;
import org.dvare.expression.operation.logical.Implies;
import org.dvare.expression.operation.logical.Not;
import org.dvare.expression.operation.logical.OR;
import org.dvare.expression.operation.predefined.*;
import org.dvare.expression.operation.relational.*;
import org.dvare.expression.operation.utility.*;
import org.dvare.expression.veriable.*;

public interface ExpressionVisitor<T> {
    T visit(FunctionExpression f);
    T visit(NamedExpression n);
    T visit(BooleanExpression b);

    // datatype
    T visit(BooleanListType bl);
    T visit(BooleanType b);
    T visit(DataTypeExpression dt);
    T visit(DateListType dl);
    T visit(DateTimeListType dtl);
    T visit(ListType l);
    T visit(DateTimeType dt);
    T visit(DateType d);
    T visit(FloatListType fl);
    T visit(FloatType f);
    T visit(IntegerListType il);
    T visit(IntegerType i);
    T visit(NullType n);
    T visit(PairListType pl);
    T visit(PairType p);
    T visit(RegexType r);
    T visit(SimpleDateListType sdl);
    T visit(SimpleDateType sd);
    T visit(StringListType sl);
    T visit(StringType s);
    T visit(TripleListType tl);
    T visit(TripleType t);

    // literal
    T visit(LiteralExpression<?> l);
    T visit(BooleanLiteral b);
    T visit(DateLiteral d);
    T visit(DateTimeLiteral dt);
    T visit(FloatLiteral f);
    T visit(IntegerLiteral i);
    T visit(ListLiteral l);
    T visit(NullLiteral<?> n);
    T visit(PairLiteral p);
    T visit(RegexLiteral r);
    T visit(SimpleDateLiteral sd);
    T visit(StringLiteral s);
    T visit(TripleLiteral t);

    // variable
    T visit(VariableExpression<?> v);
    T visit(BooleanVariable b);
    T visit(DateTimeVariable dt);
    T visit(DateVariable d);
    T visit(FloatVariable f);
    T visit(IntegerVariable i);
    T visit(ListVariable l);
    T visit(PairVariable p);
    T visit(RegexVariable r);
    T visit(SimpleDateVariable sd);
    T visit(StringVariable s);
    T visit(TripleVariable t);

    // operation
    T visit(OperationExpression o);
    T visit(AssignOperationExpression a);
    T visit(ChainOperationExpression c);
    T visit(ConditionOperationExpression c);
    T visit(CompositeOperationExpression c);
    T visit(IterationOperationExpression i);
    T visit(ListLiteralOperationENDExpression lle);
    T visit(ListLiteralOperationExpression ll);
    T visit(ListOperationExpression l);
    T visit(LogicalOperationExpression l);

    // aggregation
    T visit(AggregationOperationExpression a);
    T visit(Maximum m);
    T visit(Mean m);
    T visit(Median m);
    T visit(Minimum m);
    T visit(Mode m);
    T visit(Sum s);

    // arithmetic
    T visit(ArithmeticOperationExpression e);
    T visit(Add a);
    T visit(Divide d);
    T visit(Max m);
    T visit(Min m);
    T visit(Multiply m);
    T visit(Power p);
    T visit(Subtract s);

    // flow
    T visit(ELSE e);
    T visit(ENDIF e);
    T visit(IF i);
    T visit(TernaryOperation t);
    T visit(THEN t);

    // list
    T visit(CombinationExists ce);
    T visit(FilterOperation f);
    T visit(First f);
    T visit(GetItem gi);
    T visit(HasItem hi);
    T visit(InsideCombination ic);
    T visit(InsideExistsCombination iec);
    T visit(IsEmpty ie);
    T visit(ItemPosition ip);
    T visit(KeysOperation k);
    T visit(Last l);
    T visit(MapOperation m);
    T visit(Match m);
    T visit(MiddlesOperation m);
    T visit(NotEmpty ne);
    T visit(PairOperation p);
    T visit(SizeOperation s);
    T visit(SortOperation s);
    T visit(TripleOperation t);
    T visit(ValuesOperation v);

    // logical
    T visit(And a);
    T visit(Implies i);
    T visit(Not n);
    T visit(OR o);

    // predefined
    T visit(AddDays ad);
    T visit(AddMonths am);
    T visit(AddYears ay);
    T visit(Append a);
    T visit(Contains c);
    T visit(EndsWith ew);
    T visit(GetYear gy);
    T visit(Length l);
    T visit(Prepend p);
    T visit(SetDay sd);
    T visit(SetMonth sm);
    T visit(SetYear sy);
    T visit(StartsWith sw);
    T visit(SubDays sd);
    T visit(SubMonths sm);
    T visit(SubYears sy);
    T visit(Substring s);
    T visit(ToBoolean tb);
    T visit(ToDate td);
    T visit(ToInteger ti);
    T visit(ToKey tk);
    T visit(ToLeft tl);
    T visit(ToMiddle tm);
    T visit(ToPair tp);
    T visit(ToRight tr);
    T visit(ToString ts);
    T visit(ToTriple tt);
    T visit(ToValue tv);
    T visit(Trim t);

    // relational
    T visit(RelationalOperationExpression r);
    T visit(Between b);
    T visit(Equals e);
    T visit(GreaterEqual ge);
    T visit(GreaterThan gt);
    T visit(In i);
    T visit(LessEqual le);
    T visit(LessThan lt);
    T visit(NotEquals ne);
    T visit(NotIn ni);

    // utility
    T visit(DateOperation d);
    T visit(DateTimeOperation dt);
    T visit(DefOperation d);
    T visit(EndForAll efa);
    T visit(EndForEach efe);
    T visit(ExpressionSeparator es);
    T visit(ForAll fa);
    T visit(ForEach fe);
    T visit(Function f);
    T visit(Invoke i);
    T visit(LeftPriority lp);
    T visit(LetOperation l);
    T visit(PrintOperation p);
    T visit(RightPriority rp);
    T visit(Today t);
}
