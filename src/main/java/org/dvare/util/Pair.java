package org.dvare.util;

import java.util.Objects;

public abstract class Pair<L, R> implements Comparable<Pair<L, R>> {

    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new PairImpl<>(left, right);
    }

    public final L getKey() {
        return getLeft();
    }

    public abstract L getLeft();

    public abstract R getRight();

    public final R getValue() {
        return getRight();
    }

    public static class PairImpl<L, R> extends Pair<L, R> {
        public final L left;
        public final R right;

        public PairImpl(L left, R right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public L getLeft() {
            return left;
        }

        @Override
        public R getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PairImpl<?, ?> pair = (PairImpl<?, ?>) o;
            if (!Objects.equals(left, pair.left)) return false;
            return Objects.equals(right, pair.right);
        }

        @Override
        public int hashCode() {
            int result = left != null ? left.hashCode() : 0;
            result = 31 * result + (right != null ? right.hashCode() : 0);
            return result;
        }

        @Override
        public int compareTo(Pair<L, R> o) {
            return Integer.compare(this.getLeft().hashCode(), o.getLeft().hashCode());
        }
    }
}
