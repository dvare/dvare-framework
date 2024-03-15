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

public abstract class BaseExpressionVisitor implements ExpressionVisitor {

    @Override
    public void visit(Expression e) {}

    @Override
    public void visit(FunctionExpression f) {}

    @Override
    public void visit(NamedExpression n) {}

    @Override
    public void visit(BooleanExpression b) {}

    @Override
    public void visit(BooleanListType bl) {}

    @Override
    public void visit(BooleanType b) {}

    @Override
    public void visit(DataTypeExpression dt) {}

    @Override
    public void visit(DateListType dl) {}

    @Override
    public void visit(DateTimeListType dtl) {}

    @Override
    public void visit(ListType l) {}

    @Override
    public void visit(DateTimeType dt) {}

    @Override
    public void visit(DateType d) {}

    @Override
    public void visit(FloatListType fl) {}

    @Override
    public void visit(FloatType f) {}

    @Override
    public void visit(IntegerListType il) {}

    @Override
    public void visit(IntegerType i) {}

    @Override
    public void visit(NullType n) {}

    @Override
    public void visit(PairListType pl) {}

    @Override
    public void visit(PairType p) {}

    @Override
    public void visit(RegexType r) {}

    @Override
    public void visit(SimpleDateListType sdl) {}

    @Override
    public void visit(SimpleDateType sd) {}

    @Override
    public void visit(StringListType sl) {}

    @Override
    public void visit(StringType s) {}

    @Override
    public void visit(TripleListType tl) {}

    @Override
    public void visit(TripleType t) {}

    @Override
    public void visit(LiteralExpression<?> l) {}

    @Override
    public void visit(BooleanLiteral b) {}

    @Override
    public void visit(DateLiteral d) {}

    @Override
    public void visit(DateTimeLiteral dt) {}

    @Override
    public void visit(FloatLiteral f) {}

    @Override
    public void visit(IntegerLiteral i) {}

    @Override
    public void visit(ListLiteral l) {}

    @Override
    public void visit(NullLiteral<?> n) {}

    @Override
    public void visit(PairLiteral p) {}

    @Override
    public void visit(RegexLiteral r) {}

    @Override
    public void visit(SimpleDateLiteral sd) {}

    @Override
    public void visit(StringLiteral s) {}

    @Override
    public void visit(TripleLiteral t) {}

    @Override
    public void visit(VariableExpression<?> v) {}

    @Override
    public void visit(BooleanVariable b) {}

    @Override
    public void visit(DateTimeVariable dt) {}

    @Override
    public void visit(DateVariable d) {}

    @Override
    public void visit(FloatVariable f) {}

    @Override
    public void visit(IntegerVariable i) {}

    @Override
    public void visit(ListVariable l) {}

    @Override
    public void visit(PairVariable p) {}

    @Override
    public void visit(RegexVariable r) {}

    @Override
    public void visit(SimpleDateVariable sd) {}

    @Override
    public void visit(StringVariable s) {}

    @Override
    public void visit(TripleVariable t) {}

    @Override
    public void visit(OperationExpression o) {}

    @Override
    public void visit(AssignOperationExpression a) {}

    @Override
    public void visit(ChainOperationExpression c) {}

    @Override
    public void visit(ConditionOperationExpression c) {}

    @Override
    public void visit(CompositeOperationExpression c) {}

    @Override
    public void visit(IterationOperationExpression i) {}

    @Override
    public void visit(ListLiteralOperationENDExpression lle) {}

    @Override
    public void visit(ListLiteralOperationExpression ll) {}

    @Override
    public void visit(ListOperationExpression l) {}

    @Override
    public void visit(LogicalOperationExpression l) {}

    @Override
    public void visit(AggregationOperationExpression a) {}

    @Override
    public void visit(Maximum m) {}

    @Override
    public void visit(Mean m) {}

    @Override
    public void visit(Median m) {}

    @Override
    public void visit(Minimum m) {}

    @Override
    public void visit(Mode m) {}

    @Override
    public void visit(Sum s) {}

    @Override
    public void visit(ArithmeticOperationExpression e) {}

    @Override
    public void visit(Add a) {}

    @Override
    public void visit(Divide d) {}

    @Override
    public void visit(Max m) {}

    @Override
    public void visit(Min m) {}

    @Override
    public void visit(Multiply m) {}

    @Override
    public void visit(Power p) {}

    @Override
    public void visit(Subtract s) {}

    @Override
    public void visit(ELSE e) {}

    @Override
    public void visit(ENDIF e) {}

    @Override
    public void visit(IF i) {}

    @Override
    public void visit(TernaryOperation t) {}

    @Override
    public void visit(THEN t) {}

    @Override
    public void visit(CombinationExists ce) {}

    @Override
    public void visit(FilterOperation f) {}

    @Override
    public void visit(First f) {}

    @Override
    public void visit(GetItem gi) {}

    @Override
    public void visit(HasItem hi) {}

    @Override
    public void visit(InsideCombination ic) {}

    @Override
    public void visit(InsideExistsCombination iec) {}

    @Override
    public void visit(IsEmpty ie) {}

    @Override
    public void visit(ItemPosition ip) {}

    @Override
    public void visit(KeysOperation k) {}

    @Override
    public void visit(Last l) {}

    @Override
    public void visit(MapOperation m) {}

    @Override
    public void visit(Match m) {}

    @Override
    public void visit(MiddlesOperation m) {}

    @Override
    public void visit(NotEmpty ne) {}

    @Override
    public void visit(PairOperation p) {}

    @Override
    public void visit(SizeOperation s) {}

    @Override
    public void visit(SortOperation s) {}

    @Override
    public void visit(TripleOperation t) {}

    @Override
    public void visit(ValuesOperation v) {}

    @Override
    public void visit(And a) {}

    @Override
    public void visit(Implies i) {}

    @Override
    public void visit(Not n) {}

    @Override
    public void visit(OR o) {}

    @Override
    public void visit(AddDays ad) {}

    @Override
    public void visit(AddMonths am) {}

    @Override
    public void visit(AddYears ay) {}

    @Override
    public void visit(Append a) {}

    @Override
    public void visit(Contains c) {}

    @Override
    public void visit(EndsWith ew) {}

    @Override
    public void visit(GetYear gy) {}

    @Override
    public void visit(Length l) {}

    @Override
    public void visit(Prepend p) {}

    @Override
    public void visit(SetDay sd) {}

    @Override
    public void visit(SetMonth sm) {}

    @Override
    public void visit(SetYear sy) {}

    @Override
    public void visit(StartsWith sw) {}

    @Override
    public void visit(SubDays sd) {}

    @Override
    public void visit(SubMonths sm) {}

    @Override
    public void visit(SubYears sy) {}

    @Override
    public void visit(Substring s) {}

    @Override
    public void visit(ToBoolean tb) {}

    @Override
    public void visit(ToDate td) {}

    @Override
    public void visit(ToInteger ti) {}

    @Override
    public void visit(ToKey tk) {}

    @Override
    public void visit(ToLeft tl) {}

    @Override
    public void visit(ToMiddle tm) {}

    @Override
    public void visit(ToPair tp) {}

    @Override
    public void visit(ToRight tr) {}

    @Override
    public void visit(ToString ts) {}

    @Override
    public void visit(ToTriple tt) {}

    @Override
    public void visit(ToValue tv) {}

    @Override
    public void visit(Trim t) {}

    @Override
    public void visit(RelationalOperationExpression r) {}

    @Override
    public void visit(Between b) {}

    @Override
    public void visit(Equals e) {}

    @Override
    public void visit(GreaterEqual ge) {}

    @Override
    public void visit(GreaterThan gt) {}

    @Override
    public void visit(In i) {}

    @Override
    public void visit(LessEqual le) {}

    @Override
    public void visit(LessThan lt) {}

    @Override
    public void visit(NotEquals ne) {}

    @Override
    public void visit(NotIn ni) {}

    @Override
    public void visit(DateOperation d) {}

    @Override
    public void visit(DateTimeOperation dt) {}

    @Override
    public void visit(DefOperation d) {}

    @Override
    public void visit(EndForAll efa) {}

    @Override
    public void visit(EndForEach efe) {}

    @Override
    public void visit(ExpressionSeparator es) {}

    @Override
    public void visit(ForAll fa) {}

    @Override
    public void visit(ForEach fe) {}

    @Override
    public void visit(Function f) {}

    @Override
    public void visit(Invoke i) {}

    @Override
    public void visit(LeftPriority lp) {}

    @Override
    public void visit(LetOperation l) {}

    @Override
    public void visit(PrintOperation p) {}

    @Override
    public void visit(RightPriority rp) {}

    @Override
    public void visit(Today t) {}
}
