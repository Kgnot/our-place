package org.our_place.map.persistence.repository;

import org.our_place.map.persistence.entity.LkpPlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpPlaceCategoryRepository extends JpaRepository<LkpPlaceCategory, String> {

}