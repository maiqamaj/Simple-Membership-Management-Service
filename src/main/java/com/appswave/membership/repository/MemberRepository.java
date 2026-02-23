package com.appswave.membership.repository;

import com.appswave.membership.entity.Member;
import com.appswave.membership.enums.Gender;
import com.appswave.membership.enums.MembershipType;
import com.appswave.membership.enums.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    boolean existsByEmail(String email);

    Optional<Member> findByIdAndDeletedFalse(UUID id);

    @Query("SELECT m FROM Member m WHERE m.deleted = false AND " +
           "(:search IS NULL OR LOWER(m.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:gender IS NULL OR m.gender = :gender) AND " +
           "(:membershipType IS NULL OR m.membershipType = :membershipType) AND " +
           "(:persona IS NULL OR m.persona = :persona)")
    Page<Member> findAllWithFilters(
            @Param("search") String search,
            @Param("gender") Gender gender,
            @Param("membershipType") MembershipType membershipType,
            @Param("persona") Persona persona,
            Pageable pageable);
}
