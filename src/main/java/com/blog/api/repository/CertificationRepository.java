package com.blog.api.repository;

import com.blog.api.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, String> {
    Optional<Certification> findByIdAndExpirationDateAfterAndExpired(String token, LocalDateTime now, boolean expired);

}
