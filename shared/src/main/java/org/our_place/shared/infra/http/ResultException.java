package org.our_place.shared.infra.http;

import lombok.Getter;
import org.our_place.shared.utils.ResultIssue;

@Getter
public class ResultException extends RuntimeException {

    private final ResultIssue issue;

    public ResultException(String message, ResultIssue issue) {
        super(message);
        this.issue = issue;
    }

}
