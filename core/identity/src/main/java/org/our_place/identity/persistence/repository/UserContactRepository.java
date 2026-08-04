package org.our_place.identity.persistence.repository;

import org.our_place.identity.domain.entity.UserContact;
import org.our_place.identity.domain.entity.UserContactId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserContactRepository extends JpaRepository<UserContact, UserContactId> {

    List<UserContact> findByProfile_UserLoginId(UUID userLoginId);

    UserContact findByProfile_UserLoginIdAndIsPrimaryTrue(UUID userLoginId);

    boolean existsByIdContactTypeCodeAndIdValue(String contactTypeCode, String value);
}