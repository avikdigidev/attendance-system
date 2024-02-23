package com.avikdigidev.eventservice.repository;

import com.avikdigidev.eventservice.model.Event;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {
    // Define custom repository methods if needed
}
