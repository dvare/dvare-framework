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

public interface ExpressionVisitor {
    void visit(Expression e);
    void visit(FunctionExpression f);
    void visit(NamedExpression n);
    void visit(BooleanExpression b);

    // datatype
    void visit(BooleanListType bl);
    void visit(BooleanType b);
    void visit(DataTypeExpression dt);
    void visit(DateListType dl);
    void visit(DateTimeListType dtl);
    void visit(ListType l);
    void visit(DateTimeType dt);
    void visit(DateType d);
    void visit(FloatListType fl);
    void visit(FloatType f);
    void visit(IntegerListType il);
    void visit(IntegerType i);
    void visit(NullType n);
    void visit(PairListType pl);
    void visit(PairType p);
    void visit(RegexType r);
    void visit(SimpleDateListType sdl);
    void visit(SimpleDateType sd);
    void visit(StringListType sl);
    void visit(StringType s);
    void visit(TripleListType tl);
    void visit(TripleType t);

    // literal
    void visit(LiteralExpression<?> l);
    void visit(BooleanLiteral b);
    void visit(DateLiteral d);
    void visit(DateTimeLiteral dt);
    void visit(FloatLiteral f);
    void visit(IntegerLiteral i);
    void visit(ListLiteral l);
    void visit(NullLiteral<?> n);
    void visit(PairLiteral p);
    void visit(RegexLiteral r);
    void visit(SimpleDateLiteral sd);
    void visit(StringLiteral s);
    void visit(TripleLiteral t);

    // variable
    void visit(VariableExpression<?> v);
    void visit(BooleanVariable b);
    void visit(DateTimeVariable dt);
    void visit(DateVariable d);
    void visit(FloatVariable f);
    void visit(IntegerVariable i);
    void visit(ListVariable l);
    void visit(PairVariable p);
    void visit(RegexVariable r);
    void visit(SimpleDateVariable sd);
    void visit(StringVariable s);
    void visit(TripleVariable t);

    // operation
    void visit(OperationExpression o);
    void visit(AssignOperationExpression a);
    void visit(ChainOperationExpression c);
    void visit(ConditionOperationExpression c);
    void visit(CompositeOperationExpression c);
    void visit(IterationOperationExpression i);
    void visit(ListLiteralOperationENDExpression lle);
    void visit(ListLiteralOperationExpression ll);
    void visit(ListOperationExpression l);
    void visit(LogicalOperationExpression l);

    // aggregation
    void visit(AggregationOperationExpression a);
    void visit(Maximum m);
    void visit(Mean m);
    void visit(Median m);
    void visit(Minimum m);
    void visit(Mode m);
    void visit(Sum s);

    // arithmetic
    void visit(ArithmeticOperationExpression e);
    void visit(Add a);
    void visit(Divide d);
    void visit(Max m);
    void visit(Min m);
    void visit(Multiply m);
    void visit(Power p);
    void visit(Subtract s);

    // flow
    void visit(ELSE e);
    void visit(ENDIF e);
    void visit(IF i);
    void visit(TernaryOperation t);
    void visit(THEN t);

    // list
    void visit(CombinationExists ce);
    void visit(FilterOperation f);
    void visit(First f);
    void visit(GetItem gi);
    void visit(HasItem hi);
    void visit(InsideCombination ic);
    void visit(InsideExistsCombination iec);
    void visit(IsEmpty ie);
    void visit(ItemPosition ip);
    void visit(KeysOperation k);
    void visit(Last l);
    void visit(MapOperation m);
    void visit(Match m);
    void visit(MiddlesOperation m);
    void visit(NotEmpty ne);
    void visit(PairOperation p);
    void visit(SizeOperation s);
    void visit(SortOperation s);
    void visit(TripleOperation t);
    void visit(ValuesOperation v);

    // logical
    void visit(And a);
    void visit(Implies i);
    void visit(Not n);
    void visit(OR o);

    // predefined
    void visit(AddDays ad);
    void visit(AddMonths am);
    void visit(AddYears ay);
    void visit(Append a);
    void visit(Contains c);
    void visit(EndsWith ew);
    void visit(GetYear gy);
    void visit(Length l);
    void visit(Prepend p);
    void visit(SetDay sd);
    void visit(SetMonth sm);
    void visit(SetYear sy);
    void visit(StartsWith sw);
    void visit(SubDays sd);
    void visit(SubMonths sm);
    void visit(SubYears sy);
    void visit(Substring s);
    void visit(ToBoolean tb);
    void visit(ToDate td);
    void visit(ToInteger ti);
    void visit(ToKey tk);
    void visit(ToLeft tl);
    void visit(ToMiddle tm);
    void visit(ToPair tp);
    void visit(ToRight tr);
    void visit(ToString ts);
    void visit(ToTriple tt);
    void visit(ToValue tv);
    void visit(Trim t);

    // relational
    void visit(RelationalOperationExpression r);
    void visit(Between b);
    void visit(Equals e);
    void visit(GreaterEqual ge);
    void visit(GreaterThan gt);
    void visit(In i);
    void visit(LessEqual le);
    void visit(LessThan lt);
    void visit(NotEquals ne);
    void visit(NotIn ni);

    // utility
    void visit(DateOperation d);
    void visit(DateTimeOperation dt);
    void visit(DefOperation d);
    void visit(EndForAll efa);
    void visit(EndForEach efe);
    void visit(ExpressionSeparator es);
    void visit(ForAll fa);
    void visit(ForEach fe);
    void visit(Function f);
    void visit(Invoke i);
    void visit(LeftPriority lp);
    void visit(LetOperation l);
    void visit(PrintOperation p);
    void visit(RightPriority rp);
    void visit(Today t);
}
