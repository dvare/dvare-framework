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

import java.util.List;
import java.util.stream.Collectors;

public class BaseExpressionVisitor implements ExpressionVisitor<Expression> {
    @Override
    public Expression visit(FunctionExpression f) {
        var name = f.name.accept(this);
        var n = new FunctionExpression(name, f.getBinding());
        var nps = transformedList(f.getParameters());
        n.setParameters(nps);
        return n;
    }

    @Override
    public Expression visit(NamedExpression n) {
        return n;
    }

    @Override
    public Expression visit(BooleanExpression b) {
        return b;
    }

    @Override
    public Expression visit(BooleanListType bl) {
        return bl;
    }

    @Override
    public Expression visit(BooleanType b) {
        return b;
    }

    @Override
    public Expression visit(DateListType dl) {
        return dl;
    }

    @Override
    public Expression visit(DateTimeListType dtl) {
        return dtl;
    }

    @Override
    public Expression visit(ListType l) {
        return l;
    }

    @Override
    public Expression visit(DateTimeType dt) {
        return dt;
    }

    @Override
    public Expression visit(DateType d) {
        return d;
    }

    @Override
    public Expression visit(FloatListType fl) {
        return fl;
    }

    @Override
    public Expression visit(FloatType f) {
        return f;
    }

    @Override
    public Expression visit(IntegerListType il) {
        return il;
    }

    @Override
    public Expression visit(IntegerType i) {
        return i;
    }

    @Override
    public Expression visit(NullType n) {
        return n;
    }

    @Override
    public Expression visit(PairListType pl) {
        return pl;
    }

    @Override
    public Expression visit(PairType p) {
        return p;
    }

    @Override
    public Expression visit(RegexType r) {
        return r;
    }

    @Override
    public Expression visit(SimpleDateListType sdl) {
        return sdl;
    }

    @Override
    public Expression visit(SimpleDateType sd) {
        return sd;
    }

    @Override
    public Expression visit(StringListType sl) {
        return sl;
    }

    @Override
    public Expression visit(StringType s) {
        return s;
    }

    @Override
    public Expression visit(TripleListType tl) {
        return tl;
    }

    @Override
    public Expression visit(TripleType t) {
        return t;
    }

    @Override
    public Expression visit(BooleanLiteral b) {
        return b;
    }

    @Override
    public Expression visit(DateLiteral d) {
        return d;
    }

    @Override
    public Expression visit(DateTimeLiteral dt) {
        return dt;
    }

    @Override
    public Expression visit(FloatLiteral f) {
        return f;
    }

    @Override
    public Expression visit(IntegerLiteral i) {
        return i;
    }

    @Override
    public Expression visit(ListLiteral l) {
        return l;
    }

    @Override
    public Expression visit(NullLiteral<?> n) {
        return n;
    }

    @Override
    public Expression visit(PairLiteral p) {
        return p;
    }

    @Override
    public Expression visit(RegexLiteral r) {
        return r;
    }

    @Override
    public Expression visit(SimpleDateLiteral sd) {
        return sd;
    }

    @Override
    public Expression visit(StringLiteral s) {
        return s;
    }

    @Override
    public Expression visit(TripleLiteral t) {
        return t;
    }

    @Override
    public Expression visit(BooleanVariable b) {
        return b;
    }

    @Override
    public Expression visit(DateTimeVariable dt) {
        return dt;
    }

    @Override
    public Expression visit(DateVariable d) {
        return d;
    }

    @Override
    public Expression visit(FloatVariable f) {
        return f;
    }

    @Override
    public Expression visit(IntegerVariable i) {
        return i;
    }

    @Override
    public Expression visit(ListVariable l) {
        return l;
    }

    @Override
    public Expression visit(PairVariable p) {
        return p;
    }

    @Override
    public Expression visit(RegexVariable r) {
        return r;
    }

    @Override
    public Expression visit(SimpleDateVariable sd) {
        return sd;
    }

    @Override
    public Expression visit(StringVariable s) {
        return s;
    }

    @Override
    public Expression visit(TripleVariable t) {
        return t;
    }

    @Override
    public Expression visit(AssignOperationExpression a) {
        var n = new AssignOperationExpression();
        setLeftRightOperandNotNull(a, n);
        return n;
    }

    @Override
    public Expression visit(CompositeOperationExpression c) {
        var nes = transformedList(c.getExpressions());
        return new CompositeOperationExpression(nes);
    }

    @Override
    public Expression visit(IterationOperationExpression i) {
        var n = new IterationOperationExpression(i.getOperationType());
        setIterationOperation(i, n);
        return n;
    }

    @Override
    public Expression visit(ListLiteralOperationENDExpression lle) {
        var n = new ListLiteralOperationENDExpression();
        setLeftRightOperandNotNull(lle, n);
        return n;
    }

    @Override
    public Expression visit(ListLiteralOperationExpression ll) {
        var n = new ListLiteralOperationExpression();
        setListLiteralOperation(ll, n);
        return n;
    }

    @Override
    public Expression visit(Maximum m) {
        var n = new Maximum();
        setAggregationOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(Mean m) {
        var n = new Mean();
        setAggregationOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(Median m) {
        var n = new Median();
        setAggregationOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(Minimum m) {
        var n = new Minimum();
        setAggregationOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(Mode m) {
        var n = new Mode();
        setAggregationOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(Sum s) {
        var n = new Sum();
        setAggregationOperation(s, n);
        return n;
    }

    @Override
    public Expression visit(Add a) {
        var n = new Add();
        setArithmeticOperation(a, n);
        return n;
    }

    @Override
    public Expression visit(Divide d) {
        var n = new Divide();
        setArithmeticOperation(d, n);
        return n;
    }

    @Override
    public Expression visit(Max m) {
        var n = new Max();
        setArithmeticOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(Min m) {
        var n = new Min();
        setArithmeticOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(Multiply m) {
        var n = new Multiply();
        setArithmeticOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(Power p) {
        var n = new Power();
        setArithmeticOperation(p, n);
        return n;
    }

    @Override
    public Expression visit(Subtract s) {
        var n = new Subtract();
        setArithmeticOperation(s, n);
        return n;
    }

    @Override
    public Expression visit(ELSE e) {
        var n = new ELSE();
        setConditionOperation(n, e);
        return n;
    }

    @Override
    public Expression visit(ENDIF e) {
        var n = new ENDIF();
        setConditionOperation(e, n);
        return n;
    }

    @Override
    public Expression visit(IF i) {
        var n = new IF();
        setConditionOperation(i, n);
        return n;
    }

    @Override
    public Expression visit(TernaryOperation t) {
        var n = new TernaryOperation();
        setConditionOperation(t, n);
        return n;
    }

    @Override
    public Expression visit(THEN t) {
        var n = new THEN();
        setConditionOperation(t, n);
        return n;
    }

    @Override
    public Expression visit(CombinationExists ce) {
        var n = new CombinationExists();
        setMatch(n, ce);
        return n;
    }

    @Override
    public Expression visit(FilterOperation f) {
        var n = new FilterOperation();
        setAggregationOperation(f, n);
        return n;
    }

    @Override
    public Expression visit(First f) {
        var n = new First();
        setAggregationOperation(f, n);
        return n;
    }

    @Override
    public Expression visit(GetItem gi) {
        var n = new GetItem();
        setAggregationOperation(gi, n);
        return n;
    }

    @Override
    public Expression visit(HasItem hi) {
        var n = new HasItem();
        setAggregationOperation(hi, n);
        return n;
    }

    @Override
    public Expression visit(InsideCombination ic) {
        var n = new InsideCombination();
        setMatch(ic, n);
        return n;
    }

    @Override
    public Expression visit(InsideExistsCombination iec) {
        var n = new InsideExistsCombination();
        setMatch(iec, n);
        return n;
    }

    @Override
    public Expression visit(IsEmpty ie) {
        var n = new IsEmpty();
        setAggregationOperation(ie, n);
        return n;
    }

    @Override
    public Expression visit(ItemPosition ip) {
        var n = new ItemPosition();
        setAggregationOperation(ip, n);
        return n;
    }

    @Override
    public Expression visit(KeysOperation k) {
        var n = new KeysOperation();
        setAggregationOperation(k, n);
        return n;
    }

    @Override
    public Expression visit(Last l) {
        var n = new Last();
        setAggregationOperation(l, n);
        return n;
    }

    @Override
    public Expression visit(MapOperation m) {
        var n = new MapOperation();
        setAggregationOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(Match m) {
        var n = new Match();
        setMatch(m, n);
        return n;
    }

    @Override
    public Expression visit(MiddlesOperation m) {
        var n = new MiddlesOperation();
        setAggregationOperation(m, n);
        return n;
    }

    @Override
    public Expression visit(NotEmpty ne) {
        var n = new NotEmpty();
        setAggregationOperation(ne, n);
        return n;
    }

    @Override
    public Expression visit(PairOperation p) {
        var n = new PairOperation();
        setAggregationOperation(p, n);
        return n;
    }

    @Override
    public Expression visit(SizeOperation s) {
        var n = new SizeOperation();
        setAggregationOperation(s, n);
        return n;
    }

    @Override
    public Expression visit(SortOperation s) {
        var n = new SortOperation();
        setAggregationOperation(s, n);
        return n;
    }

    @Override
    public Expression visit(TripleOperation t) {
        var n = new TripleOperation();
        setAggregationOperation(t, n);
        return n;
    }

    @Override
    public Expression visit(ValuesOperation v) {
        var n = new ValuesOperation();
        setAggregationOperation(v, n);
        return n;
    }

    @Override
    public Expression visit(And a) {
        var n = new Add();
        setLeftRightOperandNotNull(a, n);
        return n;
    }

    @Override
    public Expression visit(Implies i) {
        var n = new Implies();
        setLeftRightOperandNotNull(i, n);
        return n;
    }

    @Override
    public Expression visit(Not n) {
        var ne = new Not();
        setRightOperandNotNull(n, ne);
        return n;
    }

    @Override
    public Expression visit(OR o) {
        var n = new OR();
        setLeftRightOperandNotNull(o, n);
        return n;
    }

    @Override
    public Expression visit(AddDays ad) {
        var n = new AddDays();
        setChainOperation(ad, n);
        return n;
    }

    @Override
    public Expression visit(AddMonths am) {
        var n = new AddMonths();
        setChainOperation(am, n);
        return n;
    }

    @Override
    public Expression visit(AddYears ay) {
        var n = new AddYears();
        setChainOperation(ay, n);
        return n;
    }

    @Override
    public Expression visit(Append a) {
        var n = new Append();
        setChainOperation(a, n);
        return n;
    }

    @Override
    public Expression visit(Contains c) {
        var n = new Contains();
        setChainOperation(c, n);
        return n;
    }

    @Override
    public Expression visit(EndsWith ew) {
        var n = new EndsWith();
        setChainOperation(ew, n);
        return n;
    }

    @Override
    public Expression visit(GetYear gy) {
        var n = new GetYear();
        setChainOperation(gy, n);
        return n;
    }

    @Override
    public Expression visit(Length l) {
        var n = new Length();
        setChainOperation(l, n);
        return n;
    }

    @Override
    public Expression visit(Prepend p) {
        var n = new Prepend();
        setChainOperation(p, n);
        return n;
    }

    @Override
    public Expression visit(SetDay sd) {
        var n = new SetDay();
        setChainOperation(sd, n);
        return n;
    }

    @Override
    public Expression visit(SetMonth sm) {
        var n = new SetMonth();
        setChainOperation(sm, n);
        return n;
    }

    @Override
    public Expression visit(SetYear sy) {
        var n = new SetYear();
        setChainOperation(sy, n);
        return n;
    }

    @Override
    public Expression visit(StartsWith sw) {
        var n = new StartsWith();
        setChainOperation(sw, n);
        return n;
    }

    @Override
    public Expression visit(SubDays sd) {
        var n = new SubDays();
        setChainOperation(sd, n);
        return n;
    }

    @Override
    public Expression visit(SubMonths sm) {
        var n = new SubMonths();
        setChainOperation(sm, n);
        return n;
    }

    @Override
    public Expression visit(SubYears sy) {
        var n = new SubYears();
        setChainOperation(sy, n);
        return n;
    }

    @Override
    public Expression visit(Substring s) {
        var n = new Substring();
        setChainOperation(s, n);
        return n;
    }

    @Override
    public Expression visit(ToBoolean tb) {
        var n = new ToBoolean();
        setChainOperation(tb, n);
        return n;
    }

    @Override
    public Expression visit(ToDate td) {
        var n = new ToDate();
        setChainOperation(td, n);
        return n;
    }

    @Override
    public Expression visit(ToInteger ti) {
        var n = new ToInteger();
        setChainOperation(ti, n);
        return n;
    }

    @Override
    public Expression visit(ToKey tk) {
        var n = new ToKey();
        setChainOperation(tk, n);
        return n;
    }

    @Override
    public Expression visit(ToLeft tl) {
        var n = new ToLeft();
        setChainOperation(tl, n);
        return n;
    }

    @Override
    public Expression visit(ToMiddle tm) {
        var n = new ToMiddle();
        setChainOperation(tm, n);
        return n;
    }

    @Override
    public Expression visit(ToPair tp) {
        var n = new ToPair();
        setChainOperation(tp, n);
        return n;
    }

    @Override
    public Expression visit(ToRight tr) {
        var n = new ToRight();
        setChainOperation(tr, n);
        return n;
    }

    @Override
    public Expression visit(ToString ts) {
        var n = new ToString();
        setChainOperation(ts, n);
        return n;
    }

    @Override
    public Expression visit(ToTriple tt) {
        var n = new ToTriple();
        setChainOperation(tt, n);
        return n;
    }

    @Override
    public Expression visit(ToValue tv) {
        var n = new ToValue();
        setChainOperation(tv, n);
        return n;
    }

    @Override
    public Expression visit(Trim t) {
        var n = new Trim();
        setChainOperation(t, n);
        return n;
    }

    @Override
    public Expression visit(Between b) {
        var n = new Between();
        setRelationalOperator(b, n);
        return n;
    }

    @Override
    public Expression visit(Equals e) {
        var n = new Equals();
        setRelationalOperator(e, n);
        return n;
    }

    @Override
    public Expression visit(GreaterEqual ge) {
        var n = new GreaterEqual();
        setRelationalOperator(ge, n);
        return n;
    }

    @Override
    public Expression visit(GreaterThan gt) {
        var n = new GreaterThan();
        setRelationalOperator(gt, n);
        return n;
    }

    @Override
    public Expression visit(In i) {
        var n = new In();
        setRelationalOperator(i, n);
        return n;
    }

    @Override
    public Expression visit(LessEqual le) {
        var n = new LessEqual();
        setRelationalOperator(le, n);
        return n;
    }

    @Override
    public Expression visit(LessThan lt) {
        var n = new LessThan();
        setRelationalOperator(lt, n);
        return n;
    }

    @Override
    public Expression visit(NotEquals ne) {
        var n = new NotEquals();
        setRelationalOperator(ne, n);
        return n;
    }

    @Override
    public Expression visit(NotIn ni) {
        var n = new NotIn();
        setRelationalOperator(ni, n);
        return n;
    }

    @Override
    public Expression visit(DateOperation d) {
        var n = new DateOperation();
        setLeftRightOperandNotNull(d, n);
        return n;
    }

    @Override
    public Expression visit(DateTimeOperation dt) {
        var n = new DateTimeOperation();
        setLeftRightOperandNotNull(dt, n);
        return n;
    }

    @Override
    public Expression visit(DefOperation d) {
        var n = new DefOperation();
        setLeftRightOperandNotNull(d, n);
        return n;
    }

    @Override
    public Expression visit(EndForAll efa) {
        var n = new EndForAll();
        setIterationOperation(efa, n);
        return n;
    }

    @Override
    public Expression visit(EndForEach efe) {
        var n = new EndForEach();
        setIterationOperation(efe, n);
        return n;
    }

    @Override
    public Expression visit(ExpressionSeparator es) {
        var n = new ExpressionSeparator();
        setLeftRightOperandNotNull(es, n);
        return n;
    }

    @Override
    public Expression visit(ForAll fa) {
        var n = new ForAll();
        setIterationOperation(fa, n);
        return n;
    }

    @Override
    public Expression visit(ForEach fe) {
        var n = new ForEach();
        setIterationOperation(fe, n);
        return n;
    }

    @Override
    public Expression visit(Function f) {
        var n = new Function();
        setLeftRightOperandNotNull(f, n);
        return n;
    }

    @Override
    public Expression visit(Invoke i) {
        var n = new Invoke();
        setLeftRightOperandNotNull(i, n);
        return n;
    }

    @Override
    public Expression visit(LeftPriority lp) {
        var n = new LeftPriority();
        setLeftRightOperandNotNull(lp, n);
        return n;
    }

    @Override
    public Expression visit(LetOperation l) {
        var n = new LetOperation();
        setLeftRightOperandNotNull(l, n);
        return n;
    }

    @Override
    public Expression visit(PrintOperation p) {
        var n = new PrintOperation();
        setLeftRightOperandNotNull(p, n);
        return n;
    }

    @Override
    public Expression visit(RightPriority rp) {
        var n = new RightPriority();
        setLeftRightOperandNotNull(rp, n);
        return n;
    }

    @Override
    public Expression visit(Today t) {
        var n = new Today();
        setLeftRightOperandNotNull(t, n);
        return n;
    }

    protected void setLeftRightOperandNotNull(OperationExpression o, OperationExpression n) {
        setLeftOperandNotNull(o, n);
        setRightOperandNotNull(o, n);
    }

    protected void setLeftOperandNotNull(OperationExpression o, OperationExpression n) {
        if (o.getLeftOperand() != null) {
            n.setLeftOperand(o.getLeftOperand().accept(this));
        }
    }

    protected void setRightOperandNotNull(OperationExpression o, OperationExpression n) {
        if (o.getRightOperand() != null) {
            n.setRightOperand(o.getRightOperand().accept(this));
        }
    }

    protected void setListLiteralOperation(ListLiteralOperationExpression o, ListLiteralOperationExpression n) {
        if (o.getRightListOperand() != null) {
            var nrl = transformedList(o.getRightListOperand());
            n.setRightListOperand(nrl);
        }
    }

    protected void setAggregationOperation(AggregationOperationExpression o, AggregationOperationExpression n) {
        setLeftOperandNotNull(o, n);
        if (o.getRightListOperand() != null) {
            var rls = transformedList(o.getRightListOperand());
            n.setRightListOperand(rls);
        }
    }

    protected void setArithmeticOperation(ArithmeticOperationExpression o, ArithmeticOperationExpression n) {
        setLeftRightOperandNotNull(o, n);
    }

    protected void setConditionOperation(ConditionOperationExpression o, ConditionOperationExpression n) {
        setLeftRightOperandNotNull(o, n);

        if (o.getCondition() != null) {
            n.setCondition(o.getCondition().accept(this));
        }

        if (o.getThenOperand() != null) {
            n.setThenOperand(o.getThenOperand().accept(this));
        }

        if (o.getElseOperand() != null) {
            n.setElseOperand(o.getThenOperand().accept(this));
        }
    }

    protected void setMatch(Match o, Match n) {
        if (o.getLeftListOperand() != null) {
            var lls = transformedList(o.getLeftListOperand());
            n.setLeftListOperand(lls);
        }
    }

    protected void setChainOperation(ChainOperationExpression o, ChainOperationExpression n) {
        setLeftOperandNotNull(o, n);
        if (o.getRightListOperand() != null) {
            var rls = transformedList(o.getRightListOperand());
            n.setRightListOperand(rls);
        }
    }

    protected void setRelationalOperator(RelationalOperationExpression o, RelationalOperationExpression n) {
        setLeftRightOperandNotNull(o, n);
    }

    protected void setIterationOperation(IterationOperationExpression o, IterationOperationExpression n) {
        if (o.getReferenceContext() != null) {
            var rc = o.getReferenceContext().accept(this);
            n.setReferenceContext(rc);
        }
        if (o.getDerivedContext() != null) {
            var dc = o.getDerivedContext().accept(this);
            n.setDerivedContext((NamedExpression) dc);
        }
        setLeftRightOperandNotNull(o, n);
    }

    protected List<Expression> transformedList(List<Expression> l) {
        return l.stream()
                .map(e -> e.accept(this))
                .collect(Collectors.toList());
    }
}