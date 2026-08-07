package org.our_place.map.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;


public class PlaceCategoryNotFoundException extends ResultException {
    public PlaceCategoryNotFoundException(String categoryCode) {
        super(
                "lkp_place_category not found. code=" + categoryCode,
                new ResultIssue(
                        "PLACE_CATEGORY_NOT_FOUND",
                        "La categoría de lugar indicada no existe.",
                        ResultIssue.Severity.WARNING
                )
        );
    }
}