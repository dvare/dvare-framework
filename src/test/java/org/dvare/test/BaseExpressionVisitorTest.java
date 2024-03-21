package org.dvare.test;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.*;
import org.dvare.expression.datatype.*;
import org.dvare.expression.literal.*;
import org.dvare.expression.operation.*;
import org.dvare.expression.operation.aggregation.*;
import org.dvare.expression.operation.arithmetic.*;
import org.dvare.expression.operation.flow.*;
import org.dvare.expression.operation.list.*;
import org.dvare.expression.operation.relational.Equals;
import org.dvare.expression.operation.relational.In;
import org.dvare.expression.operation.utility.Function;
import org.dvare.expression.operation.utility.PrintOperation;
import org.dvare.expression.veriable.*;
import org.dvare.util.Pair;
import org.dvare.util.Triple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class BaseExpressionVisitorTest {
    private final BaseExpressionVisitor v = new BaseExpressionVisitor();

    private String trimCollapseWhiteSpace(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }

    @Test
    public void visitFunction() {
        var f = new Function();
        var fe = new FunctionExpression("Func1", null);
        fe.addParameter(new NamedExpression("S1"));
        fe.addParameter(new NamedExpression("I1"));
        f.setLeftOperand(fe);

        var e = f.accept(v);

        Assertions.assertEquals(f.getClass(), e.getClass());
        var fn = (Function) e;

        Assertions.assertEquals(f.getOperationType(), fn.getOperationType());
        Assertions.assertEquals(f.getDataTypeExpression(), fn.getDataTypeExpression());
        Assertions.assertEquals(f.getLeftOperand().getClass(), fn.getLeftOperand().getClass());
        Assertions.assertNull(f.getRightOperand());
        Assertions.assertNull(fn.getRightOperand());

        var fne = (FunctionExpression) fn.getLeftOperand();
        Assertions.assertEquals(fe.name.toString(), fne.name.toString());
        Assertions.assertEquals(fe.getParameters().size(), fne.getParameters().size());
        for (var i = 0; i < fe.getParameters().size(); i++) {
            Assertions.assertEquals(fe.getParameters().get(i).toString(), fne.getParameters().get(i).toString());
        }

        Assertions.assertEquals(f.toString(), trimCollapseWhiteSpace(fn.toString()));
    }

    @Test
    public void visitFunctionExpression() {
        var o = new FunctionExpression("Func1", null);
        o.addParameter(new NamedExpression("S1"));
        o.addParameter(new NamedExpression("I1"));

        var e = o.accept(v);

        Assertions.assertEquals(o.getClass(), e.getClass());
        var n = (FunctionExpression) e;

        Assertions.assertEquals(o.name.toString(), n.name.toString());
        Assertions.assertEquals(o.getParameters().size(), n.getParameters().size());
        for (var i = 0; i < o.getParameters().size(); i++) {
            Assertions.assertEquals(o.getParameters().get(i).toString(), n.getParameters().get(i).toString());
        }
    }

    @Test
    public void visitNamedExpression() {
        var o = new NamedExpression("name");
        var e = o.accept(v);

        Assertions.assertEquals(o.getClass(), e.getClass());
        var n = (NamedExpression) e;

        Assertions.assertEquals(o.getName(), n.getName());
        Assertions.assertEquals(o.toString(), n.toString());
    }

    private void visitBooleanExpression(boolean val) {
        var o = new BooleanExpression("bn", val);
        var e = o.accept(v);

        Assertions.assertEquals(o.getClass(), e.getClass());
        var n = (BooleanExpression) e;

        Assertions.assertEquals(o.getName(), n.getName());
        Assertions.assertEquals(o.isValue(), n.isValue());

        Assertions.assertEquals(o.toString(), n.toString());
    }

    @Test
    public void visitBooleanExpression() {
        visitBooleanExpression(true);
        visitBooleanExpression(false);
    }

    private  <T extends DataTypeExpression> void visitDataTypeExpression(Class<T> clazz) {
        try {
            var o = clazz.getDeclaredConstructor().newInstance();
            var e = o.accept(v);
            Assertions.assertEquals(o.getClass(), e.getClass());
            var n = clazz.cast(e);
            Assertions.assertEquals(o.getDataType(), n.getDataType());
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void visitBooleanListType() {
        visitDataTypeExpression(BooleanListType.class);
    }

    @Test
    public void visitBooleanType() {
        visitDataTypeExpression(BooleanType.class);
    }

    @Test
    public void visitDateListType() {
        visitDataTypeExpression(DateListType.class);
    }

    @Test
    public void visitDateTimeListType() {
        visitDataTypeExpression(DateTimeListType.class);
    }

    @Test
    public void visitListType() {
        visitDataTypeExpression(ListType.class);
    }

    @Test
    public void visitDateTimeType() {
        visitDataTypeExpression(DateTimeType.class);
    }

    @Test
    public void visitDateType() {
        visitDataTypeExpression(DateType.class);
    }

    @Test
    public void visitFloatListType() {
        visitDataTypeExpression(FloatListType.class);
    }

    @Test
    public void visitFloatType() {
        visitDataTypeExpression(FloatType.class);
    }

    @Test
    public void visitIntegerListType() {
        visitDataTypeExpression(IntegerListType.class);
    }

    @Test
    public void visitIntegerType() {
        visitDataTypeExpression(IntegerType.class);
    }

    @Test
    public void visitNullType() {
        visitDataTypeExpression(NullType.class);
    }

    @Test
    public void visitPairListType() {
        visitDataTypeExpression(PairListType.class);
    }

    @Test
    public void visitPairType() {
        visitDataTypeExpression(PairType.class);
    }

    @Test
    public void visitRegexType() {
        visitDataTypeExpression(RegexType.class);
    }

    @Test
    public void visitSimpleDateListType() {
        visitDataTypeExpression(SimpleDateListType.class);
    }

    @Test
    public void visitSimpleDateType() {
        visitDataTypeExpression(SimpleDateType.class);
    }

    @Test
    public void visitStringListType() {
        visitDataTypeExpression(StringListType.class);
    }

    @Test
    public void visitStringType() {
        visitDataTypeExpression(StringType.class);
    }

    @Test
    public void visitTripleListType() {
        visitDataTypeExpression(TripleListType.class);
    }

    @Test
    public void visitTripleType() {
        visitDataTypeExpression(TripleType.class);
    }

    private <T extends LiteralExpression<?>> T visitLiteralExpression(T o) {
        var e = o.accept(v);
        Assertions.assertEquals(o.getClass(), e.getClass());
        @SuppressWarnings("unchecked") var n = (T) e;

        Assertions.assertEquals(o.getType(), n.getType());
        Assertions.assertEquals(o.getValue(), n.getValue());

        Assertions.assertEquals(o.toString(), n.toString());

        return n;
    }

    @Test
    public void visitBooleanLiteral() {
        visitLiteralExpression(new BooleanLiteral(true));
        visitLiteralExpression(new BooleanLiteral(false));
    }

    @Test
    public void visitDateLiteral() {
        visitLiteralExpression(new DateLiteral(LocalDate.now()));
    }

    @Test
    public void visitDateTimeLiteral() {
        visitLiteralExpression(new DateTimeLiteral(LocalDateTime.now()));
    }

    @Test
    public void visitFloatLiteral() {
        visitLiteralExpression(new FloatLiteral(13.8F));
    }

    @Test
    public void visitIntegerLiteral() {
        visitLiteralExpression(new IntegerLiteral(43));
    }

    @Test
    public void visitListLiteral() {
        var l = List.of(0, 1, 2, 3);
        var o = new ListLiteral(l, IntegerType.class);
        var n = visitLiteralExpression(o);
        Assertions.assertEquals(o.getListType(), n.getListType());
        Assertions.assertEquals(o.getSize(), n.getSize());
    }

    @Test
    public void visitNullLiteral() {
        visitLiteralExpression(new NullLiteral<>());
    }

    @Test
    public void visitPairLiteral() {
        var o = new Pair.PairImpl<>("A", "B");
        var n = visitLiteralExpression(new PairLiteral(o));
        Assertions.assertEquals(o.left, n.getValue().getLeft());
        Assertions.assertEquals(o.right, n.getValue().getRight());
    }

    @Test
    public void visitRegexLiteral() {
        visitLiteralExpression(new RegexLiteral("\\s+"));
    }

    @Test
    public void visitSimpleDateLiteral() {
        visitLiteralExpression(new SimpleDateLiteral(new Date()));
    }

    @Test
    public void visitStringLiteral() {
        visitLiteralExpression(new StringLiteral("abcdefg"));
    }

    @Test
    public void visitTripleLiteral() {
        var o = new Triple.TripleImpl<>("A", "B", "C");
        var n = visitLiteralExpression(new TripleLiteral(o));
        Assertions.assertEquals(o.left, n.getValue().getLeft());
        Assertions.assertEquals(o.middle, n.getValue().getMiddle());
        Assertions.assertEquals(o.right, n.getValue().getRight());
    }

    private <T extends VariableExpression<?>> T visitVariableExpression(T o) {
        var e = o.accept(v);
        Assertions.assertEquals(o.getClass(), e.getClass());
        @SuppressWarnings("unchecked") var n = (T) e;

        Assertions.assertEquals(o.getName(), n.getName());
        Assertions.assertEquals(o.getType(), n.getType());
        Assertions.assertEquals(o.getValue(), n.getValue());
        Assertions.assertEquals(o.getOperandType(), n.getOperandType());

        Assertions.assertEquals(o.toString(), n.toString());

        return n;
    }

    @Test
    public void visitBooleanVariable() {
        visitVariableExpression(new BooleanVariable("b1", true));
        visitVariableExpression(new BooleanVariable("b1", false));
    }

    @Test
    public void visitDateTimeVariable() {
        visitVariableExpression(new DateTimeVariable("dt", LocalDateTime.now()));
    }

    @Test
    public void visitDateVariable() {
        visitVariableExpression(new DateVariable("d", LocalDate.now()));
    }

    @Test
    public void visitFloatVariable() {
        visitVariableExpression(new FloatVariable("f", 0.123F));
    }

    @Test
    public void visitIntegerVariable() {
        visitVariableExpression(new IntegerVariable("i", 8213));
    }

    @Test
    public void visitListVariable() {
        var l = List.of(0, 1, 2, 3);
        var o = new ListVariable("l", IntegerType.class);
        o.setValue(l);
        var n = visitVariableExpression(o);
        Assertions.assertEquals(o.getListType(), n.getListType());
        Assertions.assertEquals(o.getSize(), n.getSize());
        for (var i = 0; i < l.size(); i++) {
            Assertions.assertEquals(o.getValue().get(0), n.getValue().get(0));
        }
    }

    @Test
    public void visitPairVariable() {
        var p = new Pair.PairImpl<>("A", "B");
        var o = new PairVariable("o");
        o.setValue(p);
        var n = visitVariableExpression(o);
        Assertions.assertEquals(o.getValue().getLeft(), n.getValue().getLeft());
        Assertions.assertEquals(o.getValue().getRight(), n.getValue().getRight());
    }

    @Test
    public void visitRegexVariable() {
        visitVariableExpression(new RegexVariable("r", "\\s+"));
    }

    @Test
    public void visitSimpleDateVariable() {
        visitVariableExpression(new SimpleDateVariable("sd", new Date()));
    }

    @Test
    public void visitStringVariable() {
        visitVariableExpression(new StringVariable("s", "value"));
    }

    @Test
    public void visitTripleVariable() {
        var t = new Triple.TripleImpl<>("A", "B", "C");
        var o = new TripleVariable("t");
        o.setValue(t);
        var n = visitVariableExpression(o);
        Assertions.assertEquals(o.getValue().getLeft(), n.getValue().getLeft());
        Assertions.assertEquals(o.getValue().getMiddle(), n.getValue().getMiddle());
        Assertions.assertEquals(o.getValue().getRight(), n.getValue().getRight());
    }


    @Test
    public void visitAssignOperationExpression() {
        var ol = new StringVariable("s");
        var or = new StringLiteral("value");
        var o = new AssignOperationExpression();
        o.setLeftOperand(ol);
        o.setRightOperand(or);

        var e = o.accept(v);

        Assertions.assertEquals(o.getClass(), e.getClass());
        var n = (AssignOperationExpression) e;

        Assertions.assertEquals(o.getLeftOperand().getClass(), n.getLeftOperand().getClass());
        Assertions.assertEquals(o.getRightOperand().getClass(), n.getRightOperand().getClass());

        var nl = (StringVariable) n.getLeftOperand();
        Assertions.assertEquals(ol.getName(), nl.getName());
        Assertions.assertEquals(ol.getType(), nl.getType());
        Assertions.assertEquals(ol.getValue(), nl.getValue());
        Assertions.assertEquals(ol.getOperandType(), nl.getOperandType());

        var nr = (StringLiteral) n.getRightOperand();
        Assertions.assertEquals(or.getType(), nr.getType());
        Assertions.assertEquals(or.getValue(), nr.getValue());

        Assertions.assertEquals(o.toString(), n.toString());
    }

    @Test
    public void visitCompositeOperationExpression() {
        List<Expression> ol = List.of(new StringLiteral("A"), new StringLiteral("B"), new StringLiteral("C"));
        var o = new CompositeOperationExpression(ol);

        var e = o.accept(v);

        Assertions.assertEquals(o.getClass(), e.getClass());
        var n = (CompositeOperationExpression) e;

        Assertions.assertEquals(o.getExpressions().size(), n.getExpressions().size());

        for (var i = 0; i < o.getExpressions().size(); i++) {
            var oe = o.getExpressions().get(i);
            var ne = n.getExpressions().get(i);
            Assertions.assertEquals(oe.getClass(), ne.getClass());

            var os = (StringLiteral) oe;
            var ns = (StringLiteral) ne;

            Assertions.assertEquals(os.getType(), ns.getType());
            Assertions.assertEquals(os.getValue(), ns.getValue());
        }

        Assertions.assertEquals(o.toString(), n.toString());
    }

    private <T extends IterationOperationExpression> T visitIterationOperationExpression(T o) {
        var e = o.accept(v);

        Assertions.assertEquals(o.getClass(), e.getClass());
        @SuppressWarnings("unchecked") var n = (T) e;

        Assertions.assertEquals(o.getOperationType(), n.getOperationType());
        Assertions.assertEquals(o.getDerivedContext().getName(), n.getDerivedContext().getName());
        Assertions.assertEquals(o.getReferenceContext().toString(), n.getReferenceContext().toString());

        Assertions.assertEquals(o.getLeftOperand().getClass(), n.getLeftOperand().getClass());
        Assertions.assertEquals(o.getRightOperand().getClass(), n.getRightOperand().getClass());

        Assertions.assertEquals(o.toString(), n.toString());

        return n;
    }

    @Test
    public void visitIterationOperationExpression() {
        var o = new IterationOperationExpression(OperationType.FORALL);
        o.setReferenceContext(new NamedExpression("ref"));
        o.setDerivedContext(new NamedExpression("der"));
        o.setLeftOperand(new PrintOperation());
        o.setRightOperand(new PrintOperation());

        visitIterationOperationExpression(o);
    }

    @Test
    public void visitListLiteralOperationENDExpression() {
        var o = new ListLiteralOperationENDExpression();
        var e = o.accept(v);
        Assertions.assertEquals(o.getClass(), e.getClass());
    }

    @Test
    public void visitListLiteralOperationExpression() {
        var o = new ListLiteralOperationExpression();
        List<Expression> ol = List.of(new StringLiteral("A"), new StringLiteral("B"), new StringLiteral("C"));
        o.setRightListOperand(ol);

        var e = o.accept(v);

        Assertions.assertEquals(o.getClass(), e.getClass());
        var n = (ListLiteralOperationExpression) e;

        Assertions.assertEquals(o.getSize(), n.getSize());

        for (var i = 0; i < o.getSize(); i++) {
            var oe = o.getRightListOperand().get(i);
            var ne = n.getRightListOperand().get(i);
            Assertions.assertEquals(oe.getClass(), ne.getClass());

            var os = (StringLiteral) oe;
            var ns = (StringLiteral) ne;

            Assertions.assertEquals(os.getType(), ns.getType());
            Assertions.assertEquals(os.getValue(), ns.getValue());
        }

        Assertions.assertEquals(o.toString(), n.toString());
    }

    private <T extends AggregationOperationExpression> void visitAggregationOperationExpression(Class<T> clazz) {
        try {
            var o = clazz.getDeclaredConstructor().newInstance();

            var ole = new ListLiteralOperationExpression();
            List<Expression> ol = List.of(new IntegerLiteral(1), new IntegerLiteral(2), new IntegerLiteral(3));
            ole.setRightListOperand(ol);
            o.setLeftOperand(ole);

            var e = o.accept(v);
            Assertions.assertEquals(o.getClass(), e.getClass());

            var n = clazz.cast(e);
            Assertions.assertEquals(o.getLeftOperand().getClass(), n.getLeftOperand().getClass());

            var nlo = n.getLeftOperand();
            Assertions.assertEquals(ole.getClass(), nlo.getClass());
            var nle = (ListLiteralOperationExpression) nlo;
            Assertions.assertEquals(ole.getSize(), nle.getSize());

            for (var i = 0; i < ole.getSize(); i++) {
                var oe = ole.getRightListOperand().get(i);
                var ne = nle.getRightListOperand().get(i);
                Assertions.assertEquals(oe.getClass(), ne.getClass());

                var oi = (IntegerLiteral) oe;
                var ni = (IntegerLiteral) ne;

                Assertions.assertEquals(oi.getType(), ni.getType());
                Assertions.assertEquals(oi.getValue(), ni.getValue());
            }

            Assertions.assertEquals(o.toString(), n.toString());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void visitMaximum() {
        visitAggregationOperationExpression(Maximum.class);
    }

    @Test
    public void visitMean() {
        visitAggregationOperationExpression(Mean.class);
    }

    @Test
    public void visitMedian() {
        visitAggregationOperationExpression(Median.class);
    }

    @Test
    public void visitMinimum() {
        visitAggregationOperationExpression(Minimum.class);
    }

    @Test
    public void visitMode() {
        visitAggregationOperationExpression(Mode.class);
    }

    @Test
    public void visitSum() {
        visitAggregationOperationExpression(Sum.class);
    }

    private <T extends ArithmeticOperationExpression> void visitArithmeticOperationExpression(Class<T> clazz) {
        try {
            var o = clazz.getDeclaredConstructor().newInstance();
            var ol = new IntegerLiteral(3);
            var or = new IntegerLiteral(2);
            o.setLeftOperand(ol);
            o.setRightOperand(or);

            var e = o.accept(v);
            Assertions.assertEquals(o.getClass(), e.getClass());

            var n = clazz.cast(e);
            Assertions.assertEquals(o.getLeftOperand().getClass(), n.getLeftOperand().getClass());

            var nlo = n.getLeftOperand();
            Assertions.assertEquals(ol.getClass(), nlo.getClass());
            var nle = (IntegerLiteral) nlo;
            Assertions.assertEquals(ol.getType(), nle.getType());
            Assertions.assertEquals(ol.getValue(), nle.getValue());

            var nro = n.getRightOperand();
            Assertions.assertEquals(or.getClass(), nro.getClass());
            var nre = (IntegerLiteral) nro;
            Assertions.assertEquals(or.getType(), nre.getType());
            Assertions.assertEquals(or.getValue(), nre.getValue());

            Assertions.assertEquals(o.toString(), n.toString());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void visitAdd() {
        visitArithmeticOperationExpression(Add.class);
    }

    @Test
    public void visitDivide() {
        visitArithmeticOperationExpression(Divide.class);
    }

    @Test
    public void visitMax() {
        visitArithmeticOperationExpression(Max.class);
    }

    @Test
    public void visitMin() {
        visitArithmeticOperationExpression(Min.class);
    }

    @Test
    public void visitMultiply() {
        visitArithmeticOperationExpression(Multiply.class);
    }

    @Test
    public void visitPower() {
        visitArithmeticOperationExpression(Power.class);
    }

    @Test
    public void visitSubtract() {
        visitArithmeticOperationExpression(Subtract.class);
    }

    private <T extends ConditionOperationExpression> void visitConditionOperationExpression(Class<T> clazz) {
        try {
            var o = clazz.getDeclaredConstructor().newInstance();
            var oc = new Equals();
            oc.setLeftOperand(new IntegerVariable("I"));
            oc.setRightOperand(new IntegerLiteral(5));
            o.setCondition(oc);
            var ot = new AssignOperationExpression();
            ot.setLeftOperand(new IntegerVariable("J"));
            ot.setRightOperand(new IntegerLiteral(1));
            o.setThenOperand(ot);
            var oe = new AssignOperationExpression();
            oe.setLeftOperand(new IntegerVariable("J"));
            oe.setRightOperand(new IntegerLiteral(2));
            o.setElseOperand(oe);

            var e = o.accept(v);
            Assertions.assertEquals(o.getClass(), e.getClass());

            var n = clazz.cast(e);

            Assertions.assertEquals(o.toString(), n.toString());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void visitELSE() {
        visitConditionOperationExpression(ELSE.class);
    }

    @Test
    public void visitENDIF() {
        visitConditionOperationExpression(ENDIF.class);
    }

    @Test
    public void visitIF() {
        visitConditionOperationExpression(IF.class);
    }

    @Test
    public void visitTernaryOperation() {
        visitConditionOperationExpression(TernaryOperation.class);
    }

    @Test
    public void visitTHEN() {
        visitConditionOperationExpression(THEN.class);
    }

    private <T extends LiteralExpression<?>> void compareListLiterals(Expression ol, Expression nl, Class<T> elementClass) {
        if (ol.getClass() != ListLiteral.class) {
            Assertions.fail("ol is not a ListLiteral but " + ol.getClass());
            return;
        }
        if (nl.getClass() != ListLiteral.class) {
            Assertions.fail("nl is not a ListLiteral but " + nl.getClass());
            return;
        }

        var l1 = (ListLiteral) ol;
        var l2 = (ListLiteral) nl;

        Assertions.assertEquals(l1.getListType(), l2.getListType());
        Assertions.assertEquals(l1.getSize(), l2.getSize());

        for (var i = 0; i < l1.getSize(); i++) {
            var l1e = l1.getValue().get(i);
            var l2e = l2.getValue().get(i);
            Assertions.assertEquals(elementClass, l1e.getClass());
            Assertions.assertEquals(l1e.getClass(), l2e.getClass());

            var l1v = elementClass.cast(l1e);
            var l2v = elementClass.cast(l2e);

            Assertions.assertEquals(l1v.getType(), l2v.getType());
            Assertions.assertEquals(l1v.getType(), l2v.getType());
        }
    }

    private <T extends Match> void visitMatchClass(Class<T> clazz) {
        try {
            var o = clazz.getDeclaredConstructor().newInstance();
            var ol1 = new ListLiteral(
                    List.of(
                            new IntegerLiteral(1),
                            new IntegerLiteral(2),
                            new IntegerLiteral(3)
                    ),
                    IntegerType.class
            );
            var ol2 = new ListLiteral(
                    List.of(
                            new IntegerLiteral(1),
                            new IntegerLiteral(2),
                            new IntegerLiteral(3)
                    ),
                    IntegerType.class
            );
            List<Expression> ol = List.of(ol1, ol2);
            o.setLeftListOperand(ol);

            var e = o.accept(v);
            Assertions.assertEquals(o.getClass(), e.getClass());

            var n = clazz.cast(e);

            Assertions.assertEquals(o.getLeftListOperand().size(), n.getLeftListOperand().size());

            var nl1e = n.getLeftListOperand().get(0);
            Assertions.assertEquals(ol1.getClass(), nl1e.getClass());
            var nl1 = (ListLiteral) nl1e;
            compareListLiterals(ol1, nl1, IntegerLiteral.class);

            var nl2e = n.getLeftListOperand().get(1);
            Assertions.assertEquals(ol2.getClass(), nl2e.getClass());
            var nl2 = (ListLiteral) nl2e;
            compareListLiterals(ol2, nl2, IntegerLiteral.class);

            Assertions.assertEquals(o.toString(), n.toString());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends ListOperationExpression> void visitListOperationExpression(Class<T> clazz) {
        try {
            var o = clazz.getDeclaredConstructor().newInstance();
            var ol = new ListLiteral(
                    List.of(
                            new IntegerLiteral(1),
                            new IntegerLiteral(2),
                            new IntegerLiteral(3)
                    ),
                    IntegerType.class
            );

            //TODO continue

            o.setLeftOperand(ol);
            var or = new IntegerVariable("I");
            o.setRightListOperand(List.of(or));
            o.setRightOperand(or);

            var e = o.accept(v);
            Assertions.assertEquals(o.getClass(), e.getClass());

            var n = clazz.cast(e);

            Assertions.assertEquals(o.toString(), n.toString());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void visitCombinationExists() {
        visitMatchClass(CombinationExists.class);
    }
    @Test
    public void visitFilterOperation() {
        visitListOperationExpression(FilterOperation.class);
    }
    @Test
    public void visitFirst() {

    }
    @Test
    public void visitGetItem() {

    }
    @Test
    public void visitHasItem() {

    }
    @Test
    public void visitInsideCombination() {
        visitMatchClass(InsideCombination.class);
    }
    @Test
    public void visitInsideExistsCombination() {
        visitMatchClass(InsideExistsCombination.class);
    }
    @Test
    public void visitIsEmpty() {

    }
    @Test
    public void visitItemPosition() {

    }
    @Test
    public void visitKeysOperation() {

    }
    @Test
    public void visitLast() {

    }
    @Test
    public void visitMapOperation() {

    }
    @Test
    public void visitMatch() {
        visitMatchClass(Match.class);
    }
    @Test
    public void visitMiddlesOperation() {

    }
    @Test
    public void visitNotEmpty() {

    }
    @Test
    public void visitPairOperation() {

    }
    @Test
    public void visitSizeOperation() {

    }
    @Test
    public void visitSortOperation() {

    }
    @Test
    public void visitTripleOperation() {

    }
    @Test
    public void visitValuesOperation() {

    }



}
