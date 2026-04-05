package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {}
