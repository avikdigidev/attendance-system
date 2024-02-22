package com.avikdigidev.eventservice.repository;

import com.avikdigidev.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Define custom repository methods if needed
}
