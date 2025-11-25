package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SeatSelection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SeatSelection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeatSelectionRepository extends JpaRepository<SeatSelection, Long> {}
