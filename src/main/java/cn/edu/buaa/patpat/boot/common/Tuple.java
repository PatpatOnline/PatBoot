/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.common;

import lombok.Data;

/**
 * A tuple of two elements. How dare Java not have this built-in? :(
 *
 * @param <T> Type of the first element.
 * @param <U> Type of the second element.
 */
@Data
public class Tuple<T, U> {
    public T first;
    public U second;

    public Tuple(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public static <T, U> Tuple<T, U> of(T first, U second) {
        return new Tuple<>(first, second);
    }

    public static <T, U> Tuple<T, U> of(T first) {
        return new Tuple<>(first, null);
    }

    public static <T, U> Tuple<T, U> of() {
        return new Tuple<>(null, null);
    }

    public boolean isEmpty() {
        return this.first == null && this.second == null;
    }

    @Override
    public int hashCode() {
        return 31 * first.hashCode() + second.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Tuple<?, ?> other)) {
            return false;
        }
        return this.first.equals(other.first) && this.second.equals(other.second);
    }
}