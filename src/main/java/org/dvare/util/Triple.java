package org.dvare.util;

import java.util.Objects;

public abstract class Triple<L, M, R> implements Comparable<Triple<L, M, R>> {

    public static <L, M, R> Triple<L, M, R> of(final L left, final M middle, final R right) {
        return new Triple.TripleImpl<>(left, middle, right);
    }

    public abstract L getLeft();

    public abstract M getMiddle();

    public abstract R getRight();

    public static class TripleImpl<L, M, R> extends Triple<L, M, R> {
        public final L left;
        public final M middle;
        public final R right;

        public TripleImpl(L left, M middle, R right) {
            this.left = left;
            this.middle = middle;
            this.right = right;
        }

        @Override
        public L getLeft() {
            return left;
        }

        @Override
        public M getMiddle() {
            return middle;
        }

        @Override
        public R getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TripleImpl<?, ?, ?> triple = (TripleImpl<?, ?, ?>) o;
            if (!Objects.equals(left, triple.left)) return false;
            if (!Objects.equals(middle, triple.middle)) return false;
            return Objects.equals(right, triple.right);
        }

        @Override
        public int hashCode() {
            int result = left != null ? left.hashCode() : 0;
            result = 31 * result + (middle != null ? middle.hashCode() : 0);
            result = 31 * result + (right != null ? right.hashCode() : 0);
            return result;
        }

        @Override
        public int compareTo(Triple<L, M, R> o) {
            return Integer.compare(this.getLeft().hashCode(), o.getLeft().hashCode());
        }
    }
}
