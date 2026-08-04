package org.our_place.identity.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

import java.time.OffsetDateTime;

public class UserLockedException extends ResultException {

    public UserLockedException(OffsetDateTime lockedUntil) {
        super(
                "Usuario bloqueado hasta " + lockedUntil,
                new ResultIssue("USER_LOCKED",
                        "La cuenta está bloqueada temporalmente por intentos fallidos",
                        ResultIssue.Severity.WARNING)
        );
    }
}