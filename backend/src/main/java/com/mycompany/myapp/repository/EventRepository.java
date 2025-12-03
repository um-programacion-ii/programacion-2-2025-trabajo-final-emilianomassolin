package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Event;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Event entity.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    Optional<Event> findOneByEventId(Long eventId);
}
