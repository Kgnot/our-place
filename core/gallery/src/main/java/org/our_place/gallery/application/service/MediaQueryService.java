package org.our_place.gallery.application.service;

import lombok.RequiredArgsConstructor;
import org.our_place.gallery.application.service.dto.MediaCommentDto;
import org.our_place.gallery.application.service.dto.MediaDetailDto;
import org.our_place.gallery.application.service.dto.MediaSummaryDto;
import org.our_place.gallery.application.service.mapper.MediaServiceMapper;
import org.our_place.gallery.domain.entity.Media;
import org.our_place.gallery.domain.entity.MediaReaction;
import org.our_place.gallery.domain.exception.MediaNotFoundException;
import org.our_place.gallery.infra.persistence.repository.MediaCommentRepository;
import org.our_place.gallery.infra.persistence.repository.MediaReactionRepository;
import org.our_place.gallery.infra.persistence.repository.MediaRepository;
import org.our_place.common.shared.r2.R2PresignedUrlGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Solo lectura — proyecta a DTO, no muta estado (ver §2).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaQueryService {

    private final MediaRepository mediaRepository;
    private final MediaCommentRepository mediaCommentRepository;
    private final MediaReactionRepository mediaReactionRepository;
    private final R2PresignedUrlGenerator r2PresignedUrlGenerator;

    public Page<MediaSummaryDto> listByRoom(UUID roomId, Pageable pageable) {
        return mediaRepository
                .findByRoomIdAndDeletedAtIsNullOrderByTakenAtDescCreatedAtDesc(roomId, pageable)
                .map(MediaServiceMapper::toSummaryDto)
                .map(dto -> dto.withThumbnailUrl(
                        r2PresignedUrlGenerator.generateGetUrl(dto.thumbnailUrl())
                ));
    }

    /**
     * Obtiene los detalles de una media.
     *
     * @param mediaId       ID de la media.
     * @param currentUserId ID del usuario actual.
     * @return Detalles de la media.
     */
    public MediaDetailDto getDetail(UUID mediaId, UUID currentUserId) {
        Media media = mediaRepository.findByIdAndDeletedAtIsNull(mediaId)
                .orElseThrow(() -> new MediaNotFoundException(mediaId));

        long commentCount = mediaCommentRepository.countByMediaIdAndDeletedAtIsNull(mediaId);
        long reactionCount = mediaReactionRepository.countByIdMediaId(mediaId);
        String currentUserReactionType = mediaReactionRepository
                .findByIdMediaIdAndIdUserLoginId(mediaId, currentUserId)
                .map(MediaReaction::getReactionType)
                .orElse(null);

        return new MediaDetailDto(
                media.getId(),
                r2PresignedUrlGenerator.generateGetUrl(media.getR2Url()),
                r2PresignedUrlGenerator.generateGetUrl(media.getThumbnailUrl()),
                media.getMediaType().getCode(),
                media.getCaption(),
                media.getTakenAt(),
                media.getUploadedByUserId(),
                commentCount,
                reactionCount,
                currentUserReactionType
        );
    }

    public List<MediaCommentDto> listComments(UUID mediaId) {
        if (mediaRepository.findByIdAndDeletedAtIsNull(mediaId).isEmpty()) {
            throw new MediaNotFoundException(mediaId);
        }
        return mediaCommentRepository.findByMediaIdAndDeletedAtIsNullOrderByCreatedAtAsc(mediaId).stream()
                .map(MediaServiceMapper::toCommentDto)
                .toList();
    }

    public String getUrlById(UUID mediaId) {
        return mediaRepository.findByIdAndDeletedAtIsNull(mediaId)
                .orElseThrow(() -> new MediaNotFoundException(mediaId))
                .getR2Url();
    }

    public List<String> getUrlsByIds(List<UUID> mediaIds) {
        return orderedMediaFor(mediaIds).stream()
                .map(Media::getR2Url)
                .toList();
    }

    public MediaSummaryDto getMediaById(UUID mediaId) {
        Media media = mediaRepository.findByIdAndDeletedAtIsNull(mediaId)
                .orElseThrow(() -> new MediaNotFoundException(mediaId));
        return MediaServiceMapper.toSummaryDto(media);
    }

    public List<MediaSummaryDto> getMediasByIds(List<UUID> mediaIds) {
        return orderedMediaFor(mediaIds).stream()
                .map(MediaServiceMapper::toSummaryDto)
                .toList();
    }

    // extra for
    private List<Media> orderedMediaFor(List<UUID> mediaIds) {
        if (mediaIds == null || mediaIds.isEmpty()) {
            return List.of();
        }
        Map<UUID, Media> byId = new LinkedHashMap<>();
        mediaRepository.findByIdInAndDeletedAtIsNull(mediaIds).forEach(m -> byId.put(m.getId(), m));

        return mediaIds.stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Fotos de un room en un rango de fechas.
     */
    public Page<MediaSummaryDto> listByRoomAndDateRange(UUID roomId, OffsetDateTime start, OffsetDateTime end, Pageable pageable) {
        return mediaRepository
                .findByRoomIdAndTakenAtBetweenAndDeletedAtIsNullOrderByTakenAtDesc(roomId, start, end, pageable)
                .map(MediaServiceMapper::toSummaryDto)
                .map(dto -> dto.withThumbnailUrl(
                        r2PresignedUrlGenerator.generateGetUrl(dto.thumbnailUrl())
                ));
    }

    /**
     * Últimas fotos subidas (por createdAt, no takenAt).
     */
    public Page<MediaSummaryDto> listLatestByRoom(UUID roomId, Pageable pageable) {
        return mediaRepository
                .findByRoomIdAndDeletedAtIsNullOrderByCreatedAtDesc(roomId, pageable)
                .map(MediaServiceMapper::toSummaryDto)
                .map(dto -> dto.withThumbnailUrl(
                        r2PresignedUrlGenerator.generateGetUrl(dto.thumbnailUrl())
                ));
    }
}