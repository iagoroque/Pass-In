package com.rock.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rock.passin.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, String>{
    
}
