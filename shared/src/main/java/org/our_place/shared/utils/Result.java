package org.our_place.shared.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// patron result para solver muchos errores, o posibles errores, funciona en dominio, o casos de uso, etc.
public sealed interface Result<T> permits Result.Success, Result.Failure {

    Logger log = LoggerFactory.getLogger(Result.class);

    record Success<T>(T value, List<ResultIssue> warnings) implements Result<T> {
    }

    record Failure<T>(ResultIssue error) implements Result<T> {
    }

    static <T> Result<T> ok(T value) {
        log.debug("Creating a successful result with value: {}", value);
        return new Success<>(value, List.of());
    }

    static <T> Result<T> okWithWarnings(T value, List<ResultIssue> warnings) {
        return new Success<>(value, warnings);
    }

    static <T> Result<T> fail(ResultIssue error) {
        return new Failure<>(error);
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default T getValue() {
        if (this instanceof Success<T> s) return s.value;
        throw new IllegalStateException("Cannot get value of a failed Result.");
    }

    default List<ResultIssue> getWarnings() {
        return this instanceof Result.Success<T> s ? s.warnings : List.of();
    }
}
