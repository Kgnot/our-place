package org.our_place.common.shared.dto;

// datos relacionados a lo "shared" entre módulos
public record SharedItemDto(
        String type,
        String className,
        String packageName,
        String description
) {
}
