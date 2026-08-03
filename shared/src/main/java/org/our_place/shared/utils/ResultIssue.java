package org.our_place.shared.utils;

public record ResultIssue(String code, String message, Severity severity) {
    public enum Severity {
        WARNING,
        CRITICAL
    }
}
